import ua.com.juja.amg.filesync.FileSynchronizer;

import java.io.IOException;

public class FileSync {
    public static void main(String[] args) throws IOException {
        verifyArgs(args);
        FileSynchronizer synchronizer = new FileSynchronizer(args[0], args[1]);
        synchronizer.synchronizeDirs();
    }

    private static void verifyArgs(String[] args) {
        if (args.length != 2) {
            throw new IllegalArgumentException("Invalid arguments number.");
        }
    }
}
