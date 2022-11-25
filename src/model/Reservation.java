package model;

import interfaces.IRoom;

import java.util.Date;
import java.util.Objects;

public class Reservation {
    private Customer customer;
    private IRoom room;
    private Date checkInDate;
    private Date checkOutDate;

    public Reservation(Customer customer, IRoom room, Date checkInDate, Date checkOutDate){
        this.customer = customer;
        this.room = room;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
    }

    public Customer getCustomer(){
        return customer;
    }

    public IRoom getRoom(){
        return room;
    }

    public Date getCheckInDate(){
        return checkInDate;
    }

    public Date getCheckOutDate(){
        return checkOutDate;
    }

    public void setCustomer(Customer customer){
        this.customer = customer;
    }

    public void setRoom(IRoom room){
        this.room = room;
    }

    public void setCheckInDate(Date checkInDate){
        this.checkInDate = checkInDate;
    }

    public void setCheckOutDate(Date checkOutDate){
        this.checkOutDate = checkOutDate;
    }

    @Override
    public String toString(){
        return "Reservation\n"+
                "Customer: " + customer + "\n"+
                "Room: " + room + "\n" +
                "Check-in date: " + checkInDate + "\n" +
                "Check-out date: " + checkOutDate +"\n";
    }
}
