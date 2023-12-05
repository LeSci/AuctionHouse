package com.sscire.auctionhouse.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.sscire.auctionhouse.Journal;
import com.sscire.auctionhouse.User;

import java.util.List;

@Dao
public interface AppDAO {

    @Insert
    void insert(Journal... journal);

    @Update
    void update(Journal... journal);

    @Delete
    void delete(Journal... journal);

    @Query("SELECT * FROM " + AppDatabase.JOURNAL_TABLE + " ORDER BY  mDate DESC")
    List<Journal> getAllGymLogs();

    @Query("SELECT * FROM " + AppDatabase.JOURNAL_TABLE + " WHERE mLogId = :logId  ORDER BY  mDate DESC")
    List<Journal> getGymLogsById(int logId);

    @Query("SELECT * FROM " + AppDatabase.JOURNAL_TABLE + " WHERE mUserId = :userId  ORDER BY  mDate DESC")
    List<Journal> getGymLogsByUserId(int userId);

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
}
