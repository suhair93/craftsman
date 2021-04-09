package com.craftsman.model;

import com.google.firebase.database.Exclude;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Work {

    String id;

    String UID;
    String CustomerID;
    String title ;
    String description ;
    int price;
    String upload_image;


    public Work() {
    }

    public Work(String id ,   String UID , String title, String description, int price, String upload_image) {
        this.id = id;

        this.UID = UID;
        this.title = title;
        this.description = description;
        this.price = price;
        this.upload_image = upload_image;
    }

    public Work(String title, String description, int price) {
        this.title = title;
        this.description = description;
        this.price = price;
    }

    public void setCustomerID(String customerID) {
        CustomerID = customerID;
    }

    public String getCustomerID() {
        return CustomerID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getUpload_image() {
        return upload_image;
    }

    public void setUpload_image(String upload_image) {
        this.upload_image = upload_image;
    }
}
