package com.example.bikepoint.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bikepoint.R;
import com.example.bikepoint.adapter.BookingAdapter;
import com.example.bikepoint.models.Booking;
import com.example.bikepoint.store.FireStoreHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ServicesHistory extends AppCompatActivity {

    private List<Booking> bookingList;

    private FirebaseUser currentUser;

    private FireStoreHelper fireStoreHelper;

    private BookingAdapter adapter;

    public ServicesHistory() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services_history);

        fireStoreHelper = new FireStoreHelper();

        ListView bookingListView = findViewById(R.id.bookingListView);
        Button homeBtn = findViewById(R.id.user_home);
        // Get the current user

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();

        bookingList = new ArrayList<>();
        adapter = new BookingAdapter(this, bookingList);
        bookingListView.setAdapter(adapter);

        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), UserActivity.class);
                startActivity(intent);
                finish();
            }
        });

        fireStoreHelper.readBookings(new FireStoreHelper.ReadBookingsCallback() {
            @Override
            public void onBookingsRead(List<Booking> bookings) {
                bookingList.clear();
                for (Booking book : bookings) {
                    Toast.makeText(ServicesHistory.this, book.getUserId(), Toast.LENGTH_SHORT).show();
                    if (Objects.equals(book.getUserId(), currentUser.getUid())){
                        bookingList.add(book);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onReadBookingsFailure(Exception e) {
                Toast.makeText(ServicesHistory.this, "Error getting documents:", Toast.LENGTH_SHORT).show();
            }
        });

    }
}