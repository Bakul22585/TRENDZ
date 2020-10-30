package com.example.trendz;

public class AutopoolEntry {
    private String number;
    private String level;
    private String amount;
    private String date;

    public AutopoolEntry() {}
    public AutopoolEntry(String number, String level, String amount, String date) {
        this.number = number;
        this.level = level;
        this.amount = amount;
        this.date = date;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
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
