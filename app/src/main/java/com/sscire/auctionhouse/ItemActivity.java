package com.sscire.auctionhouse;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sscire.auctionhouse.db.AppDAO;
import com.sscire.auctionhouse.db.AppDatabase;

import java.util.List;

public class ItemActivity extends AppCompatActivity {
    private static final String USER_ID_KEY = "com.sscire.auctionhouse.userIdKey";
    private static final String PREFENCES_KEY = "com.sscire.auctionhouse.PREFENCES_KEY";

    private User mUser;
    private int mUserId = -1;
    private AppDAO mAppDAO;

    private Item mItem;
    private TextView mItemDisplay;

    private EditText mItemId;
    private EditText mItemName;
    private EditText mItemPrice;

    private Button mItemSubmitButton;

    private Button mItemDeleteButton;

    private Button mButtonHome;

    private SharedPreferences mPreferences = null;

    private List<Item> mItemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        mUserId = getIntent().getIntExtra(USER_ID_KEY, -1);
        getDatabase();
        wireupDisplay();
        refreshDisplay();



    }

    private void wireupDisplay() {
        mItemDisplay = findViewById(R.id.itemDisplay);
        mItemDisplay.setMovementMethod(new ScrollingMovementMethod());

        mItemId = findViewById(R.id.itemIDEditText);
        mItemName = findViewById(R.id.itemNameEditText);
        mItemPrice = findViewById(R.id.itemPriceEditText);

        mButtonHome = findViewById(R.id.buttonHome);
        mItemSubmitButton = findViewById(R.id.itemSubmitButton);
        mItemDeleteButton = findViewById(R.id.itemDeleteButton);

        mItemSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int userId = mUserId;
                String itemName =  mItemName.getText().toString();
                int itemPrice = Integer.parseInt(mItemPrice.getText().toString());

                Item newItem = new Item(userId, itemName, itemPrice);
                mAppDAO.insert(newItem);
                Toast.makeText(ItemActivity.this,
                        "Item: " + itemName + " created."
                        , Toast.LENGTH_SHORT).show();
                refreshDisplay();
            }
        });

        mItemDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int itemId = Integer.parseInt(mItemId.getText().toString());
                    mItem = mAppDAO.getItemByItemId(itemId);
                    String itemName = mItem.getItemName();
                    if (mItem == null){
                        throw new NumberFormatException ();
                    }
                    mAppDAO.delete(mItem);
                    Toast.makeText(ItemActivity.this,
                            "Item: " + itemId + " " + itemName + " deleted."
                            , Toast.LENGTH_SHORT).show();
                    refreshDisplay();
                } catch (NumberFormatException e){
                    Toast.makeText(ItemActivity.this,
                            "Enter a valid itemId"
                            , Toast.LENGTH_SHORT).show();
                }
            }
        });

        mButtonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = MainActivity.intentFactory(getApplicationContext(),mUserId);
                startActivity(intent);
            }
        });
    } // end WireUp

    private void refreshDisplay() {
        mItemList = mAppDAO.getItemsByUserId(mUserId);

        if (mItemList.size() <= 0) {
            mItemDisplay.setText(R.string.no_items_in_database);
            return;
        }

        StringBuilder sb = new StringBuilder();
        String pad = "         ";
        for (Item item : mItemList) {
            String itemName = item.getItemName();
            int itemPrice = item.getItemPrice();
            sb.append(item.getItemId() + pad);
            sb.append(item.getUserId() + pad);
            sb.append(itemName + pad);
            sb.append(itemPrice + pad);
            sb.append("\n");
        }
        mItemDisplay.setText(sb.toString());
    }

    private void getDatabase(){
        mAppDAO = Room.databaseBuilder(this, AppDatabase.class, AppDatabase.DB_NAME)
                .allowMainThreadQueries()
                .build()
                .getAppDAO();
    }

    public static Intent intentFactory(Context context, int userId) {
        Intent intent = new Intent(context, ItemActivity.class);
        intent.putExtra(USER_ID_KEY, userId);
        return intent;
    }
}