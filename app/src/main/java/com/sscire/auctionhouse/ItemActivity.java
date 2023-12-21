package com.sscire.auctionhouse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
        mUser = mAppDAO.getUserByUserId(mUserId);
        wireupDisplay();
        refreshDisplay();



    }

    private void wireupDisplay() {
        mItemDisplay = findViewById(R.id.itemDisplay);
        mItemDisplay.setMovementMethod(new ScrollingMovementMethod());

        mItemId = findViewById(R.id.itemIDEditText);
        //mItemId.setShowSoftInputOnFocus(false);
        mItemName = findViewById(R.id.itemNameEditText);
        //mItemName.setShowSoftInputOnFocus(false);
        mItemPrice = findViewById(R.id.itemPriceEditText);
        //mItemPrice.setShowSoftInputOnFocus(false);

        mButtonHome = findViewById(R.id.buttonHome);
        mItemSubmitButton = findViewById(R.id.itemSubmitButton);
        mItemDeleteButton = findViewById(R.id.itemDeleteButton);

        mItemSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int userId = mUserId;

                try{
                    String itemName =  mItemName.getText().toString();
                    int itemPrice = Integer.parseInt(mItemPrice.getText().toString());
                    if(itemName.length() == 0) {
                        throw new NumberFormatException();
                    }
                    Item newItem = new Item(userId, itemName, itemPrice);
                    mAppDAO.insert(newItem);
                    Toast.makeText(ItemActivity.this,
                            "Item: " + itemName + " created."
                            , Toast.LENGTH_SHORT).show();
                    refreshDisplay();
                    mItemName.setText("");
                    mItemPrice.setText("");
                } catch (NumberFormatException e){
                    Toast.makeText(ItemActivity.this,
                            "Enter valid item name and item price."
                            , Toast.LENGTH_SHORT).show();
                }
            }
        });

        mItemDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int itemId = Integer.parseInt(mItemId.getText().toString());
                    mItem = mAppDAO.getItemByItemId(itemId);
                    if (mItem == null){
                        throw new NumberFormatException ();
                    }
                    String itemName = mItem.getItemName();
                    int itemUserId = mItem.getUserId();
                    if(itemUserId != mUserId){
                        throw new NumberFormatException ();
                    }
                    Auction auction = mAppDAO.getAuctionByItemId(itemId);
                    if(auction != null){
                        Toast.makeText(ItemActivity.this,
                                "Item: " + itemId + " " + itemName + " is assigned to "
                                + "Auction: " + auction.getAuctionId() + ". Delete auction first."
                                , Toast.LENGTH_SHORT).show();
                                mItemId.setText("");
                    } else {
                        mAppDAO.delete(mItem);
                        Toast.makeText(ItemActivity.this,
                                "Item: " + itemId + " " + itemName + " deleted."
                                , Toast.LENGTH_SHORT).show();
                        refreshDisplay();
                        mItemId.setText("");
                    }
                } catch (NumberFormatException e){
                    Toast.makeText(ItemActivity.this,
                            "Enter a valid itemId owned by user"
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
        String pad = "           ";
        for (Item item : mItemList) {
            String itemName = (item.getItemName() + pad).substring(0, 11);
            int itemPrice = item.getItemPrice();
            int userid = item.getUserId();
            User user = mAppDAO.getUserByUserId(userid);
            String username = (user.getUserName() + pad).substring(0, 11);
            String itemId = (item.getItemId() + pad).substring(0,7);
            sb.append("     " + itemId + "     ");
            //sb.append(pad + item.getUserId() + pad);
            sb.append(username + "  ");
            sb.append(itemName + "       ");
            sb.append(itemPrice);
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

    // Options Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.user_menu, menu);
        return true;
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // set menu item to currently logged-in username
        if (mUser != null) {
            MenuItem item = menu.findItem(R.id.userMenuLogout);
            item.setTitle(mUser.getUserName());
        }
        // sls - set subitem2 visible us currently logged-in user is admin
        // https://stackoverflow.com/questions/9030268/set-visibility-in-menu-programmatically-android
        MenuItem register = menu.findItem(R.id.subitem4);
        if(mUserId != -1 && mUser.getIsAdmin())
        {
            register.setVisible(true);
        }
        else
        {
            register.setVisible(false);
        }
        // sls
        invalidateOptionsMenu();
        return super.onPrepareOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.userMenuLogout) {
            Toast.makeText(this, "Go to User Profile", Toast.LENGTH_SHORT).show();
            Intent intent = ProfileActivity.intentFactory(getApplicationContext(),mUser.getUserId());
            startActivity(intent);
            return true;
        } else if (itemId == R.id.logout) {
            logoutUser();
            return true;
        } else if (itemId == R.id.item2) {
            Toast.makeText(this, "Menu", Toast.LENGTH_SHORT).show();
            return true;
        } else if (itemId == R.id.subitem1) {
            Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show();
            Intent intent = MainActivity.intentFactory(getApplicationContext(),mUser.getUserId());
            startActivity(intent);
            return true;
        } else if (itemId == R.id.subitem2) {
            Toast.makeText(this, "Items", Toast.LENGTH_SHORT).show();
            Intent intent = ItemActivity.intentFactory(getApplicationContext(),mUser.getUserId());
            startActivity(intent);
            return true;
        } else if (itemId == R.id.subitem3) {
            Toast.makeText(this, "Auction", Toast.LENGTH_SHORT).show();
            Intent intent = AuctionActivity.intentFactory(getApplicationContext(),mUser.getUserId());
            startActivity(intent);
            return true;
        } else if (itemId == R.id.subitem4) {
            if (mUser.getIsAdmin()) {
                Toast.makeText(this, "Admin", Toast.LENGTH_SHORT).show();
                Intent intent = AdminActivity.intentFactory(getApplicationContext(),mUser.getUserId());
                startActivity(intent);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void logoutUser() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setMessage(R.string.logout);
        alertBuilder.setPositiveButton(getString(R.string.yes),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        clearUserFromIntent();
                        //clearUserFromPref();
                        mUserId = -1;
                        checkForUser();
                    }
                });
        alertBuilder.setNegativeButton(getString(R.string.no),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //We don't really need to do anything here.

                    }
                });

        alertBuilder.create().show();

    }
    private void clearUserFromIntent() {
        getIntent().putExtra(USER_ID_KEY, -1);
    }

    private void checkForUser() {
        Intent intent = LoginActivity.intentFactory(this);
        startActivity(intent);
    }
    public static Intent intentFactory(Context context, int userId) {
        Intent intent = new Intent(context, ItemActivity.class);
        intent.putExtra(USER_ID_KEY, userId);
        return intent;
    }
}