package com.nim_2201744304.binusezyfoody;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class ViewItemDetails extends AppCompatActivity {
    Restaurant restaurant;
    SharedPreferences sp;
    String type;
    String content;
    ArrayList<Item> items;
    int index;
    Gson gson = new Gson();
    SharedPreferences.Editor editor;
    TextView ItemName, ItemPrice, ItemStock;
    EditText OrderQuantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_item_details);

        ItemName = findViewById(R.id.ItemName);
        ItemPrice = findViewById(R.id.ItemPrice);
        ItemStock = findViewById(R.id.ItemStock);
        OrderQuantity = findViewById(R.id.OrderQuantity);

        Intent intent = getIntent();
        restaurant = (Restaurant) intent.getSerializableExtra("restaurant");
        type = intent.getStringExtra("type");
        index = intent.getIntExtra("index", 0);

        sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        content = sp.getString(restaurant.getName() + "_" + type + "s", null);
        Log.d("ViewItemDetails", content);

        if (!(content == null)) {
            Type type = new TypeToken<ArrayList<Item>>() {}.getType();
            items = gson.fromJson(content, type);
            for (Item item : items) {
                Log.d("isiarray", item.getName());
            }
        }

        Integer qty = items.get(index).getOrderQuantity();

        ItemName.setText(items.get(index).getName());
        ItemPrice.setText("Price: IDR " + items.get(index).getPrice());
        ItemStock.setText("Stock: " + items.get(index).getStock());

        if (qty > 0) {
            OrderQuantity.setText(qty.toString());
        }

        setTitle("Order " + items.get(index).getName() + " from " + restaurant.getName());
    }

    public void saveOrder(View view) {
        try {
            Integer order_qty = Integer.parseInt(OrderQuantity.getText().toString());
            if (items.get(index).getStock() < order_qty) {
                Toast.makeText(this, "Can not order more than available stock.", Toast.LENGTH_SHORT).show();
            } else {
                items.get(index).setOrderQuantity(order_qty);
                Log.d("saveOrder", "saveOrder: "+items.get(index).getName()+ " " +items.get(index).getOrderQuantity());

                String json = gson.toJson(items);
                Log.d( type + "s_to_json", restaurant.getName()+" : "+json);

                editor = sp.edit();
                editor.putString(restaurant.getName() + "_" + type + "s", json);
                editor.apply();
                Toast.makeText(this, "Added to cart!", Toast.LENGTH_SHORT).show();
                finish();
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            Toast.makeText(this, "Please fill quantity!", Toast.LENGTH_SHORT).show();
        }
    }


}