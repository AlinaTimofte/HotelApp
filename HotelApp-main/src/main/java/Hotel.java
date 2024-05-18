import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Hotel {
    private int id;
    private String name;
    private double latitude;
    private double longitude;
    private List<Room> rooms = new ArrayList<>();

    public Hotel(int id, String name, double latitude, double longitude) {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.rooms = new ArrayList<>();
    }
}
