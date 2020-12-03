import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

public class ClerkGUI extends JFrame implements Runnable {
    private Controller controller;
    private int selectedRoomIndex;
    private int selectedBookingIndex;

    private Date zeroTimer = new Date();

    private JPanel panel1;
    private JPanel roomSearchPanel;
    private JRadioButton dateRadioButton_roomSearchPanel;
    private JDatePickerImpl JDatePickerImpl1_roomSearchPanel;
    private JRadioButton roomTypeRadioButton;
    private JRadioButton roomSizeRadioButton;
    private JComboBox comboBox1;
    private JTextField a12TextField;
    private JButton searchForRoomsButton;
    private JSpinner spinner1;
    private JTextArea roomsTextArea_roomSearchPanel;
    private JPanel roomInfoPanel;
    private JTextArea bookingListTextArea_roomInfoPanel;
    private JButton backButton_roomInfoPanel;
    private JButton bookRoomButton_roomInfoPanel;
    private JDatePickerImpl jDatePicker_roomInfoPanel;
    private JLabel idLabel_roomInfoPanel;
    private JLabel nameLabel_roomInfoPanel;
    private JLabel typeLabel_roomInfoPanel;
    private JLabel sizeLabel_roomInfoPanel;
    private JLabel statusLabel_roomInfoPanel;
    private JSpinner bookingStartSpinner_roomInfoPanel;
    private JSpinner bookingEndSpinner_roomInfoPanel;
    private JTextField nameTextField_roomInfoPanel;
    private JTextField surnameTextField_roomInfoPanel;
    private JTextField emailTextField_roomInfoPanel;
    private JTextField phoneNumberTextField_roomInfoPanel;
    private JTextField bookingNoteTextField_roomInfoPanel;
    private JTextField selectRoomTextField_roomSearchPanel;
    private JButton selectRoomButton_roomSearchPanel;
    private JButton selectBookingButton_roomInfoPanel;
    private JTextField bookingIdTextField_roomInfoPanel;
    private JPanel bookingInfoPanel;
    private JButton deleteBookingButton;
    private JButton backButton;
    private JTextField nameTextField_bookingInfoPanel;
    private JTextField reservationEndTextField_bookingInfoPanel;
    private JTextField emailTextField_bookingInfoPanel;
    private JTextField bookingNoteTextField_bookingInfoPanel;
    private JTextField surnameTextField_bookingInfoPanel;
    private JTextField phoneNumberTextField_bookingInfoPanel;
    private JTextField reservationStartTextField_bookingInfoPanel;
    private JTextField reservationDurationTextField_bookingInfoPanel;
    private JTextField dateTextField_bookingInfoPanel;
    private JTextField bookingIDTextField_bookingInfoPanel;
    private JButton termRestrictionsButton;
    private JPanel termRestrictionsPanel;
    private JTextArea termRestrictionsTextArea_termRestrictionsPanel;
    private JButton backButton1;
    private JSpinner spinner2;
    private JTextField idTextField_roomInfoPanel;
    private JTextField roomNameTextField_roomInfoPanel;
    private JTextField typeTextField_roomInfoPanel;
    private JTextField sizeTextField_roomInfoPanel;
    private JTextField statusTextField_roomInfoPanel;
    private JLabel roomsLabel;
    private JLabel bookingsLabel;


