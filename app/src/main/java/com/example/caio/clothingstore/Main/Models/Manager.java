package com.example.caio.clothingstore.Main.Models;

public class Manager extends User{

    private String role;

    public Manager(int userId, String userName, String userPassword , String role ) {

        super(userId, userName, userPassword);
        this.role = role;
    }
    
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
