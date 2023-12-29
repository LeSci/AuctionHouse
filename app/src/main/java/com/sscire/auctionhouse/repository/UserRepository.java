package com.sscire.auctionhouse.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.sscire.auctionhouse.User;
import com.sscire.auctionhouse.db.AppDAO;
import com.sscire.auctionhouse.db.AppDatabase;

import java.util.ArrayList;
import java.util.List;

// Room + ViewModel + LiveData + RecyclerView (MVVM) Part 4 - VIEWMODEL - Android Studio Tutorial

public class UserRepository {
    private AppDAO appDAO;
    private LiveData<List<User>> allUsers;

    public UserRepository(Application application){
        AppDatabase database = AppDatabase.getInstance(application);
        appDAO = database.getAppDAO();
        allUsers = appDAO.getAllUsers2();
    }

    public void insert(User user) {
        new InsertUserAsyncTask(appDAO).execute(user);
    }

    public void update(User user) {
        new UpdateUserAsyncTask(appDAO).execute(user);
    }

    public void delete(User user) {
        new DeleteUserAsyncTask(appDAO).execute(user);
    }

    public void deleteAllNotes() {
        new DeleteAllUsersAsyncTask(appDAO).execute();
    }

    public LiveData<List<User>> getAllUsers() {
        return allUsers;
    }

    //private static class InsertUserAsyncTask extends AsyncTaskExecutorService<User, Void, Void> {
    private static class InsertUserAsyncTask extends AsyncTask<User, Void, Void> {
        private AppDAO appDAO;

        private InsertUserAsyncTask(AppDAO appDAO) {
            this.appDAO = appDAO;
        }

        @Override
        protected Void doInBackground(User... users) {
            appDAO.insert(users[0]);
            return null;
        }

//        @Override
//        protected Void doInBackground(User users) {
//            appDAO.insert(users[0]);
//            return null;
//        }
    }

    private static class UpdateUserAsyncTask extends AsyncTask<User, Void, Void> {
        private AppDAO appDAO;

        private UpdateUserAsyncTask(AppDAO appDAO) {
            this.appDAO = appDAO;
        }

        @Override
        protected Void doInBackground(User... user) {
            appDAO.update(user[0]);
            return null;
        }
    }

    private static class DeleteUserAsyncTask extends AsyncTask<User, Void, Void> {
        private AppDAO appDAO;

        private DeleteUserAsyncTask(AppDAO appDAO) {
            this.appDAO = appDAO;
        }

        @Override
        protected Void doInBackground(User... users) {
            appDAO.delete(users[0]);
            return null;
        }
    }

    private static class DeleteAllUsersAsyncTask extends AsyncTask<Void, Void, Void> {
        private AppDAO appDAO;

        private DeleteAllUsersAsyncTask(AppDAO appDAO) {
            this.appDAO = appDAO;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            //appDAO.deleteAllUsers();
            return null;
        }
    }





}
