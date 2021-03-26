package com.craftsman.model;

public class Requests {

    String id;

    String UID;
    String title ;
    String description ;
    String type ;



    public Requests() {
    }

    public Requests(String id , String UID , String title, String description ,String type  ) {
        this.id = id;

        this.UID = UID;
        this.title = title;
        this.description = description;
        this.type = type;


    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
