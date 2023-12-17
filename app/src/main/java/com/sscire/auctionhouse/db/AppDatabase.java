package com.sscire.auctionhouse.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.Room;
import android.content.Context;

import com.sscire.auctionhouse.Item;
import com.sscire.auctionhouse.User;
import com.sscire.auctionhouse.db.typeConverters.DateTypeConverter;
import com.sscire.auctionhouse.Auction;
@Database(entities ={User.class, Auction.class, Item.class}, version = 1)
@TypeConverters(DateTypeConverter.class)
public abstract class AppDatabase extends RoomDatabase {
    public static final String DB_NAME = "APP_DATABASE";
    public static final String USER_TABLE = "USER_TABLE";
    public static final String AUCTION_TABLE = "AUCTION_TABLE";
    public static final String ITEM_TABLE = "ITEM_TABLE";


    private static AppDatabase instance;

    public abstract AppDAO getAppDAO();

    //sls
    public static AppDatabase getInstance(Context context){
        if (instance == null){
            synchronized (AppDatabase.class){
                if(instance == null){
                    instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class,
                            DB_NAME)
                            .fallbackToDestructiveMigration()
                            .build();
                } // end if
            } // end synchronized
        } // end if
        return instance;
    } // end method

}
