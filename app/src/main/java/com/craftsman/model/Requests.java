package com.craftsman.model;

public class Requests {

    String id;

    String UID;
    String title ;
    String description ;
    String category ;


    public Requests() {
    }

    public Requests(String id , String UID , String title, String description ,String category ) {
        this.id = id;

        this.UID = UID;
        this.title = title;
        this.description = description;
        this.category = category;

    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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


}
