package com.example.nhom_19;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    String CITY = "ho chi minh,vn";
    String API = "d6e62622c3c065c17312ae9b9940f532";
    Button button1,button2,buttonsearch,buttonmenu,buttonfavorite;
    TextView addressTxt, updated_atTxt, statusTxt, tempTxt, temp_minTxt, temp_maxTxt, sunriseTxt,
            sunsetTxt, windTxt, pressureTxt, humidityTxt,decription;
    ImageView imageView;
    LinearLayout DetailsContainer;
    DBHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences preferences=getApplicationContext().getSharedPreferences("citytemp",0);
        SharedPreferences.Editor editor = preferences.edit();
        CITY=preferences.getString("city","ho chi minh,vn");
        addressTxt = findViewById(R.id.address);
        updated_atTxt = findViewById(R.id.updated_at);
        statusTxt = findViewById(R.id.status);
        tempTxt = findViewById(R.id.temp);
        temp_minTxt = findViewById(R.id.temp_min);
        temp_maxTxt = findViewById(R.id.temp_max);
        sunriseTxt = findViewById(R.id.sunrise);
        sunsetTxt = findViewById(R.id.sunset);
        windTxt = findViewById(R.id.wind);
        pressureTxt = findViewById(R.id.pressure);
        humidityTxt = findViewById(R.id.humidity);
        decription=findViewById(R.id.shortdecript);
        DetailsContainer=findViewById(R.id.detailsContainer);
        imageView=findViewById(R.id.dayornight);
        button1=findViewById(R.id.MoreButton);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    DetailsContainer.setVisibility(View.VISIBLE);
                    button1.setVisibility(View.GONE);
                    button2.setVisibility(View.VISIBLE);
                    decription.setVisibility(View.GONE);
                    imageView.setVisibility(View.GONE);
            }
        });
        button2=findViewById(R.id.LessButton);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetailsContainer.setVisibility(View.GONE);
                button2.setVisibility(View.GONE);
                button1.setVisibility(View.VISIBLE);
                decription.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.VISIBLE);
            }
        });
        buttonsearch=findViewById(R.id.search_button);
        buttonsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,SearchActivity.class);
                startActivity(intent);
            }
        });
        buttonmenu=findViewById(R.id.menu_button);
        buttonmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,MenuActivity.class);
                editor.putString("city",CITY);
                editor.putString("temp",tempTxt.getText().toString());
                editor.putString("time",updated_atTxt.getText().toString());
                editor.putString("cloud",statusTxt.getText().toString());
                editor.apply();
                startActivity(intent);
            }
        });
        dbHelper = new DBHelper(this);
        buttonfavorite=findViewById(R.id.favorite_button);
        //Log.d("ass", "onCreate: "+preferences.getString("favorite","no")+preferences.getString("favorite","no").toString().compareTo("favorite"));
        String issearched=preferences.getString("searched","no");
        if(issearched.compareTo("yes")==0) {
            Integer pointer=CITY.indexOf(",");
            String cityname=CITY.substring(0,pointer);
            String countrycode=CITY.substring(pointer+1,CITY.length());
            Log.d("ass", "search "+cityname+countrycode);
            City city = new City(cityname,countrycode);
            if(dbHelper.insertHis(city) > 0){
                Toast.makeText(MainActivity.this, "OKEEEE", Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(MainActivity.this, "ERRRRORRRR", Toast.LENGTH_SHORT).show();
            }
            editor.putString("searched","no");
            editor.apply();
        }
            //buttonfavorite.setBackgroundResource(R.drawable.heart);
        buttonfavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer pointer=CITY.indexOf(",");
                String cityname=CITY.substring(0,pointer);
                String countrycode=CITY.substring(pointer+1,CITY.length());
                Log.d("ass", "onClick: "+pointer.toString()+cityname+countrycode);
                City city = new City(cityname,countrycode);
                if(dbHelper.insert(city) > 0){
                    Toast.makeText(MainActivity.this, "OKEEEE", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(MainActivity.this, "ERRRRORRRR", Toast.LENGTH_SHORT).show();
                }
            }
        });
        new weatherTask().execute();


    }

    class weatherTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /* Showing the ProgressBar, Making the main design GONE */
            findViewById(R.id.loader).setVisibility(View.VISIBLE);
            findViewById(R.id.mainContainer).setVisibility(View.GONE);
            findViewById(R.id.errorText).setVisibility(View.GONE);
        }

        protected String doInBackground(String... args) {
            SharedPreferences preferences=getApplicationContext().getSharedPreferences("citytemp",0);
            SharedPreferences.Editor editor = preferences.edit();
            String check= preferences.getString("latlon","false");
            String lat=preferences.getString("lat","10.75");
            String lon=preferences.getString("lon","106.6667");
            String response;
            if(check=="true")  {response= HttpRequest.excuteGet("https://api.openweathermap.org/data/2.5/weather?lat=" +lat+"&lon=" +lon +"&units=metric&appid=" + API);
            editor.putString("latlon","false");
            editor.apply();
            }
            else response = HttpRequest.excuteGet("https://api.openweathermap.org/data/2.5/weather?q=" + CITY + "&units=metric&appid=" + API);
            Log.d("ass", "doInBackground: "+response);
            if(response==null) return "";else return response;
        }

        @Override
        protected void onPostExecute(String result) {


            try {
                JSONObject jsonObj = new JSONObject(result);
                JSONObject main = jsonObj.getJSONObject("main");
                JSONObject sys = jsonObj.getJSONObject("sys");
                JSONObject wind = jsonObj.getJSONObject("wind");
                JSONObject weather = jsonObj.getJSONArray("weather").getJSONObject(0);
                Long updatedAt = jsonObj.getLong("dt");
                String updatedAtText = "Updated at: " + new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(new Date(updatedAt * 1000));
                String temp = main.getString("temp") + "°C";
                String tempMin = "Min Temp: " + main.getString("temp_min") + "°C";
                String tempMax = "Max Temp: " + main.getString("temp_max") + "°C";
                String pressure = main.getString("pressure");
                String humidity = main.getString("humidity");
                String tam=updatedAtText.substring(29,30);

                String Decription="Today";
                if(main.getDouble("temp")<0.0)
                {Decription+=" s ice cold ( VANILLA ICE cold )";getResources().getColor(R.color.red);}
                else if(main.getDouble("temp")<10.0)
                    Decription+=" is very cold";
                else if(main.getDouble("temp")<20.0)
                    Decription+=" is cold";
                else if(main.getDouble("temp")<30.0)
                    Decription+=" is warm";
                else if(main.getDouble("temp")<40.0)
                    Decription+=" is hot";
                else {Decription+=", We all burn in hell";getResources().getColor(R.color.red);}
                Decription+=", ";
                Long sunrise = sys.getLong("sunrise");
                Long sunset = sys.getLong("sunset");
                String windSpeed = wind.getString("speed");
                String weatherDescription = weather.getString("description");

                if(tam.compareTo("A")==0) {if(weatherDescription.compareTo("clear sky")==0) imageView.setImageResource(R.drawable.sun);else imageView.setImageResource(R.drawable.sunwithcloud);}
                else {if(weatherDescription.compareTo("clear sky")==0) imageView.setImageResource(R.drawable.moon);else imageView.setImageResource(R.drawable.moonwithcloud);}

                if(wind.getDouble("speed")<0.27)
                    Decription+="calm";
                else if(wind.getDouble("speed")<1.38)
                    Decription+="light air";
                else if(wind.getDouble("speed")<3.55)
                    Decription+="light breeze";
                else if(wind.getDouble("speed")<5.27)
                    Decription+="gentle breeze";
                else if(wind.getDouble("speed")<7.77)
                    Decription+="moderate breeze";
                else if(wind.getDouble("speed")<10.55)
                    Decription+="fresh Breeze";
                else if(wind.getDouble("speed")<13.61)
                    Decription+="strong Breeze";
                else if(wind.getDouble("speed")<16.94)
                    Decription+="near gale";
                else if(wind.getDouble("speed")<20.55)
                {Decription+="gale";decription.setTextColor(getResources().getColor(R.color.red));}
                else if(wind.getDouble("speed")<24.44)
                {Decription+="strong gale";decription.setTextColor(getResources().getColor(R.color.red));}
                else if(wind.getDouble("speed")<28.33)
                {Decription+="storm";decription.setTextColor(getResources().getColor(R.color.red));}
                else if(wind.getDouble("speed")<32.5)
                {Decription+="violent Storm";decription.setTextColor(getResources().getColor(R.color.red));}
                else {Decription+="hurricane ( better run for your life )";decription.setTextColor(getResources().getColor(R.color.red));}
                String address = jsonObj.getString("name") + ", " + sys.getString("country");
                Decription+=" and "+weatherDescription;
                Log.d("ass", "onPostExecute: "+Decription);
                /* Populating extracted data into our views */
                decription.setText(Decription);
                addressTxt.setText(address);
                updated_atTxt.setText(updatedAtText);
                statusTxt.setText(weatherDescription.toUpperCase());
                tempTxt.setText(temp);
                temp_minTxt.setText(tempMin);
                temp_maxTxt.setText(tempMax);
                sunriseTxt.setText(new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date(sunrise * 1000)));
                sunsetTxt.setText(new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date(sunset * 1000)));
                windTxt.setText(windSpeed);
                pressureTxt.setText(pressure);
                humidityTxt.setText(humidity);
                SharedPreferences preferences=getApplicationContext().getSharedPreferences("citytemp",0);
                SharedPreferences.Editor editor = preferences.edit();
                CITY=address;
                editor.putString("city",address);
                editor.putString("temp",temp);
                editor.apply();
                /* Views populated, Hiding the loader, Showing the main design */
                findViewById(R.id.loader).setVisibility(View.GONE);
                findViewById(R.id.mainContainer).setVisibility(View.VISIBLE);

            } catch (JSONException e) {
                findViewById(R.id.loader).setVisibility(View.GONE);
                findViewById(R.id.errorText).setVisibility(View.VISIBLE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        SharedPreferences preferences=getApplicationContext().getSharedPreferences("citytemp",0);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("city","ho chi minh,vn");
                        editor.apply();
                        Intent intent = new Intent(MainActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }, 5000);
            }

        }

    }
}