package edu.snhu.cs360.inventoryapp;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // OPTIONAL (but very useful):
        // Automatically go to Inventory screen
        // so your app actually shows the inventory UI
        Intent intent = new Intent(MainActivity.this, InventoryActivity.class);
        startActivity(intent);
        finish();
    }
}
