import java.util.List;

public interface IHotelService {
    void addRoom(int hotelId, int roomNumber, int type, int price, boolean isAvailable);

    void addNewHotel(int id, String name, double latitude, double longitude);

    List<Hotel> findHotelsWithinRadius(double userLatitude, double userLongitude, double radiusKm);

    List<Hotel> getAllHotelsRecords();

    //double UserHotelDistance();
}
