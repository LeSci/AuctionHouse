package com.sscire.auctionhouse.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.sscire.auctionhouse.User;
import com.sscire.auctionhouse.repository.UserRepository;

import java.util.List;

// https://www.youtube.com/watch?v=JLwW5HivZg4&list=PLrnPJCHvNZuAPyh6nRXsvf5hF48SJWdJb&index=5
// Room + ViewModel + LiveData + RecyclerView (MVVM) Part 5 - VIEWMODEL - Android Studio Tutorial
public class UserViewModel extends AndroidViewModel {
    private UserRepository mRepository;
    private LiveData<List<User>> allUsers;

    // AndroidViewModel gets passed application and constructor
    // Never store a context of an activity or a view that references an activity
    // in the ViewModel because the ViewModel is designed to outlive the activity
    // after it (activity) is destroyed and if we hold a reference to an already
    // destroyed activity, we have a memory leak
    // ...
    public UserViewModel(@NonNull Application application) {
        super(application);
        mRepository = new UserRepository(application);
        allUsers = mRepository.getAllUsers();
    }

    public void insert(User user) {
        mRepository.insert(user);
    }

    public void update(User user) {
        mRepository.update(user);
    }

    public void delete(User user) {
        mRepository.delete(user);
    }

    public void deleteAllUsers() {
        //mRepository.deleteAllUsers();
    }

    public LiveData<List<User>> getAllUsers() {
        return allUsers;
    }
}
