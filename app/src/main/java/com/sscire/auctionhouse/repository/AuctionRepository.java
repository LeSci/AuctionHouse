package com.sscire.auctionhouse.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.sscire.auctionhouse.Auction;
import com.sscire.auctionhouse.db.AppDAO;
import com.sscire.auctionhouse.db.AppDatabase;

import java.util.List;

public class AuctionRepository {
    private AppDAO appDAO;
    private LiveData<List<Auction>> allAuctions;

    public AuctionRepository(Application application){
        AppDatabase database = AppDatabase.getInstance(application);
        appDAO = database.getAppDAO();
        allAuctions = appDAO.getAllAuctions2();
    }


    public void insert(Auction auction) {
        new InsertAuctionAsyncTask(appDAO).execute(auction);
    }

    public void update(Auction auction) {
        new UpdateAuctionAsyncTask(appDAO).execute(auction);
    }

    public void delete(Auction auction) {
        new DeleteAuctionAsyncTask(appDAO).execute(auction);
    }

    public void deleteAllAuctions() {
        new DeleteAllAuctionsAsyncTask(appDAO).execute();
    }

    public LiveData<List<Auction>> getAllAuctions() {
        return allAuctions;
    }

    private static class InsertAuctionAsyncTask extends AsyncTask<Auction, Void, Void> {
        private AppDAO appDAO;

        private InsertAuctionAsyncTask(AppDAO appDAO) {
            this.appDAO = appDAO;
        }

        @Override
        protected Void doInBackground(Auction... Auctions) {
            appDAO.insert(Auctions[0]);
            return null;
        }
    }

    private static class UpdateAuctionAsyncTask extends AsyncTask<Auction, Void, Void> {
        private AppDAO appDAO;

        private UpdateAuctionAsyncTask(AppDAO appDAO) {
            this.appDAO = appDAO;
        }

        @Override
        protected Void doInBackground(Auction... auction) {
            appDAO.update(auction[0]);
            return null;
        }
    }

    private static class DeleteAuctionAsyncTask extends AsyncTask<Auction, Void, Void> {
        private AppDAO appDAO;

        private DeleteAuctionAsyncTask(AppDAO appDAO) {
            this.appDAO = appDAO;
        }

        @Override
        protected Void doInBackground(Auction... auctions) {
            appDAO.delete(auctions[0]);
            return null;
        }
    }

    private static class DeleteAllAuctionsAsyncTask extends AsyncTask<Void, Void, Void> {
        private AppDAO appDAO;

        private DeleteAllAuctionsAsyncTask(AppDAO appDAO) {
            this.appDAO = appDAO;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            appDAO.deleteAllAuctions();
            return null;
        }
    }





}
