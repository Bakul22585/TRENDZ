package com.example.trendz;

public class User {
    String id;
    String Fullname;
    String email;
    String username;
    String added_on;
    String mobile;
    String sponsor;
    String isJoin;

    public User(String id, String Fullname, String email, String username, String sponsor, String mobile, String isJoin) {
        this.id = id;
        this.Fullname = Fullname;
        this.email = email;
        this.username = username;
        this.sponsor = sponsor;
        this.mobile = mobile;
        this.isJoin = isJoin;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullname() {
        return Fullname;
    }

    public void setFullname(String fullname) {
        Fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAdded_on() {
        return added_on;
    }

    public void setAdded_on(String added_on) {
        this.added_on = added_on;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getSponsor() {
        return sponsor;
    }

    public void setSponsor(String sponsor) {
        this.sponsor = sponsor;
    }

    public String getIsJoin() {
        return isJoin;
    }

    public void setIsJoin(String isJoin) {
        this.isJoin = isJoin;
    }
}
