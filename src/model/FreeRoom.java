package model;

import enumerations.RoomType;

public class FreeRoom extends Room {


    public FreeRoom(String roomNumber, Double price, RoomType enumeration){


        super(roomNumber, price, enumeration);

    }



    @Override
    public String toString(){
        return "Free Room\n" +
                "Number: " + getRoomNumber() + " Price: $" + getRoomPrice() +
                " Room Type: " + getRoomType().toString().toLowerCase() +"\n";
    }
}
