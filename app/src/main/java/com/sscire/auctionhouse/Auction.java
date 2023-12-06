package com.sscire.auctionhouse;

import androidx.room.Entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.sscire.auctionhouse.db.AppDatabase;

import java.time.LocalDate;

@Entity(tableName = AppDatabase.AUCTION_TABLE)
public class Auction {
    @PrimaryKey(autoGenerate = true)
    private int mAuctionId;
    private int mUserId;
    private int mItemId;
    private int mPrice;
    private LocalDate mExpires;
    private LocalDate mClose;

    public Auction(int auctionId, int userId, int itemId, int price) {
        mAuctionId = auctionId;
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

    public LocalDate getExpires() {
        return mExpires;
    }

    public void setExpires(LocalDate expires) {
        mExpires = expires;
    }

    public LocalDate getClose() {
        return mClose;
    }

    public void setClose(LocalDate close) {
        mClose = close;
    }

    @Override
    public String toString() {
        return "Auction{" +
                "mAuctionId=" + mAuctionId +
                ", mUserId=" + mUserId +
                ", mItemId=" + mItemId +
                ", mPrice=" + mPrice +
                ", mExpires=" + ((mExpires == null) ? "-" : mExpires) +
                ", mClose=" + ((mClose == null) ? "-" : mClose) +
                '}';
    }
}

// (mExpires == null) ? "-" :
// (mClose == null) ? "-" :
