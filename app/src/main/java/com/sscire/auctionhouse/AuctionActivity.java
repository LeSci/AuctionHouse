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

import com.sscire.auctionhouse.db.AppDAO;
import com.sscire.auctionhouse.db.AppDatabase;

import java.util.List;

public class AuctionActivity extends AppCompatActivity {
    private static final String USER_ID_KEY = "com.sscire.auctionhouse.userIdKey";
    private static final String PREFENCES_KEY = "com.sscire.auctionhouse.PREFENCES_KEY";

    private User mUser;
    private int mUserId = -1;
    private AppDAO mAppDAO;

    // item
    private Item mItem;
    private TextView mItemDisplay;

    private EditText mItemId;
    private EditText mItemName;
    private EditText mItemPrice;

    private List<Item> mItemList;

    // Auction

    private TextView mAuctionDisplay;
    private Auction mAuction;

    private EditText mAuctionId;

    private EditText mUserId2;

    private List<Auction> mAuctionList;

    // Buttons

    private Button mAuctionSubmitButton;

    private Button mAuctionDeleteButton;

    private Button mButtonHome;

    private SharedPreferences mPreferences = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auction);

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

        mAuctionSubmitButton = findViewById(R.id.auctionSubmitButton);
        mAuctionDeleteButton = findViewById(R.id.auctionDeleteButton);
        mButtonHome = findViewById(R.id.buttonHome);



        mButtonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = MainActivity.intentFactory(getApplicationContext(),mUserId);
                startActivity(intent);
            }
        });
    }

    private void refreshDisplay() {
        refreshItemDisplay();
        mAuctionList = mAppDAO.getAllAuctions();

        if (mAuctionList.size() <= 0) {
            mAuctionDisplay.setText(R.string.no_items_in_database);
            return;
        }

        StringBuilder sb = new StringBuilder();
        String pad = "       ";
        for (Auction auction : mAuctionList) {
            int auctionId = auction.getAuctionId();
            int auctionPrice = auction.getPrice();
            sb.append(auctionId + pad);
            sb.append(auction.getUserId() + pad);
            sb.append(auction.getItemId() + pad);
            sb.append(mItem.getItemName() + pad);
            sb.append(auctionPrice + pad);
            sb.append("\n");
        }
        mAuctionDisplay.setText(sb.toString());

    }
    private void refreshItemDisplay() {
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
        Intent intent = new Intent(context, AuctionActivity.class);
        intent.putExtra(USER_ID_KEY, userId);
        return intent;
    }
}