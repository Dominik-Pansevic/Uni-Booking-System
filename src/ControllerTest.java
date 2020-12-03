import org.junit.jupiter.api.Test;


import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class ControllerTest {

    private Controller controller;
    private ClerkGUI clerkGUI1 ;
    private ClerkGUI clerkGUI2 ;
    private ClerkGUI clerkGUI3 ;
    private ManagerGUI managerGUI;
    private Date start;
    private Date end;
    private Date termStart;
    private Date termEnd;
    private Date weekdaysTime;
    private Date weekendsTime;
    private Booking booking;

    @org.junit.jupiter.api.BeforeEach

    void setUp() {

        controller = new Controller();
        //initialise GUIs
        clerkGUI1 = new ClerkGUI(controller);
        clerkGUI2 = new ClerkGUI(controller);
        clerkGUI3 = new ClerkGUI(controller);
        managerGUI = new ManagerGUI(controller);

        //link GUIs to the controller
        controller.linkGUIs(clerkGUI1,clerkGUI2,clerkGUI3,managerGUI);

        start = new Date();
        end = new Date();

        start.setHours(14);
        start.setMinutes(0);
        end.setHours(15);
        end.setMinutes(00);

        booking = new Booking(controller.generateBookingId(1),new Date(),start,end,"Dom","P","aaa@gmail.com",
                "123", "Society meeting");
//        assertTrue(controller.checkTermBookingTimeAppropriateness(booking));

        termStart = new Date();
        termStart.setYear(119);
        termEnd = new Date();
        termEnd.setYear(155);
        weekendsTime = new Date();
        weekendsTime.setHours(16);
        weekdaysTime = new Date();
        weekdaysTime.setHours(16);
    }



    @org.junit.jupiter.api.AfterEach

    void tearDown() {

    }



    @Test
    void linkGUIs() {


        assertEquals(clerkGUI1, controller.getClerkGUI1());
        assertEquals(clerkGUI2, controller.getClerkGUI2());
        assertEquals(clerkGUI3, controller.getClerkGUI3());
        assertEquals(managerGUI, controller.getManagerGUI());

    }


    @Test
    void generateFilteredRoomsClerk() {
    }

    @Test
    void generateFilteredRoomsManager() {
    }



    @Test
    void addRoom() {
        controller.addRoom("SJG/03", 150, "Computer Laboratory");
        assertEquals("SJG/03", controller.getRooms().get(0).getRoomName());
        assertEquals(150, controller.getRooms().get(0).getSize());
        assertEquals("Computer Laboratory", controller.getRooms().get(0).getTypeName());

    }

    @Test
    void deleteRoom() {

        controller.addRoom("SJG/03", 150, "Computer Laboratory");
        assertEquals(1, controller.getRooms().size());

        controller.deleteRoom(1);
        assertEquals(0, controller.getRooms().size());

    }

    @Test
    void convertGregCalendarToDate() {

        GregorianCalendar greg = new GregorianCalendar();
        assertEquals("class java.util.Date", controller.convertGregCalendarToDate(greg).getClass().toString());
    }

    @Test
    void checkRoomAvailability() {

        controller.addRoom("SJG/03", 150, "Computer Laboratory");

        assertTrue(controller.checkRoomAvailability(controller.getRooms().get(0)));

        Date date = new Date();
        date.setYear(126);
        controller.getRooms().get(0).setUnavailabilityEndDate(date);
        assertFalse(controller.checkRoomAvailability(controller.getRooms().get(0)));

    }

    @Test
    void checkRoomAvailabilityAtDate() {
        controller.addRoom("SJG/03", 150, "Computer Laboratory");

        assertTrue(controller.checkRoomAvailabilityAtDate(controller.getRooms().get(0),new Date()));

        Date date = new Date();
        date.setYear(126);
        controller.getRooms().get(0).setUnavailabilityEndDate(date);
        assertFalse(controller.checkRoomAvailabilityAtDate(controller.getRooms().get(0), new Date()));
    }

    @Test
    void checkTermBookingTimeAppropriateness() {

        controller.addTermTimeRestriction(new TermTimeRestriction(controller.generateTermTimeRestrictionId(),termStart,termEnd,weekdaysTime,weekendsTime));
        assertFalse(controller.checkTermBookingTimeAppropriateness(booking));

    }

    @Test
    void compareTwoTimes() {
        Controller controller = new Controller();
        Date date1 = new Date();
        Date date2 = new Date();

        assertEquals(0, controller.compareTwoTimes(date1, date2));

        date1.setMinutes(date1.getMinutes() + 1);

        assertEquals(1, controller.compareTwoTimes(date1, date2));

        assertEquals(-1, controller.compareTwoTimes(date2, date1));
    }

    @Test
    void checkBookingCollision() {

        controller.addRoom("SJG/03", 150, "Computer Laboratory");

        assertFalse(controller.checkBookingCollision(booking, controller.getRooms().get(0)));

        controller.addBooking(booking, (int) controller.getRooms().get(0).getRoomId());

        assertTrue(controller.checkBookingCollision(booking, controller.getRooms().get(0)));

    }

    @Test
    void addBooking() {
        controller.addRoom("SJG/03", 150, "Computer Laboratory");

        controller.addBooking(booking, (int) controller.getRooms().get(0).getRoomId());

        assertEquals(booking, controller.getRooms().get(0).getBookings().get(0));

    }

    @Test
    void removeBooking() {
        controller.addRoom("SJG/03", 150, "Computer Laboratory");

        controller.addBooking(booking, (int) controller.getRooms().get(0).getRoomId());
        controller.removeBooking(1,1);
        assertEquals(0, controller.getRooms().get(0).getBookings().size());
    }

    @Test
    void getRoomStatus() {
        controller.addRoom("SJG/03", 150, "Computer Laboratory");
        assertEquals("AVAILABLE", controller.getRoomStatus(controller.getRoomById(1)));
    }

    @Test
    void generateRoomId() {
        assertEquals(1, controller.generateRoomId());
        assertEquals(2, controller.generateRoomId());
    }

    @Test
    void generateBookingId() {
        controller.addRoom("SJG/03", 150, "Computer Laboratory");

        assertEquals(1, controller.generateBookingId(1));
        assertEquals(2, controller.generateBookingId(1));
    }

    @Test
    void generateTermTimeRestrictionId() {
        assertEquals(1, controller.generateTermTimeRestrictionId());
        assertEquals(2, controller.generateTermTimeRestrictionId());
    }

    @Test
    void getRoomById() {
        controller.addRoom("SJG/03", 150, "Computer Laboratory");
        assertEquals(controller.getRooms().get(0), controller.getRoomById(1));

    }

    @Test
    void roomByIdExists() {
        assertFalse(controller.roomByIdExists(1));
        controller.addRoom("SJG/03", 150, "Computer Laboratory");
        assertTrue(controller.roomByIdExists(1));
    }

    @Test
    void getBookingById() {
        controller.addRoom("SJG/03", 150, "Computer Laboratory");
        controller.addBooking(booking, 1);

        assertEquals(booking, controller.getBookingById(1,1));
    }


    @Test
    void getTermTimeRestrictionById() {

        controller.addTermTimeRestriction(new TermTimeRestriction(controller.generateTermTimeRestrictionId(),termStart,termEnd,weekdaysTime,weekendsTime));
        assertEquals(controller.getTermRestrictions().get(0), controller.getTermTimeRestrictionById(1));
    }

    @Test
    void termTimeRestrictionByIdExists() {

        controller.addTermTimeRestriction(new TermTimeRestriction(controller.generateTermTimeRestrictionId(),termStart,termEnd,weekdaysTime,weekendsTime));
        assertTrue(controller.termTimeRestrictionByIdExists(1));

    }

    @Test
    void termRestrictionCanBeSet() {
        TermTimeRestriction term = new TermTimeRestriction(controller.generateTermTimeRestrictionId(),termStart,termEnd,weekdaysTime,weekendsTime);
        assertTrue(controller.termRestrictionCanBeSet(term));
        controller.addTermTimeRestriction(term);
        assertFalse(controller.termRestrictionCanBeSet(term));
    }

    @Test
    void intersection() {
        ArrayList<Integer> list1 = new ArrayList<Integer>(Arrays.asList(1, 2, 3, 4, 5, 6));
        ArrayList<Integer> list2 = new ArrayList<Integer>(Arrays.asList(4, 5, 6, 7, 8, 9));
        ArrayList<Integer> list3 = new ArrayList<Integer>(Arrays.asList(4, 5, 6));
        ArrayList<Integer> list4 = new ArrayList<Integer>(Arrays.asList(1, 2, 3));

        assertEquals(list3,controller.intersection(list1, list2));
        assertNotEquals(list4,controller.intersection(list1, list2));
    }

    @Test
    void checkTimeValidity() {

        assertTrue(controller.checkTimeValidity(start, end));
        end.setHours(14);
        end.setMinutes(29);
        assertFalse(controller.checkTimeValidity(start, end));
    }

    @Test
    void calculateBookingDuration() {
        assertEquals(60, controller.calculateBookingDuration(start, end));
    }

}