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
import com.example.bikepoint.models.Spares;
import com.example.bikepoint.models.Status;
import com.example.bikepoint.store.FireStoreHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ServiceBooking extends AppCompatActivity {

    private String serviceId = "S1";
    private FirebaseUser currentUser;

    private FireStoreHelper fireStoreHelper;

    Button buttonBookSvc;

    TextView bikeNumberView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_booking);

        fireStoreHelper = new FireStoreHelper();

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
                    List<Spares> spares = new ArrayList<>();
                    spares.add(new Spares("Tire", 100, bookingId));


                    Booking booking = new Booking.Builder()
                            .userId(currentUser.getUid())
                            .bookingId(bookingId)
                            .bikeNumber(bikeNum)
                            .completed(false)
                            .status(Status.RECEIVED)
                            .createdAt(System.currentTimeMillis())
                            .updatedAt(System.currentTimeMillis())
                            .email(currentUser.getEmail())
                            .amount(1000L)
                            .isAmountPaid(false)
                            .build();

                    fireStoreHelper.writeBooking(booking, new FireStoreHelper.WriteBookingCallback() {
                        @Override
                        public void onBookingWritten(String documentId) {
                            Toast.makeText(ServiceBooking.this, "Book stored successfully!", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onWriteBookingFailure(Exception e) {
                            Toast.makeText(ServiceBooking.this, "Error storing book", Toast.LENGTH_SHORT).show();
                        }
                    });


                    spares.forEach(s -> fireStoreHelper.writeSpares(s, new FireStoreHelper.WriteSparesCallback() {
                        @Override
                        public void onWritten(String documentId) {
                            Toast.makeText(ServiceBooking.this, "Spares stored successfully!", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onWriteFailure(Exception e) {
                            Toast.makeText(ServiceBooking.this, "Error storing Spares", Toast.LENGTH_SHORT).show();
                        }
                    }));

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