package com.sscire.auctionhouse.repository;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.sscire.auctionhouse.Item;
import com.sscire.auctionhouse.MainActivity;
import com.sscire.auctionhouse.db.AppDAO;
import com.sscire.auctionhouse.db.AppDatabase;

import java.util.List;

public class ItemRepository {
    private AppDAO appDAO;
    private LiveData<List<Item>> allItems;
    private LiveData<List<Item>> userItems;

    private static final String USER_ID_KEY = "com.sscire.auctionhouse.userIdKey";
    private static final String PREFENCES_KEY = "com.sscire.auctionhouse.PREFENCES_KEY";
    private int mUserId = MainActivity.getmUserId();


    public ItemRepository(Application application){
        AppDatabase database = AppDatabase.getInstance(application);
        appDAO = database.getAppDAO();
        allItems = appDAO.getAllItems2();
        userItems = appDAO.getItemsByUserId2(mUserId);

    }


    public void insert(Item item) {
        new InsertItemAsyncTask(appDAO).execute(item);
    }

    public void update(Item item) {
        new UpdateItemAsyncTask(appDAO).execute(item);
    }

    public void delete(Item item) {
        new DeleteItemAsyncTask(appDAO).execute(item);
    }

    public void deleteAllItems() {
        new DeleteAllItemsAsyncTask(appDAO).execute();
    }

    public LiveData<List<Item>> getAllItems() {
        return allItems;
    }
    public LiveData<List<Item>> getUserItems() { return userItems; }

    private static class InsertItemAsyncTask extends AsyncTask<Item, Void, Void> {
        private AppDAO appDAO;

        private InsertItemAsyncTask(AppDAO appDAO) {
            this.appDAO = appDAO;
        }

        @Override
        protected Void doInBackground(Item... items) {
            appDAO.insert(items[0]);
            return null;
        }
    }

    private static class UpdateItemAsyncTask extends AsyncTask<Item, Void, Void> {
        private AppDAO appDAO;

        private UpdateItemAsyncTask(AppDAO appDAO) {
            this.appDAO = appDAO;
        }

        @Override
        protected Void doInBackground(Item... item) {
            appDAO.update(item[0]);
            return null;
        }
    }

    private static class DeleteItemAsyncTask extends AsyncTask<Item, Void, Void> {
        private AppDAO appDAO;

        private DeleteItemAsyncTask(AppDAO appDAO) {
            this.appDAO = appDAO;
        }

        @Override
        protected Void doInBackground(Item... items) {
            appDAO.delete(items[0]);
            return null;
        }
    }

    // AsyncTask deprecated, replaced with ExecutorService
    // https://techblogs.42gears.com/replacement-of-deprecated-asynctask-in-android/
    private static class DeleteAllItemsAsyncTask extends AsyncTask<Void, Void, Void> {
        private AppDAO appDAO;

        private DeleteAllItemsAsyncTask(AppDAO appDAO) {
            this.appDAO = appDAO;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            appDAO.deleteAllItems();
            return null;
        }
    }
}
