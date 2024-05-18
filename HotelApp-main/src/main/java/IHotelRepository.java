import org.jxmapviewer.viewer.Waypoint;

import java.util.List;

public interface IHotelRepository {
    public void save(Hotel hotel);

    public List<Hotel> readAll();

    public void cleanup();
}
