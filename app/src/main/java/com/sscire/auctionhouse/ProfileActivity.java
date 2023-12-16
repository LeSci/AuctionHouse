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
import android.widget.EditText;
import android.widget.Toast;

import com.sscire.auctionhouse.db.AppDAO;
import com.sscire.auctionhouse.db.AppDatabase;

import java.util.List;

public class ProfileActivity extends AppCompatActivity {
    private static final String USER_ID_KEY = "com.sscire.auctionhouse.userIdKey";
    private static final String PREFENCES_KEY = "com.sscire.auctionhouse.PREFENCES_KEY";

    private EditText mUsernameField;
    private EditText mPasswordField;

    private EditText mUserIdField;

    private int mUserId = -1;
    private AppDAO mAppDAO;
    private SharedPreferences mPreferences = null;

    private Button mButtonHome;

    private Button mButtonUpdate;

    private User mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mUserId = getIntent().getIntExtra(USER_ID_KEY, -1);

        getDatabase();
        mUser = mAppDAO.getUserByUserId(mUserId);
        wireupDisplay();

    }

    private void getDatabase(){
        mAppDAO = Room.databaseBuilder(this, AppDatabase.class, AppDatabase.DB_NAME)
                .allowMainThreadQueries()
                .build()
                .getAppDAO();
    }

    private void wireupDisplay() {
        mButtonHome = findViewById(R.id.buttonHome3);
        mButtonUpdate = findViewById(R.id.buttonUpdateAccount);
        mUserIdField = findViewById(R.id.editTextUserId);
        mUsernameField = findViewById(R.id.editTextLoginUserName3);
        mPasswordField = findViewById(R.id.editTextLoginPassword3);

        mUserIdField.setText(Integer.toString(mUser.getUserId()));
        mUsernameField.setText(mUser.getUserName());
        mPasswordField .setText(mUser.getPassword());

        mButtonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int UserID = mUser.getUserId();
                if(UserID == 1 || UserID == 2){
                    Toast.makeText(ProfileActivity.this, "Unable to update default accounts."
                            , Toast.LENGTH_SHORT).show();
                } else {
                    String UpdatedUserName = mUsernameField.getText().toString();
                    String UpdatedPassword = mPasswordField.getText().toString();
                    if(UpdatedUserName.length() <= 5 || UpdatedPassword.length() <= 5){
                        Toast.makeText(ProfileActivity.this, "Username/Password minimum 5 characters."
                                , Toast.LENGTH_SHORT).show();
                        mUsernameField.setText(mUser.getUserName());
                        mPasswordField.setText(mUser.getPassword());
                    } else {
                        User testUser = mAppDAO.getUserByUsername(UpdatedUserName);
                        if (testUser != null) {
                            Toast.makeText(ProfileActivity.this, "Username taken."
                                    , Toast.LENGTH_SHORT).show();
                            mUsernameField.setText(mUser.getUserName());
                        } else {
                            mUser.setUserName(UpdatedUserName);
                            mUser.setPassword(UpdatedPassword);
                            mAppDAO.update(mUser);
                            Toast.makeText(ProfileActivity.this, "Account Updated"
                                    , Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
        mButtonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = MainActivity.intentFactory(getApplicationContext(), mUserId);
                startActivity(intent);
            }
        });
    } // close wireupDisplay

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
        Intent intent = new Intent(context, ProfileActivity.class);
        intent.putExtra(USER_ID_KEY, userId);
        return intent;
    }
}