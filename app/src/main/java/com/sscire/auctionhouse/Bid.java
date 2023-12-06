package com.sscire.auctionhouse;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.sscire.auctionhouse.db.AppDatabase;

import java.util.Date;

@Entity(tableName = AppDatabase.BID_TABLE)
public class Bid {
    @PrimaryKey(autoGenerate = true)
    private int mBidId;
    private int mUserId;
    private int mAuctionId;
    private Date mExpires;

    public Bid(int bidId, int userId, int auctionId, Date expires) {
        mBidId = bidId;
        mUserId = userId;
        mAuctionId = auctionId;
        mExpires = expires;
    }

    public int getBidId() {
        return mBidId;
    }

    public void setBidId(int bidId) {
        mBidId = bidId;
    }

    public int getUserId() {
        return mUserId;
    }

    public void setUserId(int userId) {
        mUserId = userId;
    }

    public int getAuctionId() {
        return mAuctionId;
    }

    public void setAuctionId(int auctionId) {
        mAuctionId = auctionId;
    }

    public Date getExpires() {
        return mExpires;
    }

    public void setExpires(Date expires) {
        mExpires = expires;
    }

    @Override
    public String toString() {
        return "Bid{" +
                "mBidId=" + mBidId +
                ", mUserId=" + mUserId +
                ", mAuctionId=" + mAuctionId +
                ", mExpires=" + mExpires +
                '}';
    }
}
