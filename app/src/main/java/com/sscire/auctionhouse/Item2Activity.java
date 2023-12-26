package com.sscire.auctionhouse;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sscire.auctionhouse.db.AppDAO;
import com.sscire.auctionhouse.viewmodel.ItemViewModel;

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
    public static final int ADD_ITEM_REQUEST = 1;
    private ItemViewModel mItemViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item2);

        // sls - return home
        mUserId = getIntent().getIntExtra(USER_ID_KEY, -1);
        wireupDisplay();

        // MVVM
        FloatingActionButton buttonAddUser = findViewById(R.id.buttonAddItem);
        buttonAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Item2Activity.this, ItemAddActivity.class);
                startActivityForResult(intent, ADD_ITEM_REQUEST);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final ItemAdapter adapter = new ItemAdapter();
        recyclerView.setAdapter(adapter);

        //mUserViewModel = ViewModelProvider.of(this).get(UserViewModel.class);
        mItemViewModel = new ViewModelProvider(this,
                new ViewModelProvider.AndroidViewModelFactory(getApplication()))
                .get(ItemViewModel.class);

        //  mItemViewModel.getAllItems().observe(this, new Observer<List<Item>>() {

        mItemViewModel.getUserItems().observe(this, new Observer<List<Item>>() {
            @Override
            public void onChanged(@Nullable List<Item> items) {
                //mUserId = getIntent().getIntExtra(USER_ID_KEY, -1);
                //update RecyclerView
                adapter.setItems(items);
                //adapter.submitList(notes);
                String userid = String.valueOf(mUserId);
                Toast.makeText(Item2Activity.this, "onChanged", Toast.LENGTH_SHORT).show();
                Toast.makeText(Item2Activity.this, userid, Toast.LENGTH_SHORT).show();
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

    // add Item via Floating Action Button
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == ADD_ITEM_REQUEST && resultCode == RESULT_OK){
            String itemName = data.getStringExtra(ItemAddActivity.EXTRA_ITEMNAME);
            String itemDescription = data.getStringExtra(ItemAddActivity.EXTRA_DESCRIPTION);
            int itemPrice = data.getIntExtra(ItemAddActivity.EXTRA_PRICE, 1);

            Item item = new Item(mUserId,itemName, itemPrice);
            mItemViewModel.insert(item);
            Toast.makeText(this, "Item saved", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Item not saved", Toast.LENGTH_SHORT).show();
        }
    }

    public static Intent intentFactory(Context context, int userId) {
        Intent intent = new Intent(context, Item2Activity.class);
        intent.putExtra(USER_ID_KEY, userId);
        return intent;
    }
}