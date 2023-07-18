package com.example.bikepoint.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bikepoint.Login;
import com.example.bikepoint.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class UserActivity extends AppCompatActivity {

    FirebaseAuth auth;
    Button logoutBtn;

    Button svcBtn;

    Button svcHistoryBtn;
    TextView textView;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        auth = FirebaseAuth.getInstance();
        logoutBtn = findViewById(R.id.logout);
        svcBtn = findViewById(R.id.service);
        svcHistoryBtn = findViewById(R.id.service_history);

        textView = findViewById(R.id.user_details);

        user = auth.getCurrentUser();

        if (Objects.isNull(user)){
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }else {
            textView.setText(user.getEmail());
        }

        svcBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ServiceBooking.class);
                startActivity(intent);
                finish();
            }
        });

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });

        svcHistoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ServicesHistory.class);
                startActivity(intent);
                finish();
            }
        });
    }
}