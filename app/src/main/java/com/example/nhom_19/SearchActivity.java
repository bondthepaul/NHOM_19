package com.example.nhom_19;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class SearchActivity extends AppCompatActivity {
    EditText edtCity,edtCountry;
    Button button,backbutton,menubutton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        edtCity=findViewById(R.id.cityname);
        edtCountry=findViewById(R.id.countyname);
        button=findViewById(R.id.searchbutton);
        backbutton=findViewById(R.id.back_button);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        menubutton=findViewById(R.id.menu_button);
        menubutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchActivity.this,SearchContainerActivity.class);
                startActivity(intent);
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchActivity.this, MainActivity.class);
                String tmp=edtCity.getText().toString().toLowerCase()+","+edtCountry.getText().toString().toLowerCase();
                SharedPreferences preferences=getApplicationContext().getSharedPreferences("citytemp",0);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("city",tmp);
                editor.putString("searched","yes");
                editor.apply();
                Log.d("ass", "onClick: "+tmp);
                startActivity(intent);
            }
        });
    }
}