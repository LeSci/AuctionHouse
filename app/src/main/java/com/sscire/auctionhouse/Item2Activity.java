package com.sscire.auctionhouse;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.sscire.auctionhouse.db.AppDAO;
import com.sscire.auctionhouse.viewmodel.UserViewModel;

import java.util.List;

public class Item2Activity extends AppCompatActivity {
    // sls - return home
    private static final String USER_ID_KEY = "com.sscire.auctionhouse.userIdKey";
    private static final String PREFENCES_KEY = "com.sscire.auctionhouse.PREFENCES_KEY";
    private Button mButtonHome;

    private User mUser;
    private int mUserId = -1;
    private AppDAO mAppDAO;

    // MVVM
    private UserViewModel mUserViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item2);

        // sls - return home
        mUserId = getIntent().getIntExtra(USER_ID_KEY, -1);
        wireupDisplay();

        //mUserViewModel = ViewModelProvider.of(this).get(UserViewModel.class);
        mUserViewModel = new ViewModelProvider(this,
                new ViewModelProvider.AndroidViewModelFactory(getApplication()))
                .get(UserViewModel.class);
        mUserViewModel.getAllUsers().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(@Nullable List<User> notes) {
                //update RecyclerView
                //adapter.setNotes(notes);
                //adapter.submitList(notes);
                Toast.makeText(Item2Activity.this, "onChanged", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void wireupDisplay() {
        mButtonHome = findViewById(R.id.buttonHome);
        mButtonHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = MainActivity.intentFactory(getApplicationContext(),mUserId);
                startActivity(intent);
            }
        });
    }

    public static Intent intentFactory(Context context, int userId) {
        Intent intent = new Intent(context, Item2Activity.class);
        intent.putExtra(USER_ID_KEY, userId);
        return intent;
    }
}