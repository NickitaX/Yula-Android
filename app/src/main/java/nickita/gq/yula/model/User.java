package nickita.gq.yula.model;

/**
 * Created by admin on 14/7/17.
 */
public class User {
    private String email, firstName, lastName, userID, rating, password;

    public User(String email, String firstName, String lastName, String userID, String rating, String password) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.userID = userID;
        this.rating = rating;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
