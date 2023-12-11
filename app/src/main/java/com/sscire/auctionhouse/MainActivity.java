package com.sscire.auctionhouse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
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

//import com.sscire.auctionhouse.R.id;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String USER_ID_KEY = "com.sscire.auctionhouse.userIdKey";
    private static final String PREFENCES_KEY = "com.sscire.auctionhouse.PREFENCES_KEY";
    private TextView mMainDisplay;

    //TODO:DEBUG
    private TextView mDebug;

    private EditText mExercise;
    private EditText mWeight;
    private EditText mReps;

    private Button mSubmitButton;

    private AppDAO mJournalDAO;

    private List<Journal> mJournal;

    private int mUserId = -1;

    private SharedPreferences mPreferences = null;
    private User mUser;

    private Button mAdminButton;    // sls

   // private MenuItem mSubItem2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getDatabase();
        checkForUser();
        loginUser(mUserId);

        //        debug();

        mMainDisplay = findViewById(R.id.mainGymLogDisplay);
        mMainDisplay.setMovementMethod(new ScrollingMovementMethod());

        mExercise = findViewById(R.id.mainExerciseEditText);
        mWeight = findViewById(R.id.mainWeightEditText);
        mReps = findViewById(R.id.mainRepsEditText);

        mSubmitButton = findViewById(R.id.mainSubmitButton);

        mAdminButton = findViewById(R.id.mainAdminButton);  // sls
        //mSubItem2 = findViewById(R.id.subitem2);    // sls

        // check for Admin access - sls
        if(mUserId != -1 && mUser.getIsAdmin()) {
            mAdminButton.setVisibility(View.VISIBLE);
           // mSubItem2.setVisible(true);
        } else {
            mAdminButton.setVisibility(View.INVISIBLE);
           // mSubItem2.setVisible(false);
        }

        refreshDisplay();

        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Journal log = getValuesFromDisplay();

                // sls  
                //CST338_SP20 > Adding users to the GymLog > 1:17:00
                //log.setUserId(mUser.getUserId());

                mJournalDAO.insert(log);

                refreshDisplay();
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
        mDebug = findViewById(R.id.DEBUG);
        mDebug.setMovementMethod(new ScrollingMovementMethod());
        List<User> users = mJournalDAO.getAllUsers();

        StringBuilder sb = new StringBuilder();

        sb.append("All users:\n");

        for(User u : users){
            sb.append(u);
            sb.append("\n");
        }


        sb.append("all Logs\n");
        List<Journal> logs = mJournalDAO.getAllGymLogs();
        for(Journal log : logs){
            sb.append(log);
        }

        mDebug.setText(sb.toString());
    }

    private void loginUser(int userId) {
        mUser = mJournalDAO.getUserByUserId(userId);
        addUserToPreference(userId);
        invalidateOptionsMenu();
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
        MenuItem register = menu.findItem(R.id.subitem2);
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
        mJournalDAO = Room.databaseBuilder(this, AppDatabase.class, AppDatabase.DB_NAME)
                .allowMainThreadQueries()
                .build()
                .getAppDAO();
    }

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
        List<User> users = mJournalDAO.getAllUsers();
        if (users.size() <= 0) {
            User defaultUser = new User("testuser1", "testuser1", false);
            User altUser = new User("admin2", "admin2", true);
            mJournalDAO.insert(defaultUser,altUser);
        }
        Intent intent = LoginActivity.intentFactory(this);
        startActivity(intent);
    }

    private void getPrefs() {
        mPreferences = this.getSharedPreferences(PREFENCES_KEY, Context.MODE_PRIVATE);
    }

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

    private Journal getValuesFromDisplay() {
        String exercise = "No record found";
        double weight = 0.0;
        int reps = 0;

        exercise = mExercise.getText().toString();

        try {
            weight = Double.parseDouble(mWeight.getText().toString());
        } catch (NumberFormatException e) {
            Log.d("Journal", "Couldn't convert weight");
        }

        try {
            reps = Integer.parseInt(mReps.getText().toString());
        } catch (NumberFormatException e) {
            Log.d("Journal", "Couldn't convert reps");
        }

        Journal log = new Journal(exercise, reps, weight, mUserId);

        return log;

    }

    private void refreshDisplay() {
        mJournal = mJournalDAO.getGymLogsByUserId(mUserId);

        if (mJournal.size() <= 0) {
            mMainDisplay.setText(R.string.noLogsMessage);
            return;
        }

        StringBuilder sb = new StringBuilder();
        for (Journal log : mJournal) {
            sb.append(log);
            sb.append("\n");
            sb.append("=-=-=-=-=-=-=-=-");
            sb.append("\n");
        }
        mMainDisplay.setText(sb.toString());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.user_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.userMenuLogout) {
            Toast.makeText(this, "What you want?", Toast.LENGTH_SHORT).show();
            return true;
        } else if (itemId == R.id.logout) {
            logoutUser();
            return true;
        } else if (itemId == R.id.item2) {
            Toast.makeText(this, "Item 2 selected", Toast.LENGTH_SHORT).show();
            return true;
        } else if (itemId == R.id.subitem1) {
            Toast.makeText(this, "Sub Item 1 selected", Toast.LENGTH_SHORT).show();
            return true;
        } else if (itemId == R.id.subitem2) {
            if (mUser.getIsAdmin()) {
                Toast.makeText(this, "Admin: Sub Item 2 selected", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "User: Sub Item 2 selected", Toast.LENGTH_SHORT).show();
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