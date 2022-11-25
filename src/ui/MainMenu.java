package ui;

import api.AdminResource;
import api.HotelResource;
import interfaces.IRoom;
import model.Customer;
import model.Reservation;
import util.DateUtil;
import util.IntegerUtil;
import validator.DateValidator;

import java.util.*;
import java.util.regex.Pattern;

public final class MainMenu {

    private static final Map<String, String> menuItems = new HashMap<>();
    private static final String pointTabSpace = ".\t";
    private static final String invalidInputMessage = "Error: Invalid input, please choose an option from 1 to 5";
    private static final HotelResource hotelResource = HotelResource.getSingleton();

    private static final AdminResource adminResource = AdminResource.getSingleton();
    //    private static final String emailRegex = "^(.+)@(.+).(.+)$";
    private static final String emailRegex = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@" +
            "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";

    private static final int DEFAULT_DAYS = 7;


    private MainMenu(){}

    public static void runMainMenu(){

        displayMenu();
        Scanner userInput = new Scanner(System.in);
        boolean running = true;
        String menuChoice;

        while(running){
            menuChoice = userInput.nextLine();

            if(menuChoice.length() == 1) {
                switch (menuChoice) {
                    case "1" -> {
                        findAndReserveARoom();
                        runMainMenu();
                    }
                    case "2" -> {
                        seeMyReservations();
                        runMainMenu();
                    }
                    case "3" -> {
                        // Done
                        createAccount();
                        runMainMenu();
                    }
                    case "4" -> {
                        // Done
                        running = false;
                        AdminMenu.runAdminMenu();
                    }
                    case "5" ->
                        // System.out.println("5.\tExit");
                        // Done
                            running = false;
                    default -> System.out.println(invalidInputMessage);
                }
            }else {
                System.out.println(invalidInputMessage);
            }
        }
    }
    private static void populateMenu(){
        menuItems.put("1", "Find and reserve a room");
        menuItems.put("2", "See my reservations");
        menuItems.put("3", "Create an account");
        menuItems.put("4", "Admin");
        menuItems.put("5", "Exit");
    }
    private static void displayMenu(){

        populateMenu();

        System.out.println("Welcome to the Hotel Reservation Application");
        System.out.println("_________________________________________________________");
        menuItems.forEach((option, text)->System.out.println(option + pointTabSpace + text));
        System.out.println("_________________________________________________________");
        System.out.println("Please select a number for the menu options: ");

    }


    // After adding room and seeing all rooms, creating account
    private static void createAccount(){

        String firstNameString = "First name";
        String lastNameString = "Last name";

        // Adding email
        String email = getEmailToAdd().toLowerCase();

        // Adding firstname
        String firstName = getFirstNameOrLastName(firstNameString);

        // Adding lastname
        String lastName = getFirstNameOrLastName(lastNameString);

        try {
            hotelResource.createACustomer(email, firstName, lastName);
        }catch (IllegalArgumentException ex){
            System.out.println(ex.getLocalizedMessage());
        }

//        MainMenu.runMainMenu();

    }

