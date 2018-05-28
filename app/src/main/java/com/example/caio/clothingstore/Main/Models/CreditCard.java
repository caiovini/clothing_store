package com.example.caio.clothingstore.Main.Models;

public class CreditCard {

    private int creditCardId;
    private int creditCardNumber;
    private int securityCreditCardNumber;
    private String creditCardType;


    public CreditCard(int creditCardId, int creditCardNumber, int securityCreditCardNumber, String creditCardType) {

        this.creditCardId = creditCardId;
        this.creditCardNumber = creditCardNumber;
        this.securityCreditCardNumber = securityCreditCardNumber;
        this.creditCardType = creditCardType;
    }

    public int getCreditCardId() {
        return creditCardId;
    }

    public void setCreditCardId(int creditCardId) {
        this.creditCardId = creditCardId;
    }

    public int getCreditCardNumber() {
        return creditCardNumber;
    }

    public void setCreditCardNumber(int creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

    public int getSecurityCreditCardNumber() {
        return securityCreditCardNumber;
    }

    public void setSecurityCreditCardNumber(int securityCreditCardNumber) {
        this.securityCreditCardNumber = securityCreditCardNumber;
    }

    public String getCreditCardType() {
        return creditCardType;
    }

    public void setCreditCardType(String creditCardType) {
        this.creditCardType = creditCardType;
    }
}
