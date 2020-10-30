package com.example.trendz;

public class TeamEntry {

    private String number;
    private String name;
    private String sponsor;
    private String amount;
    private String phone;
    private String date;

    public TeamEntry() {}
    public TeamEntry(String number, String name, String sponsor, String amount, String phone, String date) {
        this.number = number;
        this.name = name;
        this.sponsor = sponsor;
        this.amount = amount;
        this.phone = phone;
        this.date = date;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSponsor() {
        return sponsor;
    }

    public void setSponsor(String sponsor) {
        this.sponsor = sponsor;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
