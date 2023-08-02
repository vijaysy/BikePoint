package com.example.bikepoint.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bikepoint.R;
import com.example.bikepoint.adapter.ItemAdapter;
import com.example.bikepoint.models.Item;
import com.example.bikepoint.store.FireStoreHelper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cart extends AppCompatActivity {

    private List<Item> itemList;
    private List<Item> basketItems;
    private final Map<String, List<Item>> stringListMap = new HashMap<>();
    private View loaderView;

    private TextView totalAmountTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        loaderView = findViewById(R.id.loaderProgressBar);

        RecyclerView recyclerViewItems = findViewById(R.id.recyclerViewItems);
        Button btnViewBasket = findViewById(R.id.btnViewBasket);

        FireStoreHelper fireStoreHelper = new FireStoreHelper();
        totalAmountTextView = findViewById(R.id.textViewTotalAmount);

        Button btnHome = findViewById(R.id.btnHome);
        btnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), UserActivity.class);
                startActivity(intent);
                finish();
            }
        });

        itemList = new ArrayList<>();
        basketItems = new ArrayList<>();
        ItemAdapter itemAdapter = new ItemAdapter(itemList, this::addItemToBasket);

        showLoader();
        fireStoreHelper.readListings(new FireStoreHelper.ReadListingCallback() {
            @Override
            public void onRead(List<Item> items) {
                itemList.clear();
                itemList.addAll(items);
                hideLoader();
                itemAdapter.notifyDataSetChanged();
            }

            @Override
            public void onReadFailure(Exception e) {
                Toast.makeText(Cart.this, "Read Services Listing Failed", Toast.LENGTH_SHORT).show();
            }
        });

        recyclerViewItems.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewItems.setAdapter(itemAdapter);

        btnViewBasket.setOnClickListener(v -> viewBasket());
    }


    private void addItemToBasket(Item item, boolean addItem, TextView textViewTotalAmount) {
        List<Item> items = stringListMap.getOrDefault(item.getId(), new ArrayList<>());
        assert items != null;
        if (addItem) {
            items.add(item);
        } else if (items.size() > 0) {
            items.remove(0);
        }

        stringListMap.put(item.getId(), items);
        textViewTotalAmount.setText(String.format("%d", item.getPrice() * items.size()));
        long totalAmount = calculateTotalAmount();
        totalAmountTextView.setText(String.format("Total Amount = %d", totalAmount));
        basketItems.add(item);
    }

    private void viewBasket() {
        if (basketItems.isEmpty()) {
            Toast.makeText(this, "Basket is empty", Toast.LENGTH_SHORT).show();
        } else {
            basketItems.clear();
            for (String key : stringListMap.keySet()) {
                List<Item> itemList = stringListMap.get(key);
                assert itemList != null;
                if (itemList.size() > 0) {
                    basketItems.addAll(itemList);
                }
            }
            Intent intent = new Intent(this, Checkout.class);
            intent.putExtra("basketItems", (Serializable) basketItems);
            startActivity(intent);
        }
    }

    private void showLoader() {
        loaderView.setVisibility(View.VISIBLE);
    }

    private void hideLoader() {
        loaderView.setVisibility(View.GONE);
    }

    private long calculateTotalAmount() {
        long total = 0;
        for (String key : stringListMap.keySet()) {
            List<Item> itemList = stringListMap.get(key);
            assert itemList != null;
            if (itemList.size() > 0) {
                total = total + (itemList.size() * itemList.get(0).getPrice());
            }
        }
        return total;
    }
}