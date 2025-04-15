package model;

public class User {
    private String name;
    private String userId;
    private String password;
    private int age;
    private String maritalStatus;

    public User(String name, String userId, int age, String maritalStatus, String password) {
        this.name = name;
        this.userId = userId;
        this.password = password;
        this.age = age;
        this.maritalStatus = maritalStatus;
    }

    // Getter and Setter for name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getter and Setter for userId (NRIC)
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    // Getter and Setter for password
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Getter and Setter for age
    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    // Getter and Setter for maritalStatus
    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    // Change password method
    public void changePassword(String newPassword) {
        this.password = newPassword;
    }
}
