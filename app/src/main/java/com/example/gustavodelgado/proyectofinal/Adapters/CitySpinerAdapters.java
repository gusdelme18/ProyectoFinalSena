package com.example.gustavodelgado.proyectofinal.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.gustavodelgado.proyectofinal.Model.CityModel;
import com.example.gustavodelgado.proyectofinal.R;

import java.util.ArrayList;

/**
 * Created by gustavodelgado on 28/06/17.
 */

public class CitySpinerAdapters extends ArrayAdapter<CityModel> {

    public CitySpinerAdapters(Context context, ArrayList<CityModel> city) {
        super(context, R.layout.items_spiner_city, city);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        CityModel city = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.items_spiner_city, parent, false);
        }
        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.txtNameCity);
        // Populate the data into the template view using the data object
        tvName.setText(city.getName());
        // Return the completed view to render on screen
        return convertView;
    }
}
