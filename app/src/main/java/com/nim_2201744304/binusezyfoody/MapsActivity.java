package com.nim_2201744304.binusezyfoody;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

@SuppressWarnings("ALL")
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnInfoWindowClickListener {

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    FirebaseDatabase database;
    DatabaseReference myRef;
    public ArrayList<Restaurant> restaurantArrayList = new ArrayList<Restaurant>();
    Location mLastLocation;
    Marker mCurrLocationMarker;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    Boolean isCentered = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("locations");

        if (ContextCompat.checkSelfPermission(MapsActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(MapsActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)){
                ActivityCompat.requestPermissions(MapsActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }else{

            }
        } else {

        }

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                for (DataSnapshot restaurantDataSnapshot : dataSnapshot.getChildren()) {
                    float latitude = restaurantDataSnapshot.child("latitude").getValue(float.class);
                    float longitude = restaurantDataSnapshot.child("longitude").getValue(float.class);
                    String name = restaurantDataSnapshot.child("name").getValue(String.class);
//                    Toast.makeText(MapsActivity.this, latitude, Toast.LENGTH_SHORT).show();
                    Log.d("latitude", Float.toString(latitude));
                    Log.d("longitude", Float.toString(longitude));
                    Log.d("name", name);

                    Restaurant restaurant = new Restaurant(latitude, longitude, name);
                    restaurantArrayList.add(restaurant);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("oncancelled", "Failed to read value.", error.toException());
            }
        });

        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Toast.makeText(this, "Jika marker tidak muncul, mohon restart aplikasi.", Toast.LENGTH_LONG).show();
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        setMarker(); Log.d("onRestart", "Line 114");
//        LatLng latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude()); mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
//        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
//        isCentered = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
//        isCentered = false; Log.d("onPause", "Line 121");
    }

    @Override
    protected void onResume() {
        super.onResume();
        setMarker();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        switch (requestCode){
            case 1: {
                if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if (ContextCompat.checkSelfPermission(MapsActivity.this,
                            Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();

                        finish();
                        setMarker();
                        startActivity(getIntent());
                    }
                }else{
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnInfoWindowClickListener(this);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        setMarker();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            buildGoogleApiClient();
//            mMap.setMyLocationEnabled(true);
            ActivityCompat.requestPermissions(MapsActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        } else {
            buildGoogleApiClient();
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            mMap.setMyLocationEnabled(true);
        }

//        Location currentLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
//        LocationManager lm = (LocationManager) ContextCompat.getSystemService(this, LocationManager.class);
//        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null){
                    mLastLocation = location;
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    LatLng latLng = new LatLng(latitude,longitude);
                    Log.d("getlastlocation", "MyLastLocation coordinat :"+latLng);

                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(latLng);
                    markerOptions.title("Your Position");
                    markerOptions.snippet("Click to order from nearest restaurant.");
                    markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                    mCurrLocationMarker = mMap.addMarker(markerOptions);

                    //move map camera
                    if(!isCentered) {
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
                        isCentered = true;
                        Log.d("onSuccess @ onMapReady", "Line 220");
                    }

                    for (int i = 0; i < restaurantArrayList.size(); i++) {
                        LatLng loc = new LatLng(restaurantArrayList.get(i).getLatitude(), restaurantArrayList.get(i).getLongitude());
                        mMap.addMarker(new MarkerOptions().position(loc).title(restaurantArrayList.get(i).getName()).snippet("Click to order from this restaurant."));
                    }
                }
            }
        }).addOnFailureListener(this, new OnFailureListener() {

            @Override
            public void onFailure(@NonNull Exception e) {
                setMarker();
            }
        });

