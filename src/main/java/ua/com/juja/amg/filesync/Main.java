package ua.com.juja.amg.filesync;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        FileSynchronizer synchronizer = new FileSynchronizer("src/main/resources/source",
                "src/test/resources");
        synchronizer.synchronizeDirs();
    }
}
