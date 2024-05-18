import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileReadUtil {
    public static List<String> readAllFileLines(String file) throws IOException {
        Path path = Paths.get(file);
        return Files.readAllLines(path, StandardCharsets.UTF_8);
    }
}
