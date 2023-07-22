package com.example.bikepoint.store;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.bikepoint.models.Booking;
import com.example.bikepoint.models.Item;
import com.example.bikepoint.models.Service;
import com.example.bikepoint.models.Spares;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class FireStoreHelper {

    public static final String BOOKINGS = "/bookings";
    public static final String SPARES = "/spares";

    public static final String SERVICE = "/service";

    public static final String LISTINGS = "/listings";


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

    public void writeSpares(Spares spares, WriteSparesCallback callback) {
        CollectionReference usersCollection = db.collection(SPARES);
        usersCollection.add(spares)
                .addOnSuccessListener(documentReference -> {
                    String documentId = documentReference.getId();
                    if (callback != null) {
                        callback.onWritten(documentId);
                    }
                })
                .addOnFailureListener(e -> {
                    if (callback != null) {
                        callback.onWriteFailure(e);
                    }
                });
    }

    public void writeService(Service service, WriteServiceCallback callback) {
        CollectionReference usersCollection = db.collection(SERVICE);
        usersCollection.add(service)
                .addOnSuccessListener(documentReference -> {
                    String documentId = documentReference.getId();
                    if (callback != null) {
                        callback.onWritten(documentId);
                    }
                })
                .addOnFailureListener(e -> {
                    if (callback != null) {
                        callback.onWriteFailure(e);
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

    public void readSpares(ReadSparesCallback callback) {
        CollectionReference usersCollection = db.collection(SPARES);
        usersCollection.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Spares> sparesList = new ArrayList<>();
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Spares spares = documentSnapshot.toObject(Spares.class);
                        assert spares != null;
                        sparesList.add(spares);
                        if (callback != null) {
                            callback.onRead(sparesList);
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    if (callback != null) {
                        callback.onReadFailure(e);
                    }
                });
    }


    public void readListings(ReadListingCallback callback) {
        CollectionReference usersCollection = db.collection(LISTINGS);
        usersCollection.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Item> itemList = new ArrayList<>();
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Item item = documentSnapshot.toObject(Item.class);
                        assert item != null;
                        itemList.add(item);
                    }
                    if (callback != null) {
                        callback.onRead(itemList);
                    }

                })
                .addOnFailureListener(e -> {
                    if (callback != null) {
                        callback.onReadFailure(e);
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

    public interface ReadSparesCallback {
        void onRead(List<Spares> sparesList);

        void onReadFailure(Exception e);
    }

    public interface WriteSparesCallback {
        void onWritten(String documentId);

        void onWriteFailure(Exception e);
    }

    public interface WriteServiceCallback {
        void onWritten(String documentId);

        void onWriteFailure(Exception e);
    }

    public interface ReadListingCallback {
        void onRead(List<Item> serviceList);

        void onReadFailure(Exception e);
    }

    public interface UpdateBookingCallback {
        void onBookingUpdated(String userId);

        void onUpdateBookingFailure(Exception e);
    }
}
