package com.nim_2201744304.binusezyfoody;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.widget.Toast;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ViewCategoryActivity extends AppCompatActivity implements RecyclerViewAdapter.ListItemClickListener {
    SharedPreferences sp;
    String type;
    String content;
    ArrayList<Item> items;
    Restaurant restaurant;
    Gson gson = new Gson();
    Integer balance = 0;

    private RecyclerView rvView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_category);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();

        sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        balance = sp.getInt("balance", 0);

        type = intent.getStringExtra("type");
        restaurant = (Restaurant) intent.getSerializableExtra("restaurant");

        setTitle(restaurant.getName() + " - Order " + type);

        content = sp.getString(restaurant.getName() + "_" + type + "s", null);

        if (!(content == null)) {
            Type type = new TypeToken<ArrayList<Item>>() {}.getType();
            items = gson.fromJson(content, type);
        }

        rvView = (RecyclerView) findViewById(R.id.rv_main);
        rvView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        rvView.setLayoutManager(layoutManager);

        adapter = new RecyclerViewAdapter(items, this);
        rvView.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        Bundle bundle = getIntent().getExtras();
        bundle.remove("type");
        Intent intent = new Intent(ViewCategoryActivity.this, MenuActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
        super.onDestroy();
    }

    @Override
    public void onListItemClick(int position) {
        if (items.get(position).getStock() < 1) {
            Toast.makeText(this, "Out of Stock. Please order other item.", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(this, ViewItemDetails.class);
            Bundle bundle = getIntent().getExtras();
            intent.putExtras(bundle);
            intent.putExtra("index", position);
            startActivity(intent);
        }
    }
}