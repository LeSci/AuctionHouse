package com.sscire.auctionhouse;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sscire.auctionhouse.db.AppDAO;
import com.sscire.auctionhouse.db.AppDatabase;

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
                String UpdatedUserName = mUsernameField.getText().toString();
                String UpdatedPassword = mPasswordField.getText().toString();
                mUser.setUserName(UpdatedUserName);
                mUser.setPassword(UpdatedPassword);
                mAppDAO.update(mUser);
                Toast.makeText(ProfileActivity.this, "Account Updated"
                        , Toast.LENGTH_SHORT).show();
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

    public static Intent intentFactory(Context context, int userId) {
        Intent intent = new Intent(context, ProfileActivity.class);
        intent.putExtra(USER_ID_KEY, userId);
        return intent;
    }
}