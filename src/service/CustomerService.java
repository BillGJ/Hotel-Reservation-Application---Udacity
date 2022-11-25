package service;

import model.Customer;

import java.util.*;

public final class CustomerService {

    private static final CustomerService SINGLETON = new CustomerService();
    public static Map<String, Customer> customerList = new HashMap<>();
    // Prevent instantiation of this class
    private CustomerService(){}

    public static CustomerService getSingleton(){
        return SINGLETON;
    }


    public void addCustomer(String email, String firstName, String lastName){

        Customer customer = new Customer(firstName, lastName, email);
        if(customerList.get(email) == null) {
//            System.out.println("Adding a customer:");
            System.out.println(customer);
            customerList.put(customer.getEmail(), customer);
        }else {
            throw new IllegalArgumentException("Customer with email: " + email + " already exists!");
        }
    }

    public Customer getCustomer(String customerEmail){
//        System.out.println("Getting a customer:");
        return customerList.get(customerEmail);
    }

    public Collection<Customer> getAllCustomers(){
//        System.out.println("Getting all customers:");
        return customerList.values();
    }

}
