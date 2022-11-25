package service;

import interfaces.IRoom;
import model.Customer;
import model.Reservation;
import model.Room;

import java.util.*;

public final class ReservationService {

    public static final ReservationService SINGLETON = new ReservationService();
    public static Map<String,IRoom> roomList = new HashMap<>();
    public static List<Reservation> reservationList = new ArrayList<>();

    private ReservationService(){}

    public static ReservationService getSingleton(){
        return SINGLETON;
    }

    public void addRoom(IRoom room){
        Room roomObj = new Room(room.getRoomNumber(), room.getRoomPrice(), room.getRoomType());
        if(roomList.get(room.getRoomNumber()) == null){
            roomList.put(room.getRoomNumber(), roomObj);
        }else{
            throw new IllegalArgumentException("Room with number " + room.getRoomNumber() + " already exists");
        }
    }

    public IRoom getARoom(String roomId){
        // room number should serve as roomId
        return roomList.get(roomId);
    }

    public Reservation reserveARoom(Customer customer, IRoom room, Date checkInDate, Date checkOutDate){
        Reservation reservation = new Reservation(customer, room, checkInDate, checkOutDate);
        reservationList.add(reservation);
        return reservation;
    }

    public Collection<IRoom> findRooms(Date checkInDate, Date checkOutDate){

        // Available room number
        Collection<IRoom> availableRoomsList = new ArrayList<>();
        // Booked room numbers
        Collection<IRoom> bookedRoomsList = new ArrayList<>();
        // Get all rooms
        Collection<IRoom> allRooms = getAllRooms();

        // Scenario where there is no reservation
        if(reservationList.isEmpty()) {
            //availableRoomsList.add(re)
            return allRooms;
        }

        // Scenario where there is at least one reservation
        reservationList.forEach((reservation)->{

            // Among reservations, if no date conflict, available rooms are added in a list
            if ((!checkInDate.after(reservation.getCheckOutDate())
                    && !checkOutDate.before(reservation.getCheckInDate())))
            {
                // System.out.println("Test");
                bookedRoomsList.add(reservation.getRoom());
            }
        });

        // That is to add all rooms that aren't booked
        roomList.forEach((number, room) ->{
            if(!bookedRoomsList.contains(room))
                availableRoomsList.add(room);
        });

        /*
        System.out.println("available: " + availableRoomsList);
        System.out.println("booked: " + bookedRoomsList);
        */
        return availableRoomsList;
    }

    public Collection<Reservation> getCustomersReservation(Customer customer){
        List<Reservation> customersReservation = new ArrayList<>();
        if(customer != null) {
            String customerEmail = customer.getEmail();
            for(Reservation reservation:reservationList){
                if(reservation.getCustomer() != null) {
                    if (reservation.getCustomer().getEmail().equals(customerEmail)) {
                        customersReservation.add(reservation);
                    }
                }
            }
        }

        return  customersReservation;
    }

    public void printAllReservation(){
        if(getAllReservations().isEmpty()) {
            System.out.println("No reservations found!");
        }else {
            getAllReservations().forEach(System.out::println);
        }
    }

    // Get all reservations
    Collection<Reservation> getAllReservations(){
        return reservationList;
    }

    // Get all rooms
    public Collection<IRoom> getAllRooms(){
        return roomList.values();
    }

}