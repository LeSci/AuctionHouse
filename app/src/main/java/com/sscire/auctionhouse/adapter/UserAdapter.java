package com.sscire.auctionhouse.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sscire.auctionhouse.Item;
import com.sscire.auctionhouse.R;
import com.sscire.auctionhouse.User;

import java.util.ArrayList;
import java.util.List;

// Room + ViewModel + LiveData + RecyclerView (MVVM) Part 6 - RECYCLERVIEW + ADAPTER - Android Tutorial

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserHolder> {
    private List<User> users = new ArrayList<>();

    UserAdapter.OnUserClickListener mListener; // part 9

    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_item, parent, false);
        return new UserHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull UserHolder holder, int position) {
        User currentUser = users.get(position);
//        User currentUser = getUser(position);
        holder.mTextViewId.setText(String.valueOf(currentUser.getUserId()));
        holder.mTextViewUsername.setText(currentUser.getUserName());
        holder.mTextViewPassword.setText(currentUser.getPassword());
        holder.mTextViewIsAdmin.setText(String.valueOf(currentUser.getIsAdmin()));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public void setUsers(List<User> users){
        this.users = users;
        notifyDataSetChanged();
    }

    // swipe functionality part 8
    public User getUserAt(int position){
        return users.get(position);
    }

//    public User getUserAt(int position){
//        //return notes.get(position);
//        return getUser(position);
//    }

    class UserHolder extends RecyclerView.ViewHolder{
        private TextView mTextViewId;
        private TextView mTextViewUsername;
        private TextView mTextViewPassword;
        private TextView mTextViewIsAdmin;
        public UserHolder(@NonNull View userView) {
            super(userView);
            mTextViewId = userView.findViewById(R.id.textview_id);
            mTextViewUsername = userView.findViewById(R.id.textview_username);
            mTextViewPassword = userView.findViewById(R.id.textview_password);
            mTextViewIsAdmin = userView.findViewById(R.id.textview_isadmin);

            //part 9
            userView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(mListener != null && position != RecyclerView.NO_POSITION){
                        mListener.onUserClick(users.get(position));
                    }
                }
            });
        }
    }

    // Part 9
    // Room + ViewModel + LiveData + RecyclerView (MVVM) Part 9 - EDIT NOTES ON ITEM CLICK - Android
    public interface OnUserClickListener {
        void onUserClick(User user);
    }

    public void setOnUserClickListener(UserAdapter.OnUserClickListener listener){
        this.mListener = listener;
    }
}
