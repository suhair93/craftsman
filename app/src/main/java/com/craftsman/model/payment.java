package com.craftsman.model;

import java.util.ArrayList;

public class payment {

    String id;

    ArrayList list = new ArrayList<Work>();
    String type_payment;
    String number_home;
    String address;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList getList() {
        return list;
    }

    public void setList(ArrayList list) {
        this.list = list;
    }

    public String getType_payment() {
        return type_payment;
    }

    public void setType_payment(String type_payment) {
        this.type_payment = type_payment;
    }

    public String getNumber_home() {
        return number_home;
    }

    public void setNumber_home(String number_home) {
        this.number_home = number_home;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
