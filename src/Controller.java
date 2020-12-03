import javax.swing.*;
import java.time.*;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;


public class Controller {

    public ClerkGUI clerkGUI1, clerkGUI2, clerkGUI3;
    public ManagerGUI managerGUI;
    public ArrayList<Room> rooms;
    private ArrayList<TermTimeRestriction> termRestrictions;
    private long nextRoomId = 0;
    private long nextTermTimeRestrictionId = 0;

    public Controller()
    {
        rooms = new ArrayList<Room>();
        termRestrictions = new ArrayList<TermTimeRestriction>();
    }

    public void linkGUIs (ClerkGUI clerkGUI1, ClerkGUI clerkGUI2, ClerkGUI clerkGUI3, ManagerGUI managerGUI) {

        this.clerkGUI1 = clerkGUI1;
        this.clerkGUI2 = clerkGUI2;
        this.clerkGUI3 = clerkGUI3;
        this.managerGUI = managerGUI;
    }

    public void notifyOfTermRestrictionChange()
    {
        managerGUI.refreshTermRestrictionList();
        clerkGUI1.refreshTermRestrictionList();
        clerkGUI2.refreshTermRestrictionList();
        clerkGUI3.refreshTermRestrictionList();
    }

    public void notifyOfRoomChange()
    {
        clerkGUI1.refreshRoomList(generateFilteredRoomsClerk(clerkGUI1));
        clerkGUI2.refreshRoomList(generateFilteredRoomsClerk(clerkGUI2));
        clerkGUI3.refreshRoomList(generateFilteredRoomsClerk(clerkGUI3));
        managerGUI.refreshRoomList(generateFilteredRoomsManager(managerGUI));

    }

    public ArrayList<Room> generateFilteredRoomsClerk(ClerkGUI clerkGUI)
    {
        ArrayList <Room> rooms = new ArrayList<Room>();
        ArrayList <Room> rooms2 = new ArrayList<Room>();
        ArrayList <Room> rooms3 = new ArrayList<Room>();
        ArrayList <Room> combinedRooms = new ArrayList<Room>();

        boolean dateAndTimeFilter = clerkGUI.getDateRadioButton_roomSearchPanel().isSelected();
        boolean roomTypeFilter = clerkGUI.getRoomTypeRadioButton().isSelected();
        boolean roomSizeFilter = clerkGUI.getRoomSizeRadioButton().isSelected();

        Date date = null;
        Date startTime = null;
        Date endTime = null;

        try
        {
            date = convertGregCalendarToDate((GregorianCalendar) clerkGUI.getJDatePickerImpl1_roomSearchPanel().getJFormattedTextField().getValue());
            startTime = (Date) clerkGUI.getSpinner1().getValue();
            endTime = (Date) clerkGUI.getSpinner2().getValue();
        }
        catch (Exception ex)
        {

        }

        if(dateAndTimeFilter || roomTypeFilter || roomSizeFilter) {

            if (dateAndTimeFilter) {
                rooms.addAll(filterRoomsByDateAndTime(date, startTime, endTime));
                combinedRooms.addAll(rooms);
            }

            if (roomTypeFilter) {

                rooms2.addAll(filterRoomsByRoomType(clerkGUI.getComboBox1().getSelectedItem().toString()));
                if(combinedRooms.size() == 0)
                {
                    combinedRooms.addAll(rooms2);
                }
                else
                {
                    combinedRooms = (ArrayList<Room>) intersection(combinedRooms,rooms2);
                }

            }

            if (roomSizeFilter) {

                rooms3.addAll(filterRoomsByRoomSize(Integer.parseInt(clerkGUI.getA12TextField().getText())));
                if(combinedRooms.size() == 0)
                {
                    combinedRooms.addAll(rooms3);
                }
                else
                {
                    combinedRooms = (ArrayList<Room>) intersection(combinedRooms,rooms3);
                }
            }
        }
        else
        {
            combinedRooms.addAll(getRooms());
            return combinedRooms;

        }

        return combinedRooms;
    }

