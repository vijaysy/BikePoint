package com.example.bikepoint.models;

public class Booking {

    private String documentId;
    private String userId;
    private String bookingId;
    private String sevId;
    private String bikeNumber;
    private boolean completed;

    public Booking() {
        // Default constructor required for calls to DataSnapshot.getValue(Booking.class)
    }

    public Booking(String userId, String bookingId, String sevId, String bikeNumber, boolean completed) {
        this.userId = userId;
        this.bookingId = bookingId;
        this.sevId = sevId;
        this.bikeNumber = bikeNumber;
        this.completed = completed;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getSevId() {
        return sevId;
    }

    public void setSevId(String sevId) {
        this.sevId = sevId;
    }

    public String getBikeNumber() {
        return bikeNumber;
    }

    public void setBikeNumber(String bikeNumber) {
        this.bikeNumber = bikeNumber;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }


    @Override
    public String toString() {
        return "Booking{" +
                "userId='" + userId + '\'' +
                ", bookingId='" + bookingId + '\'' +
                ", sevId='" + sevId + '\'' +
                ", bikeNumber='" + bikeNumber + '\'' +
                ", completed=" + completed +
                '}';
    }
}

