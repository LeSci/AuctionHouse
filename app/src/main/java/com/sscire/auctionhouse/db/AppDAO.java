package com.sscire.auctionhouse.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.sscire.auctionhouse.Journal;
import com.sscire.auctionhouse.User;
import com.sscire.auctionhouse.Auction;
import com.sscire.auctionhouse.Bid;
import java.util.List;

/**
 * @author Shannon Scire
 * Project 02: Part 02 Login and Landing Page
 * 05 December 2023
 */
@Dao
public interface AppDAO {

    // Journal
    @Insert
    void insert(Journal... journals);

    @Update
    void update(Journal... journals);

    @Delete
    void delete(Journal... journal);

    @Query("SELECT * FROM " + AppDatabase.JOURNAL_TABLE + " ORDER BY  mDate DESC")
    List<Journal> getAllGymLogs();

    @Query("SELECT * FROM " + AppDatabase.JOURNAL_TABLE + " WHERE mLogId = :logId  ORDER BY  mDate DESC")
    List<Journal> getGymLogsById(int logId);

    @Query("SELECT * FROM " + AppDatabase.JOURNAL_TABLE + " WHERE mUserId = :userId  ORDER BY  mDate DESC")
    List<Journal> getGymLogsByUserId(int userId);

    // Users
    @Insert
    void insert(User...users);

    @Update
    void update(User... users);

    @Delete
    void delete(User user);

    @Query("SELECT * FROM " + AppDatabase.USER_TABLE)
    List<User> getAllUsers();

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

    // Bid
    @Insert
    void insert(Bid...bids);

    @Update
    void update(Bid...bids);

    @Delete
    void delete(Bid...Bid);

    @Query("SELECT * FROM " + AppDatabase.BID_TABLE)
    List<Bid> getAllBids();

    @Query("SELECT * FROM " + AppDatabase.BID_TABLE + " WHERE mUserId = :userId")
    Bid getBidByUserId(int userId);

    @Query("SELECT * FROM " + AppDatabase.BID_TABLE + " WHERE mBidId = :bidId")
    Bid getBidByBidId(int bidId);
}