    public ArrayList<Room> generateFilteredRoomsManager(ManagerGUI managerGUI)
    {
        ArrayList <Room> rooms2 = new ArrayList<Room>();
        ArrayList <Room> rooms3 = new ArrayList<Room>();
        ArrayList <Room> combinedRooms = new ArrayList<Room>();

        boolean roomTypeFilter = managerGUI.getRoomTypeRadioButton().isSelected();
        boolean roomSizeFilter = managerGUI.getRoomSizeRadioButton().isSelected();

        if(roomTypeFilter || roomSizeFilter) {

            if (roomTypeFilter) {

                rooms2.addAll(filterRoomsByRoomType(managerGUI.getComboBox1().getSelectedItem().toString()));

                    combinedRooms.addAll(rooms2);
            }

            if (roomSizeFilter) {

                rooms3.addAll(filterRoomsByRoomSize(Integer.parseInt(managerGUI.getA12TextField().getText())));
                if(combinedRooms.size() == 0)
                {
                    combinedRooms.addAll(rooms3);
                }
                else
                {
                    combinedRooms = (ArrayList<Room>) intersection(combinedRooms,rooms3);
                }
            }
        }
        else
        {
            combinedRooms.addAll(getRooms());
            return combinedRooms;

        }

        return combinedRooms;
    }

    public void notifyOfBookingListChange()
    {
        clerkGUI1.refreshBookingList();
        clerkGUI2.refreshBookingList();
        clerkGUI3.refreshBookingList();
        managerGUI.refreshBookingList();
    }

    public void addRoom(String roomName, int size, String typeName)
    {
        getRooms().add(new Room(generateRoomId(),roomName,size,typeName));
        notifyOfRoomChange();
    }

    public void deleteRoom(int roomIndex)
    {
        getRooms().remove(getRoomById(roomIndex));
        notifyOfRoomChange();
    }

    public Date convertGregCalendarToDate(GregorianCalendar calendar)
    {
        ZonedDateTime zdt = calendar.toZonedDateTime() ;
        Instant instant = zdt.toInstant() ;

        Date date = Date.from(instant);
        return date;
    }

    public boolean checkRoomAvailability(Room room)
    {
        Date currentDate = new Date();

        if(currentDate.compareTo(room.getUnavailabilityEndDate()) > 0)
        {
            return true;
        }
        return false;
    }

    public boolean checkRoomAvailabilityAtDate(Room room, Date date)
    {
        if(date.compareTo(room.getUnavailabilityEndDate()) > 0)
        {
            return true;
        }
        return false;
    }


    public boolean checkTermBookingTimeAppropriateness(Booking booking)
    {

        //booking dates
        Date bookingDay =  booking.getDate();

        //booking start time
        Date bookingStartTime =  booking.getReservationStart();

        for(int i = 0; i < getTermRestrictions().size(); i++)
        {
            // restriction dates
            Date termRestrictionStart = getTermRestrictions().get(i).getRestrictionStartDate();
            Date termRestrictionEnd = getTermRestrictions().get(i).getRestrictionEndDate();

            // restriction times
            Date appriopriateReservationTimeWeekdays = getTermRestrictions().get(i).getWeekdayBookingTimeRestriction();
            Date appriopriateReservationTimeWeekends = getTermRestrictions().get(i).getWeekendBookingTimeRestriction();


            if((bookingDay.compareTo(termRestrictionStart) >= 0) && (bookingDay.compareTo(termRestrictionEnd) <=0))
            {
                //check if weekend
                if(isWeekend(convertToLocalDateViaInstant(bookingDay)))
                {
                    if(compareTwoTimes(bookingStartTime,appriopriateReservationTimeWeekends) < 0)
                    {
                        return false;
                    }
                }
                else
                {
                    if(compareTwoTimes(bookingStartTime,appriopriateReservationTimeWeekdays) < 0)
                    {
                        return false;
                    }
                }

            }
        }
        return true;
    }

