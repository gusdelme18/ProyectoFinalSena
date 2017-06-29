package com.example.gustavodelgado.proyectofinal;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gustavodelgado.proyectofinal.Adapters.CityAdatpters;
import com.example.gustavodelgado.proyectofinal.Helpers.MyApplication;
import com.example.gustavodelgado.proyectofinal.Model.CityModel;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity  {

    DatabaseReference dref;
    ListView listview;
    ArrayList<CityModel> cityModelArrayList = new ArrayList<CityModel>();



    CityAdatpters cityAdatpters = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Manually checking internet connection
       // checkConnection();


//        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        listview = (ListView) findViewById(R.id.ltvCity);

        //      final ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,list);
//        listview.setAdapter(adapter);

        // Construct the data source

        cityAdatpters = new CityAdatpters(this, cityModelArrayList);
        listview.setAdapter(cityAdatpters);

        //INITIALIZE FIREBASE DB
        dref = FirebaseDatabase.getInstance().getReference("city");


        dref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                CityModel cityModel = dataSnapshot.getValue(CityModel.class);
                cityModelArrayList.add(cityModel);
                cityAdatpters.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                CityModel cityModel = dataSnapshot.getValue(CityModel.class);
                cityModelArrayList.add(cityModel);
                cityAdatpters.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                cityModelArrayList.remove(dataSnapshot.getValue(CityModel.class));
                cityAdatpters.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                CityModel cityModel = cityModelArrayList.get(position);
//                Toast.makeText(getApplicationContext(), "Click en la posición "  + cityModel.getId(), Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(getApplicationContext(), DetalleCityActivity.class);
                intent.putExtra("idCity", "" + cityModel.getId());
                intent.putExtra("name", cityModel.getName());
                intent.putExtra("imagen", cityModel.getImagen());
                startActivity(intent);
            }
        });
    }


    protected boolean isOnline() {

        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        if (netInfo != null && netInfo.isConnectedOrConnecting()) {

            return true;

        } else {

            return false;

        }

    }

   /* public void checkConnection(){


        String message;
        int color;
        if (isOnline()) {
            message = "Good! Connected to Internet";
            color = Color.WHITE;
        } else {
            message = "Sorry! Not connected to internet";
            color = Color.RED;
        }

        Snackbar snackbar = Snackbar.make(coordinatorLayout, message, Snackbar.LENGTH_LONG);

        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(color);
        snackbar.show();

    }*/


}
