package com.example.bikepoint.models;

import java.io.Serializable;

public class Item implements Serializable {

    private ItemType type;

    private long price;

    private String description;

    private String id;

    public Item(ItemType type, long price, String description,String id) {
        this.type = type;
        this.price = price;
        this.description = description;
        this.id = id;
    }

    public Item() {
    }

    public ItemType getType() {
        return type;
    }

    public void setType(ItemType type) {
        this.type = type;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
