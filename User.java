package User;
import Enums.*;

public abstract class User {
    protected String id;
    protected String firstName;
    protected String lastName;
    protected String password;
    protected String birthdate;
    protected UserState userState;

    public String getId() {
        return id;
    }

    public void setId(String id) { this.id = id; }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }
    public UserState getState() {
        return userState;
    }

    public void setState(UserState userState) {
        this.userState = userState;
    }
    public void setState() {
        this.userState = UserState.ACTIVE;
    }

    abstract protected void display();
}
