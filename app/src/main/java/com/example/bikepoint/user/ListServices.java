package com.example.bikepoint.user;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bikepoint.R;
import com.example.bikepoint.models.Booking;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ListServices extends AppCompatActivity {

    private ListView bookingListView;
    private List<Booking> bookingList;
    private ArrayAdapter<Booking> bookingAdapter;

    private FirebaseFirestore db;

    private FirebaseUser currentUser;

    public ListServices() {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_services);

        bookingListView = findViewById(R.id.bookingListView);

        // Get the current user

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();



        bookingList = new ArrayList<>();
        bookingAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, bookingList);
        bookingListView.setAdapter(bookingAdapter);

        db = FirebaseFirestore.getInstance();


        CollectionReference booksCollection = db.collection("bookings");

        booksCollection.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Booking book = document.toObject(Booking.class);
                                Toast.makeText(ListServices.this, book.getUserId(), Toast.LENGTH_SHORT).show();
                                if (Objects.equals(book.getUserId(), currentUser.getUid())){
                                    bookingList.add(book);
                                }
                            }
                            bookingAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(ListServices.this, "Error getting documents:", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}