    public int compareTwoTimes (Date date1, Date date2)
    {
        int date1Hours = date1.getHours();
        int date1Minutes = date1.getMinutes();

        int date2Hours = date2.getHours();
        int date2Minutes = date2.getMinutes();

        if(date1Hours > date2Hours)
        {
            return 1;
        }
        else if (date1Hours == date2Hours)
        {
            if(date1Minutes > date2Minutes)
            {
                return 1;
            }
            else if(date1Minutes == date2Minutes)
            {
                return 0;
            }
            else if (date1Minutes < date2Minutes)
            {
                return -1;
            }
        }

        return -1;
    }

    public boolean checkBookingCollision(Booking booking, Room room)
    {

        for(int i = 0; i < room.getBookings().size(); i++)
        {
            //check if booking is on the same date as an existing booking
            if(booking.getDate().compareTo(room.getBookings().get(i).getDate()) == 0)
            {

                Date start1 = booking.getReservationStart();
                Date end1 = booking.getReservationEnd();

                Date start2 = room.getBookings().get(i).getReservationStart();
                Date end2 = room.getBookings().get(i).getReservationEnd();

                if(((compareTwoTimes(start1, start2) >= 0) && (compareTwoTimes(start1, end2) < 0)) || ((compareTwoTimes(end1, start2) > 0) && (compareTwoTimes(end1,end2) <= 0))
                        || ((compareTwoTimes(start1, start2) < 0) && (compareTwoTimes(end1, end2) > 0)))
                {
                    return true;
                }
            }
        }

        return false;
    }

    public synchronized void addBooking(Booking booking, int selectedRoomIndex)
    {
        if(canRoomBeBooked(booking,selectedRoomIndex))
        {
            getRoomById(selectedRoomIndex).getBookings().add(booking);
            notifyOfBookingListChange();
        }

    }
    public void removeBooking(int selectedRoomIndex, int bookingId)
    {
        getRoomById(selectedRoomIndex).getBookings().remove(getBookingById(selectedRoomIndex,bookingId));
        notifyOfBookingListChange();

    }


    public static boolean isWeekend(LocalDate date) {
        DayOfWeek dayOfWeek = DayOfWeek.of(date.get(ChronoField.DAY_OF_WEEK));
        switch (dayOfWeek) {
            case SATURDAY:
            case SUNDAY:
                return true;
            default:
                return false;
        }
    }

