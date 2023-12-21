package com.sscire.auctionhouse.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.sscire.auctionhouse.Auction;
import com.sscire.auctionhouse.repository.AuctionRepository;

import java.util.List;

// https://www.youtube.com/watch?v=JLwW5HivZg4&list=PLrnPJCHvNZuAPyh6nRXsvf5hF48SJWdJb&index=5
// Room + ViewModel + LiveData + RecyclerView (MVVM) Part 5 - VIEWMODEL - Android Studio Tutorial
public class AuctionViewModel extends AndroidViewModel {
    private AuctionRepository mRepository;
    private LiveData<List<Auction>> allAuctions;

    // AndroidViewModel gets passed application and constructor
    // Never store a context of an activity or a view that references an activity
    // in the ViewModel because the ViewModel is designed to outlive the activity
    // after it (activity) is destroyed and if we hold a reference to an already
    // destroyed activity, we have a memory leak
    // ...
    public AuctionViewModel(@NonNull Application application) {
        super(application);
        mRepository = new AuctionRepository(application);
        allAuctions = mRepository.getAllAuctions();
    }

    public void insert(Auction auction) { mRepository.insert(auction); }

    public void update(Auction auction) {
        mRepository.update(auction);
    }

    public void delete(Auction auction) {
        mRepository.delete(auction);
    }

    public void deleteAllAuctions() {
        mRepository.deleteAllAuctions();
    }

    public LiveData<List<Auction>> getAllAuctions() {
        return allAuctions;
    }
}
