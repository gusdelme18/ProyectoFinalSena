package com.example.gustavodelgado.proyectofinal;

import android.app.ProgressDialog;
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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gustavodelgado.proyectofinal.Adapters.CityAdatpters;
import com.example.gustavodelgado.proyectofinal.Adapters.CitySpinerAdapters;
import com.example.gustavodelgado.proyectofinal.Helpers.MyApplication;
import com.example.gustavodelgado.proyectofinal.Model.CityModel;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    DatabaseReference dref;
    ListView listview;
    ArrayList<CityModel> cityModelArrayList = new ArrayList<CityModel>();
    CitySpinerAdapters citySpinerAdapters = null;
    CityAdatpters cityAdatpters = null;
    Spinner spnCity;
    String idCity,NameCity,ImagCity;
    ProgressDialog progress = null;
    ArrayAdapter<CityModel> areasAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        // Manually checking internet connection
       // checkConnection();


//        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        progress = new ProgressDialog(this);
        progress.setMessage("Loading data");
        progress.show();
        //listview = (ListView) findViewById(R.id.ltvCity);
        spnCity = (Spinner) findViewById(R.id.spinnerCity);
        spnCity.setOnItemSelectedListener(this);





        //      final ArrayAdapter<String> adapter=new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,list);
//        listview.setAdapter(adapter);

        // Construct the data source

       // cityAdatpters = new CityAdatpters(this, cityModelArrayList);
        //listview.setAdapter(cityAdatpters);


       // citySpinerAdapters = new CitySpinerAdapters(this, cityModelArrayList);

        //spnCity.setAdapter(citySpinerAdapters);


        //INITIALIZE FIREBASE DB
        dref = FirebaseDatabase.getInstance().getReference("city");




        dref.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // Is better to use a List, because you don't know the size
                // of the iterator returned by dataSnapshot.getChildren() to
                // initialize the array
               final ArrayList<CityModel> areas = new ArrayList<CityModel>();

                for (DataSnapshot areaSnapshot: dataSnapshot.getChildren()) {
                    CityModel areaName = areaSnapshot.getValue(CityModel.class);
                    areas.add(areaName);
                }
                //areasAdapter = new ArrayAdapter<CityModel>(MainActivity.this, android.R.layout.simple_spinner_item, areas);
                //areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                citySpinerAdapters= new CitySpinerAdapters(getBaseContext(), areas);
                citySpinerAdapters.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spnCity.setAdapter(citySpinerAdapters);


                /* CityModel cityModel = dataSnapshot.getValue(CityModel.class);
                cityModelArrayList.add(cityModel);

                //areasAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_item, );
                cityAdatpters= new CityAdatpters(getBaseContext(), cityModelArrayList);
                cityAdatpters.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spnCity.setAdapter(cityAdatpters);
                */
                progress.dismiss();



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


       /* listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
        });*/
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

       CityModel cityModel = (CityModel) parent.getItemAtPosition(position);
        Toast.makeText(getBaseContext(), "# id" + cityModel.getId(), Toast.LENGTH_SHORT).show();
        idCity = ""+cityModel.getId();
        NameCity = cityModel.getName();
        ImagCity = cityModel.getImagen();

       // String item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        //Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void onClicCity(View v){

        Intent intent = new Intent(getApplicationContext(), DetalleCityActivity.class);
        intent.putExtra("idCity", "" + idCity);
        intent.putExtra("name", NameCity);
        intent.putExtra("imagen", ImagCity);
        startActivity(intent);

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
