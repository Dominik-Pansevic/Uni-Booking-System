import java.util.ArrayList;
import java.util.Date;

public class Room {
    private long roomId;
    private String roomName;
    private int size;
    private String typeName;
    private ArrayList<Booking> bookings;
    private long nextBookingId;

    private Date unavailabilityEndDate;
    private boolean available;
    private String unavailabilityNote;



    public Room(long roomId, String roomName, int size, String typeName) {

        bookings = new ArrayList<Booking>();

        this.roomId = roomId;
        this.roomName = roomName;
        this.size = size;
        this.typeName = typeName;
        available = true;
        unavailabilityNote = "";
        unavailabilityEndDate = new Date();
        unavailabilityEndDate.setYear(0);

    }
    // Getters and Setters

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public ArrayList<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(ArrayList<Booking> bookings) {
        this.bookings = bookings;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public String getUnavailabilityNote() {
        return unavailabilityNote;
    }

    public void setUnavailabilityNote(String unavailabilityNote) {
        this.unavailabilityNote = unavailabilityNote;
    }

    public Date getUnavailabilityEndDate() {
        return unavailabilityEndDate;
    }

    public void setUnavailabilityEndDate(Date unavailabilityEndDate) {
        this.unavailabilityEndDate = unavailabilityEndDate;
    }

    public long getRoomId() {
        return roomId;
    }

    public void setRoomId(long roomId) {
        this.roomId = roomId;
    }

    public long getNextBookingId() {
        return nextBookingId;
    }

    public void setNextBookingId(long nextBookingId) {
        this.nextBookingId = nextBookingId;
    }
}
