package com.example.bikepoint.store;

import com.example.bikepoint.models.Booking;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class FireStoreHelper {

    public static final String BOOKINGS = "/bookings";
    private final FirebaseFirestore db;

    public FireStoreHelper() {
        db = FirebaseFirestore.getInstance();
    }

    public void writeBooking(Booking booking, WriteBookingCallback callback) {
        CollectionReference usersCollection = db.collection(BOOKINGS);
        usersCollection.add(booking)
                .addOnSuccessListener(documentReference -> {
                    String documentId = documentReference.getId();
                    if (callback != null) {
                        callback.onBookingWritten(documentId);
                    }
                })
                .addOnFailureListener(e -> {
                    if (callback != null) {
                        callback.onWriteBookingFailure(e);
                    }
                });
    }

    public void updateBooking(String bookingId, Booking updatedBooking, UpdateBookingCallback callback) {
        CollectionReference usersCollection = db.collection(BOOKINGS);
        DocumentReference userRef = usersCollection.document(bookingId);
        userRef.set(updatedBooking)
                .addOnSuccessListener(aVoid -> {
                    if (callback != null) {
                        callback.onBookingUpdated(bookingId);
                    }
                })
                .addOnFailureListener(e -> {
                    if (callback != null) {
                        callback.onUpdateBookingFailure(e);
                    }
                });
    }


    public void readBookings(ReadBookingsCallback callback) {
        CollectionReference usersCollection = db.collection(BOOKINGS);
        usersCollection.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Booking> bookingList = new ArrayList<>();
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Booking booking = documentSnapshot.toObject(Booking.class);
                        assert booking != null;
                        booking.setDocumentId(documentSnapshot.getId());
                        bookingList.add(booking);
                        if (callback != null) {
                            callback.onBookingsRead(bookingList);
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    if (callback != null) {
                        callback.onReadBookingsFailure(e);
                    }
                });
    }

    public interface WriteBookingCallback {
        void onBookingWritten(String documentId);
        void onWriteBookingFailure(Exception e);
    }

    public interface ReadBookingsCallback {
        void onBookingsRead(List<Booking> bookings);
        void onReadBookingsFailure(Exception e);
    }

    public interface UpdateBookingCallback {
        void onBookingUpdated(String userId);
        void onUpdateBookingFailure(Exception e);
    }
}
