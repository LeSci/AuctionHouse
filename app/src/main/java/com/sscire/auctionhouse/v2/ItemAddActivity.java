package com.sscire.auctionhouse.v2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.sscire.auctionhouse.R;

// https://www.youtube.com/watch?v=RhGMd8SsA14&list=PLrnPJCHvNZuAPyh6nRXsvf5hF48SJWdJb&index=8
// Room + ViewModel + LiveData + RecyclerView (MVVM) Part 7 - ADD NOTE ACTIVITY - Android

public class ItemAddActivity extends AppCompatActivity {
    public static final String EXTRA_ITEMNAME = "com.sscire.auctionhouse.itemnameKey";
    public static final String EXTRA_DESCRIPTION = "com.sscire.auctionhouse.descriptionKey";
    public static final String EXTRA_PRICE= "com.sscire.auctionhouse.priceKey";
    private EditText mEditTextItemName;
    private EditText mEditTextItemDescription;
    private EditText mEditTextItemPrice;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_add);

        mEditTextItemName = findViewById(R.id.editTextItemName);
        mEditTextItemDescription = findViewById(R.id.editTextItemDescription);
        mEditTextItemPrice = findViewById(R.id.editTextItemPrice);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        setTitle("Add Item");
    }

    private void saveItem(){
        String itemName = mEditTextItemName.getText().toString();
        String itemDescription = mEditTextItemDescription.getText().toString();
        int itemPrice = Integer.parseInt(mEditTextItemPrice.getText().toString());

        if(itemName.trim().length()<3 || itemPrice < 1){
            Toast.makeText(this,
                    "Must provide valid item name/price",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        Intent data = new Intent();
        data.putExtra(EXTRA_ITEMNAME, itemName);
        data.putExtra(EXTRA_DESCRIPTION, itemDescription);
        data.putExtra(EXTRA_PRICE, itemPrice);

        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_item_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.saveItem) {
            saveItem();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}