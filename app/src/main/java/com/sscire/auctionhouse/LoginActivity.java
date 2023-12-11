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
        mPasswordField = findViewById(R.id.editTextLoginPassword);

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
//                getValuesFromDisplay();
//                if(checkForUserInDatabase()){
//                    Toast.makeText(LoginActivity.this, "User already exists", Toast.LENGTH_SHORT).show();
//                } else{
//                    User newUser = new User(mUsername, mPassword, false);
//                    mAppDAO.insert(newUser);
//                    Toast.makeText(LoginActivity.this,
//                            "Account: " + mUsername + " created. Please Login."
//                            , Toast.LENGTH_SHORT).show();
//                    mUsernameField.getText().toString();
//                    mPasswordField.setText("");
//                };
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