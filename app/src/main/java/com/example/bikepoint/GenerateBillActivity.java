package com.example.bikepoint;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Callback;
import okhttp3.Credentials;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class GenerateBillActivity extends AppCompatActivity {

    private static final int REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION = 1;
    private static final int REQUEST_MANAGE_EXTERNAL_STORAGE_PERMISSION = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_bill);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivityForResult(intent, REQUEST_MANAGE_EXTERNAL_STORAGE_PERMISSION);
            }
        } else {
            // For Android 10 and lower, request the permission
            requestWriteStoragePermission();
        }

        Button generatePdfButton = findViewById(R.id.generatePdfButton);
        String htmlFilePath = "MyLayout.html";
        String outOutFileName = "HelloWorld.pdf";
        generatePdfButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generatePdf(htmlFilePath, outOutFileName);
            }
        });

        // Generate the HTML content
        String htmlContent = "<html><body><h1>Hello, MyLayout!</h1><p>This is a sample HTML content.</p></body></html>";
//        String htmlContent =  getHTML();

        // Save the HTML content to a file
        File htmlFile = saveHTMLToFile(htmlFilePath, htmlContent);
        if (htmlFile != null) {
            Toast.makeText(GenerateBillActivity.this, "HTML file saved to: " + htmlFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(GenerateBillActivity.this, "Failed to save HTML file.", Toast.LENGTH_SHORT).show();

        }
    }

    private String getHTML(){
        String userName = "John Doe";
        String companyName = "Acme Inc.";
        String customerName = "Customer123";
        String date = "2023-08-03";
        String time = "12:30 PM";
        double totalAmount = 25.00;

        // Sample list of items and their prices: Replace this with your actual data
        List<Item> items = new ArrayList<>();
        items.add(new Item("Product A", 12.50));
        items.add(new Item("Product B", 10.00));

        // Get the HTML content template
        String htmlContent = getHTMLContent();

        // Replace the variables in the HTML content
        htmlContent = htmlContent.replace("{{USER_NAME}}", userName);
        htmlContent = htmlContent.replace("{{COMPANY_NAME}}", companyName);
        htmlContent = htmlContent.replace("{{CUSTOMER_NAME}}", customerName);
        htmlContent = htmlContent.replace("{{DATE}}", date);
        htmlContent = htmlContent.replace("{{TIME}}", time);

        // Replace the item names and prices dynamically
        StringBuilder itemRows = new StringBuilder();
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        for (Item item : items) {
            String itemName = item.getName();
            double itemPrice = item.getPrice();
            String formattedPrice = "$" + decimalFormat.format(itemPrice);

            String itemRow = "<div class=\"item-row\">\n" +
                    "    <span class=\"item-name\">" + itemName + "</span>\n" +
                    "    <span class=\"item-price\">" + formattedPrice + "</span>\n" +
                    "</div>\n";

            itemRows.append(itemRow);
        }

        // Replace the placeholder for item rows in the HTML content
        htmlContent = htmlContent.replace("<!-- Add more items and their prices here -->", itemRows.toString());

        // Replace the total amount dynamically
        String formattedTotalAmount = "$" + decimalFormat.format(totalAmount);
        htmlContent = htmlContent.replace("{{TOTAL_AMOUNT}}", formattedTotalAmount);
        return htmlContent;

    }

    private File saveHTMLToFile(String fileName, String content) {
        File file = null;
        try {
            // Create the file in the app's internal storage
            File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            file = new File(downloadsDir, fileName);

            FileOutputStream fos = new FileOutputStream(file);
            fos.write(content.getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    private void generatePdf(String htmlFilePath, String outPutFileName) {
        String username = "vijayyalasangimath";
        String apiKey = "701998bb8fef1ef76be0b8416daee97b";

        // Replace with the path to the HTML file you want to convert
        File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File htmlFile = new File(downloadsDir, htmlFilePath);


        // Create the OkHttp client
        OkHttpClient client = new OkHttpClient();

        // Build the request body with the HTML file
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", htmlFile.getName(), RequestBody.create(okhttp3.MediaType.parse("text/html"), htmlFile))
                .build();

        // Build the request with Basic Authentication and the request body
        Request request = new Request.Builder()
                .url("https://api.pdfcrowd.com/convert/20.10/")
                .addHeader("Authorization", Credentials.basic(username, apiKey))
                .post(requestBody)
                .build();

        // Make the asynchronous HTTP call
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {

            }

            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                if (response.isSuccessful()) {
                    // The response contains the PDF file
                    File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                    File pdfFile = new File(downloadsDir, outPutFileName);

                    FileOutputStream fos = new FileOutputStream(pdfFile);
                    fos.write(response.body().bytes());
                    fos.close();
                    Toast.makeText(GenerateBillActivity.this, "PDF saved to: " + pdfFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(GenerateBillActivity.this, "HTTP request failed with code: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void requestWriteStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION);
        }
    }


    // Handle permission request result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    private static class Item {
        private String name;
        private double price;

        public Item(String name, double price) {
            this.name = name;
            this.price = price;
        }

        public String getName() {
            return name;
        }

        public double getPrice() {
            return price;
        }
    }

    private static String getHTMLContent() {
        // Replace this with the actual HTML content
        return "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <title>Receipt</title>\n" +
                "    <style>\n" +
                "        /* Add the CSS styles here */\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"receipt-container\">\n" +
                "        <div class=\"receipt-header\">\n" +
                "            <h2>Receipt</h2>\n" +
                "        </div>\n" +
                "        <div>\n" +
                "            <strong>User Name:</strong> {{USER_NAME}}\n" +
                "        </div>\n" +
                "        <div>\n" +
                "            <strong>Customer Name:</strong> {{CUSTOMER_NAME}}\n" +
                "        </div>\n" +
                "        <div>\n" +
                "            <strong>Company Name:</strong> {{COMPANY_NAME}}\n" +
                "        </div>\n" +
                "        <div>\n" +
                "            <strong>Date:</strong> {{DATE}}\n" +
                "        </div>\n" +
                "        <div>\n" +
                "            <strong>Time:</strong> {{TIME}}\n" +
                "        </div>\n" +
                "        <div class=\"receipt-items\">\n" +
                "            <!-- Add more items and their prices here -->\n" +
                "        </div>\n" +
                "        <div class=\"receipt-total\">\n" +
                "            <strong>Total:</strong> ${{TOTAL_AMOUNT}}\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>";
    }
}
