package com.nim_2201744304.binusezyfoody;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MenuActivity extends AppCompatActivity {
    String title;
    Restaurant restaurant;
    Intent intent;

    TextView address;
    TextView city;
    TextView distance;
    TextView balanceText;

    Double latitude;
    Double longitude;
    Double dist;
    Integer balance = 0;
    Geocoder geocoder;
    List<Address> addresses;

    FirebaseDatabase database;
    DatabaseReference myRefDrink;
    DatabaseReference myRefFood;
    DatabaseReference myRefSnack;
    ValueEventListener listenerDrink;
    ValueEventListener listenerFood;
    ValueEventListener listenerSnack;

    ArrayList<Item> drinks;
    ArrayList<Item> foods;
    ArrayList<Item> snacks;

    SharedPreferences sp;
    SharedPreferences.Editor editor;
    Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Toolbar toolbar = findViewById(R.id.toolbar);
        address = findViewById(R.id.address);
        city = findViewById(R.id.city);
        distance = findViewById(R.id.distance);
        balanceText = findViewById(R.id.balanceText);

        drinks = new ArrayList<Item>();
        foods = new ArrayList<Item>();
        snacks = new ArrayList<Item>();

        sp = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        balance = sp.getInt("balance", -1000);

        if (balance == -1000) {
            editor = sp.edit();
            editor.putInt("balance", 0);
            editor.apply();
        }

        balanceText.setText("Balace: IDR " + balance);

        setSupportActionBar(toolbar);

        intent = getIntent();

        restaurant = (Restaurant) intent.getSerializableExtra("restaurant");
        latitude = intent.getDoubleExtra("latitude", 0.0);
        longitude = intent.getDoubleExtra("longitude", 0.0);
        dist = intent.getDoubleExtra("distance", 0.0);

        geocoder = new Geocoder(this,Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);

            String geo_address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String geo_city = addresses.get(0).getLocality();
            String geo_state = addresses.get(0).getAdminArea();
            String geo_country = addresses.get(0).getCountryName();
            String geo_postalCode = addresses.get(0).getPostalCode();
            String geo_knownName = addresses.get(0).getFeatureName();

            address.setText(geo_address);
            city.setText(geo_city + ", " + geo_state);
            distance.setText("Distance: " + dist.toString().substring(0,6) + "KM from restaurant.");
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Geocoder error!", Toast.LENGTH_LONG).show();
        }

        editor = sp.edit();
        String addrString = addresses.get(0).getLocality() + " - " + addresses.get(0).getAdminArea();
        editor.putString("address",  addrString);
        editor.apply();

        setTitle("Order from " + restaurant.getName());

        String content = sp.getString(restaurant.getName() + "_foods", null);

        if (!(content == null)) {
            Log.d("contenthaloo", content);
            Type type = new TypeToken<ArrayList<Item>>() {}.getType();
            foods = gson.fromJson(content, type);
            for (Item item : foods) {
                Log.d("isiarray", item.getName());
            }
        }

        content = sp.getString(restaurant.getName() + "_drinks", null);

        if (!(content == null)) {
            Log.d("contenthaloo", content);
            Type type = new TypeToken<ArrayList<Item>>() {}.getType();
            drinks = gson.fromJson(content, type);
            for (Item item : drinks) {
                Log.d("isiarray", item.getName());
            }
        }

        content = sp.getString(restaurant.getName() + "_snacks", null);

        if (!(content == null)) {
            Log.d("contenthaloo", content);
            Type type = new TypeToken<ArrayList<Item>>() {}.getType();
            snacks = gson.fromJson(content, type);
            for (Item item : snacks) {
                Log.d("isiarray", item.getName());
            }
        }

        database = FirebaseDatabase.getInstance();
        myRefDrink = database.getReference("menu/drink");

        if (drinks.isEmpty()) {
            listenerDrink = myRefDrink.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    for (DataSnapshot item : dataSnapshot.getChildren()) {
                        Item newItem = new Item();
                        newItem.setName(item.getKey());
                        newItem.setPrice(item.child("price").getValue(Integer.class));
                        newItem.setStock(item.child("stock").child(restaurant.getName()).getValue(Integer.class));
                        newItem.setType("drink");

                        Log.d("name", newItem.getName());
                        Log.d("price", newItem.getPrice().toString());
                        Log.d("stock", newItem.getStock().toString());
                        Log.d("stock", newItem.getType());

                        drinks.add(newItem);
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w("oncancelled", "Failed to read value.", error.toException());
                }
            });
        }


        database = FirebaseDatabase.getInstance();
        myRefFood = database.getReference("menu/food");

        if (foods.isEmpty()) {
            listenerFood = myRefFood.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    for (DataSnapshot item : dataSnapshot.getChildren()) {
                        Item newItem = new Item();
                        newItem.setName(item.getKey());
                        newItem.setPrice(item.child("price").getValue(Integer.class));
                        newItem.setStock(item.child("stock").child(restaurant.getName()).getValue(Integer.class));
                        newItem.setType("food");

                        Log.d("name", newItem.getName());
                        Log.d("price", newItem.getPrice().toString());
                        Log.d("stock", newItem.getStock().toString());
                        Log.d("stock", newItem.getType());

                        foods.add(newItem);
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w("oncancelled", "Failed to read value.", error.toException());
                }
            });
        }


        database = FirebaseDatabase.getInstance();
        myRefSnack = database.getReference("menu/snack");

        if (snacks.isEmpty()) {
            listenerSnack = myRefSnack.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    for (DataSnapshot item : dataSnapshot.getChildren()) {
                        Item newItem = new Item();
                        newItem.setName(item.getKey());
                        newItem.setPrice(item.child("price").getValue(Integer.class));
                        newItem.setStock(item.child("stock").child(restaurant.getName()).getValue(Integer.class));
                        newItem.setType("snack");

                        Log.d("name", newItem.getName());
                        Log.d("price", newItem.getPrice().toString());
                        Log.d("stock", newItem.getStock().toString());
                        Log.d("stock", newItem.getType());

                        snacks.add(newItem);
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Failed to read value
                    Log.w("oncancelled", "Failed to read value.", error.toException());
                }
            });
        }


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "  ", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                saveToPreferences();
                intent = new Intent(MenuActivity.this, ViewCart.class);
                Bundle bundle = getIntent().getExtras();
                intent.putExtra("restaurant", restaurant);
                String addrString = gson.toJson(addresses);
                intent.putExtra("address", addrString);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onPause() {
        finish();
        super.onPause();
    }

    public void getDrinksList(View view) {
        saveToPreferences();
        Intent intent = new Intent(MenuActivity.this, ViewCategoryActivity.class);
        intent.putExtra("type", "drink");
        intent.putExtra("restaurant", restaurant);
        Bundle bundle = getIntent().getExtras();
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void getFoodsList(View view) {
        saveToPreferences();
        Intent intent = new Intent(MenuActivity.this, ViewCategoryActivity.class);
        intent.putExtra("type", "food");
        intent.putExtra("restaurant", restaurant);
        Bundle bundle = getIntent().getExtras();
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public  void getSnacksList(View view) {
        saveToPreferences();
        Intent intent = new Intent(MenuActivity.this, ViewCategoryActivity.class);
        intent.putExtra("type", "snack");
        intent.putExtra("restaurant", restaurant);
        Bundle bundle = getIntent().getExtras();
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void saveToPreferences() {
        String json = gson.toJson(drinks);
        Log.d("drinks_to_json", restaurant.getName()+" : "+json);

        editor = sp.edit();
        editor.putString(restaurant.getName() + "_drinks", json);
        editor.apply();

        String drink_ = sp.getString(restaurant.getName()+"_drinks", "not found");

        Type type = new TypeToken<ArrayList<Item>>() { }.getType();
        ArrayList<Item> arrPackageData = gson.fromJson(drink_, type);

        Log.d("hallo_drinks", arrPackageData.get(0).getName());


        json = gson.toJson(foods);
        Log.d("foods_to_json", restaurant.getName()+" : "+json);

        editor = sp.edit();
        editor.putString(restaurant.getName() + "_foods", json);
        editor.apply();

        String food_ = sp.getString(restaurant.getName()+"_foods", "not found");

        type = new TypeToken<ArrayList<Item>>() { }.getType();
        arrPackageData = gson.fromJson(food_, type);

        Log.d("hallo_foods", arrPackageData.get(0).getName());


        json = gson.toJson(snacks);
        Log.d("snacks_to_json", restaurant.getName()+" : "+json);

        editor = sp.edit();
        editor.putString(restaurant.getName() + "_snacks", json);
        editor.apply();

        String snack_ = sp.getString(restaurant.getName()+"_snacks", "not found");

        type = new TypeToken<ArrayList<Item>>() { }.getType();
        arrPackageData = gson.fromJson(snack_, type);

        Log.d("hallo_snacks", arrPackageData.get(0).getName());
    }

    public void topUp(View view) {
        Intent intent = new Intent(this, TopupActivity.class);
        Bundle bundle = getIntent().getExtras();
        intent.putExtra("balance", balance);
        intent.putExtra("restaurant", restaurant);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}