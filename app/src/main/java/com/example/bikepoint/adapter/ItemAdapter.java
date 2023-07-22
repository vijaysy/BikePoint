package com.example.bikepoint.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bikepoint.R;
import com.example.bikepoint.models.Item;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    private final List<Item> itemList;
    private final OnItemClickListener listener;

    private final boolean disableAddToCart;

    public ItemAdapter(List<Item> itemList, OnItemClickListener listener) {
        this.itemList = itemList;
        this.listener = listener;
        this.disableAddToCart = false;
    }

    public ItemAdapter(List<Item> itemList, OnItemClickListener listener, boolean disableAddToCart) {
        this.itemList = itemList;
        this.listener = listener;
        this.disableAddToCart = disableAddToCart;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row, parent, false);
        return new ViewHolder(itemView, disableAddToCart);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item item = itemList.get(position);
        holder.bind(item, listener);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView textViewItemType;
        private final TextView textViewItemDescription;
        private final TextView textViewItemAmount;
        private final Button btnAddToBasket;


        public ViewHolder(@NonNull View itemView, boolean disableAddToCart) {
            super(itemView);
            textViewItemType = itemView.findViewById(R.id.textViewItemType);
            textViewItemDescription = itemView.findViewById(R.id.textViewItemDescription);
            textViewItemAmount = itemView.findViewById(R.id.textViewItemAmount);
            btnAddToBasket = itemView.findViewById(R.id.btnAddToBasket);
            if (disableAddToCart) {
                btnAddToBasket.setVisibility(View.GONE);
            }
        }

        public void bind(Item item, OnItemClickListener listener) {
            textViewItemType.setText(item.getType().toString());
            textViewItemDescription.setText(item.getDescription());
            textViewItemAmount.setText(String.valueOf(item.getPrice()));
            btnAddToBasket.setOnClickListener(v -> listener.onItemClick(item));
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Item item);
    }
}
