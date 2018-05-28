package com.example.caio.clothingstore.Main.Models;

public class Store {

    private int storeId;
    private String branchAddress;
    private String telephoneNumber;
    private char isMainBranch;

    public Store(String branchAddress, String telephoneNumber, char isMainBranch) {

        this.storeId = 0;
        this.branchAddress = branchAddress;
        this.telephoneNumber = telephoneNumber;
        this.isMainBranch = isMainBranch;
    }


    public int getStoreId() {
        return storeId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public String getBranchAddress() {
        return branchAddress;
    }

    public void setBranchAddress(String branchAddress) {
        this.branchAddress = branchAddress;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public char getIsMainBranch() {
        return isMainBranch;
    }

    public void setIsMainBranch(char isMainBranch) {
        this.isMainBranch = isMainBranch;
    }
}
