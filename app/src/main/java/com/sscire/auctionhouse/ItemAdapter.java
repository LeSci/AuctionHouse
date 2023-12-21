package com.sscire.auctionhouse;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemHolder> {
    private List<Item> items = new ArrayList<>();
    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_item, parent, false);
        return new ItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        Item currentItem = items.get(position);
//        Item currentItem = getItem(position);
        holder.mTextViewId.setText(String.valueOf(currentItem.getItemId()));
        holder.mTextViewName.setText(currentItem.getItemName());
        holder.mTextViewPrice.setText(String.valueOf(currentItem.getItemPrice()));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItems(List<Item> items){
        this.items = items;
        notifyDataSetChanged();
    }


//    public Item getItemAt(int position){
//        //return notes.get(position);
//        return getItem(position);
//    }

    class ItemHolder extends RecyclerView.ViewHolder{
        private TextView mTextViewId;
        private TextView mTextViewName;
        private TextView mTextViewPrice;
        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            mTextViewId = itemView.findViewById(R.id.textview_id);
            mTextViewName = itemView.findViewById(R.id.textview_name);
            mTextViewPrice = itemView.findViewById(R.id.textview_price);
        }
    }
}
