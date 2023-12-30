package com.sscire.auctionhouse.view2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.sscire.auctionhouse.Item;
import com.sscire.auctionhouse.MainActivity;
import com.sscire.auctionhouse.R;
import com.sscire.auctionhouse.User;
import com.sscire.auctionhouse.adapter.ItemAdapter;
import com.sscire.auctionhouse.adapter.UserAdapter;
import com.sscire.auctionhouse.db.AppDAO;
import com.sscire.auctionhouse.viewmodel.UserViewModel;

import java.util.List;

// https://www.youtube.com/watch?v=Jwdty9jQN0E&list=PLrnPJCHvNZuAPyh6nRXsvf5hF48SJWdJb&index=3
// Room + ViewModel + LiveData + RecyclerView (MVVM) Part 2 - ENTITY - Android Studio Tutorial
public class User2Activity extends AppCompatActivity {

    // sls - return home
    private static final String USER_ID_KEY = "com.sscire.auctionhouse.userIdKey";
    private static final String PREFENCES_KEY = "com.sscire.auctionhouse.PREFENCES_KEY";
    private Button mButtonHome;

    private User mUser;
    private int mUserId = -1;
    private AppDAO mAppDAO;

    // MVVM
    public static final int ADD_USER_REQUEST = 1;

    public static final int EDIT_USER_REQUEST = 2;  // part 9
    private UserViewModel mUserViewModel;

    // setup reference to RecyclerView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user2);

        // sls - return home
        mUserId = getIntent().getIntExtra(USER_ID_KEY, -1);
        wireupDisplay();

        // MVVM
        FloatingActionButton buttonAddUser = findViewById(R.id.buttonAddUser);
        buttonAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(User2Activity.this, UserEditAddActivity.class);
                startActivityForResult(intent, ADD_USER_REQUEST);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final UserAdapter adapter = new UserAdapter();
        recyclerView.setAdapter(adapter);

        //mUserViewModel = ViewModelProvider.of(this).get(UserViewModel.class);
        mUserViewModel = new ViewModelProvider(this,
                new ViewModelProvider.AndroidViewModelFactory(getApplication()))
                .get(UserViewModel.class);
        mUserViewModel.getAllUsers().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(@Nullable List<User> users) {
                //update RecyclerView
                adapter.setUsers(users);
                //adapter.submitList(users);
                //Toast.makeText(Admin2Activity.this, "onChanged", Toast.LENGTH_SHORT).show();
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
                User user = adapter.getUserAt(viewHolder.getAdapterPosition());
                // set/flip user's IsAdmin
                if(direction == ItemTouchHelper.RIGHT) {
                    user.setIsAdmin(!user.getIsAdmin());
                    mUserViewModel.update(user);
                    Toast.makeText(User2Activity.this,
                            user.getUserName() + " IsAdmin updated to " + user.getIsAdmin(),
                            Toast.LENGTH_SHORT).show();
                } else if(direction == ItemTouchHelper.LEFT) {
                    // add code to delete user
                    mUserViewModel.update(user); // added so list refreshes
                    Toast.makeText(User2Activity.this,
                            user.getUserName() + " swiped left", Toast.LENGTH_SHORT).show();
                }
            }
        }).attachToRecyclerView(recyclerView);

        // part 9
        adapter.setOnUserClickListener(new UserAdapter.OnUserClickListener() {
            @Override
            public void onUserClick(User user) {
                Intent intent =
                        new Intent(User2Activity.this, UserEditAddActivity.class);
                intent.putExtra(UserEditAddActivity.EXTRA_USERID, user.getUserId());
                intent.putExtra(UserEditAddActivity.EXTRA_USERNAME, user.getUserName());
                intent.putExtra(UserEditAddActivity.EXTRA_PASSWORD, user.getPassword());
                intent.putExtra(UserEditAddActivity.EXTRA_ISADMIN, user.getIsAdmin());
                //intent.putExtra(UserEditAddActivity.EXTRA_CURRENCY, user.getCurrency());
                startActivityForResult(intent, EDIT_USER_REQUEST);
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == ADD_USER_REQUEST && resultCode == RESULT_OK){
            String username = data.getStringExtra(UserEditAddActivity.EXTRA_USERNAME);
            String password = data.getStringExtra(UserEditAddActivity.EXTRA_PASSWORD);
            boolean isadmin =
                    (data.getIntExtra(UserEditAddActivity.EXTRA_ISADMIN, 0) == 0)
                            ? false : true;
            int currency = data.getIntExtra(UserEditAddActivity.EXTRA_CURRENCY, 5);

            User user = new User(username, password, isadmin);
            mUserViewModel.insert(user);
            Toast.makeText(this, "User saved", Toast.LENGTH_SHORT).show();
            // part 9
        }  else if(requestCode == EDIT_USER_REQUEST && resultCode == RESULT_OK) {
            int userId = data.getIntExtra(UserEditAddActivity.EXTRA_USERID, -1);

            if (userId < 3){
                Toast.makeText(this, "User can't be updated", Toast.LENGTH_SHORT).show();
                return;
            }
            String username = data.getStringExtra(UserEditAddActivity.EXTRA_USERNAME);
            String password = data.getStringExtra(UserEditAddActivity.EXTRA_PASSWORD);
            boolean isAdmin =
                    (data.getIntExtra(UserEditAddActivity.EXTRA_ISADMIN, 0)
                    == 0 ? false : true);
            //int currency = data.getIntExtra(UserEditAddActivity.EXTRA_CURRENCY, 1);
            User user = new User(username, password, isAdmin);
            user.setUserId(userId);
            mUserViewModel.update(user);
            Toast.makeText(this, "Item updated", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "User not saved", Toast.LENGTH_SHORT).show();
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
            // mUserViewModel.deleteAllUsers();
            Toast.makeText(
                    this, "all users deleted - not!", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    public static Intent intentFactory(Context context, int userId) {
        Intent intent = new Intent(context, User2Activity.class);
        intent.putExtra(USER_ID_KEY, userId);
        return intent;
    }
}