package com.craftsman.model;

import com.google.firebase.database.Exclude;

import java.text.SimpleDateFormat;
import java.util.Date;

public class User {
    String id;
    String fullname;
    String Email;
    String password;
    String phone;
    String category;
    int UserType;
    String  image;
    String  cv;
    String  city;
    String  description;
    public User() {
    }

    public User(String id, String fullname, String email, String password, String phone, int userType ,String category,String city,  String  cv) {
        this.id = id;
        this.fullname = fullname;
        Email = email;
        this.password = password;
        this.phone = phone;
        UserType = userType;
        this.category = category;
        this.city = city;
        this.cv = cv;
    }
    public User(String id, String fullname, String email, String password, String phone, int userType ,String city) {
        this.id = id;
        this.fullname = fullname;
        Email = email;
        this.password = password;
        this.phone = phone;
        UserType = userType;
        this.category = category;
        this.city = city;
    }
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User(String fullname, String category) {
        this.fullname = fullname;
        this.category = category;
    }

    public String getCv() {
        return cv;
    }

    public void setCv(String cv) {
        this.cv = cv;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getUserType() {
        return UserType;
    }

    public void setUserType(int userType) {
        UserType = userType;
    }
}
