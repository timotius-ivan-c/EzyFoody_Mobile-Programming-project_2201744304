package com.nim_2201744304.binusezyfoody;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ViewCart extends AppCompatActivity implements RecyclerViewAdapter.ListItemClickListener {
    public ArrayList<Item> drinks = new ArrayList<>();
    public ArrayList<Item> foods  = new ArrayList<>();
    public ArrayList<Item> snacks = new ArrayList<>();

    Restaurant restaurant;
    String address;

    public ArrayList<Item> items = new ArrayList<>();
    Date currentTime;
    Integer balance;
    Integer grandTotal = 0;

    boolean needsRefresh = false;

    SharedPreferences sp;
    SharedPreferences.Editor editor;
    public Gson gson;

    TextView balanceText;
    TextView grandtotalText;
    Button addMore;
    Button payNow;

    private RecyclerView rvView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_cart);

        balanceText = findViewById(R.id.BalanceText_cart);
        grandtotalText = findViewById(R.id.grandTot_cart);
        addMore = findViewById(R.id.add_more);
        payNow = findViewById(R.id.buy_button);

        sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Intent intent = getIntent();

        restaurant = (Restaurant) intent.getSerializableExtra("restaurant");
        balance = sp.getInt("balance", 0);

        balanceText.setText("Balance: IDR " + balance);

        setTitle("Your cart from " + restaurant.getName());

        gson = new Gson();

        refreshArrayList();

        rvView = (RecyclerView) findViewById(R.id.cart_recycler);
        rvView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        rvView.setLayoutManager(layoutManager);

        adapter = new RecyclerViewAdapter(items, ViewCart.this, true);
        rvView.setAdapter(adapter);

        for (Item item : items) {
            grandTotal += item.getOrderQuantity() * item.getPrice();
        }

        grandtotalText.setText("Total: IDR " + grandTotal);
    }

    @Override
    public void onListItemClick(int position) {
        Intent intent = new Intent(ViewCart.this, ViewItemDetails.class);
        Bundle bundle = getIntent().getExtras();
        String type = items.get(position).getType();
        intent.putExtra("type", type);
        int pos;
        if (type.equals("drink")) {
            pos = position;
        } else if (type.equals("food")) {
            pos = position - drinks.size();
        } else {
            pos = position - (drinks.size() + foods.size());
        }
        intent.putExtra("index", pos);
        intent.putExtras(bundle);

        startActivity(intent);
        items.clear();
        overridePendingTransition(0,0);
    }

    @Override
    protected void onDestroy() {
        Bundle bundle = getIntent().getExtras();
        bundle.remove("type");
        Intent intent = new Intent(ViewCart.this, MenuActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        items.clear();
        refreshArrayList();
        Log.d("refreshArrayList", "onResume: ");
        adapter.notifyDataSetChanged();
        if (needsRefresh) {
            needsRefresh = false;
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }
        super.onResume();
    }

    @Override
    protected void onRestart() {
        items.clear();
        refreshArrayList();
        super.onRestart();
    }

    @Override
    protected void onStop() {
        items.clear();
        super.onStop();
    }

    @Override
    protected void onPause() {
        items.clear();
        super.onPause();
    }

    public void refreshArrayList() {
        String drinksString = sp.getString(restaurant.getName() + "_drinks", null);
        String foodsString = sp.getString(restaurant.getName() + "_foods", null);
        String snacksString = sp.getString(restaurant.getName() + "_snacks", null);

        Log.d("View Cart", "onCreate: " + drinksString);
        Log.d("View Cart", "onCreate: " + foodsString);
        Log.d("View Cart", "onCreate: " + snacksString);

        if (!(drinksString == null)) {
            Type type = new TypeToken<ArrayList<Item>>() {}.getType();
            drinks = gson.fromJson(drinksString, type);
            items.addAll(drinks);
        }

        if (foodsString != null) {
            Type type = new TypeToken<ArrayList<Item>>() {}.getType();
            foods = gson.fromJson(foodsString, type);
            items.addAll(foods);
        }

        if (snacksString != null) {
            Type type = new TypeToken<ArrayList<Item>>() {}.getType();
            snacks = gson.fromJson(snacksString, type);
            items.addAll(snacks);
        }
    }

    public void buy(View view) {
        ArrayList<History> histories = new ArrayList<>();
        String histories_string = sp.getString("history", null);

        if (histories_string != null) {
            Type type = new TypeToken<ArrayList<History>>() {}.getType();
            histories = gson.fromJson(histories_string, type);
        }

        address = sp.getString("address", null);

        balance = sp.getInt("balance", 0);
        int totalItems = 0;

        for (Item item : items) {
            if (item.getOrderQuantity() > 0 && item.getStock() > 0) {
                totalItems++;
            }
        }

        String zz = gson.toJson(items);
        Log.d("viewcartWoy", "buy: " + zz);

        if (grandTotal > balance) {
            Toast.makeText(this, "Insufficient balance. Please top up first!", Toast.LENGTH_SHORT).show();
        } else if (totalItems == 0) {
            Toast.makeText(this, "No items in cart is purchasable. Please add other items!", Toast.LENGTH_SHORT).show();
        } else {
            needsRefresh = true;

            History history = new History();
            history.setAddress(address);
            history.setRestaurant(restaurant);
            currentTime = Calendar.getInstance().getTime();
            history.setDate(currentTime);
            int i = 0;
            Integer totalPurchasablePrice = 0;
            for (Item item : items) {
                if (item.getOrderQuantity() > 0) {
                    ArrayList<Item> histItems = history.getItems();
                    if (item.getStock() < item.getOrderQuantity()) {
                        item.setOrderQuantity(item.getStock());
                    }
                    histItems.add(new Item(item.getName(), item.getType(), item.getPrice(), item.getStock()-item.getOrderQuantity(), item.getOrderQuantity()));
                    Log.d("item_before", "OrderQuantity: " + item.getOrderQuantity());
                    history.setItems(histItems);
                    history.setItemCount(histItems.size());
                    Integer tmp = item.getOrderQuantity() * item.getPrice();
                    totalPurchasablePrice += tmp;
                    item.setStock(item.getStock()-item.getOrderQuantity());
                    item.setOrderQuantity(0);
                    Log.d("item_after", "OrderQuantity: " + histItems.get(histItems.size()-1).getOrderQuantity());
                    items.set(i, item);
                }
                i++;
            }
            String itemsString = gson.toJson(items);
            Log.d("view cart ngebug bos", "buy: " + itemsString);

            history.setTotal(totalPurchasablePrice);
            histories.add(history);

            sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            editor = sp.edit();
            String historiesString_ = gson.toJson(histories);
            Log.d("HistoriesString", historiesString_);
            editor.putString("history", historiesString_);
            editor.apply();
            editor.putInt("balance", balance-grandTotal);
            editor.apply();

            updateSharedPreferences();

            Toast.makeText(this, "Order completed! Thank you!", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(ViewCart.this, OrderComplete.class);
            Bundle bundle = getIntent().getExtras();
            String historyString = gson.toJson(histories.get(histories.size()-1));
            intent.putExtras(bundle);
            intent.putExtra("history", historyString);

            startActivity(intent);
        }
    }

    public void updateSharedPreferences() {
        drinks.clear();
        foods.clear();
        snacks.clear();

        for (int i=0; i<items.size(); i++) {
            if (items.get(i).getType().equals("drink")) {
                drinks.add(items.get(i));
            } else if (items.get(i).getType().equals("food")) {
                foods.add(items.get(i));
            } else {
                snacks.add(items.get(i));
            }
        }

        String json = gson.toJson(drinks);
        Log.d("drinks_to_json viewcart", restaurant.getName()+" : "+json);

        editor = sp.edit();
        editor.putString(restaurant.getName() + "_drinks", json);
        editor.apply();

        json = gson.toJson(foods);
        Log.d("foods_to_json viewcart", restaurant.getName()+" : "+json);

        editor = sp.edit();
        editor.putString(restaurant.getName() + "_foods", json);
        editor.apply();

        json = gson.toJson(snacks);
        Log.d("snacks_to_json viewcart", restaurant.getName()+" : "+json);

        editor = sp.edit();
        editor.putString(restaurant.getName() + "_snacks", json);
        editor.apply();
    }

    public void back(View view) {
        finish();
    }
}