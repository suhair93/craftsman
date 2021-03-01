package com.craftsman.model;

public class Post {

    String id;

    String UID;
    String title ;



    public Post() {
    }

    public Post(String id , String UID , String title  ) {
        this.id = id;

        this.UID = UID;
        this.title = title;


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



}
