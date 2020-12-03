import net.sourceforge.jdatepicker.impl.JDatePanelImpl;
import net.sourceforge.jdatepicker.impl.JDatePickerImpl;
import net.sourceforge.jdatepicker.impl.UtilDateModel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

public class ManagerGUI extends JFrame implements Runnable {
    private int selectedRoomIndex;
    private int selectedTermRestrictionIndex;
    private Date zeroTimer = new Date();


    private int number;
    private Controller controller;

    private JPanel panel1;
    private JButton manageRoomsButton;
    private JButton setTermRestrictionsButton;
    private JPanel menuPanel;
    private JPanel roomSearchPanel;
    private JDatePickerImpl JDatePickerImpl1;
    private JRadioButton roomTypeRadioButton;
    private JRadioButton roomSizeRadioButton;
    private JComboBox comboBox1;
    private JTextField a12TextField;
    private JButton addRoomButton;
    private JSpinner spinner1;
    private JPanel addRoomPanel;
    private JComboBox addRoom_typeComboBox;
    private JTextField addRoom_sizeTextField;
    private JTextField addRoom_nameTextField;
    private JButton backButton;
    private JButton confirmButton;
    private JTextArea roomsTextArea_roomSearchPanel;
    private JTextField selectRoomTextField_roomSearchPanel;
    private JButton selectRoomButton_roomSearchPanel;
    private JPanel roomInfoPanel;
    private JButton backButton_roomInfoPanel;
    private JButton deleteButton;
    private JTextArea bookingListTextArea_roomInfoPanel;
    private JDatePickerImpl JDatePickerStart_termRestrictionsPanel;
    private JDatePickerImpl JDatePickerEnd_termRestrictionsPanel;
    private JPanel termRestrictionsPanel;
    private JButton backButton_termRestrictionsPanel;
    private JSpinner weekendsSpiner_termRestrictionsPanel;
    private JSpinner weekdaysSpinner_termRestrictionsPanel;
    private JButton setRestrictionButton_termRestrictionsPanel;
    private JButton markAsUnavailableButton;
    private JDatePickerImpl JDatePicker_roomInfoPanel;
    private JTextField noteTextField_roomInfoPanel;
    private JTextArea termRestrictionsTextArea_termRestrictionsPanel;
    private JButton removeRestrictionButton;
    private JTextField termRestrictionIdTextField_termRestrictionPanel;
    private JButton searchForRoomsButton;
    private JButton backButton1;
    private JTextField idTextField_roomInfoPanel;
    private JTextField nameTextField_roomInfoPanel;
    private JTextField typeTextField_roomInfoPanel;
    private JTextField sizeTextField_roomInfoPanel;
    private JTextField statusTextField_roomInfoPanel;
    private JTextField currentNoteTextField_roomInfoPanel;
    private JLabel roomsLabel;
    private JLabel bookingsLabel;
    private JSpinner spinner2;

