package com.example.caio.clothingstore.Main.Models;

import java.io.Serializable;

public class Customer extends User {

    private String address;
    private boolean isAdmin;
    private CreditCard creditCard;

    public Customer(int userId, String userName, String userPassword , String address , boolean isAdmin) {

        super(userId, userName, userPassword);
        this.address = address;
        this.isAdmin = isAdmin;
    }

    public Customer(int userId, String userName, String userPassword , String address , boolean isAdmin , CreditCard creditCard) {

        super(userId, userName, userPassword);
        this.address = address;
        this.isAdmin = isAdmin;
        this.creditCard = creditCard;
    }


    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public CreditCard getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(CreditCard creditCard) {
        this.creditCard = creditCard;
    }
}
