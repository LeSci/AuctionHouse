package com.sscire.auctionhouse;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.sscire.auctionhouse.db.AppDatabase;
/**
 * @author Shannon Scire
 * Project 02: Part 02 Login and Landing Page
 * 05 December 2023
 */
@Entity(tableName = AppDatabase.USER_TABLE)
public class User {
    @PrimaryKey(autoGenerate = true)
    private int mUserId;

    private String mUserName;
    private String mPassword;

    private boolean mIsAdmin;

    public User(String userName, String password, boolean isAdmin) {
        mUserName = userName;
        mPassword = password;
        mIsAdmin = isAdmin;
        // no mUserId = userId;
    }

    public int getUserId() {
        return mUserId;
    }

    public void setUserId(int userId) {
        mUserId = userId;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String userName) {
        mUserName = userName;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
    }

    public boolean getIsAdmin() { return mIsAdmin;}

    public void setIsAdmin(boolean isAdmin) { mIsAdmin = isAdmin; }

    @Override
    public String toString() {
        return "User{" +
                "mUserId=" + mUserId +
                ", mUserName='" + mUserName + '\'' +
                ", mPassword='" + mPassword + '\'' +
                ", mIsAdmin='" + mIsAdmin + '\'' +
                '}';
    }

}
