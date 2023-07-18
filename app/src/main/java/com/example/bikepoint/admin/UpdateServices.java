package com.example.bikepoint.admin;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bikepoint.R;
import com.example.bikepoint.adapter.BookingAdapter;
import com.example.bikepoint.models.Booking;
import com.example.bikepoint.models.Status;
import com.example.bikepoint.store.FireStoreHelper;

import java.util.ArrayList;
import java.util.List;

public class UpdateServices extends AppCompatActivity {

    private List<Booking> bookingList;

    Button adminPageBtn;

    private FireStoreHelper fireStoreHelper;

    private BookingAdapter adapter;

    private Spinner dropdown;

    public UpdateServices() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_services);

        fireStoreHelper = new FireStoreHelper();

        ListView bookingListView = findViewById(R.id.bookingListViewAdmn);


        adminPageBtn = findViewById(R.id.logout_admn);

        bookingList = new ArrayList<>();
        adapter = new BookingAdapter(this, bookingList, true);
        bookingListView.setAdapter(adapter);

        adminPageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Admin.class);
                startActivity(intent);
                finish();
            }
        });


        fireStoreHelper.readBookings(new FireStoreHelper.ReadBookingsCallback() {
            @Override
            public void onBookingsRead(List<Booking> bookings) {
                bookingList.clear();
                for (Booking book : bookings) {
                    Toast.makeText(UpdateServices.this, book.getUserId(), Toast.LENGTH_SHORT).show();
                    bookingList.add(book);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onReadBookingsFailure(Exception e) {
                Toast.makeText(UpdateServices.this, "Error getting documents:", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void showConfirmationDialog(Booking book) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select an Item");
        builder.setView(dropdown);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Handle OK button click
                int selectedPosition = dropdown.getSelectedItemPosition();
                String selectedItem = (String) dropdown.getSelectedItem();
                Status status = Status.valueOf(selectedItem);
                book.setStatus(status);
                Toast.makeText(UpdateServices.this, "Selected: " + selectedItem, Toast.LENGTH_SHORT).show();
                fireStoreHelper.updateBooking(book.getDocumentId(), book, new FireStoreHelper.UpdateBookingCallback() {
                    @Override
                    public void onBookingUpdated(String userId) {
                        Toast.makeText(UpdateServices.this, "Booking marked as done: " + book.getBookingId(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onUpdateBookingFailure(Exception e) {
                        Toast.makeText(UpdateServices.this, "Booking is not marked as done: " + book.getBookingId(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Handle Cancel button click
                dialogInterface.dismiss();
            }
        });
        builder.create().show();
    }
}