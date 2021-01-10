package com.nim_2201744304.binusezyfoody;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class History_RecyclerViewAdapter extends RecyclerView.Adapter<History_RecyclerViewAdapter.ViewHolder> {

    private ArrayList<History> rvData;
    boolean isCartView = false;
    Integer grandTotal = 0;

    interface ListItemClickListener{
        void onListItemClick(int position);
    }

    final private ListItemClickListener mOnClickListener;

    public History_RecyclerViewAdapter(ArrayList<History> inputData, ListItemClickListener onClickListener) {
        rvData = inputData;
        this.mOnClickListener = onClickListener;
    }

    public History_RecyclerViewAdapter(ArrayList<History> inputData, ListItemClickListener onClickListener, boolean isCartView) {
        rvData = inputData;
        this.mOnClickListener = onClickListener;
        this.isCartView = isCartView;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView orderTime;
        public TextView orderAddress;
        public TextView orderQty;
        public TextView orderPrice;
        public CardView card;

        public ViewHolder(@NonNull View v) {
            super(v);
            orderTime = (TextView) v.findViewById(R.id.order_time);
            orderAddress = (TextView) v.findViewById(R.id.address_history);
            orderQty = (TextView) v.findViewById(R.id.item_count);
            orderPrice = (TextView) v.findViewById(R.id.totalPrice_history);
            card = (CardView) v.findViewById(R.id.card_history);

            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            mOnClickListener.onListItemClick(position);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_recyclerview_history, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final History history = rvData.get(position);
        holder.card.setCardBackgroundColor(0x787878);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            holder.card.setElevation(1);
        }
        holder.orderTime.setText(history.getDate().toString());
        holder.orderAddress.setText(history.getAddress());
        holder.orderQty.setText(history.getItemCount() + "items");
        holder.orderPrice.setText("Total: IDR " + history.getTotal());
    }

    @Override
    public int getItemCount() {
        // menghitung ukuran dataset / jumlah data yang ditampilkan di RecyclerView
        return rvData.size();
    }
}