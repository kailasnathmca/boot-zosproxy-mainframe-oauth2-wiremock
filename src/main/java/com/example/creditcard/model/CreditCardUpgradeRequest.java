package com.example.creditcard.model;

public class CreditCardUpgradeRequest {
    private String accountNumber;
    private String currentCardType;
    private String desiredCardType;

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getCurrentCardType() {
        return currentCardType;
    }

    public void setCurrentCardType(String currentCardType) {
        this.currentCardType = currentCardType;
    }

    public String getDesiredCardType() {
        return desiredCardType;
    }

    public void setDesiredCardType(String desiredCardType) {
        this.desiredCardType = desiredCardType;
    }
}
