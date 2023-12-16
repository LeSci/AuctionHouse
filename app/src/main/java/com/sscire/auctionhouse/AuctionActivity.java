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

    private Button mAuctionBuyButton;

    private Button mButtonHome;

    private SharedPreferences mPreferences = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auction);

        mUserId = getIntent().getIntExtra(USER_ID_KEY, -1);

        getDatabase();
        wireupDisplay();
        refreshItemDisplay();
        refreshDisplay();

    }

    private void wireupDisplay() {
        mItemDisplay = findViewById(R.id.auctionItemDisplay);
        mItemDisplay.setMovementMethod(new ScrollingMovementMethod());
        mItemId = findViewById(R.id.itemIDEditText);
        mItemId.setShowSoftInputOnFocus(false);
//        mItemName = findViewById(R.id.itemNameEditText);
//        mItemPrice = findViewById(R.id.itemPriceEditText);

        mAuctionDisplay = findViewById(R.id.auctionDisplay);
        mAuctionDisplay.setMovementMethod(new ScrollingMovementMethod());

        mAuctionId = findViewById(R.id.auctionIDEditText);
        mAuctionId.setShowSoftInputOnFocus(false);
        mAuctionSubmitButton = findViewById(R.id.auctionSubmitButton);
        mAuctionBuyButton = findViewById(R.id.auctionBuyButton);
        mAuctionDeleteButton = findViewById(R.id.auctionDeleteButton);
        mButtonHome = findViewById(R.id.buttonHome);


        mAuctionSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    int itemId = Integer.parseInt(mItemId.getText().toString());
                    mItem = mAppDAO.getItemByItemId(itemId);
                    if (mItem == null || mItem.getUserId() != mUserId){
                        throw new NumberFormatException ();
                    }
                    String itemName = mItem.getItemName();
                    mAuction = mAppDAO.getAuctionByItemId(itemId);
                    if(mAuction != null){
                        Toast.makeText(AuctionActivity.this,
                                "Auction: " + itemName + " already exists!"
                                , Toast.LENGTH_SHORT).show();
                    } else {
                        int itemPrice = mItem.getItemPrice();
                        int itemUserId = mItem.getUserId();
                        if (itemUserId != mUserId) {
                            throw new NumberFormatException();
                        }
                        Auction newAuction = new Auction(itemUserId, itemId, itemPrice);
                        mAppDAO.insert(newAuction);
                        Toast.makeText(AuctionActivity.this,
                                "Auction: " + itemName + " created."
                                , Toast.LENGTH_SHORT).show();
                        refreshDisplay();
                        mItemId.setText("");
                    }
                } catch (NumberFormatException e){
                    Toast.makeText(AuctionActivity.this,
                            "Enter a valid itemId owned by user"
                            , Toast.LENGTH_SHORT).show();
                    mItemId.setText("");
                }
            }
        });

        mAuctionDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    int auctionId = Integer.parseInt(mAuctionId.getText().toString());
                    mAuction = mAppDAO.getAuctionByAuctionId(auctionId);
                    if(mAuction == null || mAuction.getUserId() != mUserId){
                        throw new NumberFormatException();
                    }
                    int itemId = mAuction.getItemId();
                    mItem = mAppDAO.getItemByItemId(itemId);
                    String itemName = mItem.getItemName();
                    mAppDAO.delete(mAuction);
                    Toast.makeText(AuctionActivity.this,
                            "Auction: " + auctionId + " " + itemName + " deleted."
                            , Toast.LENGTH_SHORT).show();
                    refreshDisplay();
                    mAuctionId.setText("");
                } catch (NumberFormatException e){
                    Toast.makeText(AuctionActivity.this,
                            "Enter a valid auctionId created by user"
                            , Toast.LENGTH_SHORT).show();
                    mAuctionId.setText("");
                }
            }
        });

        mAuctionBuyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    int auctionId = Integer.parseInt(mAuctionId.getText().toString());
                    mAuction = mAppDAO.getAuctionByAuctionId(auctionId);
                    if(mAuction == null || mAuction.getUserId() == mUserId){
                        throw new NumberFormatException();
                    }
                    int itemId = mAuction.getItemId();
                    mItem = mAppDAO.getItemByItemId(itemId);
                    String itemName = mItem.getItemName();
                    mItem.setUserId(mUserId);
                    mAppDAO.update(mItem);
                    mAppDAO.delete(mAuction);
                    Toast.makeText(AuctionActivity.this,
                            "Auction: " + auctionId + " " + itemName + " purchased."
                            , Toast.LENGTH_SHORT).show();
                    refreshDisplay();
                    refreshItemDisplay();
                    mAuctionId.setText("");
                } catch (NumberFormatException e){
                    Toast.makeText(AuctionActivity.this,
                            "Enter a valid auctionId not created by user"
                            , Toast.LENGTH_SHORT).show();
                    mAuctionId.setText("");
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
    }

    private void refreshDisplay() {
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
            int itemId = auction.getItemId();
            Item item = mAppDAO.getItemByItemId(itemId);
            sb.append(auctionId + pad);
            sb.append(auction.getUserId() + pad);
            sb.append(auction.getItemId() + pad);
            sb.append(item.getItemName() + pad);
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