    public ManagerGUI(Controller controller) {

        super();
        this.controller = controller;
        initGUI();
        zeroTimer.setHours(0);
        zeroTimer.setMinutes(0);


        roomsTextArea_roomSearchPanel.setText("ID | Name| Type | Seats | Availability\n\n");
        bookingListTextArea_roomInfoPanel.setText("ID | Date| Start Time | End Time \n\n");
        termRestrictionsTextArea_termRestrictionsPanel.setText("ID | Term Start | Term End | Weekday Booking Time | Weekend Booking Time\n\n");


        manageRoomsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                menuPanel.setVisible(false);
                roomSearchPanel.setVisible(true);
            }
        });
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                addRoom_nameTextField.setText("");
                addRoom_sizeTextField.setText("");

               addRoomPanel.setVisible(false);
               roomSearchPanel.setVisible(true);

               addRoom_typeComboBox.setSelectedIndex(0);

            }
        });
        addRoomButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                roomSearchPanel.setVisible(false);
                addRoomPanel.setVisible(true);

            }
        });
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                if(addRoom_nameTextField.getText().trim().equals("") || addRoom_sizeTextField.getText().trim().equals(""))
                {
                    JOptionPane.showMessageDialog(getInstanceOfGUI(), "Fill in the room information!");
                }
                else
                {
                    getController().addRoom(addRoom_nameTextField.getText().trim(),Integer.parseInt(addRoom_sizeTextField.getText().trim()),addRoom_typeComboBox.getSelectedItem().toString());
                    JOptionPane.showMessageDialog(getInstanceOfGUI(), "The room was successfully added.");
                    addRoom_nameTextField.setText("");
                    addRoom_sizeTextField.setText("");
                    addRoom_typeComboBox.setSelectedIndex(0);
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

                roomInfoPanel.setVisible(false);
                roomSearchPanel.setVisible(true);
            }
        });
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                removeRoom();
                JOptionPane.showMessageDialog(getInstanceOfGUI(), "The room was successfully deleted");
                roomInfoPanel.setVisible(false);
                roomSearchPanel.setVisible(true);

            }
        });
        setTermRestrictionsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                menuPanel.setVisible(false);
                termRestrictionsPanel.setVisible(true);

            }
        });
        backButton_termRestrictionsPanel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                JDatePickerStart_termRestrictionsPanel.getJFormattedTextField().setValue(null);
                JDatePickerEnd_termRestrictionsPanel.getJFormattedTextField().setValue(null);
                weekdaysSpinner_termRestrictionsPanel.setValue(zeroTimer);
                weekendsSpiner_termRestrictionsPanel.setValue(zeroTimer);
                termRestrictionIdTextField_termRestrictionPanel.setText("");

                termRestrictionsPanel.setVisible(false);
                menuPanel.setVisible(true);
            }
        });
        setRestrictionButton_termRestrictionsPanel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                setTermRestriction();

            }
        });
        markAsUnavailableButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                if(JDatePicker_roomInfoPanel.getJFormattedTextField().getValue() == null)
                {
                    JOptionPane.showMessageDialog(getInstanceOfGUI(), "Set the date!");
                }

                else
                {
                    setRoomAsUnavailable(getController().convertGregCalendarToDate((GregorianCalendar) JDatePicker_roomInfoPanel.getJFormattedTextField().getValue()), noteTextField_roomInfoPanel.getText());

                    getController().notifyOfRoomChange();

                    JOptionPane.showMessageDialog(getInstanceOfGUI(), "Room set as unavailable until: " + getController().generateDateString(getController().getRoomById(getSelectedRoomIndex()).getUnavailabilityEndDate()));

                }
            }
        });
        removeRestrictionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                try
                {
                    setSelectedTermRestrictionIndex(Integer.parseInt(termRestrictionIdTextField_termRestrictionPanel.getText().trim()));
                    removeRestriction();
                }
                catch (Exception ex)
                {
                    JOptionPane.showMessageDialog(getInstanceOfGUI(), "Restriction does not exist.");
                }


            }
        });
        backButton1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                roomSearchPanel.setVisible(false);
                menuPanel.setVisible(true);
            }
        });
        searchForRoomsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                boolean informationprovided = true;

                if((roomSizeRadioButton.isSelected() && a12TextField.getText().equals("")))
                {
                    informationprovided = false;
                    JOptionPane.showMessageDialog(getInstanceOfGUI(), "Fill in the room size information!");
                }

                if(informationprovided)
                {
                    try
                    {
                        refreshRoomList(getController().generateFilteredRoomsManager(getInstanceOfGUI()));
                    }
                    catch(Exception exception)
                    {
                        System.out.println(exception);
                        JOptionPane.showMessageDialog(getInstanceOfGUI(), "The given room size information is not valid!");
                    }
                }
            }
        });
    }

    public void removeRestriction()
    {
        if (getController().termTimeRestrictionByIdExists(getSelectedTermRestrictionIndex()))
        {
            getController().removeTermTimeRestriction(getSelectedTermRestrictionIndex());
            JOptionPane.showMessageDialog(getInstanceOfGUI(), "Restriction removed.");
        }
        else
        {
            System.out.println("Term restriction by the given id does not exist");
            JOptionPane.showMessageDialog(getInstanceOfGUI(), "Restriction does not exist.");
        }

    }

    public void removeRoom()
    {
        if(getController().roomByIdExists(getSelectedRoomIndex()))
        {
            getController().deleteRoom(getSelectedRoomIndex());
        }
        else
        {
            System.out.println("This room no longer exists");
        }
    }


    public void setRoomAsUnavailable(Date unavailabilityEndDate, String note)
    {

        // Set the room as unavailable for booking under current new info (date, note).
        getController().getRoomById(getSelectedRoomIndex()).setUnavailabilityEndDate(unavailabilityEndDate);

        getController().getRoomById(getSelectedRoomIndex()).setUnavailabilityNote(note);

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

        this.setTitle("Manager GUI");
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
        JDatePickerImpl1 = datePicker;


        // Room Type Combo Box - roomSearchPanel and addRoomPanel

        String[] roomStrings = { "Computer Lab", "Tutorial Room", "Lecture Theatre" };

        comboBox1 = new JComboBox(roomStrings);
        addRoom_typeComboBox = new JComboBox(roomStrings);

        // JSpinner for time - roomSearchPanel

        JSpinner timeSpinner = new JSpinner( new SpinnerDateModel() );
        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(timeSpinner, "HH:mm");
        timeSpinner.setEditor(timeEditor);
        timeSpinner.setValue(new Date()); // will only show the current time

        spinner1 = timeSpinner;

        JSpinner timeSpinner5 = new JSpinner( new SpinnerDateModel() );
        JSpinner.DateEditor timeEditor5 = new JSpinner.DateEditor(timeSpinner5, "HH:mm");
        timeSpinner5.setEditor(timeEditor5);
        timeSpinner5.setValue(new Date()); // will only show the current time

        spinner2 = timeSpinner;


        //J Date Picker - termRestrictionsPanel

        UtilDateModel model2 = new UtilDateModel();
        JDatePanelImpl datePanel2 = new JDatePanelImpl(model2);
        JDatePickerImpl datePicker2 = new JDatePickerImpl(datePanel2);
        JDatePickerStart_termRestrictionsPanel = datePicker2;

        UtilDateModel model3 = new UtilDateModel();
        JDatePanelImpl datePanel3 = new JDatePanelImpl(model3);
        JDatePickerImpl datePicker3 = new JDatePickerImpl(datePanel3);
        JDatePickerEnd_termRestrictionsPanel = datePicker3;

        // JSpinners for time - termRestrictionsPanel

        JSpinner timeSpinner2 = new JSpinner( new SpinnerDateModel() );
        JSpinner.DateEditor timeEditor2 = new JSpinner.DateEditor(timeSpinner2, "HH:mm");
        timeSpinner2.setEditor(timeEditor2);
        Date date2 = new Date();
        date2.setHours(0);
        date2.setMinutes(0);
        timeSpinner2.setValue(date2); // will only show the current time
        weekdaysSpinner_termRestrictionsPanel = timeSpinner2;


        JSpinner timeSpinner3 = new JSpinner( new SpinnerDateModel() );
        JSpinner.DateEditor timeEditor3 = new JSpinner.DateEditor(timeSpinner3, "HH:mm");
        timeSpinner3.setEditor(timeEditor3);
        timeSpinner3.setValue(date2); // will only show the current time
        weekendsSpiner_termRestrictionsPanel = timeSpinner3;

        //J Date Picker - roomInfoPanel

        UtilDateModel model4 = new UtilDateModel();
        JDatePanelImpl datePanel4 = new JDatePanelImpl(model4);
        JDatePickerImpl datePicker4 = new JDatePickerImpl(datePanel4);
        JDatePicker_roomInfoPanel = datePicker4;

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

    public void setTermRestriction()
    {

        if((JDatePickerStart_termRestrictionsPanel.getJFormattedTextField().getValue() == null )
                || (JDatePickerEnd_termRestrictionsPanel.getJFormattedTextField().getValue() == null))

        {
            JOptionPane.showMessageDialog(getInstanceOfGUI(), "Fill in the dates!");

        }
        else
        {
            //create restriction
            Date restrictionStartDate = getController().convertGregCalendarToDate((GregorianCalendar) JDatePickerStart_termRestrictionsPanel.getJFormattedTextField().getValue());
            Date restrictionEndDate = getController().convertGregCalendarToDate((GregorianCalendar) JDatePickerEnd_termRestrictionsPanel.getJFormattedTextField().getValue());
            Date weekdayBookingTimeRestriction = (Date) weekdaysSpinner_termRestrictionsPanel.getValue();
            Date weekendBookingTimeRestriction = (Date) weekendsSpiner_termRestrictionsPanel.getValue();

            TermTimeRestriction restriction = new TermTimeRestriction(getController().generateTermTimeRestrictionId(),restrictionStartDate, restrictionEndDate, weekdayBookingTimeRestriction, weekendBookingTimeRestriction);

            if(restrictionEndDate.compareTo(restrictionStartDate) > 0)
            {
                if(getController().termRestrictionCanBeSet(restriction))
                {
                    getController().addTermTimeRestriction(restriction);
                    JOptionPane.showMessageDialog(getInstanceOfGUI(), "Term restriction set successfully.");
                }
                else
                {
                    JOptionPane.showMessageDialog(getInstanceOfGUI(), "Term restriction could not be set.");
                    controller.setNextTermTimeRestrictionId(getController().getNextTermTimeRestrictionId() - 1);
                }
            }
            else
            {
                JOptionPane.showMessageDialog(getInstanceOfGUI(), "Term restriction end date has to be after start date.");
            }
        }
    }


    public void refreshRoomInfoPanel()
    {
        if(getController().roomByIdExists(getSelectedRoomIndex()))
        {
            //Set Room id name type size labels
            idTextField_roomInfoPanel.setText(""+getController().getRoomById(getSelectedRoomIndex()).getRoomId());
            nameTextField_roomInfoPanel.setText(""+getController().getRoomById(getSelectedRoomIndex()).getRoomName());
            typeTextField_roomInfoPanel.setText(""+getController().getRoomById(getSelectedRoomIndex()).getTypeName());
            sizeTextField_roomInfoPanel.setText(""+getController().getRoomById(getSelectedRoomIndex()).getSize());
            statusTextField_roomInfoPanel.setText(""+getController().getRoomStatus(getController().getRoomById(getSelectedRoomIndex())));
            currentNoteTextField_roomInfoPanel.setText(getController().getRoomById(getSelectedRoomIndex()).getUnavailabilityNote());
        }
        else
        {
            System.out.println("room no longer exists");
        }
    }



    //Getters And Setters


    public Controller getController() {
        return controller;
    }

    public void setController (Controller controller)
    {
        this.controller = controller;
        System.out.println(getController());
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getSelectedRoomIndex() {
        return selectedRoomIndex;
    }

    public void setSelectedRoomIndex(int selectedRoomIndex) {
        this.selectedRoomIndex = selectedRoomIndex;
    }

    public int getSelectedTermRestrictionIndex() {
        return selectedTermRestrictionIndex;
    }

    public void setSelectedTermRestrictionIndex(int selectedTermRestrictionIndex) {
        this.selectedTermRestrictionIndex = selectedTermRestrictionIndex;
    }

    public ManagerGUI getInstanceOfGUI()
    {
        return this;
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
