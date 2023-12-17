package com.sscire.auctionhouse.db;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.sscire.auctionhouse.User;
import com.sscire.auctionhouse.Auction;
import com.sscire.auctionhouse.Item;
import java.util.List;

/**
 * @author Shannon Scire
 * Project 02: Part 02 Login and Landing Page
 * 05 December 2023
 */
@Dao
public interface AppDAO {

    // Users
    @Insert
    void insert(User...users);

    @Update
    void update(User... users);

    @Delete
    void delete(User user);

    @Query("SELECT * FROM " + AppDatabase.USER_TABLE)
    List<User> getAllUsers();
    //LiveData<List<User>> getAllUsers();

    @Query("SELECT * FROM " + AppDatabase.USER_TABLE + " WHERE mUserName = :username")
    User getUserByUsername(String username);

    @Query("SELECT * FROM " + AppDatabase.USER_TABLE + " WHERE mUserId = :userId")
    User getUserByUserId(int userId);

    // Auction
    @Insert
    void insert(Auction...auctions);

    @Update
    void update(Auction...auctions);

    @Delete
    void delete(Auction...auction);

    @Query("SELECT * FROM " + AppDatabase.AUCTION_TABLE)
    List<Auction> getAllAuctions();

    @Query("SELECT * FROM " + AppDatabase.AUCTION_TABLE + " WHERE mUserId = :userId")
    Auction getAuctionByUserId(int userId);

    @Query("SELECT * FROM " + AppDatabase.AUCTION_TABLE + " WHERE mAuctionId = :auctionId")
    Auction getAuctionByAuctionId(int auctionId);

    @Query("SELECT * FROM " + AppDatabase.AUCTION_TABLE + " WHERE mItemId = :itemId")
    Auction getAuctionByItemId(int itemId);

    @Query("SELECT * FROM " + AppDatabase.AUCTION_TABLE+ " WHERE mUserId = :userId")
    List<Auction> getAuctionsByUserID(int userId);


    // Item
    @Insert
    void insert(Item...items);

    @Update
    void update(Item...items);

    @Delete
    void delete(Item...items);

    @Query("SELECT * FROM " + AppDatabase.ITEM_TABLE)
    List<Item> getAllItems();

    @Query("SELECT * FROM " + AppDatabase.ITEM_TABLE + " WHERE mUserId = :userId")
    Item getItemByUserId(int userId);

    @Query("SELECT * FROM " + AppDatabase.ITEM_TABLE + " WHERE mUserId = :userId")
    List<Item> getItemsByUserId(int userId);

    @Query("SELECT * FROM " + AppDatabase.ITEM_TABLE + " WHERE mItemId = :itemId")
    Item getItemByItemId(int itemId);

}
