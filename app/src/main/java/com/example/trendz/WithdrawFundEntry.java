package com.example.trendz;

public class WithdrawFundEntry {
    private String number;
    private String type;
    private String amount;
    private String date;

    public WithdrawFundEntry() {}

    public WithdrawFundEntry(String number, String type, String amount, String date) {
        this.number = number;
        this.type = type;
        this.amount = amount;
        this.date = date;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
