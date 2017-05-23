package ua.com.juja.amg.filesync;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Test {
    public static void main(String[] args) {
        Path p1 = Paths.get("src/main/resources/source");
        Path p2 = Paths.get("");
        System.out.println(p2.resolve(p1));

    }
}
