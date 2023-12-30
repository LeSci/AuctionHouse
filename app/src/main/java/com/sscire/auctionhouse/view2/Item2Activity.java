package com.sscire.auctionhouse.view2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sscire.auctionhouse.Auction;
import com.sscire.auctionhouse.Item;
import com.sscire.auctionhouse.MainActivity;
import com.sscire.auctionhouse.R;
import com.sscire.auctionhouse.User;
import com.sscire.auctionhouse.adapter.ItemAdapter;
import com.sscire.auctionhouse.db.AppDAO;
import com.sscire.auctionhouse.db.AppDatabase;
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
    public static final int EDIT_ITEM_REQUEST = 2;  // part 9
    private ItemViewModel mItemViewModel;

    // setup reference to RecyclerView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item2);

        // sls - return home
        mUserId = getIntent().getIntExtra(USER_ID_KEY, -1);
        wireupDisplay();
        getDatabase();

        // MVVM
        FloatingActionButton buttonAddUser = findViewById(R.id.buttonAddItem);
        buttonAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Item2Activity.this, ItemEditAddActivity.class);
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
                // part 10
                //adapter.setItems(items);
                adapter.submitList(items);
//                String userid = String.valueOf(mUserId);
//                Toast.makeText(Item2Activity.this, "onChanged", Toast.LENGTH_SHORT).show();
//                Toast.makeText(Item2Activity.this, userid, Toast.LENGTH_SHORT).show();
            }
        });

        // Swipe functionality - Part 8
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            // onMove is for Drag-and-Drop, onSwiped is for swiping
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                //viewHolder.getAdapterPosition()
                Item item = adapter.getItemAt(viewHolder.getAdapterPosition());
                String itemName = item.getItemName();
                int itemId = item.getItemId();
                int itemUserId = item.getUserId();
                // add item to Auction
                if(direction == ItemTouchHelper.RIGHT) {
                    Auction auction;
                    auction = mAppDAO.getAuctionByItemId(itemId);
                    // check if item already exists in Auction
                    if(auction != null){
                        Toast.makeText(Item2Activity.this,
                                "Auction: " + itemName + " already exists!"
                                , Toast.LENGTH_SHORT).show();
                        // check that item belongs to current user
                    } else if (itemUserId == mUserId){
                        // if item doesn't exist in auction, insert item into auction table
                        int itemPrice = item.getItemPrice();
                        Auction newAuction = new Auction(itemUserId, itemId, itemPrice);
                        mAppDAO.insert(newAuction);
                        Toast.makeText(Item2Activity.this,
                                "Auction: " + itemName + " created."
                                , Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Item2Activity.this,
                                "Cannot create auction, " +
                                        itemName + " belongs to someone else."
                                , Toast.LENGTH_SHORT).show();
                    }
                    mItemViewModel.update(item); // added so list refreshes, broken after part10
                    // see
                    // https://developer.android.com/develop/ui/views/touch-and-input/swipe/add-swipe-interface
                } else if(direction == ItemTouchHelper.LEFT) {
                    // add code to delete item
                    Toast.makeText(Item2Activity.this,
                            item.getItemName()+ " swiped left", Toast.LENGTH_SHORT).show();
                    mItemViewModel.update(item); // added so list refreshes, broken after part10
                }
            }
        }).attachToRecyclerView(recyclerView);

        // part 9
        adapter.setOnItemClickListener(new ItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Item item) {
                Intent intent =
                        new Intent(Item2Activity.this, ItemEditAddActivity.class);
                intent.putExtra(ItemEditAddActivity.EXTRA_ITEMID, item.getItemId());
                intent.putExtra(ItemEditAddActivity.EXTRA_USERID, item.getUserId());
                intent.putExtra(ItemEditAddActivity.EXTRA_ITEMNAME, item.getItemName());
                intent.putExtra(ItemEditAddActivity.EXTRA_PRICE, item.getItemPrice());
//                Toast.makeText(Item2Activity.this,
//                          item.getItemId() + " "
//                        + item.getUserId() + " "
//                        + item.getItemName() + " "
//                        + item.getItemPrice() + " " , Toast.LENGTH_SHORT).show();
                //intent.putExtra(ItemEditAddActivity.EXTRA_DESCRIPTION, item.getItemDescription());
                startActivityForResult(intent, EDIT_ITEM_REQUEST);
            }
        });

    } // end onCreate



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
    // also part 9
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == ADD_ITEM_REQUEST && resultCode == RESULT_OK){
            String itemName = data.getStringExtra(ItemEditAddActivity.EXTRA_ITEMNAME);
            String itemDescription = data.getStringExtra(ItemEditAddActivity.EXTRA_DESCRIPTION);
            int itemPrice = data.getIntExtra(ItemEditAddActivity.EXTRA_PRICE, 1);

            Item item = new Item(mUserId,itemName, itemPrice);
            mItemViewModel.insert(item);
            Toast.makeText(this, "Item saved", Toast.LENGTH_SHORT).show();
            // part 9
        } else if(requestCode == EDIT_ITEM_REQUEST && resultCode == RESULT_OK) {
            int itemId = data.getIntExtra(ItemEditAddActivity.EXTRA_ITEMID, -1);
            int userId = data.getIntExtra(ItemEditAddActivity.EXTRA_USERID, -1);

            if (itemId == -1 || userId == -1){
                Toast.makeText(this, "Item can't be updated", Toast.LENGTH_SHORT).show();
                return;
            }

            String itemName = data.getStringExtra(ItemEditAddActivity.EXTRA_ITEMNAME);
            //String itemDescription = data.getStringExtra(ItemEditAddActivity.EXTRA_DESCRIPTION);
            int itemPrice = data.getIntExtra(ItemEditAddActivity.EXTRA_PRICE, 1);

            Item item = new Item(userId, itemName, itemPrice);
            item.setItemId(itemId);
            mItemViewModel.update(item);
            Toast.makeText(this, "Item updated", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Item not saved", Toast.LENGTH_SHORT).show();
        }
    }

    // part 8
    // delete all users
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.delete_user_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.deleteAllUsers) {
            // mUserViewModel.deleteAllItems();
            Toast.makeText(
                    this, "all items deleted - not!", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void getDatabase(){
        mAppDAO = Room.databaseBuilder(this, AppDatabase.class, AppDatabase.DB_NAME)
                .allowMainThreadQueries()
                .build()
                .getAppDAO();
    }

    public static Intent intentFactory(Context context, int userId) {
        Intent intent = new Intent(context, Item2Activity.class);
        intent.putExtra(USER_ID_KEY, userId);
        return intent;
    }
}