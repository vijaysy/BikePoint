package com.example.bikepoint.models;

import java.util.List;

public class Booking {

    private String documentId;
    private String userId;
    private String bookingId;
    private String bikeNumber;
    private boolean completed;
    private Status status;
    private long createdAt;
    private long updatedAt;
    private String email;
    private long amount;
    private boolean isAmountPaid;


    public static class Builder {
        private String documentId;
        private String userId;
        private String bookingId;
        private String bikeNumber;
        private boolean completed;
        private Status status;
        private long createdAt;
        private long updatedAt;
        private String email;
        private long amount;
        private boolean isAmountPaid;

        public Builder() {}

        public Builder documentId(String documentId) {
            this.documentId = documentId;
            return this;
        }

        public Builder userId(String userId) {
            this.userId = userId;
            return this;
        }

        public Builder bookingId(String bookingId) {
            this.bookingId = bookingId;
            return this;
        }

        public Builder bikeNumber(String bikeNumber) {
            this.bikeNumber = bikeNumber;
            return this;
        }

        public Builder completed(boolean completed) {
            this.completed = completed;
            return this;
        }

        public Builder status(Status status) {
            this.status = status;
            return this;
        }

        public Builder createdAt(long createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Builder updatedAt(long updatedAt) {
            this.updatedAt = updatedAt;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder amount(long amount) {
            this.amount = amount;
            return this;
        }

        public Builder isAmountPaid(boolean isAmountPaid) {
            this.isAmountPaid = isAmountPaid;
            return this;
        }

        public Booking build() {
            Booking booking = new Booking();
            booking.documentId = this.documentId;
            booking.userId = this.userId;
            booking.bookingId = this.bookingId;
            booking.bikeNumber = this.bikeNumber;
            booking.completed = this.completed;
            booking.status = this.status;
            booking.createdAt = this.createdAt;
            booking.updatedAt = this.updatedAt;
            booking.email = this.email;
            booking.amount = this.amount;
            booking.isAmountPaid = this.isAmountPaid;
            return booking;
        }
    }


    public Booking() {
        // Default constructor required for calls to DataSnapshot.getValue(Booking.class)
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public boolean isAmountPaid() {
        return isAmountPaid;
    }

    public void setAmountPaid(boolean amountPaid) {
        isAmountPaid = amountPaid;
    }

}

