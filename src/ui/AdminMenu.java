package ui;

import api.AdminResource;
import api.HotelResource;
import enumerations.RoomType;
import interfaces.IRoom;
import model.Customer;
import model.Room;
import util.IntegerUtil;

import java.util.*;

public final class AdminMenu {

    private static final Map<String, String> menuItems = new HashMap<>();
    private static final AdminResource adminResource = AdminResource.getSingleton();
    private static final HotelResource hotelResource = HotelResource.getSingleton();
    private static final String pointTabSpace = ".\t";
    private static final String invalidInputMessage = "Error: Invalid input, please choose an option from 1 to 5";

    private AdminMenu(){}

    public static void runAdminMenu(){
        displayMenu();
        Scanner userInput = new Scanner(System.in);
        boolean running = true;
        String choice;

        while(running){
            choice = userInput.nextLine();
            if(choice.length() == 1) {
                switch (choice) {
                    case "1" -> {
                        // Done
                        seeAllCustomers();
                        runAdminMenu();
                    }
                    case "2" -> {
                        // Done
                        seeAllRooms();
                        runAdminMenu();
                    }
                    case "3" -> {
                        // Done
                        seeAllReservations();
                        runAdminMenu();
                    }
                    case "4" -> {
                        // Done
                        addARoom();
                        runAdminMenu();
                    }
                    case "5" -> {
                        // Done
                        running = false;
                        MainMenu.runMainMenu();
                    }
                    default -> System.out.println(invalidInputMessage);
                }
            }else{
                System.out.println(invalidInputMessage);
            }
        }
    }

    private static void populateMenu(){
        menuItems.put("1", "See all customers");
        menuItems.put("2", "See all rooms");
        menuItems.put("3", "See all reservations");
        menuItems.put("4", "Add a room");
        menuItems.put("5", "Back to main menu");
    }
    private static void displayMenu(){

        populateMenu();

        System.out.println("Admin Menu");
        System.out.println("_________________________________________________________");
        menuItems.forEach((option, text)->System.out.println(option + pointTabSpace + text));
        System.out.println("_________________________________________________________");
        System.out.println("Please select a number for the menu options: ");
    }

    // First, adding a room
    private static void addARoom(){

        boolean addingRoom = true;
        String addRoomString;

        // Getting room number
        String roomNumber = getRoomNumber();

        // Getting room price
        Double roomPrice = getRoomPrice();

        // Getting room type
        RoomType roomType = getRoomType();

        try {
            Room room = new Room(roomNumber, roomPrice, roomType);
            adminResource.addARoom(room);
            System.out.println(room);
        }catch (IllegalArgumentException ex){
            System.out.println(ex.getLocalizedMessage());
        }

        // Asking the user about adding another room
        while(addingRoom){
            System.out.println("Would you like to add another room y/n");
            Scanner scanner = new Scanner(System.in);
            addRoomString = scanner.nextLine();
            addRoomString = addRoomString.toLowerCase();

            if(addRoomString.length() == 1) {
                switch (addRoomString) {
                    case "y" -> addARoom();
                    case "n" -> {
                        addingRoom = false;
                        AdminMenu.runAdminMenu();
                    }
                    default -> System.out.println("Please enter Y (Yes) or N (No)");
                }
            }else {
                System.out.println(invalidInputMessage);
            }
        }
    }

    // Second, seeing all rooms
    private static void seeAllRooms(){
        Collection<IRoom> rooms = adminResource.getAllRooms();

        if(rooms.isEmpty())
            System.out.println("No rooms added");
        else{
            rooms.forEach(System.out::println);
        }
    }

    // Seeing all reservations
    private static void seeAllReservations(){
        adminResource.displayAllReservations();
    }

    // After adding customers in main menu, see all customers
    private static void seeAllCustomers(){
        Collection<Customer> customers = adminResource.getAllCustomers();
        if(customers.isEmpty())
            System.out.println("No customers added");
        else {
            customers.forEach(System.out::println);
        }
    }


    // Getting room number
    private static String getRoomNumber(){

        boolean correctRoomNumber = false;
        String roomNumber = "";

        while(!correctRoomNumber){
            System.out.println("Enter a room number");
            Scanner roomNumberScanner = new Scanner(System.in);
            roomNumber = roomNumberScanner.nextLine();

            if(!IntegerUtil.isInteger(roomNumber)){
                System.out.println("Please provide a valid number");
            }else {
                if(hotelResource.getRoom(roomNumber) != null){
                    System.out.println("Room with number "+ roomNumber + " already exists, please enter another number");
                }else {
                    correctRoomNumber = true;
                }
            }
        }
        return roomNumber;
    }

    // Getting room price
    private static Double getRoomPrice(){

        boolean correctPrice = false;
        double roomPrice = 0.0;

        while(!correctPrice){
            try {
                System.out.println("Enter enter price per night: ");
                Scanner roomPriceScanner = new Scanner(System.in);
                roomPrice = roomPriceScanner.nextDouble();
                correctPrice = true;
            } catch (InputMismatchException ex) {
                System.out.println("Please provide a valid price");
            }
        }

        return roomPrice;

    }

    // Adding room type
    private static RoomType getRoomType(){

        boolean correctRoomType = false;
        String roomTypeString;
        RoomType roomType = null;

        while (!correctRoomType) {
            System.out.println("Enter room type: 1 for single bed, to for double bed");
            Scanner roomTypeScanner = new Scanner(System.in);
            roomTypeString = roomTypeScanner.nextLine();

            if (roomTypeString.length() == 1){
                switch (roomTypeString) {
                    case "1" -> {
                        roomType = RoomType.SINGLE;
                        correctRoomType = true;
                    }
                    case "2" -> {
                        roomType = RoomType.DOUBLE;
                        correctRoomType = true;
                    }
                    default -> System.out.println("Please provide a valid room type");
                }
            }else{
                System.out.println(invalidInputMessage);
            }
        }
        return  roomType;
    }

}

