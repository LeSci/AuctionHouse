package com.sscire.auctionhouse;

import androidx.room.Entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.sscire.auctionhouse.db.AppDatabase;

import java.time.LocalDate;
import java.util.Date;

@Entity(tableName = AppDatabase.AUCTION_TABLE)
public class Auction {
    @PrimaryKey(autoGenerate = true)
    private int mAuctionId;
    private int mUserId;
    private int mItemId;
    private int mPrice;


    public Auction(int userId, int itemId, int price) {
        mUserId = userId;
        mItemId = itemId;
        mPrice = price;
    }

    public int getAuctionId() {
        return mAuctionId;
    }

    public void setAuctionId(int auctionId) {
        mAuctionId = auctionId;
    }

    public int getUserId() {
        return mUserId;
    }

    public void setUserId(int userId) {
        mUserId = userId;
    }

    public int getItemId() {
        return mItemId;
    }

    public void setItemId(int itemId) {
        mItemId = itemId;
    }

    public int getPrice() {
        return mPrice;
    }

    public void setPrice(int price) {
        mPrice = price;
    }

    @Override
    public String toString() {
        return "Auction{" +
                "mAuctionId=" + mAuctionId +
                ", mUserId=" + mUserId +
                ", mItemId=" + mItemId +
                ", mPrice=" + mPrice +
                '}';
    }
    // ((mExpires == null) ? "-" : mExpires)
}

