import java.io.File;
import java.util.Arrays;
import java.util.List;

public class FilesAndFoldersUtil {
    public static void createFolder(String folder){
        File d = new File(folder);
        d.mkdirs();
    }

    public static List<String> getFilesInFolder(String folder){
        File file = null;
        String[] paths;
        file = new File(folder);
        return Arrays.asList(file.list());
    }
}
