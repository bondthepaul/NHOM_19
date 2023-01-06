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

public class SearchContainerActivity extends AppCompatActivity implements CitySearchAdapter.Listener{
    RecyclerView rcCity;
    ArrayList<City> arrayList;
    CitySearchAdapter cityAdapter;
    DBHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_container);
        dbHelper = new DBHelper(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        rcCity = findViewById(R.id.recyclerview);
        arrayList = dbHelper.getCitiesHistory();
        cityAdapter = new CitySearchAdapter(arrayList, SearchContainerActivity.this);
        rcCity.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        DividerItemDecoration dividerItemDecoration=new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        dividerItemDecoration.setDrawable(getResources().getDrawable(R.drawable.line1));
        rcCity.addItemDecoration(dividerItemDecoration);
        rcCity.setAdapter(cityAdapter);
    }

    @Override
    public void onClick(City city) {
        Intent intent = new Intent(SearchContainerActivity.this, MainActivity.class);
        String tmp=city.name+","+city.country;
        SharedPreferences preferences=getApplicationContext().getSharedPreferences("citytemp",0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("city",tmp);
        editor.putString("favorite","favorite");
        editor.apply();
        Log.d("ass", "onClick: "+tmp);
        startActivity(intent);
    }

    @Override
    public void onClickDelete(City city) {
        if(dbHelper.deleteHis(city.id) > 0){
            arrayList.clear();
            arrayList.addAll(dbHelper.getCitiesHistory());
            cityAdapter.notifyDataSetChanged();
            Toast.makeText(this, "Successfully deleted", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(this, "OERAOWAODRAEOA", Toast.LENGTH_SHORT).show();
        }
    }
}