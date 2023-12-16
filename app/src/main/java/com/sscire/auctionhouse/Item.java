package com.sscire.auctionhouse;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.sscire.auctionhouse.db.AppDatabase;

@Entity(tableName = AppDatabase.ITEM_TABLE)
public class Item {
    @PrimaryKey(autoGenerate = true)
    private int mItemId;

    private int mUserId;

    private String mItemName;

    private int mItemPrice;

    public Item(int userId, String itemName, int itemPrice) {
        mUserId = userId;
        mItemName = itemName;
        mItemPrice = itemPrice;
    }

    public int getItemId() {
        return mItemId;
    }

    public void setItemId(int itemId) {
        mItemId = itemId;
    }

    public int getUserId() {
        return mUserId;
    }

    public void setUserId(int userId) {
        mUserId = userId;
    }

    public String getItemName() {
        return mItemName;
    }

    public void setItemName(String itemName) {
        mItemName = itemName;
    }

    public int getItemPrice() {
        return mItemPrice;
    }

    public void setItemPrice(int itemPrice) {
        mItemPrice = itemPrice;
    }

    @Override
    public String toString() {
        return "Item{" +
                "mItemId=" + mItemId +
                ", mUserId=" + mUserId +
                ", mItemName='" + mItemName + '\'' +
                ", mItemPrice=" + mItemPrice +
                '}';
    }
}
