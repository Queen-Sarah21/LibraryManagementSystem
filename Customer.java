package User.Client;

import User.User;

public class Customer extends User {
    private static int customerCounter = 1000;

    public Customer(){}
    public Customer(String fname, String lname, String password, String bdate){
        setId();
        setFirstName(fname);
        setLastName(lname);
        setPassword(password);
        setBirthdate(bdate);
        setState();
    }
    public void setId(){
        this.id = "CST" + customerCounter;
        customerCounter++;
    }

    public void display(){
        System.out.println("---------------- Customer ID " + id + " ----------------");
        System.out.println("\tDate of birth: " + birthdate + "\n\tFirst name: " + firstName);
        System.out.println("\tLast name: " + lastName + "\n\tCurrent state: " + userState);
    }
}
