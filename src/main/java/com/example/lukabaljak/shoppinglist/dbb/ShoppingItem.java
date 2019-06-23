package com.example.lukabaljak.shoppinglist.dbb;

public class ShoppingItem {

    private int id;
    private String photoURL;
    private String description;

    public ShoppingItem(int id, String photoURL, String description) {
        this.id = id;
        this.photoURL = photoURL;
        this.description = description;
    }

    public ShoppingItem() {

    }

    public ShoppingItem(String photoURL, String description) {
        this.photoURL = photoURL;
        this.description = description;
    }

    public ShoppingItem(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
