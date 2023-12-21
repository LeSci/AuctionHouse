package com.sscire.auctionhouse.db;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.Room;
import androidx.sqlite.db.SupportSQLiteDatabase;

import android.content.Context;
import android.os.AsyncTask;

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

    // MVVM
    //public static synchronized AppDatabase getInstance(Context context)
    // synchronized limits access to one thread at a time
    //sls
    public static AppDatabase getInstance(Context context){
        if (instance == null){
            synchronized (AppDatabase.class){
        //        if(instance == null){
                    instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class,
                            DB_NAME)
                            .fallbackToDestructiveMigration()
                            //.addCallback(roomCallback)
                            .build();
        //        } // end if
            } // end synchronized
        } // end if
        return instance;
    } // end method

    // MVVM
    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private AppDAO appDAO;

        private PopulateDbAsyncTask(AppDatabase db) {
            appDAO = db.getAppDAO();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            User defaultUser = new User("testuser1", "testuser1", false);
            User altUser = new User("admin2", "admin2", true);
            appDAO.insert(defaultUser,altUser);
//            appDAO.insert(new Note("Title 1", "Description 1", 1));
//            appDAO.insert(new Note("Title 2", "Description 2", 2));
//            appDAO.insert(new Note("Title 3", "Description 3", 3));
            return null;
        }
    }
}
