package com.sscire.auctionhouse.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.sscire.auctionhouse.Item;
import com.sscire.auctionhouse.R;
import com.sscire.auctionhouse.User;

import java.util.ArrayList;
import java.util.List;

// Room + ViewModel + LiveData + RecyclerView (MVVM) Part 6 - RECYCLERVIEW + ADAPTER - Android Tutorial
// public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemHolder> {

// Room + ViewModel + LiveData + RecyclerView (MVVM) Part 10 - ADD ANIMATIONS WITH LISTADAPTER/DIFFUTIL
public class ItemAdapter extends ListAdapter<Item,ItemAdapter.ItemHolder> {
    //private List<Item> items = new ArrayList<>(); // removed for part 10

    //part 9
    OnItemClickListener mListener;

    // part 10
    public ItemAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Item> DIFF_CALLBACK = new DiffUtil.ItemCallback<Item>() {
        @Override
        public boolean areItemsTheSame(@NonNull Item oldItem, @NonNull Item newItem) {
            return oldItem.getItemId() == newItem.getItemId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Item oldItem, @NonNull Item newItem) {
            return oldItem.getUserId() == newItem.getUserId()
                    && oldItem.getItemName().equals(newItem.getItemName())
                   // && oldItem.getDescription() == newItem.Description()
                    && oldItem.getItemPrice() == newItem.getItemPrice();
        }
    };

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_item, parent, false);
        return new ItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        //Item currentItem = items.get(position); // part 10 as we pass the list directly to superclass
        Item currentItem = getItem(position);
        holder.mTextViewId.setText(String.valueOf(currentItem.getItemId()));
        holder.mTextViewName.setText(currentItem.getItemName());
        holder.mTextViewPrice.setText(String.valueOf(currentItem.getItemPrice()));
    }

    // not needed after part 10
//    @Override
//    public int getItemCount() {
//        return items.size();
//    }

    // not needed after part 10
//    public void setItems(List<Item> items){
//        this.items = items;
//        notifyDataSetChanged();
//    }

    // swipe functionality part 8
    public Item getItemAt(int position){
        //return items.get(position);
        return getItem(position);
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

            //part 9
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(mListener != null && position != RecyclerView.NO_POSITION){
                        // part 10
                       // mListener.onItemClick(items.get(position));
                        mListener.onItemClick(getItem(position));
                    }
                }
            });
        }
    }

    // Part 9
    // Room + ViewModel + LiveData + RecyclerView (MVVM) Part 9 - EDIT NOTES ON ITEM CLICK - Android
    public interface OnItemClickListener {
        void onItemClick(Item item);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.mListener = listener;
    }
}
