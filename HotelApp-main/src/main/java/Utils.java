import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class Utils {
    public static List<String> getFilesInFolder(String folder) {
        File file = new File(folder);
        return Arrays.asList(file.list());
    }

    public static List<String> readAllFileLines(String file) throws IOException {
        Path path = Paths.get(file);
        return Files.readAllLines(path, StandardCharsets.UTF_8);
    }
}
