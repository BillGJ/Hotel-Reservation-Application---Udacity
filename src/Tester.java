import ui.AdminMenu;
import ui.MainMenu;
import validator.DateValidator;

import javax.xml.validation.Validator;
import java.util.Locale;

public class Tester {
    public static void main(String[] args){

//         Test with valid email
//        try {
//            Customer customer = new Customer("first", "second", "j@gmail.com");
//            System.out.println(customer);
//        }catch (IllegalArgumentException ex){
//            System.out.println(ex.getLocalizedMessage());
//        }



//        System.out.println(1+1);
////        CustomerService customerService = new CustomerService();
//
////        Test with valid email
//        System.out.println("1- Test with valid email");
//        try {
//            CustomerService.addCustomer("e@gmail.com", "ebill", "gj");
//        }catch (IllegalArgumentException ex){
//            System.out.println(ex.getLocalizedMessage());
//        }
//
////        Test with duplicate email
//        System.out.println("2- Test with duplicate email");
//        try {
//            CustomerService.addCustomer("e@gmail.com", "bill", "gj");
//        }catch (IllegalArgumentException ex){
//            System.out.println(ex.getLocalizedMessage());
//        }
//
//        // Test with invalid email
//        System.out.println("3- Test with invalid email");
//        try {
//            CustomerService.addCustomer("igmail.com", "mike", "gj");
//        }catch (IllegalArgumentException ex){
//            System.out.println(ex.getLocalizedMessage());
//        }
//
//        //        Test with another valid email
//        System.out.println("4- Test with another valid email");
//        try {
//            CustomerService.addCustomer("i@gmail.com", "mike", "gj");
//        }catch (IllegalArgumentException ex){
//            System.out.println(ex.getLocalizedMessage());
//        }
//
//        System.out.println(CustomerService.getCustomer("e@gmail.com"));
//        System.out.println(CustomerService.getAllCustomers());

//        MainMenu mainMenu = new MainMenu();

//        mainMenu.displayMenu();

//        AdminMenu adminMenu = new AdminMenu();
//        adminMenu.displayMenu();
       System.out.println(DateValidator.isValidFormat("MM/dd/yyyy", "", Locale.ENGLISH));

    }


}
