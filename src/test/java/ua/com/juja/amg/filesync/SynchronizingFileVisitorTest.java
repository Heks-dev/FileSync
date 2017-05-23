package ua.com.juja.amg.filesync;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.FileVisitor;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.*;

public class SynchronizingFileVisitorTest {

    @Test
    public void test() throws IOException {
        // given
        Path source = Paths.get("src/main/resources/source");
        Path dest = Paths.get("src/test/resources/dest");
        FileVisitor<Path> fileVisitor = new SynchronizingFileVisitor(source, dest);
        // when
        fileVisitor.postVisitDirectory(source, null);
    }
}