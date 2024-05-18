import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class HotelService implements IHotelService {
    private List<Hotel> hotels = new ArrayList<>();
    private IHotelRepository repository;

    public HotelService(IHotelRepository repository) {
        this.repository = repository;
    }

    @Override
    public void addRoom(int hotelId, int roomNumber, int type, int price, boolean isAvailable) {
        List<Hotel> hotels = repository.readAll();
        for (Hotel hotel : hotels) {
            if (hotel.getId() == hotelId) {
                if (hotel.getRooms() == null) {
                    hotel.setRooms(new ArrayList<>());
                }
                Room room = new Room(roomNumber, type, price, isAvailable);
                hotel.getRooms().add(room);
                repository.save(hotel); // salvez update-ul in hotel (camerele)
                return; // exit- pentru a elimina dublicarile
            }
        }
    }

    @Override
    public List<Hotel> findHotelsWithinRadius(double userLatitude, double userLongitude, double radiusKm) {
        return repository.readAll().stream()
                .filter(hotel -> DistanceHotelUser.calculateDistance(userLatitude, userLongitude, hotel.getLatitude(), hotel.getLongitude()) <= radiusKm * 1000)
                .collect(Collectors.toList());
    }

    @Override
    public List<Hotel> getAllHotelsRecords() { // Implement the method here
        return repository.readAll();
    }

    @Override
    public void addNewHotel(int id, String name, double latitude, double longitude) {
        Hotel hotel = new Hotel(id, name, latitude, longitude, new ArrayList<>());
        repository.save(hotel);
    }
}
