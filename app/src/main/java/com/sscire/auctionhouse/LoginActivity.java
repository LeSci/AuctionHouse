package com.sscire.auctionhouse;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.sscire.auctionhouse.R;
import com.sscire.auctionhouse.db.AppDatabase;
import com.sscire.auctionhouse.db.AppDAO;

public class LoginActivity extends AppCompatActivity {
    private EditText mUsernameField;
    private EditText mPasswordField;

    private Button mButtonLogin;

    private AppDAO mAppDAO;

    private String mUsername;
    private String mPassword;

    private User mUser;

    //sls
    private Button mButtonSignUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        wireupDisplay();
        getDatabase();
    }

    private void wireupDisplay(){
        mUsernameField = findViewById(R.id.editTextLoginUserName);
        //mUsernameField.setShowSoftInputOnFocus(false);
        mPasswordField = findViewById(R.id.editTextLoginPassword);
        //mPasswordField.setShowSoftInputOnFocus(false);

        mButtonLogin = findViewById(R.id.buttonLogin);

        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getValuesFromDisplay();
                if(checkForUserInDatabase()){
                    if(!validatePassword()){
                        Toast.makeText(LoginActivity.this, "Invalid password", Toast.LENGTH_SHORT).show();
                    }else{
                        Intent intent = MainActivity.intentFactory(getApplicationContext(),mUser.getUserId());
                        startActivity(intent);
                    }
                };

            }
        });
        // sls - need to add update user button to give user admin rights isAdmin=true
        mButtonSignUp = findViewById(R.id.buttonSignUp);
        mButtonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = SignUpActivity.intentFactory(getApplicationContext());
                startActivity(intent);
            }
        });

    } // close wireupDisplay

    private boolean validatePassword(){
        return mUser.getPassword().equals(mPassword);
    }

    private void getValuesFromDisplay(){
        mUsername = mUsernameField.getText().toString();
        mPassword = mPasswordField.getText().toString();
    }

    private boolean checkForUserInDatabase(){
        mUser = mAppDAO.getUserByUsername(mUsername);
        if(mUser == null || mPassword == null){
            Toast.makeText(this, "no user " + mUsername + " found", Toast.LENGTH_SHORT).show();
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

    public static Intent intentFactory(Context context){
        Intent intent = new Intent(context, LoginActivity.class);
        return intent;
    }

}