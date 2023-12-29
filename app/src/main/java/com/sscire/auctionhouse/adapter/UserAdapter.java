package com.sscire.auctionhouse.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sscire.auctionhouse.R;
import com.sscire.auctionhouse.User;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserHolder> {
    private List<User> users = new ArrayList<>();
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
        public UserHolder(@NonNull View itemView) {
            super(itemView);
            mTextViewId = itemView.findViewById(R.id.textview_id);
            mTextViewUsername = itemView.findViewById(R.id.textview_username);
            mTextViewPassword = itemView.findViewById(R.id.textview_password);
            mTextViewIsAdmin = itemView.findViewById(R.id.textview_isadmin);
        }
    }
}