    public LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }


    public String getRoomStatus(Room room)
    {
        if(checkRoomAvailability(room) == true)
        {
            return "AVAILABLE";
        }
        return "UNAVAILABLE UNTIL " + generateDateString(room.getUnavailabilityEndDate());
    }

    public long generateRoomId()
    {
        setNextRoomId(getNextRoomId() + 1);
        return getNextRoomId();
    }

    public long generateBookingId(long selectedRoomId)
    {
        if (roomByIdExists(selectedRoomId))
        {
            getRoomById(selectedRoomId).setNextBookingId(getRoomById(selectedRoomId).getNextBookingId() + 1);
            return getRoomById(selectedRoomId).getNextBookingId();
        }

        return 0;
    }

    public long generateTermTimeRestrictionId()
    {
        setNextTermTimeRestrictionId(getNextTermTimeRestrictionId() + 1);
        return getNextTermTimeRestrictionId();
    }



    public Room getRoomById(long roomId)
    {
        for(int i = 0; i < getRooms().size(); i++)
        {
            if(getRooms().get(i).getRoomId() == roomId)
            {
                return getRooms().get(i);
            }
        }
        return getRooms().get(0);
    }

    public boolean roomByIdExists(long roomId)
    {
        for(int i = 0; i < getRooms().size(); i++)
        {
            if(getRooms().get(i).getRoomId() == roomId)
            {
                return true;
            }
        }
        return false;
    }

    public Booking getBookingById(long roomId, long bookingId)
    {
        for(int i = 0; i < getRooms().size(); i++)
        {
            if(getRooms().get(i).getRoomId() == roomId)
            {
                for(int j = 0; j < getRooms().get(i).getBookings().size(); j++)
                {
                    if(getRooms().get(i).getBookings().get(j).getBookingId() == bookingId)
                    {
                        return getRooms().get(i).getBookings().get(j);
                    }
                }
            }
        }

        return getRooms().get(0).getBookings().get(0);
    }

    public boolean bookingByIdExists(long roomId, long bookingId)
    {
        for(int i = 0; i < getRooms().size(); i++)
        {
            if(getRooms().get(i).getRoomId() == roomId)
            {
                for(int j = 0; j < getRooms().get(i).getBookings().size(); j++)
                {
                    if(getRooms().get(i).getBookings().get(j).getBookingId() == bookingId)
                    {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public TermTimeRestriction getTermTimeRestrictionById(long termId)
    {
        for(int i = 0; i < getTermRestrictions().size(); i++)
        {
            if(getTermRestrictions().get(i).getTermTimeRestrictionId() == termId)
            {
                return getTermRestrictions().get(i);
            }
        }
        return getTermRestrictions().get(0);
    }

    public boolean termTimeRestrictionByIdExists(long termId)
    {
        for(int i = 0; i < getTermRestrictions().size(); i++)
        {
            if(getTermRestrictions().get(i).getTermTimeRestrictionId() == termId)
            {
                return true;
            }
        }
        return false;
    }


    public boolean termRestrictionCanBeSet(TermTimeRestriction termRestriction)
    {
        for(int i = 0; i < getTermRestrictions().size(); i++)
        {
                Date start1 = termRestriction.getRestrictionStartDate();
                Date end1 = termRestriction.getRestrictionEndDate();

                Date start2 = getTermRestrictions().get(i).getRestrictionStartDate();
                Date end2 = getTermRestrictions().get(i).getRestrictionEndDate();

                if(((start1.compareTo(start2) >= 0) && (start1.compareTo(end2) < 0)) || ((end1.compareTo(start2) > 0) && (end1.compareTo(end2) <= 0))
                        || ((start1.compareTo(start2) < 0) && (end1.compareTo(end2) > 0)))
                {
                    return false;
                }
            }
        return true;

    }


    public ArrayList<Room> filterRoomsByDateAndTime(Date date, Date startingTime, Date endingTime) {
        ArrayList<Room> rooms = new ArrayList<Room>();
        Booking booking = new Booking(0, date, startingTime, endingTime, "", "", "", "", "");

        for (int i = 0; i < getRooms().size(); i++) {

            if (canRoomBeBooked(booking, getRooms().get(i).getRoomId())) {

                rooms.add(getRooms().get(i));

            } else {

            }

        }
            return rooms;
        }

    public ArrayList<Room> filterRoomsByRoomType(String roomType) {

        ArrayList<Room> rooms = new ArrayList<Room>();

        for (int i = 0; i < getRooms().size(); i++) {

            if (getRooms().get(i).getTypeName().equals(roomType)) {

                rooms.add(getRooms().get(i));

            } else {

            }

        }
        return rooms;
    }

    public ArrayList<Room> filterRoomsByRoomSize(int size) {

        ArrayList<Room> rooms = new ArrayList<Room>();

        for (int i = 0; i < getRooms().size(); i++) {

            if (getRooms().get(i).getSize() >= size) {

                rooms.add(getRooms().get(i));

            } else {

            }
        }
        return rooms;
    }

    public boolean canRoomBeBooked(Booking booking, long selectedRoomIndex)
    {
        boolean roomIsAvailableForBookingAtGivenDate = checkRoomAvailabilityAtDate(getRoomById(selectedRoomIndex), booking.getDate());
        boolean bookingTimeIsAccordingToTermRestrictions = checkTermBookingTimeAppropriateness(booking);
        boolean anyBookingCollisions = checkBookingCollision(booking, getRoomById(selectedRoomIndex));


        if(roomIsAvailableForBookingAtGivenDate && bookingTimeIsAccordingToTermRestrictions && !anyBookingCollisions )
        {
            return true;
        }

        return false;
    }


    public <T> List<T> intersection(List<T> list1, List<T> list2) {
        List<T> list = new ArrayList<T>();

        for (T t : list1) {
            if(list2.contains(t)) {
                list.add(t);
            }
        }

        return list;
    }

    public boolean checkTimeValidity(Date reservationStart, Date reservationEnd)
    {
        if(calculateBookingDuration(reservationStart,reservationEnd) < 30)
        {
            return false;
        }

        return true;
    }

    public int calculateBookingDuration(Date reservationStart, Date reservationEnd)
    {
        // calculate reservation duration

        int h1 = reservationStart.getHours();
        int h2 = reservationEnd.getHours();

        int m1 = reservationStart.getMinutes();
        int m2 = reservationEnd.getMinutes();

        int reservationDuration = (h2 - h1) * 60 + m2 - m1;

        return reservationDuration;
    }

    public String generateTimeString(Date date)
    {
        String time = "";
        if(date.getHours() < 10)
        {
            time = "0"+date.getHours() +":";
        }
        else
        {
            time = Integer.toString(date.getHours())+":";
        }
        if (date.getMinutes() < 10) {
            time = time + "0" + date.getMinutes();
        }
        else
        {
            time = time + Integer.toString(date.getMinutes());
        }

        return time;
    }

    public String generateDateString(Date date)
    {
        String dateString = "";

        //Day
        if(date.getDate() < 10)
        {
            dateString = "0" + date.getDate() + ".";
        }
        else
        {
            dateString = date.getDate() + ".";
        }

        //Month
        if((date.getMonth()+1) < 10)
        {
            dateString = dateString + "0" + (date.getMonth()+1) + ".";
        }
        else
        {
            dateString = dateString + (date.getMonth()+1) + ".";
        }

       //Year
            dateString = dateString + (date.getYear() + 1900);

        return dateString;
    }


    // Getters and Setters


    public ArrayList<Room> getRooms() {
        return rooms;
    }

    public void setRooms(ArrayList<Room> rooms) {
        this.rooms = rooms;
    }

    public ArrayList<TermTimeRestriction> getTermRestrictions() {
        return termRestrictions;
    }

    public void setTermRestrictions(ArrayList<TermTimeRestriction> termRestrictions) {
        this.termRestrictions = termRestrictions;
    }

    public void addTermTimeRestriction(TermTimeRestriction restriction)
    {
        termRestrictions.add(restriction);
        notifyOfTermRestrictionChange();

    }

    public void removeTermTimeRestriction(int id)
    {
        termRestrictions.remove(getTermTimeRestrictionById(id));
        notifyOfTermRestrictionChange();
    }

    public long getNextRoomId() {
        return nextRoomId;
    }

    public void setNextRoomId(long nextRoomId) {
        this.nextRoomId = nextRoomId;
    }

    public long getNextTermTimeRestrictionId() {
        return nextTermTimeRestrictionId;
    }

    public void setNextTermTimeRestrictionId(long nextTermTimeRestrictionId) {
        this.nextTermTimeRestrictionId = nextTermTimeRestrictionId;
    }

    public ClerkGUI getClerkGUI1() {
        return clerkGUI1;
    }

    public void setClerkGUI1(ClerkGUI clerkGUI1) {
        this.clerkGUI1 = clerkGUI1;
    }

    public ClerkGUI getClerkGUI2() {
        return clerkGUI2;
    }

    public void setClerkGUI2(ClerkGUI clerkGUI2) {
        this.clerkGUI2 = clerkGUI2;
    }

    public ClerkGUI getClerkGUI3() {
        return clerkGUI3;
    }

    public void setClerkGUI3(ClerkGUI clerkGUI3) {
        this.clerkGUI3 = clerkGUI3;
    }

    public ManagerGUI getManagerGUI() {
        return managerGUI;
    }

    public void setManagerGUI(ManagerGUI managerGUI) {
        this.managerGUI = managerGUI;
    }
}
