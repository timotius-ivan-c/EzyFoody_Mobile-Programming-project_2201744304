package com.nim_2201744304.binusezyfoody;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

@SuppressWarnings("ALL")
public class OrderComplete extends AppCompatActivity implements RecyclerViewAdapter.ListItemClickListener {
    History history;
    ArrayList<History> histories;
    SharedPreferences sp;
    Gson gson = new Gson();

    private RecyclerView rvView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    TextView grandtotalText;
    public ArrayList<Item> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_complete);
        sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Intent intent = getIntent();
//        String historyString = intent.getStringExtra("history");
        String historyString = sp.getString("history", null);

        if (historyString != null) {
            Type type = new TypeToken<ArrayList<History>>() {}.getType();
            histories = gson.fromJson(historyString, type);
        }

        history = histories.get(histories.size()-1);

        setTitle("Order completed - " + history.getRestaurant().getName());
        grandtotalText = (TextView) findViewById(R.id.total_order);
        grandtotalText.setText("Total: IDR " + history.getTotal());

        items = history.getItems();

        rvView = (RecyclerView) findViewById(R.id.order_recycler);
        rvView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        rvView.setLayoutManager(layoutManager);

        adapter = new RecyclerViewAdapter(items, OrderComplete.this, true);
        rvView.setAdapter(adapter);
    }

    @Override
    public void onListItemClick(int position) {
        Toast.makeText(this, history.getItems().get(position).getName() + " (" + history.getItems().get(position).getOrderQuantity() + ")", Toast.LENGTH_SHORT).show();
    }

    public void back(View view) {
        finish();
    }
}