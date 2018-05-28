package com.example.caio.clothingstore.Main.Models;

public class Manager {

    private int idManager;
    private String managerName;
    private String managerPassword;
    private String role;

    public Manager(int idManager, String managerName, String managerPassword, String role) {

        this.idManager = idManager;
        this.managerName = managerName;
        this.managerPassword = managerPassword;
        this.role = role;
    }

    public int getIdManager() {
        return idManager;
    }

    public void setIdManager(int idManager) {
        this.idManager = idManager;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public String getManagerPassword() {
        return managerPassword;
    }

    public void setManagerPassword(String managerPassword) {
        this.managerPassword = managerPassword;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
