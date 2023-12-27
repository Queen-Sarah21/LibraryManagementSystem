package User.Employee;

import Enums.Role;
import Enums.UserState;

public class ManagerEmpBuilder implements IEmployeeBuilder{
    Employee employee;
    public ManagerEmpBuilder(){this.employee = new Employee();}
    public void setBirthdate(String birthdate){employee.setBirthdate(birthdate);}
    public void setFirstName(String firstName){employee.setFirstName(firstName);}
    public void setLastName(String lastName){employee.setLastName(lastName);}
    public void setPassword(String password){employee.setPassword(password);}
    public void setState(){employee.setState(UserState.ACTIVE);}
    public void setRole(){employee.setRole(Role.MANAGER);}
    public void setYearsOfExp(int yearsOfExp){employee.setYearsOfExp(yearsOfExp);}
    @Override
    public Employee getEmployee() {return employee;}
}
