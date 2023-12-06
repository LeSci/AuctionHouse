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
    private Date mExpires;
    private Date mClose;

    public Auction(int auctionId, int userId, int itemId, int price, Date expires, Date close) {
        mAuctionId = auctionId;
        mUserId = userId;
        mItemId = itemId;
        mPrice = price;
        mExpires = expires;
        mClose = close;
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

    public Date getExpires() {
        return mExpires;
    }

    public void setExpires(Date expires) {
        mExpires = expires;
    }

    public Date getClose() {
        return mClose;
    }

    public void setClose(Date close) {
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
                ", mClose=" + ((mClose == null) ? "-" : mClose)+
                '}';
    }
    // ((mExpires == null) ? "-" : mExpires)
}

