package com.sscire.auctionhouse;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.sscire.auctionhouse.db.AppDatabase;

import java.util.Date;

@Entity(tableName = AppDatabase.JOURNAL_TABLE)
public class Journal {

    @PrimaryKey(autoGenerate = true)
    private int mLogId;
    private String mExercise;
    private int mReps;
    private double mWeight;
    private Date mDate;
    private int mUserId;

    public Journal(String exercise, int reps, double weight, int userId) {
        mExercise = exercise;
        mReps = reps;
        mWeight = weight;
        mDate = new Date();
        mUserId = userId;
    }

    public int getUserId() {
        return mUserId;
    }

    public void setUserId(int userId) {
        mUserId = userId;
    }

    public int getLogId() {
        return mLogId;
    }

    public void setLogId(int logId) {
        mLogId = logId;
    }

    public String getExercise() {
        return mExercise;
    }

    public void setExercise(String exercise) {
        mExercise = exercise;
    }

    public int getReps() {
        return mReps;
    }

    public void setReps(int reps) {
        mReps = reps;
    }

    public double getWeight() {
        return mWeight;
    }

    public void setWeight(double weight) {
        mWeight = weight;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    @Override
    public String toString() {
        String output;

        output = mExercise  + " " + mWeight + " : " + mReps;
        output += "\n";
        output += getDate();
        output += "\n";
        output += "userId == " + mUserId;

        return output;
    }

}
