package edu.snhu.cs360.inventoryapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/*
 Main inventory screen.

 Lets the user:
 - add inventory items
 - view items in a list
 - send a test SMS notification
*/
public class InventoryActivity extends AppCompatActivity {

    // Code used when requesting SMS permission
    private static final int SMS_PERMISSION_CODE = 100;

    // Input fields for item name and quantity
    EditText editItemName, editItemQuantity;

    // Buttons for adding items and sending SMS
    Button btnAddItem, btnSendSms;

    // RecyclerView that shows the inventory list
    RecyclerView recyclerView;

    // Database helper for SQLite operations
    DatabaseHelper db;

    // Lists used to store item data temporarily
    ArrayList<String> itemNames;
    ArrayList<Integer> itemQtys;
    ArrayList<Integer> itemIds;
    // NEW improved data structure
    ArrayList<Item> items;

    // Adapter that connects the data to the RecyclerView
    SimpleInventoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the inventory screen layout
        setContentView(R.layout.activity_inventory);

        // Link screen fields and buttons to the code
        editItemName = findViewById(R.id.editItemName);
        editItemQuantity = findViewById(R.id.editItemQuantity);
        btnAddItem = findViewById(R.id.btnAddItem);
        btnSendSms = findViewById(R.id.btnSendSms);
        recyclerView = findViewById(R.id.recyclerInventory);

        // Create database helper
        db = new DatabaseHelper(this);

        // Set how the list will display items
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Load items from the database
        loadItems();

        // Button used to add a new item
        btnAddItem.setOnClickListener(v -> {

            String name = editItemName.getText().toString().trim();
            String qtyStr = editItemQuantity.getText().toString().trim();

            // Make sure both fields have values
            if (name.isEmpty() || qtyStr.isEmpty()) {
                Toast.makeText(this, "Enter all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            int qty;

            // Prevent crash if quantity is not a number
            try {
                qty = Integer.parseInt(qtyStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Quantity must be a number", Toast.LENGTH_SHORT).show();
                return;
            }

            // Insert item into the database
            boolean success = addNewItem(name, qty);

            if (success) {
                Toast.makeText(this, "Item Added", Toast.LENGTH_SHORT).show();

                // Clear the input fields
                editItemName.setText("");
                editItemQuantity.setText("");

                // Reload the list to show the new item
                loadItems();

            } else {
                Toast.makeText(this, "Error Adding Item", Toast.LENGTH_SHORT).show();
            }
        });

        // Button used to test SMS notifications
        btnSendSms.setOnClickListener(v -> {
            if (checkSmsPermission()) {
                sendTestSms();
            } else {
                requestSmsPermission();
            }
        });
    }

    // Load all items from the database and show them in the list
    // Initialize data structures (enhanced with Item object list)
    private void loadItems() {
        items = new ArrayList<>();
        itemNames = new ArrayList<>();
        itemQtys = new ArrayList<>();
        itemIds = new ArrayList<>();

        Cursor cursor = db.getAllItems();

        // Read each row returned from the database
        //New enhancement: create Item objects while keeping adapter lists
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            int qty = cursor.getInt(2);

            // NEW: Create structured Item object
            Item item = new Item(id, name, qty);
            items.add(item);

            // KEEP existing lists for adapter (so app still works)
            itemIds.add(id);
            itemNames.add(name);
            itemQtys.add(qty);
        }

        // Connect the data to the RecyclerView
        adapter = new SimpleInventoryAdapter(this, itemNames, itemQtys, itemIds);
        recyclerView.setAdapter(adapter);
    }

    // Helper method that inserts a new item into the database
    private boolean addNewItem(String name, int qty) {
        return db.addItem(name, qty, "General", "");
    }

    // Check if the app already has permission to send SMS
    private boolean checkSmsPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                == PackageManager.PERMISSION_GRANTED;
    }

    // Ask the user for SMS permission
    private void requestSmsPermission() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{Manifest.permission.SEND_SMS},
                SMS_PERMISSION_CODE
        );
    }

    // Handle the result of the permission request
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == SMS_PERMISSION_CODE) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                sendTestSms();
            } else {
                Toast.makeText(this,
                        "SMS Permission Denied — App will continue without SMS alerts.",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    // Send a simple test SMS using the SmsService class
    private void sendTestSms() {
        try {

            String phone = "5554"; // emulator number
            String message = "Inventory Alert: This is a test SMS.";

            SmsService.sendSms(phone, message);

            Toast.makeText(this, "SMS Sent!", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {

            Toast.makeText(this,
                    "SMS could not be sent. Please check SMS permission or network connection.",
                    Toast.LENGTH_LONG).show();
        }
    }
}