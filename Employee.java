package User.Employee;
import User.*;
import Enums.*;
public class Employee extends User {
    private static int employeeCounter = 1000;
    private Role role;
    private int yearsOfExp;
    public Employee(String id){setId(id);}
    public Employee(){
        setId();
    }
    public void setId(){
        this.id = "EMP" + employeeCounter;
        employeeCounter++;
    }
    public void setId(String id){
        this.id = id;
    }
    public int getYearsOfExp() {
        return yearsOfExp;
    }

    public void setYearsOfExp(int yearsOfExp) {
        this.yearsOfExp = yearsOfExp;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void display(){
        System.out.println("---------------- Employee ID " + id + " ----------------");
        System.out.println("\tDate of birth: " + birthdate + "\n\tFirst name: " + firstName);
        System.out.println("\tLast name: " + lastName + "\n\tCurrent state: " + userState);
        System.out.println("\tRole: " + role + "\n\tYears of experience: " + yearsOfExp + "\n");
    }
}
