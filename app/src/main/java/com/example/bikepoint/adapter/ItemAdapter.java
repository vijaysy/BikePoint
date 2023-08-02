package com.example.bikepoint.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bikepoint.R;
import com.example.bikepoint.models.Item;
import com.example.bikepoint.models.ItemType;

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
        private final TextView textViewAddItem;
        private final TextView textViewRemoveItem;
        private final TextView textViewTotalAmount;

        private final ImageView imageView;


        public ViewHolder(@NonNull View itemView, boolean disableAddToCart) {
            super(itemView);
            textViewItemType = itemView.findViewById(R.id.item_name);
            textViewItemDescription = itemView.findViewById(R.id.item_short_desc);
            textViewItemAmount = itemView.findViewById(R.id.item_price);
            textViewAddItem = itemView.findViewById(R.id.add_item);
            textViewRemoveItem = itemView.findViewById(R.id.remove_item);
            textViewTotalAmount = itemView.findViewById(R.id.iteam_amount);
            imageView = itemView.findViewById(R.id.product_thumb);
            if (disableAddToCart) {
                textViewAddItem.setVisibility(View.GONE);
                textViewRemoveItem.setVisibility(View.GONE);
                textViewAddItem.setVisibility(View.GONE);
                textViewTotalAmount.setVisibility(View.GONE);
            }
        }

        public void bind(Item item, OnItemClickListener listener) {
            if (item.getType() == ItemType.SERVICE)
                imageView.setImageResource(R.drawable.service_img);
            else
                imageView.setImageResource(R.drawable.spare_parts_img);
            textViewItemType.setText(item.getType().toString());
            textViewItemDescription.setText(item.getDescription());
            textViewItemAmount.setText(String.valueOf(item.getPrice()));
            textViewAddItem.setOnClickListener(v -> listener.onItemClick(item, true, textViewTotalAmount));
            textViewRemoveItem.setOnClickListener(v -> listener.onItemClick(item, false, textViewTotalAmount));


        }
    }

    public interface OnItemClickListener {
        void onItemClick(Item item, boolean addItem, TextView textViewTotalAmount);
    }
}
