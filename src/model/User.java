package model;

public class User{
    private String name;
    private String userId;
    private String password;
    private int age;
    private String maritalStatus;

    public User(String name, String userId, String password, int age, String maritalStatus) {
        this.name = name;
        this.userId = userId;
        this.password = password;
        this.age = age;
        this.maritalStatus = maritalStatus;
    }

    public String getName(){
        return name;
    }

    public void changePassword(String newPassword){
        this.password = newPassword;
    }
}