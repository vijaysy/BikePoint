package com.example.bikepoint.models;

public class Service {

    private String id;

    private String description;

    private long amount;

    private boolean active;

    private String bookingId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public Service(String id, String description, long amount, boolean active) {
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.active = active;
    }

    public Service(String id, String description, long amount, boolean active,String bookingId) {
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.active = active;
        this.bookingId = bookingId;
    }

    public Service(String id, String description, long amount,String bookingId) {
        this.id = id;
        this.description = description;
        this.amount = amount;
        this.bookingId = bookingId;
    }


    public Service() {
    }
}
