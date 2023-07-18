package com.example.bikepoint.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bikepoint.R;
import com.example.bikepoint.models.Booking;
import com.example.bikepoint.models.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.UUID;

public class ServiceBooking extends AppCompatActivity {

    private String serviceId = "S1";
    private FirebaseUser currentUser;

    private FirebaseFirestore db;

    Button buttonBookSvc;

    TextView bikeNumberView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_booking);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        CollectionReference booksCollection = db.collection("/bookings");
        DocumentReference bookDocument = booksCollection.document();

        buttonBookSvc = findViewById(R.id.btn_book_svc);
        bikeNumberView = findViewById(R.id.bike_number);

        Spinner spinner = findViewById(R.id.svc_list);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.svc_array, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                serviceId = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        // Get the current user
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();

        buttonBookSvc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String bikeNum = bikeNumberView.getText().toString().trim();

                if (!serviceId.isEmpty() && !bikeNum.isEmpty()) {

                    String bookingId = String.valueOf(UUID.randomUUID());
                    Booking booking = new Booking(currentUser.getUid(), bookingId, serviceId, bikeNum, false, Status.RECEIVED);

                    bookDocument.set(booking)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(ServiceBooking.this, "Book stored successfully!", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(ServiceBooking.this, "Error storing book!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });


                    // Clear the input fields
                    bikeNumberView.setText("");

                    Intent intent = new Intent(getApplicationContext(), UserActivity.class);
                    startActivity(intent);
                    finish();

                } else {
                    Toast.makeText(ServiceBooking.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

}