    public ClerkGUI(Controller controller)
    {
        super();
        this.controller = controller;
        initGUI();
        zeroTimer.setHours(0);
        zeroTimer.setMinutes(0);


        roomsTextArea_roomSearchPanel.setText("ID | Name| Type | Seats | Availability\n\n");
        bookingListTextArea_roomInfoPanel.setText("ID | Date| Start Time | End Time \n\n");
        termRestrictionsTextArea_termRestrictionsPanel.setText("ID | Term Start | Term End | Weekday Booking Time | Weekend Booking Time\n\n");


        searchForRoomsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                boolean informationprovided = true;

                if((dateRadioButton_roomSearchPanel.isSelected() && JDatePickerImpl1_roomSearchPanel.getJFormattedTextField().getValue() == null))
                {
                    informationprovided = false;
                    System.out.println("Fill in the Date and Time information!");
                    JOptionPane.showMessageDialog(getInstanceOfGUI(), "Fill in the date and time information!");

                }
                else if((dateRadioButton_roomSearchPanel.isSelected() && !getController().checkTimeValidity((Date) spinner1.getValue(),(Date) spinner2.getValue())))
                {
                    informationprovided = false;
                    System.out.println("Minimum allowed booking time is 30 minutes!");
                    JOptionPane.showMessageDialog(getInstanceOfGUI(), "Minimum allowed booking time is 30 minutes!");

                }
                else if((roomSizeRadioButton.isSelected() && a12TextField.getText().equals("")))
                {
                    informationprovided = false;
                    System.out.println("Fill in the room size information!");
                    JOptionPane.showMessageDialog(getInstanceOfGUI(), "Fill in the room size information!");
                }

                if(informationprovided)
                {
                    try
                    {
                        refreshRoomList(getController().generateFilteredRoomsClerk(getInstanceOfGUI()));
                    }
                    catch(Exception exception)
                    {
                        System.out.println(exception);
                        JOptionPane.showMessageDialog(getInstanceOfGUI(), "The given room size information is not valid!");
                    }
                }
            }
        });
        selectRoomButton_roomSearchPanel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                try{
                    setSelectedRoomIndex(Integer.parseInt(selectRoomTextField_roomSearchPanel.getText().trim()));
                    if(getController().roomByIdExists(getSelectedRoomIndex()))
                    {

                        roomSearchPanel.setVisible(false);
                        roomInfoPanel.setVisible(true);


                        refreshRoomInfoPanel();
                    }
                    else
                    {
                        JOptionPane.showMessageDialog(getInstanceOfGUI(), "Room by the given ID was not found");
                    }
                }
                catch (Exception ex)
                {
                    JOptionPane.showMessageDialog(getInstanceOfGUI(), "Room by the given ID was not found");
                    System.out.println(ex);
                }
            }
        });
        backButton_roomInfoPanel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                // set booking fields empty
                jDatePicker_roomInfoPanel.getJFormattedTextField().setValue(null);
                bookingStartSpinner_roomInfoPanel.setValue(zeroTimer);
                bookingEndSpinner_roomInfoPanel.setValue(zeroTimer);
                nameTextField_roomInfoPanel.setText("");
                surnameTextField_roomInfoPanel.setText("");
                emailTextField_roomInfoPanel.setText("");
                phoneNumberTextField_roomInfoPanel.setText("");
                bookingNoteTextField_roomInfoPanel.setText("");
                bookingIdTextField_roomInfoPanel.setText("");

                roomInfoPanel.setVisible(false);
                roomSearchPanel.setVisible(true);
            }
        });
        bookRoomButton_roomInfoPanel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                Booking booking = null;

                if(getController().roomByIdExists(getSelectedRoomIndex()))
                {

                    if((jDatePicker_roomInfoPanel.getJFormattedTextField().getValue() == null) || (nameTextField_roomInfoPanel.getText().trim().equals("")) || (surnameTextField_roomInfoPanel.getText().trim().equals(""))
                            || (emailTextField_roomInfoPanel.getText().trim().equals("")) || (phoneNumberTextField_roomInfoPanel.getText().trim().equals("")) || (bookingNoteTextField_roomInfoPanel.getText().trim().equals("")))
                    {
                        System.out.println("Fill in the booking information!");
                        JOptionPane.showMessageDialog(getInstanceOfGUI(), "Fill in the booking information!");

                    }
                    else if((!getController().checkTimeValidity((Date) bookingStartSpinner_roomInfoPanel.getValue(),(Date) bookingEndSpinner_roomInfoPanel.getValue())))
                    {

                        System.out.println("Minimum allowed booking time is 30 minutes!");
                        JOptionPane.showMessageDialog(getInstanceOfGUI(), "Minimum allowed booking time is 30 minutes.");

                    }
                    else
                    {
                        //create the booking
                        booking = new Booking(getController().generateBookingId(getSelectedRoomIndex()),getController().convertGregCalendarToDate((GregorianCalendar) jDatePicker_roomInfoPanel.getJFormattedTextField().getValue()),((Date) bookingStartSpinner_roomInfoPanel.getValue()),
                                ((Date) bookingEndSpinner_roomInfoPanel.getValue()), nameTextField_roomInfoPanel.getText().trim(), surnameTextField_roomInfoPanel.getText().trim(), emailTextField_roomInfoPanel.getText().trim(),
                                phoneNumberTextField_roomInfoPanel.getText().trim(), bookingNoteTextField_roomInfoPanel.getText().trim());
                    }

                    if(booking != null)
                    {

                        if(getController().canRoomBeBooked(booking, getSelectedRoomIndex()))
                        {
                            System.out.println("room was successfully booked");
                            bookRoom(booking);
                            JOptionPane.showMessageDialog(getInstanceOfGUI(), "The room was successfully booked!");

                            // set booking fields empty
                            jDatePicker_roomInfoPanel.getJFormattedTextField().setValue(null);
                            bookingStartSpinner_roomInfoPanel.setValue(zeroTimer);
                            bookingEndSpinner_roomInfoPanel.setValue(zeroTimer);
                            nameTextField_roomInfoPanel.setText("");
                            surnameTextField_roomInfoPanel.setText("");
                            emailTextField_roomInfoPanel.setText("");
                            phoneNumberTextField_roomInfoPanel.setText("");
                            bookingNoteTextField_roomInfoPanel.setText("");
                        }
                        else
                        {
                            System.out.println("room could not be booked at this time");
                            getController().getRoomById(getSelectedRoomIndex()).setNextBookingId(getController().getRoomById(getSelectedRoomIndex()).getNextBookingId() - 1);
                            JOptionPane.showMessageDialog(getInstanceOfGUI(), "The room could not be booked at this time.");
                        }
                    }
                }
                else
                {
                    System.out.println("This room no longer exists");
                    JOptionPane.showMessageDialog(getInstanceOfGUI(), "This room no longer exists");
                }

            }
        });
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                bookingInfoPanel.setVisible(false);
                roomInfoPanel.setVisible(true);
            }
        });
        selectBookingButton_roomInfoPanel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                try{
                    setSelectedBookingIndex(Integer.parseInt(bookingIdTextField_roomInfoPanel.getText().trim()));
                    if(getController().bookingByIdExists(getSelectedRoomIndex(),getSelectedBookingIndex()))
                    {
                        roomInfoPanel.setVisible(false);
                        bookingInfoPanel.setVisible(true);
                        refreshBookingInfoPanel();
                    }
                    else
                    {
                        JOptionPane.showMessageDialog(getInstanceOfGUI(), "Booking by the given ID was not found");
                    }
                }
                catch (Exception ex)
                {
                    JOptionPane.showMessageDialog(getInstanceOfGUI(), "Booking by the given ID was not found");
                    System.out.println(ex);
                }


            }
        });
        deleteBookingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                removeBooking();
                JOptionPane.showMessageDialog(getInstanceOfGUI(), "The booking was successfully deleted");
                bookingInfoPanel.setVisible(false);
                roomInfoPanel.setVisible(true);
            }
        });
        termRestrictionsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                roomSearchPanel.setVisible(false);
                termRestrictionsPanel.setVisible(true);
            }
        });
        backButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                termRestrictionsPanel.setVisible(false);
                roomSearchPanel.setVisible(true);
            }
        });
    }


    public void bookRoom(Booking booking)
    {
        controller.addBooking(booking, getSelectedRoomIndex());

    }

    public void removeBooking()
    {
        if(getController().bookingByIdExists(getSelectedRoomIndex(),getSelectedBookingIndex()))
        {
            getController().removeBooking(getSelectedRoomIndex(), getSelectedBookingIndex());
        }
        else
        {
            System.out.println("This booking no longer exists");
        }

    }

    public void refreshRoomInfoPanel()
    {
        if(getController().roomByIdExists(getSelectedRoomIndex()))
        {
            //Set Room id name type size labels
            idTextField_roomInfoPanel.setText(""+getController().getRoomById(getSelectedRoomIndex()).getRoomId());
            roomNameTextField_roomInfoPanel.setText(""+getController().getRoomById(getSelectedRoomIndex()).getRoomName());
            typeTextField_roomInfoPanel.setText(""+getController().getRoomById(getSelectedRoomIndex()).getTypeName());
            sizeTextField_roomInfoPanel.setText(""+getController().getRoomById(getSelectedRoomIndex()).getSize());
            statusTextField_roomInfoPanel.setText(""+getController().getRoomStatus(getController().getRoomById(getSelectedRoomIndex())));
        }
        else
        {
            System.out.println("room no longer exists");
        }
    }

    public void refreshBookingInfoPanel() {
        if (getController().bookingByIdExists(getSelectedRoomIndex(), getSelectedBookingIndex())) {



            Booking selectedBooking = getController().getBookingById(getSelectedRoomIndex(),getSelectedBookingIndex());

            bookingIDTextField_bookingInfoPanel.setText(Long.toString(selectedBooking.getBookingId()));
            dateTextField_bookingInfoPanel.setText(getController().generateDateString(selectedBooking.getDate()));

            reservationDurationTextField_bookingInfoPanel.setText(Integer.toString(selectedBooking.getReservationDuration()) + " minutes");

            reservationStartTextField_bookingInfoPanel.setText(getController().generateTimeString(selectedBooking.getReservationStart()));

            reservationEndTextField_bookingInfoPanel.setText(getController().generateTimeString(selectedBooking.getReservationEnd()));

            nameTextField_bookingInfoPanel.setText(selectedBooking.getName());
            surnameTextField_bookingInfoPanel.setText(selectedBooking.getSurname());
            emailTextField_bookingInfoPanel.setText(selectedBooking.getEmailAddress());
            phoneNumberTextField_bookingInfoPanel.setText(selectedBooking.getPhoneNumber());
            bookingNoteTextField_bookingInfoPanel.setText(selectedBooking.getBookingNote());


        } else {
            System.out.println("booking by this Id does not exist");
        }

    }

    public void run()
    {
        this.setVisible(true);
    }

    public void initGUI(){

        //Look and feel setup
        try {
            UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName() );
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.setContentPane(this.panel1);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        this.setTitle("Clerk GUI");
        this.setResizable(false);


        pack();
        this.setSize(600, 400);

    }
    public void createUIComponents()
    {
        //J Date Picker - roomSearchPanel

        UtilDateModel model = new UtilDateModel();
        JDatePanelImpl datePanel = new JDatePanelImpl(model);
        JDatePickerImpl datePicker = new JDatePickerImpl(datePanel);
        JDatePickerImpl1_roomSearchPanel = datePicker;


        // Room Type Combo Box - roomSearchPanel

        String[] roomStrings = { "Computer Lab", "Tutorial Room", "Lecture Theatre" };

        comboBox1 = new JComboBox(roomStrings);

        // JSpinner for time - roomSearchPanel

        JSpinner timeSpinner = new JSpinner( new SpinnerDateModel() );
        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(timeSpinner, "HH:mm");
        timeSpinner.setEditor(timeEditor);
        Date date1 = new Date();
        date1.setHours(0);
        date1.setMinutes(0);
        timeSpinner.setValue(date1); // will only show the current time

        spinner1 = timeSpinner;

        JSpinner timeSpinner5 = new JSpinner( new SpinnerDateModel() );
        JSpinner.DateEditor timeEditor5 = new JSpinner.DateEditor(timeSpinner5, "HH:mm");
        timeSpinner5.setEditor(timeEditor5);
        Date date2 = new Date();
        date2.setHours(0);
        date2.setMinutes(0);
        timeSpinner5.setValue(date2); // will only show the current time

        spinner2 = timeSpinner5;

        //J Date Picker - roomInfoPanel

        UtilDateModel model4 = new UtilDateModel();
        JDatePanelImpl datePanel4 = new JDatePanelImpl(model4);
        JDatePickerImpl datePicker4 = new JDatePickerImpl(datePanel4);
        jDatePicker_roomInfoPanel = datePicker4;

        // JSpinners for time - roomInfoPanel

        JSpinner timeSpinner2 = new JSpinner( new SpinnerDateModel() );
        JSpinner.DateEditor timeEditor2 = new JSpinner.DateEditor(timeSpinner2, "HH:mm");
        timeSpinner2.setEditor(timeEditor2);
        timeSpinner2.setValue(date1); // will only show the current time


        JSpinner timeSpinner3 = new JSpinner( new SpinnerDateModel() );
        JSpinner.DateEditor timeEditor3 = new JSpinner.DateEditor(timeSpinner3, "HH:mm");
        timeSpinner3.setEditor(timeEditor3);
        timeSpinner3.setValue(date2); // will only show the current time

        bookingStartSpinner_roomInfoPanel = timeSpinner2;
        bookingEndSpinner_roomInfoPanel = timeSpinner3;


    }

    public void refreshRoomList(ArrayList<Room> rooms)
    {
        //refresh roomSearchPanel
        roomsLabel.setText("Rooms ( " + getController().getRooms().size() + " )");
        roomsTextArea_roomSearchPanel.setText("ID | Name| Type | Seats | Availability\n\n");

        for(int i = 0; i < rooms.size(); i++)
        {
            Room currentRoom = rooms.get(i);
            roomsTextArea_roomSearchPanel.append(currentRoom.getRoomId() + " | " + currentRoom.getRoomName() + " | " + currentRoom.getTypeName() + " | " + currentRoom.getSize() + "\n");

        }

        refreshRoomInfoPanel();
    }

    public void refreshBookingList()
    {
        //refresh roomSearchPanel
        bookingsLabel.setText("Bookings ( " + getController().getRoomById(getSelectedRoomIndex()).getBookings().size() + " )");
        bookingListTextArea_roomInfoPanel.setText("ID | Date| Start Time | End Time \n\n");


        for(int i = 0; i < getController().getRoomById(getSelectedRoomIndex()).getBookings().size(); i++)
        {
            Booking currentBooking = getController().getRoomById(getSelectedRoomIndex()).getBookings().get(i);
            bookingListTextArea_roomInfoPanel.append(currentBooking.getBookingId() + " | " + getController().generateDateString(currentBooking.getDate()) + " | " + getController().generateTimeString(currentBooking.getReservationStart())
                    + " | " + getController().generateTimeString(currentBooking.getReservationEnd()) + "\n");

        }


    }

    public void refreshTermRestrictionList()
    {
        termRestrictionsTextArea_termRestrictionsPanel.setText("ID | Term Start | Term End | Weekday Booking Time | Weekend Booking Time\n\n");

        for(int i = 0; i < getController().getTermRestrictions().size(); i++)
        {
            TermTimeRestriction currentTermRestriction = getController().getTermRestrictions().get(i);
            termRestrictionsTextArea_termRestrictionsPanel.append(currentTermRestriction.getTermTimeRestrictionId() + " | " + getController().generateDateString(currentTermRestriction.getRestrictionStartDate()) +
                    " | " + getController().generateDateString(currentTermRestriction.getRestrictionEndDate()) + " | " + getController().generateTimeString(currentTermRestriction.getWeekdayBookingTimeRestriction()) + " | "
                    + getController().generateTimeString(currentTermRestriction.getWeekendBookingTimeRestriction()) +  "\n");

        }


    }
    //Getters and Setters

    public JPanel getPanel1() {
        return panel1;
    }

    public void setPanel1(JPanel panel1) {
        this.panel1 = panel1;
    }

    public Controller getController() {
        return controller;
    }

    public int getSelectedRoomIndex() {
        return selectedRoomIndex;
    }

    public void setSelectedRoomIndex(int selectedRoomIndex) {
        this.selectedRoomIndex = selectedRoomIndex;
    }

    public int getSelectedBookingIndex() {
        return selectedBookingIndex;
    }

    public void setSelectedBookingIndex(int selectedBookingIndex) {
        this.selectedBookingIndex = selectedBookingIndex;
    }

    public JRadioButton getDateRadioButton_roomSearchPanel() {
        return dateRadioButton_roomSearchPanel;
    }

    public void setDateRadioButton_roomSearchPanel(JRadioButton dateRadioButton_roomSearchPanel) {
        this.dateRadioButton_roomSearchPanel = dateRadioButton_roomSearchPanel;
    }

    public JDatePickerImpl getJDatePickerImpl1_roomSearchPanel() {
        return JDatePickerImpl1_roomSearchPanel;
    }

    public void setJDatePickerImpl1_roomSearchPanel(JDatePickerImpl JDatePickerImpl1_roomSearchPanel) {
        this.JDatePickerImpl1_roomSearchPanel = JDatePickerImpl1_roomSearchPanel;
    }

    public JRadioButton getRoomTypeRadioButton() {
        return roomTypeRadioButton;
    }

    public void setRoomTypeRadioButton(JRadioButton roomTypeRadioButton) {
        this.roomTypeRadioButton = roomTypeRadioButton;
    }

    public JRadioButton getRoomSizeRadioButton() {
        return roomSizeRadioButton;
    }

    public void setRoomSizeRadioButton(JRadioButton roomSizeRadioButton) {
        this.roomSizeRadioButton = roomSizeRadioButton;
    }

    public JSpinner getSpinner1() {
        return spinner1;
    }

    public void setSpinner1(JSpinner spinner1) {
        this.spinner1 = spinner1;
    }

    public JSpinner getSpinner2() {
        return spinner2;
    }

    public void setSpinner2(JSpinner spinner2) {
        this.spinner2 = spinner2;
    }

    public ClerkGUI getInstanceOfGUI()
    {
        return this;
    }

    public JComboBox getComboBox1() {
        return comboBox1;
    }

    public void setComboBox1(JComboBox comboBox1) {
        this.comboBox1 = comboBox1;
    }

    public JTextField getA12TextField() {
        return a12TextField;
    }

    public void setA12TextField(JTextField a12TextField) {
        this.a12TextField = a12TextField;
    }
}
