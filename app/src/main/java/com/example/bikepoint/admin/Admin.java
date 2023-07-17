package com.example.bikepoint.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.bikepoint.Login;
import com.example.bikepoint.R;
import com.example.bikepoint.models.Booking;
import com.example.bikepoint.user.ListServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Admin extends AppCompatActivity {

    private List<Booking> bookingList;
    private ArrayAdapter<Booking> bookingAdapter;

    Button logoutBtn;
    private CollectionReference booksCollection;

    public Admin() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        ListView bookingListView = findViewById(R.id.bookingListViewAdmn);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        logoutBtn = findViewById(R.id.logout_admn);

        bookingList = new ArrayList<>();
        bookingAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, bookingList);
        bookingListView.setAdapter(bookingAdapter);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });

        booksCollection = db.collection("bookings");

        booksCollection.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Booking book = document.toObject(Booking.class);
                                book.setDocumentId(document.getId());
                                Toast.makeText(Admin.this, book.getUserId(), Toast.LENGTH_SHORT).show();
                                bookingList.add(book);
                            }
                            bookingAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(Admin.this, "Error getting documents:", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        bookingListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Booking book = bookingList.get(position);
                showConfirmationDialog(book);
            }
        });
    }

    private void showConfirmationDialog(Booking book) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm");
        builder.setMessage("Mark booking as done: " + book.getBookingId() + "?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Mark booking as done in Firebase Database


                DocumentReference bookingRef = booksCollection.document(book.getDocumentId());
                bookingRef.update("completed",true)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(Admin.this, "Booking marked as done: " + book.getBookingId(), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Admin.this, "Booking is not marked as done: " + book.getBookingId(), Toast.LENGTH_SHORT).show();
                            }
                        });

                Toast.makeText(Admin.this, "Booking marked as done: " + book.getBookingId(), Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("No", null);
        builder.show();
    }
}