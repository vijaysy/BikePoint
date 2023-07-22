package com.example.bikepoint.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bikepoint.R;
import com.example.bikepoint.models.Booking;
import com.example.bikepoint.models.Status;
import com.example.bikepoint.store.FireStoreHelper;

import java.util.List;

public class BookingAdapter extends ArrayAdapter<Booking> {

    private final Context context;
    private final List<Booking> bookingList;

    private final FireStoreHelper fireStoreHelper;

    private final boolean addSpinner;

    private String statusString = "RECEIVED";

    public BookingAdapter(Context context, List<Booking> bookingList) {
        super(context, 0, bookingList);
        fireStoreHelper = new FireStoreHelper();
        this.context = context;
        this.bookingList = bookingList;
        this.addSpinner = false;
    }

    public BookingAdapter(Context context, List<Booking> bookingList, boolean addSpinner) {
        super(context, 0, bookingList);
        fireStoreHelper = new FireStoreHelper();
        this.context = context;
        this.bookingList = bookingList;
        this.addSpinner = addSpinner;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View itemView = convertView;
        if (itemView == null) {
            itemView = LayoutInflater.from(context).inflate(R.layout.list_bookings_layout, parent, false);
        }

        Booking booking = bookingList.get(position);

        Button svcUpdateButton = itemView.findViewById(R.id.svc_update_btn);
        svcUpdateButton.setVisibility(View.GONE);

        Spinner spinner = itemView.findViewById(R.id.spinner);
        spinner.setVisibility(View.GONE);

        TextView userId = itemView.findViewById(R.id.userId);
        TextView bikeNumber = itemView.findViewById(R.id.bikeNumber);
        TextView bookingId = itemView.findViewById(R.id.bookingId);
        TextView sevId = itemView.findViewById(R.id.sevId);
        TextView completed = itemView.findViewById(R.id.completed);
        TextView status = itemView.findViewById(R.id.status);


        if (addSpinner) {
            svcUpdateButton.setVisibility(View.VISIBLE);
            spinner.setVisibility(View.VISIBLE);


            ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(context,
                    R.array.spinner_booking_status, android.R.layout.simple_spinner_item);
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(spinnerAdapter);

            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String selectedItem = (String) parent.getItemAtPosition(position);
                    statusString = (String) parent.getItemAtPosition(position);
                    ;
                    Toast.makeText(context, "Selected: " + selectedItem, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // Do nothing
                }
            });

            svcUpdateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    handleItemClick(position);
                }
            });

        }

        userId.setText(booking.getUserId());
        bikeNumber.setText(booking.getBikeNumber());
        bookingId.setText(booking.getBookingId());
        completed.setText(String.valueOf(booking.isCompleted()));
        status.setText(booking.getStatus().toString());

        return itemView;
    }

    private void handleItemClick(int position) {
        Booking clickedBooking = bookingList.get(position);
        clickedBooking.setStatus(Status.valueOf(statusString));
        fireStoreHelper.updateBooking(clickedBooking.getDocumentId(), clickedBooking, new FireStoreHelper.UpdateBookingCallback() {
            @Override
            public void onBookingUpdated(String userId) {
                Toast.makeText(context, "Booking marked as: " + statusString + " : " + clickedBooking.getBookingId(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onUpdateBookingFailure(Exception e) {
                Toast.makeText(context, "Booking is not marked as: " + statusString + " : " + clickedBooking.getBookingId(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}






