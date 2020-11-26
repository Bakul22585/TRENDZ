package com.example.trendz;

public class WithdrawRequestEntry {

    private String id;
    private String user_id;
    private String number;
    private String name;
    private String account_number;
    private String ifsc_code;
    private String amount;
    private String date;

    public WithdrawRequestEntry() {}

    public WithdrawRequestEntry(String id, String user_id, String number, String name, String account_number, String ifsc_code, String amount, String date) {
        this.id = id;
        this.user_id = user_id;
        this.number = number;
        this.name = name;
        this.account_number = account_number;
        this.ifsc_code = ifsc_code;
        this.amount = amount;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
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

    public String getAccount_number() {
        return account_number;
    }

    public void setAccount_number(String account_number) {
        this.account_number = account_number;
    }

    public String getIfsc_code() {
        return ifsc_code;
    }

    public void setIfsc_code(String ifsc_code) {
        this.ifsc_code = ifsc_code;
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
