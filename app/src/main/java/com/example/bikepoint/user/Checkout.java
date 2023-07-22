package com.example.bikepoint.user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.bikepoint.R;
import com.example.bikepoint.adapter.ItemAdapter;
import com.example.bikepoint.models.Booking;
import com.example.bikepoint.models.Item;
import com.example.bikepoint.models.ItemType;
import com.example.bikepoint.models.Service;
import com.example.bikepoint.models.Spares;
import com.example.bikepoint.models.Status;
import com.example.bikepoint.store.FireStoreHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Checkout extends AppCompatActivity {

    private RecyclerView recyclerViewBasket;
    private ItemAdapter basketAdapter;
    private List<Item> basketItems;
    private TextView textViewTotalAmount;
    private String totalAmountText;

    private FirebaseUser currentUser;

    private FireStoreHelper fireStoreHelper;

    private TextView bikeNumberView;

    private long totalAmount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();
//        totalAmountText = "Total Amount: " + String.format("%.2f", 0);

        bikeNumberView = findViewById(R.id.bike_number);
        recyclerViewBasket = findViewById(R.id.recyclerViewBasket);
        textViewTotalAmount = findViewById(R.id.textViewTotalAmount);
        Button btnCheckout = findViewById(R.id.btnCheckout);

        fireStoreHelper = new FireStoreHelper();

        Button btnHome = findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), UserActivity.class);
                startActivity(intent);
                finish();
            }
        });

        basketItems = (List<Item>) getIntent().getSerializableExtra("basketItems");

        if (basketItems == null || basketItems.isEmpty()) {
            Toast.makeText(this, "Basket is empty", Toast.LENGTH_SHORT).show();
        } else {
            basketAdapter = new ItemAdapter(basketItems, item -> {
                // Handle clicks on basket items if needed
            }, true);

            recyclerViewBasket.setLayoutManager(new LinearLayoutManager(this));
            recyclerViewBasket.setAdapter(basketAdapter);

            totalAmount = calculateTotalAmount();
            totalAmountText = "Total Amount: " + totalAmount;
            textViewTotalAmount.setText(totalAmountText);
        }

        btnCheckout.setOnClickListener(v -> checkoutBasket());
    }

    private long calculateTotalAmount() {
        long total = 0;
        for (Item item : basketItems) {
            total += item.getPrice();
        }
        return total;
    }

    private void checkoutBasket() {
        List<Spares> spares = new ArrayList<>();
        List<Service> services = new ArrayList<>();
        String bookingId = String.valueOf(UUID.randomUUID());
        StringBuilder checkoutMessage = new StringBuilder("Checkout Items:\n");
        for (Item item : basketItems) {
            if (item.getType() == ItemType.SPARE) {
                spares.add(new Spares(item.getDescription(), item.getPrice(), bookingId, item.getId()));
            } else {
                services.add(new Service(item.getId(), item.getDescription(), item.getPrice(), bookingId));
            }
            checkoutMessage.append(item.getDescription()).append("\n");
        }
        Toast.makeText(this, checkoutMessage.toString(), Toast.LENGTH_LONG).show();
        String bikeNum = bikeNumberView.getText().toString().trim();
        Booking booking = new Booking.Builder()
                .userId(currentUser.getUid())
                .bookingId(bookingId)
                .bikeNumber(bikeNum)
                .completed(false)
                .status(Status.RECEIVED)
                .createdAt(System.currentTimeMillis())
                .updatedAt(System.currentTimeMillis())
                .email(currentUser.getEmail())
                .amount(totalAmount)
                .isAmountPaid(false)
                .build();

        fireStoreHelper.writeBooking(booking, new FireStoreHelper.WriteBookingCallback() {
            @Override
            public void onBookingWritten(String documentId) {
                Toast.makeText(Checkout.this, "Book stored successfully!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onWriteBookingFailure(Exception e) {
                Toast.makeText(Checkout.this, "Error storing book", Toast.LENGTH_SHORT).show();
            }
        });


        spares.forEach(s -> fireStoreHelper.writeSpares(s, new FireStoreHelper.WriteSparesCallback() {
            @Override
            public void onWritten(String documentId) {
                Toast.makeText(Checkout.this, "Spares stored successfully!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onWriteFailure(Exception e) {
                Toast.makeText(Checkout.this, "Error storing Spares", Toast.LENGTH_SHORT).show();
            }
        }));

        services.forEach(s -> fireStoreHelper.writeService(s, new FireStoreHelper.WriteServiceCallback() {
            @Override
            public void onWritten(String documentId) {
                Toast.makeText(Checkout.this, "Service stored successfully!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onWriteFailure(Exception e) {
                Toast.makeText(Checkout.this, "Error storing Service", Toast.LENGTH_SHORT).show();
            }
        }));

        Intent intent = new Intent(getApplicationContext(), UserActivity.class);
        startActivity(intent);
        finish();



        // Here you can proceed with the checkout process, e.g., payment gateway integration, order placement, etc.
    }
}
