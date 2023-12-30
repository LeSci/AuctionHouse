package com.sscire.auctionhouse.view2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.sscire.auctionhouse.R;
import com.sscire.auctionhouse.User;
import com.sscire.auctionhouse.db.AppDAO;
import com.sscire.auctionhouse.db.AppDatabase;

// https://www.youtube.com/watch?v=RhGMd8SsA14&list=PLrnPJCHvNZuAPyh6nRXsvf5hF48SJWdJb&index=8
// Room + ViewModel + LiveData + RecyclerView (MVVM) Part 7 - ADD NOTE ACTIVITY - Android

public class UserEditAddActivity extends AppCompatActivity {

    private User mUser;
    private int mUserId = -1;
    private AppDAO mAppDAO;
    public static final String EXTRA_USERID= "com.sscire.auctionhouse.userIdKey"; // part 9
    public static final String EXTRA_USERNAME = "com.sscire.auctionhouse.usernameKey";
    public static final String EXTRA_PASSWORD = "com.sscire.auctionhouse.passwordKey";
    public static final String EXTRA_ISADMIN = "com.sscire.auctionhouse.isadminKey";
    public static final String EXTRA_CURRENCY = "com.sscire.auctionhouse.currencyKey";
    private EditText mEditTextUsername;
    private EditText mEditTextPassword;
    private NumberPicker mNumberPickerIsAdmin;
    private NumberPicker mNumberPickerCurrency;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_add);

        getDatabase();

        mEditTextUsername = findViewById(R.id.editTextLoginUserName);
        mEditTextPassword = findViewById(R.id.editTextLoginPassword);
        mNumberPickerIsAdmin = findViewById(R.id.numberPickerIsAdmin);
        mNumberPickerCurrency = findViewById(R.id.numberPickerCurrency);

        mNumberPickerIsAdmin.setMinValue(0);
        mNumberPickerIsAdmin.setMaxValue(1);
        mNumberPickerCurrency.setMinValue(1);
        mNumberPickerCurrency.setMaxValue(10);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        // part 9
        Intent intent = getIntent();
        if(intent.hasExtra(EXTRA_USERID)){
            setTitle("Edit Item");
            mEditTextUsername.setText(intent.getStringExtra(EXTRA_USERNAME));
            mEditTextPassword.setText(intent.getStringExtra(EXTRA_PASSWORD));
            mNumberPickerIsAdmin.setValue(Integer.valueOf(intent.getIntExtra(EXTRA_ISADMIN, 0)));
            mNumberPickerCurrency.setValue(Integer.valueOf(intent.getIntExtra(EXTRA_CURRENCY, 1)));
        } else {
            setTitle("Add Item");
        }
    }

    private void saveUser(){
        String username = mEditTextUsername.getText().toString();
        String password = mEditTextPassword.getText().toString();
        int isadmin = mNumberPickerIsAdmin.getValue();
        int currency = mNumberPickerCurrency.getValue();

        Intent data = new Intent();
        data.putExtra(EXTRA_USERNAME, username);
        data.putExtra(EXTRA_PASSWORD, password);
        data.putExtra(EXTRA_ISADMIN, isadmin);
        data.putExtra(EXTRA_CURRENCY, currency);

        // part 9
        int userId = getIntent().getIntExtra(EXTRA_USERID, -1);

        if(userId != -1){
            data.putExtra(EXTRA_USERID, userId);
        }

        if(username.trim().length()<5 || password.trim().length() < 5){
            Toast.makeText(this,
                    "Username/Password must be greater than 5 characters",
                    Toast.LENGTH_SHORT).show();
            return;
        } else if(checkForUserInDatabase(username) && userId == -1){
            Toast.makeText(this,
                    "Username already exists",
                    Toast.LENGTH_SHORT).show();
            return;
        }

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
            saveUser();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // verify username
    private boolean checkForUserInDatabase(String username){
        mUser = mAppDAO.getUserByUsername(username);
        if(mUser == null){
            //Toast.makeText(this, "no user " + mUsername + " found", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void getDatabase(){
        mAppDAO = Room.databaseBuilder(this, AppDatabase.class, AppDatabase.DB_NAME)
                .allowMainThreadQueries()
                .build()
                .getAppDAO();
    }
}