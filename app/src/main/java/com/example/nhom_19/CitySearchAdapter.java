package com.example.nhom_19;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CitySearchAdapter extends RecyclerView.Adapter<CitySearchAdapter.CityVH> {
    ArrayList<City> arrayList;
    Listener listener;
    public CitySearchAdapter(ArrayList<City> arrayList, Listener listener){
        this.arrayList=arrayList;this.listener=listener;
    }
    @NonNull
    @Override
    public CityVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_itemsearch, parent, false);

        return new CityVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CityVH holder, int position) {
        City city = arrayList.get(position);
        holder.txtName.setText(city.name.toUpperCase());
        holder.txtCountry.setText(city.country.toUpperCase());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(city);
            }
        });
        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClickDelete(city);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(arrayList==null) return 0;else return arrayList.size();
    }

    class CityVH extends RecyclerView.ViewHolder{
        TextView txtName, txtCountry;
        ImageView imgDelete;
        public CityVH(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtCityname);
            txtCountry = itemView.findViewById(R.id.txtCounrtycode);
            imgDelete = itemView.findViewById(R.id.imgDelete);
        }
    }
    interface Listener{
        void onClick(City city); //view
        void onClickDelete(City city);
    }
}
