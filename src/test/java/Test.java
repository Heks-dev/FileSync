import java.nio.file.Path;
import java.nio.file.Paths;

public class Test {
    public static void main(String[] args) {
        Path source = Paths.get("src/main/resources/source/photos/asdasd");
        Path dest = Paths.get("src/test/resources/dest/photos/asdasd/");
        System.out.println(source.equals(dest));
        System.out.println(source.resolveSibling(dest));
    }
}
