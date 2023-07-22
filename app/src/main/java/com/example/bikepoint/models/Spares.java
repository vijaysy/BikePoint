package com.example.bikepoint.models;

public class Spares {

    private String description;

    private long amount;

    private boolean active;

    private String bookingId;

    private String id;

    public Spares(String description, long amount, String bookingId) {
        this.description = description;
        this.amount = amount;
        this.bookingId = bookingId;
    }

    public Spares(String description, long amount, String bookingId, String id) {
        this.description = description;
        this.amount = amount;
        this.bookingId = bookingId;
        this.id = id;
    }

    public Spares() {
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

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
