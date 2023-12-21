package com.sscire.auctionhouse.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.sscire.auctionhouse.Item;
import com.sscire.auctionhouse.repository.ItemRepository;

import java.util.List;

// https://www.youtube.com/watch?v=JLwW5HivZg4&list=PLrnPJCHvNZuAPyh6nRXsvf5hF48SJWdJb&index=5
// Room + ViewModel + LiveData + RecyclerView (MVVM) Part 5 - VIEWMODEL - Android Studio Tutorial
public class ItemViewModel extends AndroidViewModel {
    private ItemRepository mRepository;
    private LiveData<List<Item>> allItems;
    private LiveData<List<Item>> userItems;

    private int mUserId = 1;

    // AndroidViewModel gets passed application and constructor
    // Never store a context of an activity or a view that references an activity
    // in the ViewModel because the ViewModel is designed to outlive the activity
    // after it (activity) is destroyed and if we hold a reference to an already
    // destroyed activity, we have a memory leak
    // ...
    public ItemViewModel(@NonNull Application application) {
        super(application);
        mRepository = new ItemRepository(application);
        allItems = mRepository.getAllItems();
        userItems = mRepository.getUserItems();
    }

    public void insert(Item item) { mRepository.insert(item); }

    public void update(Item item) {
        mRepository.update(item);
    }

    public void delete(Item item) {
        mRepository.delete(item);
    }

    public void deleteAllItems() {
        mRepository.deleteAllItems();
    }

    public LiveData<List<Item>> getAllItems() {
        return allItems;
    }

    public LiveData<List<Item>> getUserItems() {
        return userItems;
    }
}
