package com.example.gustavodelgado.proyectofinal;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.gustavodelgado.proyectofinal.Model.HotelesModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MapsHotelActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap ;
    public String idCity,NameHotel;
    DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_hotel);

        Intent intent = getIntent();
        idCity = intent.getStringExtra("idCity");
        NameHotel = intent.getStringExtra("name");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(android.support.v7.appcompat.R.drawable.abc_ic_ab_back_material);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mMap = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mMap.getMapAsync(this);



        // get Data Hoteles for maps

        mDatabaseReference = FirebaseDatabase.getInstance().getReference("hoteles");
        Query hotelist = mDatabaseReference.orderByChild("idCity").equalTo(idCity);

        hotelist.addValueEventListener(new ValueEventListener() {
            public static final String TAG = "HotelesMap";


            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e("CountHotel" ,""+dataSnapshot.getChildrenCount());
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    HotelesModel hotelesModel = postSnapshot.getValue(HotelesModel.class);
                    Log.e("DataHotel", hotelesModel.getAddress());
                    Log.e("NameHotel", NameHotel);

                    getPositionHotels(hotelesModel.getAddress(),NameHotel, hotelesModel.getName());

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });





    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(10, 10);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Hello Work"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

       /* Geocoder coder = new Geocoder(this);
        List<Address> addresses = null;
        String addressHotel = " Carrera 24b #2-48, Cali, Valle del Cauca, Colombia";
        //String addressHotel = "300 West Martin Luther King Junior Boulevard, Austin, TX";

            try {
                if(coder.isPresent()) {
                    addresses = coder.getFromLocationName(addressHotel, 5);
                    if(addresses==null){
                        return;
                    }
                    Log.e("ADDRESS", "" + addresses);
                    Address location = addresses.get(0);
                    double lat = location.getLatitude();
                    double lng = location.getLongitude();
                    Log.e("Lat", "" + lat);
                    Log.e("Lng", "" + lng);
                    LatLng latLng = new LatLng(lat, lng);
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(latLng);
                    googleMap.addMarker(markerOptions);
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));
                }
                else {
                    Log.d("NOENTRO", "onMapReady: ");

                }
            } catch (IOException e) {

                Log.i("ADDRESS", "" + addresses);
                e.printStackTrace();
                Log.e("ErrorAddres", "onMapReady: ",e );
                //System.out.print(e.printStackTrace());
            }
            Log.d("ENTRO", "onMapReady: ");


/*
        double lng= 0,lat = 0;

        Geocoder gc = new Geocoder(this);
        if(gc.isPresent()){
            List<Address> list = null;
            try {
                list = gc.getFromLocationName("155 Park Theater, Palo Alto, CA", 1);
                Address address = list.get(0);
                Log.d("ADDRESS", "onMapReady: "+address);
                 lat = address.getLatitude();
                 lng = address.getLongitude();

            } catch (IOException e) {
                e.printStackTrace();
                Log.d("ADDRESS Error", "onMapReady: "+e);
            }

        }

        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(lat, lng);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

       LatLng muze = new LatLng(41.326382, 19.821458);

        mMap.addMarker(new MarkerOptions().position(muze).title("National Museum of Tirana"));


        LatLng liqen = new LatLng(41.326382, 19.821458);

        mMap.addMarker(new MarkerOptions().position(liqen).title("Artifical Lake of Tirana"));



        LatLng monument = new LatLng(41.320155, 19.786377);

        mMap.addMarker(new MarkerOptions().position(monument).title("Skanderbeg STatue"));


        LatLng teater = new LatLng(41.321693, 19.817442);

        mMap.addMarker(new MarkerOptions().position(teater).title("National Theatre"));


        mMap.moveCamera(CameraUpdateFactory.newLatLng(muze));

        LatLng TiranaCenter = new LatLng(41.327541, 19.818609);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(TiranaCenter,15.0f));*/
    }

    public void getPositionHotels (String address, String NameCity, String NameHotel){

        Geocoder coder = new Geocoder(getBaseContext(), Locale.getDefault());
        String addresHotel = " " +address+", "+NameCity+", Valle del Cauca, Colombia";
       // String addresHotel = "731 Market St, San Francisco, CA";
        Log.e("AddressHotel", addresHotel);

        List<Address> addresses = null;

        try {
            if(coder.isPresent()) {
                addresses = coder.getFromLocationName(addresHotel, 5);
                Log.e("ADDRESS", "" + addresses);

                if(addresses!=null || addresses.isEmpty() || addresses.size()<0 ){

                    Log.e("ENTROADDRES", "" + addresses);
                    Address location = addresses.get(0);
                    double lat = location.getLatitude();
                    double lng = location.getLongitude();
                    Log.e("Lat", "" + lat);
                    Log.e("Lng", "" + lng);
                    LatLng latLng = new LatLng(lat, lng);
                    //MarkerOptions markerOptions = new MarkerOptions();
                    //markerOptions.position(latLng);
                    //mMap.addMarker(markerOptions);
                    mMap.addMarker(new MarkerOptions().position(latLng).title(NameHotel));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));
                }
                else {
                    Log.d("NOENTRO", "onMapReady: ");
                }

            }
            else {
                Log.d("NOENTRO", "onMapReady: ");

            }
        } catch (IOException e) {

            Log.i("ADDRESS", "" + addresses);
            e.printStackTrace();
            Log.e("ErrorAddres", "onMapReady: ",e );
            //System.out.print(e.printStackTrace());
        }
        Log.d("ENTRO", "onMapReady: ");

    }


}
