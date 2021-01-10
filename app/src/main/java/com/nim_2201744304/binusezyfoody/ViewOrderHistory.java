package com.nim_2201744304.binusezyfoody;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ViewOrderHistory extends AppCompatActivity implements History_RecyclerViewAdapter.ListItemClickListener {
    ArrayList<History> histories = new ArrayList<>();
    SharedPreferences sp;

    Gson gson = new Gson();

    private RecyclerView rvView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_order_history);

        sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String historiesString = sp.getString("history", null);

        if (historiesString != null) {
            Type type = new TypeToken<ArrayList<History>>() {}.getType();
            histories = gson.fromJson(historiesString, type);
        }

        setTitle("Order History");

        rvView = (RecyclerView) findViewById(R.id.history_recycler);
        rvView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        rvView.setLayoutManager(layoutManager);

        adapter = new History_RecyclerViewAdapter(histories, this);
        rvView.setAdapter(adapter);
    }

    @Override
    public void onListItemClick(int position) {
        Toast.makeText(this, "Dengan hormat, saya mohon maaf Pak karena fitur ini belum dapat diimplementasikan karena keterbatasan waktu. " +
                "Saya sudah begadang sampai jam 4 selama berhari-hari dan belum selesai juga. Terima kasih atas pengertian Bapak.", Toast.LENGTH_LONG).show();
    }
}