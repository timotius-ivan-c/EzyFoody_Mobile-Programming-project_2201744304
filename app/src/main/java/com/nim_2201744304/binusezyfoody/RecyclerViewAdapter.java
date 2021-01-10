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

@SuppressWarnings("ALL")
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private ArrayList<Item> rvData;
    boolean isCartView = false;
    Integer grandTotal = 0;

    interface ListItemClickListener{
        void onListItemClick(int position);
    }

    final private ListItemClickListener mOnClickListener;

    public RecyclerViewAdapter(ArrayList<Item> inputData, ListItemClickListener onClickListener) {
        rvData = inputData;
        this.mOnClickListener = onClickListener;
    }

    public RecyclerViewAdapter(ArrayList<Item> inputData, ListItemClickListener onClickListener, boolean isCartView) {
        rvData = inputData;
        this.mOnClickListener = onClickListener;
        this.isCartView = isCartView;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tvTitle;
        public TextView tvSubtitle;
        public TextView outOfStock;
        public TextView itemName;
        public TextView itemQty;
        public TextView totalPrice;
        public CardView card;

        public ViewHolder(@NonNull View v) {
            super(v);
            if (!isCartView) {
                tvTitle = (TextView) v.findViewById(R.id.tv_title);
                tvSubtitle = (TextView) v.findViewById(R.id.tv_subtitle);
                outOfStock = (TextView) v.findViewById(R.id.outOfStock);
                card = (CardView) v.findViewById(R.id.card);
            } else {
                itemName = (TextView) v.findViewById(R.id.itemName_cart);
                itemQty = (TextView) v.findViewById(R.id.itemQty_cart);
                totalPrice = (TextView) v.findViewById(R.id.totalPrice_cart);
                card = (CardView) v.findViewById(R.id.card_cart);
            }
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
        if (!isCartView) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_recyclerview_item, parent, false);
        } else {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_recyclerview_cart_item, parent, false);
        }
        // mengeset ukuran view, margin, padding, dan parameter layout lainnya
        //noinspection UnnecessaryLocalVariable
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - mengambil elemen dari dataset (ArrayList) pada posisi tertentu
        // - mengeset isi view dengan elemen dari dataset tersebut
        final Item item = rvData.get(position);
        if (!isCartView) {
            if (item.getStock() < 1) {
                holder.card.setCardBackgroundColor(0x787878);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    holder.card.setElevation(1);
                    holder.outOfStock.setText("Out of Stock");
                    holder.outOfStock.setElevation(20);
                    holder.outOfStock.setVisibility(View.VISIBLE);
                }
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    holder.card.setElevation(5);
                    holder.outOfStock.setVisibility(View.GONE);
                }
            }
            holder.tvTitle.setText(rvData.get(position).getName());
            holder.tvSubtitle.setText(new String("Price: "+rvData.get(position).getPrice()+""));
        } else {
            if (item.getOrderQuantity() < 1) {
                holder.card.setVisibility(View.GONE);
            } else {
                if (item.getStock() < 1) {
                    holder.card.setCardBackgroundColor(0x787878);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        holder.card.setElevation(1);
                    }
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        holder.card.setElevation(5);
                        holder.itemName.setText(item.getName());
                        holder.itemQty.setText("Qty: " + item.getOrderQuantity());
                        holder.totalPrice.setText("Subtot: IDR. " + (item.getOrderQuantity()*item.getPrice()));

                    }
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        // menghitung ukuran dataset / jumlah data yang ditampilkan di RecyclerView
        return rvData.size();
    }
}