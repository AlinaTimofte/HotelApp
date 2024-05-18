import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class MainApplication {
    private static IHotelService hotelService;
    private static IHotelRepository hotelRepository;
    private static List<Reservation> reservations = new ArrayList<>();

    public static void main(String[] args) {
        hotelRepository = new HotelRepository("Hotels");
        hotelService = new HotelService(hotelRepository);

        hotelRepository.cleanup(); //curata datele in cazul in care utilizatorul nu a iesit conform meniului din aplicatie
        addSampleHotels(); //adauga hoteluri exemplu

        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("1. Find Hotels");
            System.out.println("2. Select a Hotel");
            System.out.println("3. Book a Room");
            System.out.println("4. Cancel/Change Reservation");
            System.out.println("5. Leave Feedback");
            System.out.println("6. Exit");

            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    findHotels(scanner);
                    break;
                case 2:
                    selectHotel(scanner);
                    break;
                case 3:
                    bookRoom(scanner);
                    break;
                case 4:
                    cancelOrChangeReservation(scanner);
                    break;
                case 5:
                    leaveFeedback(scanner);
                    break;
                case 6:
                    System.out.println("Exiting application.");
                    hotelRepository.cleanup();
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void addSampleHotels() {
        List<Hotel> sampleHotels = Arrays.asList(
                new Hotel(1, "Hotel Ramada", 46.764654252624204, 23.598674125224626, Arrays.asList(
                        new Room(210, 2, 200, true),
                        new Room(125, 1, 350, true),
                        new Room(87, 1, 300, false)
                )),
                new Hotel(2, "Grand Hotel Italia", 46.7522792440665, 23.605990381045697, Arrays.asList(
                        new Room(41, 3, 240, true)
                )),
                new Hotel(3, "Hampton by Hilton", 46.77539900854998, 23.60182699638966, Arrays.asList(
                        new Room(32, 2, 410, false),
                        new Room(21, 2, 350, true),
                        new Room(64, 3, 300, true)
                ))
        );

        for (Hotel hotel : sampleHotels) {
            hotelService.addNewHotel(hotel.getId(), hotel.getName(), hotel.getLatitude(), hotel.getLongitude());
            for (Room room : hotel.getRooms()) {
                hotelService.addRoom(hotel.getId(), room.getRoomNumber(), room.getType(), room.getPrice(), room.isAvailable());
            }
        }
    }

    private static void findHotels(Scanner scanner) {
        System.out.println("Enter your latitude:");
        double latitude = scanner.nextDouble();
        System.out.println("Enter your longitude:");
        double longitude = scanner.nextDouble();
        System.out.println("Enter radius in kilometers:");
        double radius = scanner.nextDouble();

        List<Hotel> nearbyHotels = hotelService.findHotelsWithinRadius(latitude, longitude, radius);
        System.out.println("Nearby Hotels:");
        for (Hotel hotel : nearbyHotels) {
            System.out.println("ID: " + hotel.getId() + ", Name: " + hotel.getName());
        }
    }

    private static void selectHotel(Scanner scanner) {
        System.out.println("Enter hotel ID to view rooms:");
        int hotelId = scanner.nextInt();
        List<Hotel> allHotels = hotelService.getAllHotelsRecords();

        Hotel selectedHotel = allHotels.stream()
                .filter(h -> h.getId() == hotelId)
                .findFirst()
                .orElse(null);

        if (selectedHotel != null) {
            System.out.println("Available Rooms:");
            for (Room room : selectedHotel.getRooms()) {
                System.out.println("Room Number: " + room.getRoomNumber() + ", Type: " + room.getType() + ", Price: " + room.getPrice() + ", Available: " + room.isAvailable());
            }
        } else {
            System.out.println("Hotel not found.");
        }
    }

    private static void bookRoom(Scanner scanner) {
        System.out.println("Enter hotel ID to book a room:");
        int hotelId = scanner.nextInt();
        List<Hotel> allHotels = hotelService.getAllHotelsRecords();

        Hotel selectedHotel = allHotels.stream()
                .filter(h -> h.getId() == hotelId)
                .findFirst()
                .orElse(null);

        if (selectedHotel != null) {
            System.out.println("Enter room number to book:");
            int roomNumber = scanner.nextInt();

            Room roomToBook = selectedHotel.getRooms().stream()
                    .filter(r -> r.getRoomNumber() == roomNumber)
                    .findFirst()
                    .orElse(null);

            if (roomToBook != null && roomToBook.isAvailable()) {
                roomToBook.setAvailable(false);
                Reservation reservation = new Reservation((long) (reservations.size() + 1), roomToBook, LocalDateTime.now(), LocalDateTime.now().plusDays(1));
                reservations.add(reservation);
                System.out.println("Room booked successfully: " + reservation);
            } else {
                System.out.println("Room not available.");
            }
        } else {
            System.out.println("Hotel not found.");
        }
    }

    private static void cancelOrChangeReservation(Scanner scanner) {
        System.out.println("Enter reservation ID to cancel/change:");
        long reservationId = scanner.nextLong();

        Reservation reservation = reservations.stream()
                .filter(r -> r.getId() == reservationId)
                .findFirst()
                .orElse(null);

        if (reservation != null) {
            System.out.println("1. Cancel Reservation");
            System.out.println("2. Change Reservation");
            int choice = scanner.nextInt();
            if (choice == 1) {
                reservations.remove(reservation);
                reservation.getRoom().setAvailable(true);
                System.out.println("Reservation canceled successfully.");
            } else if (choice == 2) {
                System.out.println("Enter new room number:");
                int newRoomNumber = scanner.nextInt();

                Hotel hotel = hotelService.getAllHotelsRecords().stream()
                        .filter(h -> h.getRooms().contains(reservation.getRoom()))
                        .findFirst()
                        .orElse(null);

                if (hotel != null) {
                    Room newRoom = hotel.getRooms().stream()
                            .filter(r -> r.getRoomNumber() == newRoomNumber && r.isAvailable())
                            .findFirst()
                            .orElse(null);

                    if (newRoom != null) {
                        reservation.getRoom().setAvailable(true);
                        reservation.setRoom(newRoom);
                        newRoom.setAvailable(false);
                        System.out.println("Reservation changed successfully.");
                    } else {
                        System.out.println("New room not available.");
                    }
                } else {
                    System.out.println("Hotel not found.");
                }
            } else {
                System.out.println("Invalid choice.");
            }
        } else {
            System.out.println("Reservation not found.");
        }
    }

    private static void leaveFeedback(Scanner scanner) {
        System.out.println("Enter hotel ID to leave feedback:");
        int hotelId = scanner.nextInt();

        Hotel hotel = hotelService.getAllHotelsRecords().stream()
                .filter(h -> h.getId() == hotelId)
                .findFirst()
                .orElse(null);

        if (hotel != null) {
            System.out.println("Enter service rating (1-5):");
            String serviceRating = scanner.next();
            System.out.println("Enter cleanliness rating (1-5):");
            String cleanlinessRating = scanner.next();
            System.out.println("Enter any additional comments:");
            scanner.nextLine();
            String comments = scanner.nextLine();

            Feedback feedback = new Feedback((long) (hotel.getRooms().size() + 1), serviceRating, cleanlinessRating, comments, hotel);
            System.out.println("Feedback submitted successfully: " + feedback);
        } else {
            System.out.println("Hotel not found.");
        }
    }
}