    private static void findAndReserveARoom(){

        boolean correctDate = false;
        String checkInString = "check-in";
        String checkOutString = "check-out";
        Date today = new Date();
        String checkinDateExample = DateUtil.getStringFromDate(today);
        Date dayAfterToday = DateUtil.addDaysToDate(today, 1);
        String checkoutDateExample = DateUtil.getStringFromDate(dayAfterToday);
        String proceedBookingString = "Would you like to book a room? y/n";
        String proceedAccountString = "Do you have an account with us? y/n";

        // Before all, checking if there are rooms and display menu to add room for user
        if (adminResource.getAllRooms().size() == 0){
            System.out.println("Please add a room first");
            AdminMenu.runAdminMenu();
        }

        // Getting check-in and check-out date
        Date checkInDate = getDateForReservation(checkInString, checkinDateExample);
        Date checkOutDate = null;

        while (!correctDate){
            checkOutDate = getDateForReservation(checkOutString, checkoutDateExample);
            if(checkOutDate.after(checkInDate))
                correctDate = true;
        }


        // Find room(s) by a date range
        Collection<IRoom> availableRoomsFound = hotelResource.findARoom(checkInDate, checkOutDate);
        Collection<String> availableRoomNumbersList = new ArrayList<>();

        if(availableRoomsFound.isEmpty()){

            String checkInDateString = DateUtil.getStringFromDate(checkInDate);
            String checkOutDateString = DateUtil.getStringFromDate(checkOutDate);

            System.out.println("No rooms found for reservation from " + checkInDateString + " to " + checkOutDateString);

            // Search for recommended rooms
            checkInDate = DateUtil.addDaysToDate(checkInDate, DEFAULT_DAYS);
            checkOutDate = DateUtil.addDaysToDate(checkOutDate, DEFAULT_DAYS);
            String newCheckInDateString = DateUtil.getStringFromDate(checkInDate);
            String newCheckOutDateString = DateUtil.getStringFromDate(checkOutDate);

            availableRoomsFound = hotelResource.findARoom(checkInDate, checkOutDate);
            if(availableRoomsFound.size() > 0){
                System.out.println("Recommended rooms from "+ newCheckInDateString + " to " + newCheckOutDateString);
                availableRoomsFound.forEach(room ->{
                    System.out.println(room);
                    availableRoomNumbersList.add(room.getRoomNumber());
                });
            }else{
                System.out.println("No recommended rooms for recommended dates from " + newCheckInDateString + " to " + newCheckOutDateString);
                MainMenu.runMainMenu();
            }
        }else {
            availableRoomsFound.forEach((room) ->{
                System.out.println(room);
                availableRoomNumbersList.add(room.getRoomNumber());
            });
        }

        // Confirm Booking Proceeding
        if(confirmProceeding(proceedBookingString)){
            // Get Customer Account
            int customerNumbers = adminResource.getAllCustomers().size();
            // Check if there are Customers on the system to proceed
            if(customerNumbers == 0){
                System.out.println("There are no customers, please add a customer");
                runMainMenu();
            }

            if(confirmProceeding(proceedAccountString)){
                // Get Customer
                String customerEmail = getEmailForAccount();
                Customer customer = hotelResource.getCustomer(customerEmail);

                while (customer == null){
                    confirmProceeding(proceedAccountString);
                    customerEmail = getEmailForAccount();
                    customer = hotelResource.getCustomer(customerEmail);
                }
                // Get Room
                boolean reserved = true;
                String roomNumber = getRoomNumber();
                IRoom room = hotelResource.getRoom(roomNumber);

                while (reserved){
                    if (!availableRoomNumbersList.contains(roomNumber)) {
                        System.out.println("This room is reserved");
                        roomNumber = getRoomNumber();

                    }else{
                        room = hotelResource.getRoom(roomNumber);
                        reserved = false;
                    }
                }
                //while (reserved);

                // Book a room
                Reservation reservation = hotelResource.bookARoom(customerEmail,room, checkInDate, checkOutDate);
                // Print reservation
                System.out.println(reservation);
            }
        }


    }

    private static void seeMyReservations(){
        if(adminResource.getAllCustomers().size() == 0){
            System.out.println("Please add a customer first");
        }else {

            String emailAccount = getEmailForAccount();
            System.out.println("Reservations for customer with email "+ emailAccount);
            Collection<Reservation> customerReservations = hotelResource.getCustomersReservations(emailAccount);

            if (customerReservations.isEmpty()) {
                System.out.println("No reservations made for that customer");
            } else {
                customerReservations.forEach(System.out::println);
            }
        }
    }

    // Get email to add Customer
    private static String getEmailToAdd(){
        boolean correctEmail = false;
        String emailString = "";

        while (!correctEmail){

            System.out.println("Enter email format name@domain.com");
            Scanner emailScanner = new Scanner(System.in);
            emailString = emailScanner.nextLine();

            Pattern pattern = Pattern.compile(emailRegex, Pattern.CASE_INSENSITIVE);

            if(hotelResource.getCustomer(emailString) != null){
                System.out.println("Please provide another email, customer with email " + emailString + " exists");
            }

            if(!pattern.matcher(emailString).matches()){
                System.out.println("Invalid email");
            }

            if(pattern.matcher(emailString).matches()
                    && hotelResource.getCustomer(emailString) == null ){
                correctEmail = true;
            }
        }

        return emailString;
    }


