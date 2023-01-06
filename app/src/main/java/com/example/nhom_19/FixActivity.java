package com.example.nhom_19;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class FixActivity extends AppCompatActivity {
    TextView CityName,CountryName,Des;
    Button button;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fix);
        dbHelper = new DBHelper(this);
        CityName=findViewById(R.id.cityname);
        CountryName=findViewById(R.id.countryname);
        Des=findViewById(R.id.countrydes);
        Intent intent=getIntent();
        City city= (City) intent.getSerializableExtra("item");
        Log.d("honk", "onCreate: "+city.toString());
        CityName.setText(city.name.toUpperCase());
        CountryName.setText(city.country.toUpperCase());
        Des.setText(city.description);
        button=findViewById(R.id.savebutton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 city.description=Des.getText().toString();

                 if(dbHelper.update(city)==0) Log.d("honk", "onCreate: "+city.toString());
                 Intent intent1 = new Intent(FixActivity.this, ContainerActivity.class);
                 startActivity(intent1);
            }
        });
    }
}