package com.example.nhom_19;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ContainerActivity extends AppCompatActivity implements CityAdapter.Listener{
    RecyclerView rcCity;
    ArrayList<City> arrayList;
    CityAdapter cityAdapter;
    DBHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);
        dbHelper = new DBHelper(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        rcCity = findViewById(R.id.recyclerview);
        arrayList = dbHelper.getCities();
        cityAdapter = new CityAdapter(arrayList, ContainerActivity.this);
        rcCity.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        DividerItemDecoration dividerItemDecoration=new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.line1));
        rcCity.addItemDecoration(dividerItemDecoration);
        rcCity.setAdapter(cityAdapter);
    }

    @Override
    public void onClick(City city) {
        Intent intent = new Intent(ContainerActivity.this,MainActivity.class);
        String tmp=city.name+","+city.country;
        SharedPreferences preferences=getApplicationContext().getSharedPreferences("citytemp",0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("city",tmp);
        editor.apply();
        Log.d("ass", "onClick: "+tmp);
        startActivity(intent);
    }

    @Override
    public void onClickDelete(City city) {
        if(dbHelper.delete(city.id) > 0){
            arrayList.clear();
            arrayList.addAll(dbHelper.getCities());
            cityAdapter.notifyDataSetChanged();
            Toast.makeText(this, "Successfully deleted", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "OERAOWAODRAEOA", Toast.LENGTH_SHORT).show();
        }
    }
    public void onClickFix(City city) {
        Intent intent = new Intent(ContainerActivity.this,FixActivity.class);
        intent.putExtra("item", city);
        startActivity(intent);
    }
}