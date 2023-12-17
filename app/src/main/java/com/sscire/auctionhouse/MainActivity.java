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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.sscire.auctionhouse.db.AppDAO;
import com.sscire.auctionhouse.db.AppDatabase;

//import com.sscire.auctionhouse.R.id;

import java.util.List;

/**
 * @author Shannon Scire
 * @since 2023.12.16
 * @version 1.0.07
 * @description An Android application that simulates an Auction House. Users can add/remove items
 * from their inventory and buy/sell items on a virtual auction house.
 * There is also a few utility features and administrator functionality
 * Project 2
 * CST338_FA23
 * Dr. Clinkenbeard.
 */

public class MainActivity extends AppCompatActivity {
    private static final String USER_ID_KEY = "com.sscire.auctionhouse.userIdKey";
    private static final String PREFENCES_KEY = "com.sscire.auctionhouse.PREFENCES_KEY";
    private AppDAO mAppDAO;
    private int mUserId = -1;

    private SharedPreferences mPreferences = null;
    private User mUser;

    private Button mAdminButton;    // sls

    private Button mItemButton;

    private Button mAuctionButton;

   // private MenuItem mSubItem2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getDatabase();
        checkForUser();
        loginUser(mUserId);

        mAdminButton = findViewById(R.id.mainAdminButton);  // sls
        //mSubItem2 = findViewById(R.id.subitem2);    // sls
        mItemButton = findViewById(R.id.mainItemButton);
        mAuctionButton = findViewById(R.id.mainAuctionButton);

        // check for Admin access - sls
        if(mUserId != -1 && mUser.getIsAdmin()) {
            mAdminButton.setVisibility(View.VISIBLE);
           // mSubItem2.setVisible(true);
        } else {
            mAdminButton.setVisibility(View.INVISIBLE);
           // mSubItem2.setVisible(false);
        }

        mItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ItemActivity.intentFactory(getApplicationContext(),mUser.getUserId());
                startActivity(intent);
            }
        });

        mAuctionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = AuctionActivity.intentFactory(getApplicationContext(),mUser.getUserId());
                startActivity(intent);
            }
        });


        mAdminButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = AdminActivity.intentFactory(getApplicationContext(),mUser.getUserId());
                startActivity(intent);
            }
        });

    }

    private void debug() {
    }

    private void loginUser(int userId) {
        mUser = mAppDAO.getUserByUserId(userId);
        addUserToPreference(userId);
        invalidateOptionsMenu();
    }


    private void addUserToPreference(int userId) {
        if (mPreferences == null) {
            getPrefs();
        }
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putInt(USER_ID_KEY, userId);
        //Thanks Yasha
        editor.apply();
    }

    private void getDatabase() {
        mAppDAO = Room.databaseBuilder(this, AppDatabase.class, AppDatabase.DB_NAME)
                .allowMainThreadQueries()
                .build()
                .getAppDAO();
    }

    private void getPrefs() {
        mPreferences = this.getSharedPreferences(PREFENCES_KEY, Context.MODE_PRIVATE);
    }

    // logout user
    private void checkForUser() {
        // do we have a user in the intent?
        mUserId = getIntent().getIntExtra(USER_ID_KEY, -1);
        //do we have a user in the preferences?
        if (mUserId != -1) {
            return;
        }
        if (mPreferences == null) {
            getPrefs();
        }
        mUserId = mPreferences.getInt(USER_ID_KEY, -1);
        if (mUserId != -1) {
            return;
        }
        //do we have any users at all?
        List<User> users = mAppDAO.getAllUsers();
        if (users.size() <= 0) {
            User defaultUser = new User("testuser1", "testuser1", false);
            User altUser = new User("admin2", "admin2", true);
            mAppDAO.insert(defaultUser,altUser);
        }
        Intent intent = LoginActivity.intentFactory(this);
        startActivity(intent);
    }

    // logout user
    private void logoutUser() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setMessage(R.string.logout);
        alertBuilder.setPositiveButton(getString(R.string.yes),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        clearUserFromIntent();
                        clearUserFromPref();
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

    private void clearUserFromPref() {
        addUserToPreference(-1);
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

    public static Intent intentFactory(Context context, int userId) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(USER_ID_KEY, userId);

        return intent;
    }


}