import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class HotelRepository implements IHotelRepository {
    private String workingFolder; //Hotels
    private ObjectMapper objectMapper = new ObjectMapper();

    public HotelRepository(String workingFolder) {
        FilesAndFoldersUtil.createFolder(workingFolder);
        this.workingFolder = workingFolder;
    }

    public void cleanup() {
        File folder = new File(workingFolder);
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".json")) {
                    file.delete();
                }
            }
        }
    }

    @Override
    public void save(Hotel hotel) {
        try {
            String filePath = workingFolder + File.separator + "hotel" + hotel.getId() + ".json";
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(new File(filePath), hotel);
        } catch (IOException ex) {
            System.err.println("Error writing the value!");
        }
    }

    @Override
    public List<Hotel> readAll() {
        List<Hotel> hotel = new ArrayList<>();
        try {
            List<String> files = FilesAndFoldersUtil.getFilesInFolder(workingFolder); //returneaza o lista cu fisierele din workingFolder
            for (String file : files) {
                String jsonContent = FileReadUtil.readAllFileLines(workingFolder + "/" + file).stream().collect(Collectors.joining("\n"));
                Hotel hotels = objectMapper.readValue(jsonContent, Hotel.class);
                hotel.add(hotels);
            }
        } catch (IOException ex) {
            Logger.getLogger(HotelService.class.getName()).log(Level.SEVERE, null, ex);
        }
        return hotel;
    }
}
