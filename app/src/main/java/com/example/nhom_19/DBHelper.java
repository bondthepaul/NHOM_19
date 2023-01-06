package com.example.nhom_19;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

public class DBHelper {
    String dbName = "City.db";
    SQLiteDatabase db;
    Context context;

    public DBHelper(Context context) {
        this.context = context;
    }

    public SQLiteDatabase openDB(){
        return context.openOrCreateDatabase(dbName, Context.MODE_PRIVATE, null);
    }

    public void closeDB(SQLiteDatabase db){
        db.close();
    }

    public void createTable(){
        db = openDB();
        String sqlCity = "CREATE TABLE IF NOT EXISTS tblCity(" +
                " ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                " NAME TEXT," +
                " COUNTRY TEXT," +
                " DESCRIPTION TEXT);";
        db.execSQL(sqlCity);
        String sqlHistory = "CREATE TABLE IF NOT EXISTS tblHistory(" +
                " ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                " NAME TEXT," +
                " COUNTRY TEXT);";
        db.execSQL(sqlHistory);
        db.close();
    }
    public ArrayList<City> getCitiesHistory(){
        ArrayList<City> tmp = new ArrayList<>();
        db = openDB();
        String sql = "Select * From tblHistory";
        Cursor cursor = db.rawQuery(sql,null);
        if(cursor.moveToFirst()){
            do{
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String country = cursor.getString(2);

                tmp.add(new City(id, name, country));
            }while (cursor.moveToNext());
        }

        closeDB(db);
        return tmp;
    }
    public ArrayList<City> getCities(){
        ArrayList<City> tmp = new ArrayList<>();
        db = openDB();
        String sql = "Select * From tblCity";
        Cursor cursor = db.rawQuery(sql,null);
        if(cursor.moveToFirst()){
            do{
                int id = cursor.getInt(0);
                String name = cursor.getString(1);
                String country = cursor.getString(2);
                String description = cursor.getString(3);
                tmp.add(new City(id, name, country,description));
            }while (cursor.moveToNext());
        }

        closeDB(db);
        return tmp;
    }
    public long insertHis(City city){

        if(!checkdup("history",city.name))
        {
            db = openDB();
        ContentValues contentValues = new ContentValues();
        contentValues.put("NAME", city.name.toLowerCase());
        contentValues.put("COUNTRY", city.country.toLowerCase());
        long tmp = db.insert("tblHistory", null, contentValues);
        closeDB(db);
        return tmp;}
        return 0;
    }
    public long insert(City city){

        if(!checkdup("favorite",city.name)){
            db = openDB();
        ContentValues contentValues = new ContentValues();
        contentValues.put("NAME", city.name.toLowerCase());
        contentValues.put("COUNTRY", city.country.toLowerCase());
        long tmp = db.insert("tblCity", null, contentValues);
        closeDB(db);
        return tmp;
        }
        return 0;
    }

    public long update(City city){
        db = openDB();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ID", city.id);
        contentValues.put("NAME", city.name.toLowerCase());
        contentValues.put("COUNTRY", city.country.toLowerCase());
        contentValues.put("DESCRIPTION", city.description);
        long tmp = db.update("tblCity", contentValues, "ID="+city.id,null);
        Log.d("honk", "update: honk "+tmp);
        closeDB(db);
        return tmp;
    }
    public int deleteHis(int id){
        db = openDB();
        int tmp = db.delete("tblHistory","ID="+id, null);
        closeDB(db);
        return tmp;
    }
    public int delete(int id){
        db = openDB();
        int tmp = db.delete("tblCity","ID="+id, null);
        closeDB(db);
        return tmp;
    }
    public boolean checkdup(String which, String thing)
    {
        db = openDB();
        boolean tmp=false;
        if(which == "favorite")
        {
            String sql = "Select * From tblCity";
            Cursor cursor = db.rawQuery(sql,null);
            if(cursor.moveToFirst()){
                do{
                    String name = cursor.getString(1);
                    //Log.d("honk", "checkdup: "+thing+" "+name);
                    if(thing.toLowerCase().compareTo(name.toLowerCase())==0) {tmp=true;break;}
                }while (cursor.moveToNext());
            }
        }
        else
        {
            String sql = "Select * From tblHistory";
            Cursor cursor = db.rawQuery(sql,null);
            if(cursor.moveToFirst()){
                do{
                    String name = cursor.getString(1);
                    //Log.d("honk", "checkdup: "+thing+" "+name);
                    if(thing.toLowerCase().compareTo(name.toLowerCase())==0) {tmp=true;break;}
                }while (cursor.moveToNext());
            }
        }
        closeDB(db);
        return tmp;
    }
    public City getOneCity(int id){
        City tmp = new City();
        db = openDB();
        String sql = "Select * From tblCity where ID = " + id;
        Cursor cursor = db.rawQuery(sql,null);
        if(cursor.moveToFirst()){
            tmp.id = cursor.getInt(0);
            tmp.name= cursor.getString(1);
            tmp.country = cursor.getString(2);
        }

        closeDB(db);
        return tmp;
    }
}
