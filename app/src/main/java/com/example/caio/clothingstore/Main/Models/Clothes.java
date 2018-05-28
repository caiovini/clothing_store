package com.example.caio.clothingstore.Main.Models;

import java.io.Serializable;

public class Clothes implements Serializable {

    private int clothIdNumber;
    private String name;
    private double price;
    private String storageAdress;
    private int numberInStock;
    private boolean isPicked;

    public Clothes(int clothIdNumber, String name, double price , int numberInStock) {

        this.clothIdNumber = clothIdNumber;
        this.name = name;
        this.price = price;
        this.storageAdress = "";
        this.numberInStock = numberInStock;
        isPicked = false;
    }


    public Clothes(int clothIdNumber, String name, double price , String address , int numberInStock) {

        this.clothIdNumber = clothIdNumber;
        this.name = name;
        this.price = price;
        this.storageAdress = address;
        this.numberInStock = numberInStock;
        isPicked = false;
    }

    public Clothes(String name, double price , int numberInStock) {

        this.clothIdNumber = 0;
        this.name = name;
        this.price = price;
        this.storageAdress = "";
        this.numberInStock = numberInStock;
        isPicked = false;
    }


    public int getNumberInStock() {
        return numberInStock;
    }

    public void setNumberInStock(int numberInStock) {
        this.numberInStock = numberInStock;
    }

    public boolean isPicked() {
        return isPicked;
    }

    public void setPicked(boolean picked) {
        isPicked = picked;
    }

    public String getStorageAdress() {
        return storageAdress;
    }

    public void setStorageAdress(String storageAdress) {
        this.storageAdress = storageAdress;
    }

    public int getClothIdNumber() {
        return clothIdNumber;
    }

    public void setClothIdNumber(int clothIdNumber) {
        this.clothIdNumber = clothIdNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