    // Get first name  or last name to add customer
    private static String getFirstNameOrLastName(String firstNameOrLastName){
        System.out.println(firstNameOrLastName);
        Scanner inputScanner =  new Scanner(System.in);
        String inputScannerString = inputScanner.nextLine();
        String s1 =  inputScannerString.substring(0, 1).toUpperCase();
        String s2 = inputScannerString.substring(1).toLowerCase();
        return s1 + s2 ;
    }

    private static Date getDateForReservation(String dateType, String dateExample){

        String dateInputString;
        Date dateFromString = null;
        String pattern = "MM/dd/yyyy";
        String patternString = "mm/dd/yyyy";
        String note = "(date must start from today and not before): ";
        Date today = new Date();

        boolean correctDate = false;

        while(!correctDate){
            System.out.println("Enter " + dateType + " date "+ patternString + " example " + dateExample + " "+ note);
            Scanner dateInputScanner = new Scanner(System.in);
            dateInputString = dateInputScanner.nextLine();

            if(DateValidator.isValidFormat(pattern, dateInputString, Locale.ENGLISH) ) {
                dateFromString = DateUtil.getDateFromString(dateInputString);

                // A valid date is a date that starts from today and not before
                // To check if two dates are equal,
                // in that case a comparison between the two days of the date is made
                int todayDay = DateUtil.getDayOfWeek(today);
                int dateFromStringDay = DateUtil.getDayOfWeek(dateFromString);

                if (today.before(dateFromString) || todayDay == dateFromStringDay) {
                    correctDate = true;
                }
            }
        }
        return dateFromString;

    }

    private static boolean confirmProceeding(String proceedingString){

        System.out.println(proceedingString);
        Scanner choiceScanner = new Scanner(System.in);
        String choiceString;
        boolean correctProceeding = false;

        while (!correctProceeding){
            choiceString = choiceScanner.nextLine();
            choiceString = choiceString.toLowerCase();

            if (choiceString.length() == 1) {
                switch (choiceString) {
                    case "y" -> correctProceeding = true;
                    case "n" -> MainMenu.runMainMenu();
                    default -> System.out.println("Enter Y (Yes) or N (No)");
                }
            }else{
                System.out.println("Enter Y (Yes) or N (No)");
            }
        }

        return true;
    }


    // Return a room number that exists
    private static String getRoomNumber(){
        System.out.println("What room number would you like to reserve?");
        String roomNumber = "";

        boolean correctRoom = false;
        while (!correctRoom){
            Scanner roomNumberScanner = new Scanner(System.in);
            roomNumber = roomNumberScanner.nextLine();
            if(!IntegerUtil.isInteger(roomNumber)){
                System.out.println("Please provide a valid number");
            }else {
                if(hotelResource.getRoom(roomNumber) == null){
                    System.out.println("Please enter a room that exists");
                }else {
                    correctRoom = true;
                }
            }
        }
        return roomNumber;
    }

    //
    private static String getEmailForAccount(){
        boolean correctEmail = false;
        String emailString = "";

        while (!correctEmail){

            System.out.println("Enter email format name@domain.com");
            Scanner emailScanner = new Scanner(System.in);
            emailString = emailScanner.nextLine();

            Pattern pattern = Pattern.compile(emailRegex, Pattern.CASE_INSENSITIVE);

            /*if(hotelResource.getCustomer(emailString) == null){
                System.out.println("Please provide another email, customer with email " + emailString + " does not exist");
            }*/

            if(!pattern.matcher(emailString).matches()){
                System.out.println("Invalid email");
            }

            if(pattern.matcher(emailString).matches()){
//                    && hotelResource.getCustomer(emailString) != null ){
                correctEmail = true;
            }
        }
        return emailString;
    }
}