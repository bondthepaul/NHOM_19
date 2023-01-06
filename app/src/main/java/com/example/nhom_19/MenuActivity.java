package com.example.nhom_19;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

public class MenuActivity extends AppCompatActivity {
    LinearLayout linearLayout1, linearLayout2;
    Button button;
    ImageView imageView;
    TextView txtCity, txtTemp;
    private static final int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("citytemp", 0);
        SharedPreferences.Editor editor = preferences.edit();
        imageView = findViewById(R.id.imgweather);
        txtCity = findViewById(R.id.cityname);
        txtTemp = findViewById(R.id.temp);
        txtCity.setText(preferences.getString("city", "ho chi minh,vn"));
        txtTemp.setText(preferences.getString("temp", "00.00"));
        String updatedAtText = preferences.getString("time", "A");
        String weatherDescription = preferences.getString("cloud", "clear sky");
        String tam = updatedAtText.substring(29, 30);
        if (tam.compareTo("A") == 0) {
            if (weatherDescription.compareTo("clear sky") == 0)
                imageView.setImageResource(R.drawable.sun);
            else imageView.setImageResource(R.drawable.sunwithcloud);
        } else {
            if (weatherDescription.compareTo("clear sky") == 0)
                imageView.setImageResource(R.drawable.moon);
            else imageView.setImageResource(R.drawable.moonwithcloud);
        }
        linearLayout1 = findViewById(R.id.linearLayout1);
        linearLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, ContainerActivity.class);
                startActivity(intent);
            }
        });
        linearLayout2 = findViewById(R.id.linearLayout2);
        linearLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    getCurrentLocation();
            }
        });
        button = findViewById(R.id.back_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MenuActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                getCurrentLocation();
                Log.d("loc", "onRequestPermissionsResult: wtf");
                Intent intent = new Intent(MenuActivity.this, MainActivity.class);
                startActivity(intent);
            }
        }
    }

    private void getCurrentLocation() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(locationRequest.PRIORITY_HIGH_ACCURACY);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MenuActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
        }
        LocationServices.getFusedLocationProviderClient(MenuActivity.this)
                .requestLocationUpdates(locationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        LocationServices.getFusedLocationProviderClient(MenuActivity.this).removeLocationUpdates(this);
                        if (locationResult != null && locationResult.getLocations().size() > 0) {
                            int latest = locationResult.getLocations().size() - 1;
                            double lat = locationResult.getLocations().get(latest).getLatitude();
                            double lon = locationResult.getLocations().get(latest).getLongitude();
                            Log.d("loc", "onLocationResult: " + lat + " " + lon);
                            SharedPreferences preferences=getApplicationContext().getSharedPreferences("citytemp",0);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("latlon","true");
                            editor.putString("lat", String.valueOf(lat));
                            editor.putString("lon", String.valueOf(lon));
                            editor.apply();
                            Intent intent = new Intent(MenuActivity.this, MainActivity.class);
                            startActivity(intent);
                        }
                    }
                }, Looper.getMainLooper());
    }
}