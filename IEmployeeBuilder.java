package User.Employee;

public interface IEmployeeBuilder {
    void setBirthdate(String birthdate);
    void setFirstName(String firstName);
    void setLastName(String lastName);
    void setPassword(String password);
    void setState();
    void setRole();
    void setYearsOfExp(int yearsOfExp);
    Employee getEmployee();
}
