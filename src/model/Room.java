package model;

import enumerations.RoomType;
import interfaces.IRoom;

import java.util.Objects;

public class Room implements IRoom {
    private String roomNumber;
    private Double price;
    private RoomType enumeration;


    public Room(String roomNumber, Double price, RoomType enumeration){
        super();
        this.roomNumber = roomNumber;
        this.price = price;
        this.enumeration = enumeration;
    }

    @Override
    public String getRoomNumber(){
        return roomNumber;
    }

    @Override
    public Double getRoomPrice(){
        return price;
    }

    @Override
    public RoomType getRoomType(){
        return enumeration;
    }

    // Creating setters
    public void setRoomNumber(String roomNumber){
        this.roomNumber = roomNumber;
    }

    public void setPrice(double price){
        this.price = price;
    }

    public void  setRoomType(RoomType enumeration){
        this.enumeration = enumeration;
    }

    @Override
    public boolean isFree(){

        return this.price == 0.0;
    }

    // Overriding equals() to compare two Complex objects
    @Override
    public boolean equals(Object o) {

        // If the object is compared with itself then return true
        if (o == this) {
            return true;
        }

        /* Check if o is an instance of Complex or not
          "null instanceof [type]" also returns false */
        if (!(o instanceof Room)) {
            return false;
        }

        // typecast o to Complex so that we can compare data members
        Room room = (Room) o;

        // Compare the data members and return accordingly
        return Objects.equals(this.roomNumber, room.roomNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roomNumber);
    }

    @Override
    public String toString(){
        if(this.price > 0) {
            return "Room \n" +
                    "Number: " + roomNumber + " Price: $" + price +
                    " Room Type: " + enumeration.toString().toLowerCase() + " bed room" + "\n";
        }else {
            FreeRoom freeRoom = new FreeRoom(roomNumber, price, enumeration);
            return freeRoom.toString();
        }
    }
}
