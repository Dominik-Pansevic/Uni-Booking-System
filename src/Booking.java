import java.util.Date;

public class Booking {
    private long bookingId;
    private Date reservationStart;
    private Date reservationEnd;
    private int reservationDuration;
    private Date date;

    private String name;
    private String surname;
    private String emailAddress;
    private String phoneNumber;
    private String bookingNote;

    public Booking(long bookingId, Date date, Date reservationStart, Date reservationEnd, String name, String surname, String emailAddress, String phoneNumber, String bookingNote)
    {
        this.bookingId = bookingId;
        this.date = date;
        this.reservationStart = reservationStart;
        this.reservationEnd = reservationEnd;

        // calculate reservation duration

        int h1 = this.reservationStart.getHours();
        int h2 = this.reservationEnd.getHours();

        int m1 = this.reservationStart.getMinutes();
        int m2 = this.reservationEnd.getMinutes();

        reservationDuration = (h2 - h1) * 60 + m2 - m1;

        this.name = name;
        this.surname = surname;
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
        this.bookingNote = bookingNote;

    }

    public Date getReservationStart() {
        return reservationStart;
    }

    public void setReservationStart(Date reservationStart) {
        this.reservationStart = reservationStart;
    }

    public Date getReservationEnd() {
        return reservationEnd;
    }

    public void setReservationEnd(Date reservationEnd) {
        this.reservationEnd = reservationEnd;
    }

    public int getReservationDuration() {
        return reservationDuration;
    }

    public void setReservationDuration(int reservationDuration) {
        this.reservationDuration = reservationDuration;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getBookingNote() {
        return bookingNote;
    }

    public void setBookingNote(String bookingNote) {
        this.bookingNote = bookingNote;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public long getBookingId() {
        return bookingId;
    }

    public void setBookingId(long bookingId) {
        this.bookingId = bookingId;
    }
}
