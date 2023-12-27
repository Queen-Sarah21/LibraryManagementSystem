package User.Employee;
import Enums.*;
public class LibrarianEmpBuilder implements IEmployeeBuilder{
    Employee employee;
    public LibrarianEmpBuilder(){this.employee = new Employee();}
    public void setBirthdate(String birthdate){employee.setBirthdate(birthdate);}
    public void setFirstName(String firstName){employee.setFirstName(firstName);}
    public void setLastName(String lastName){employee.setLastName(lastName);}
    public void setPassword(String password){employee.setPassword(password);}
    public void setState(){employee.setState(UserState.ACTIVE);}
    public void setRole(){employee.setRole(Role.LIBRARIAN);}
    public void setYearsOfExp(int yearsOfExp){employee.setYearsOfExp(yearsOfExp);}
    @Override
    public Employee getEmployee() {return employee;}
}
