package ua.com.juja.amg.filesync;

import java.io.IOException;
import java.nio.file.*;

public class FileSynchronizer {

    private static final Path EXP_DEST_DIR = Paths.get("dest");
    private Path src;
    private Path dst;

    public FileSynchronizer(String source, String dest) {
        verifyArgs(source, dest);
    }

    public void synchronizeDirs() throws IOException {
        boolean destDirIsNotSpecified = !dst.endsWith(EXP_DEST_DIR);
        if (destDirIsNotSpecified) createDestFile();
        Files.walkFileTree(src, new SynchronizingFileVisitor(src, dst));
    }

    private void createDestFile() throws IOException {
        dst = dst.resolve(EXP_DEST_DIR);
        if (Files.notExists(dst) || !Files.isDirectory(dst))  {
            Files.createDirectory(dst);
            System.out.println(String.format("Created destination directory - %s", dst.getFileName()));
        }
    }

    private void verifyArgs(String source, String dest) {
        notNullOrEmpty(source);
        notNullOrEmpty(dest);
        src = Paths.get(source);
        dst = Paths.get(dest);
        isDir(src);
        isDir(dst);
    }

    private void isDir(Path path) {
        if (!Files.isDirectory(path)) {
            throw new IllegalArgumentException(String.format("Specified path %s is not a directory", path));
        }
    }

    private void notNullOrEmpty(String path) {
        if (path == null || path.isEmpty()) {
            throw new IllegalArgumentException("Path should not be empty.");
        }

    }
}