//        LatLng lastKnown = new LatLng(location.getLatitude(), location.getLongitude());
//        Log.d("lastKnown", Double.toString(lastKnown.latitude)+Double.toString(lastKnown.longitude));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(lastKnown));
    }

    @Override
    public void onLocationChanged(Location location) {

        mLastLocation = location;

        //Place current location marker
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Your Position");
        markerOptions.snippet("Click me to order from nearest restaurant.");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.setPosition(latLng);
        } else {
            mCurrLocationMarker = mMap.addMarker(markerOptions);
        }

        //move map camera
        if (!isCentered) {
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
            isCentered = true;
            Log.d("onLocationChanged", "Line 265");
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

            setMarker();

            LatLng latLng;

            if (mLastLocation!=null) {
                latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            } else {
                latLng = new LatLng(-6.1754, 106.8272);
            }
//            MarkerOptions markerOptions = new MarkerOptions();
//            markerOptions.position(latLng);
//            markerOptions.title("Current Position");
//            markerOptions.snippet("Click me to order from nearest restaurant.");
//            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
//            mCurrLocationMarker = mMap.addMarker(markerOptions);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
            Log.d("onConnected", "Line 306");
        }

        setMarker();
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Intent intent = new Intent(MapsActivity.this, MenuActivity.class);
        String title = marker.getTitle();
        if (title.contains("EzyFood")) { // if it is a marker restaurant
            intent.putExtra("restaurantName", title);
            intent.putExtra("latitude", mLastLocation.getLatitude());
            intent.putExtra("longitude", mLastLocation.getLongitude());
            Restaurant clickedRestaurant = restaurantArrayList.get(0);
            for (Restaurant rest : restaurantArrayList) {
                if(rest.getName().equals(title)) {
                    clickedRestaurant = rest;
                    break;
                }
            }

            Double distance = distance(clickedRestaurant.getLatitude(), mLastLocation.getLatitude(),
                    clickedRestaurant.getLongitude(), mLastLocation.getLongitude());

            intent.putExtra("distance", distance);
            intent.putExtra("restaurant", clickedRestaurant);

            startActivity(intent);

        } else if (title.equals("Your Position") || title.equals("Current Position")) {
            orderFromNearest(null);
        } else{
            Log.d("onInfoWindowClick", "this is if title contains something else");
        }
    }

    public void orderFromNearest(View view) {
        Restaurant nearest = restaurantArrayList.get(0);
        Double minDistance = 9999999.0;
        for (Restaurant restaurant : restaurantArrayList) {
            float lat1 = restaurant.getLatitude();
            double lat2 = mLastLocation.getLatitude();
            float long1 = restaurant.getLongitude();
            double long2 = mLastLocation.getLongitude();

            Double distance = distance(lat1, lat2, long1, long2);

            if (distance < minDistance) {
                minDistance = distance;
                nearest = restaurant;
            }
        }

        Intent intent = new Intent(MapsActivity.this, MenuActivity.class);
        intent.putExtra("restaurantName", nearest.getName());
        intent.putExtra("latitude", mLastLocation.getLatitude());
        intent.putExtra("longitude", mLastLocation.getLongitude());
        intent.putExtra("restaurant", nearest);
        intent.putExtra("distance", minDistance);
        startActivity(intent);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        setMarker();
    }

    public void setMarker() {
        for (int i = 0; i < restaurantArrayList.size(); i++) {
            LatLng location = new LatLng(restaurantArrayList.get(i).getLatitude(), restaurantArrayList.get(i).getLongitude());
            mMap.addMarker(new MarkerOptions().position(location).title(restaurantArrayList.get(i).getName()).snippet("Click to order from this restaurant."));
        }
    }

    public static double distance(double lat1, double lat2, double lon1, double lon2) {

        lon1 = Math.toRadians(lon1);
        lon2 = Math.toRadians(lon2);
        lat1 = Math.toRadians(lat1);
        lat2 = Math.toRadians(lat2);

        // Haversine formula
        double dlon = lon2 - lon1;
        double dlat = lat2 - lat1;
        double a = Math.pow(Math.sin(dlat / 2), 2) + Math.cos(lat1) * Math.cos(lat2)
                * Math.pow(Math.sin(dlon / 2),2);

        double c = 2 * Math.asin(Math.sqrt(a));

        // Radius of earth in kilometers. Use 3956 for miles
        double r = 6371;

        // calculate the result
        return(c * r);
    }

    public void viewOrderHistory(View view) {
        Intent intent = new Intent(MapsActivity.this, ViewOrderHistory.class);
//        Bundle bundle = getIntent().getExtras();
//        intent.putExtras(bundle);
        startActivity(intent);
    }
}