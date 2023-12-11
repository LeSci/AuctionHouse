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

import com.sscire.auctionhouse.db.AppDAO;
import com.sscire.auctionhouse.db.AppDatabase;

public class SignUpActivity extends AppCompatActivity {
    private EditText mUsernameField2;
    private EditText mPasswordField2;

    private Button mButtonCreateAccount;

    private AppDAO mAppDAO;

    private String mUsername;
    private String mPassword;

    private User mUser;
    //sls
    private Button mButtonReturnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        wireupDisplay();

        getDatabase();

    }
    private void wireupDisplay() {
        mUsernameField2 = findViewById(R.id.editTextLoginUserName2);
        mPasswordField2 = findViewById(R.id.editTextLoginPassword2);

        mButtonReturnLogin = findViewById(R.id.buttonReturnLogin);
        mButtonCreateAccount = findViewById(R.id.buttonCreateAccount);
        mButtonCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getValuesFromDisplay();
                if(checkForUserInDatabase()){
                    Toast.makeText(SignUpActivity.this, "User already exists", Toast.LENGTH_SHORT).show();
                } else{
                    User newUser = new User(mUsername, mPassword, false);
                    mAppDAO.insert(newUser);
                    Toast.makeText(SignUpActivity.this,
                            "Account: " + mUsername + " created. Please return to Login page."
                            , Toast.LENGTH_SHORT).show();
                    mUsernameField2.getText().toString();
                    mPasswordField2.setText("");
                };
            }
        }); // close mButtonSignUp2.setOnClickListener

        mButtonReturnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = LoginActivity.intentFactory(getApplicationContext());
                startActivity(intent);
            }
        });


    } // close wireupDisplay

    private void getValuesFromDisplay(){
        mUsername = mUsernameField2.getText().toString();
        mPassword = mPasswordField2.getText().toString();
    }

    private boolean checkForUserInDatabase(){
        mUser = mAppDAO.getUserByUsername(mUsername);
        if(mUser == null || mPassword == null){
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

    public static Intent intentFactory(Context context){
        Intent intent = new Intent(context, SignUpActivity.class);
        return intent;
    }
}