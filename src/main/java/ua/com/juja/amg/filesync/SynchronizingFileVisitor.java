package ua.com.juja.amg.filesync;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

import static java.nio.file.FileVisitResult.CONTINUE;

public class SynchronizingFileVisitor extends SimpleFileVisitor<Path> {

    private static final Path ROOT = Paths.get("");
    private final Path srcRoot;
    private final Path dstRoot;
    private Path currentDir;

    public SynchronizingFileVisitor(Path src, Path dest) {
        this.srcRoot = src;
        this.dstRoot = dest;
        currentDir = ROOT;
    }

    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        boolean isRoot = dir.getFileName().equals(srcRoot.getFileName());
        if (!isRoot) {
            currentDir = currentDir.resolve(dir.getFileName());
            Path targetDirPos = dstRoot.resolve(currentDir);
            if (Files.notExists(targetDirPos)) {
                Files.createDirectory(targetDirPos);
                System.out.println(String.format("Synchronized directory %s", targetDirPos));
            }
        }
        return CONTINUE;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        Path targetFilePos = dstRoot.resolve(currentDir).resolve(file.getFileName());
        if (Files.notExists(targetFilePos)) {
            Files.copy(file, targetFilePos);
            System.out.println(String.format("Synchronized file: %s (%s)", targetFilePos, attrs.size()));
        } else {
            BasicFileAttributes targetFileAttrs = Files.readAttributes(targetFilePos, BasicFileAttributes.class);
            boolean hasDiffs = attrs.size() != targetFileAttrs.size();
            if (hasDiffs) {
                long sizeChanged = attrs.size() - targetFileAttrs.size();
                Files.copy(file, targetFilePos, StandardCopyOption.REPLACE_EXISTING);
                System.out.println(String.format("Updated file: %s, size changed to (%s)", targetFilePos, sizeChanged));
            }
        }
        return CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
        deleteUnnecessaryFiles(dir, dstRoot.resolve(currentDir));
        currentDir = (currentDir.getParent() != null) ? currentDir.getParent() : ROOT;
        return CONTINUE;
    }

    private void deleteUnnecessaryFiles(Path srcCur, Path dstCur) throws IOException {
        Files.walkFileTree(dstRoot.resolve(currentDir), new FileVisitor<Path>() {

            private Path currentDir = ROOT;

            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                boolean isRoot = dstCur.equals(dir);
                if (!isRoot) {
                    currentDir = currentDir.resolve(dir.getFileName());
                }
                return CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Path targetFilePos = srcCur.resolve(currentDir).resolve(file.getFileName());
                if (Files.notExists(targetFilePos)) {
                    Files.delete(file);
                    System.out.println("File was deleted: " + file);
                }
                return CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                throw exc;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                boolean isRoot = dir.getFileName().equals(dstCur.getFileName());
                Path targetDirPos = srcCur;
                if (!isRoot) {
                    targetDirPos = targetDirPos.resolve(currentDir);
                }
                if (Files.notExists(targetDirPos)) {
                    Files.delete(dir);
                    System.out.println("Deleted directory: " + dir);
                }
                currentDir = (currentDir.getParent() != null) ? currentDir.getParent() : ROOT;
                return CONTINUE;
            }
        });
    }
}
