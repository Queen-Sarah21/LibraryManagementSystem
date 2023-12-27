package User.Employee;

public class EmployeeMaker {
    private IEmployeeBuilder iEmployeeBuilder;
    public EmployeeMaker(IEmployeeBuilder iEmployeeBuilder){
        this.iEmployeeBuilder = iEmployeeBuilder;
    }
    public void makeEmployee(String fname, String lname, String password, String bdate, int yOfX){
        this.iEmployeeBuilder.setFirstName(fname);
        this.iEmployeeBuilder.setLastName(lname);
        this.iEmployeeBuilder.setPassword(password);
        this.iEmployeeBuilder.setBirthdate(bdate);
        this.iEmployeeBuilder.setState();
        this.iEmployeeBuilder.setRole();
        this.iEmployeeBuilder.setYearsOfExp(yOfX);
    }
    public Employee getEmployee(){return this.iEmployeeBuilder.getEmployee();}
    public void reset(IEmployeeBuilder iEmployeeBuilder){this.iEmployeeBuilder = iEmployeeBuilder;}
}
