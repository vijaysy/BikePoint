package com.example.bikepoint.admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bikepoint.Login;
import com.example.bikepoint.R;
import com.google.firebase.auth.FirebaseAuth;

public class Admin extends AppCompatActivity {

    Button logoutBtn;

    Button svcBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        logoutBtn = findViewById(R.id.logout);
        svcBtn = findViewById(R.id.service);

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });


        svcBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), UpdateServices.class);
                startActivity(intent);
                finish();
            }
        });
    }
}