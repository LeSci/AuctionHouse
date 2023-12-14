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

public class AdminActivity extends AppCompatActivity {
    private static final String USER_ID_KEY = "com.sscire.auctionhouse.userIdKey";
    private static final String PREFENCES_KEY = "com.sscire.auctionhouse.PREFENCES_KEY";
    private User mUser;
    private int mUserId = -1;
    private AppDAO mAppDAO;
    private SharedPreferences mPreferences = null;

    private Button mButtonHome;
    private Button mButtonUpdateIsAdmin;

    private TextView mAdminUserDisplay;

    private List<User> mUserList;

    private EditText mUserIdField;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        mUserId = getIntent().getIntExtra(USER_ID_KEY, -1);
        getDatabase();
        wireupDisplay();

    }

    private void getDatabase(){
        mAppDAO = Room.databaseBuilder(this, AppDatabase.class, AppDatabase.DB_NAME)
                .allowMainThreadQueries()
                .build()
                .getAppDAO();
    }

    private void wireupDisplay() {
        mButtonHome = findViewById(R.id.buttonHome);
        mButtonUpdateIsAdmin = findViewById(R.id.adminSubmitButton);
        mAdminUserDisplay = findViewById(R.id.adminUserDisplay);
        mAdminUserDisplay.setMovementMethod(new ScrollingMovementMethod());
        mUserIdField = findViewById(R.id.editTextUserId);

        refreshDisplay();

        mButtonUpdateIsAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    int userID = Integer.parseInt(mUserIdField.getText().toString());
                    User user = mAppDAO.getUserByUserId(userID);
                    if(user == null){
                        throw new NumberFormatException();
                    }
                    user.setIsAdmin(!user.getIsAdmin());
                    mAppDAO.update(user);
                    Toast.makeText(AdminActivity.this,
                            user.getUserName() + " updated", Toast.LENGTH_SHORT).show();
                    mUserIdField.setText("");
                    refreshDisplay();
                } catch (NumberFormatException e) {
                    Toast.makeText(AdminActivity.this,
                            "Enter valid UserId", Toast.LENGTH_SHORT).show();
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


    } // close wireupDisplay

    private void refreshDisplay() {
        mUserList = mAppDAO.getAllUsers();

        if (mUserList.size() <= 0) {
            mAdminUserDisplay.setText(R.string.noLogsMessage);
            return;
        }

        StringBuilder sb = new StringBuilder();
        String pad = "         ";
        for (User user : mUserList) {
            String username = user.getUserName();
            String password = user.getPassword();
            sb.append(user.getUserId() + pad);
            sb.append(username + pad);
            sb.append(password + pad);
            sb.append(user.getIsAdmin());
            sb.append("\n");
        }
        mAdminUserDisplay.setText(sb.toString());
    }
    public static Intent intentFactory(Context context, int userId) {
        Intent intent = new Intent(context, AdminActivity.class);
        intent.putExtra(USER_ID_KEY, userId);
        return intent;
    }
}