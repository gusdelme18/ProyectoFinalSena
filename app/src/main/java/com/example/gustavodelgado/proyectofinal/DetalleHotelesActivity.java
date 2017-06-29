package com.example.gustavodelgado.proyectofinal;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gustavodelgado.proyectofinal.Model.CityModel;
import com.example.gustavodelgado.proyectofinal.Model.HotelesModel;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

public class DetalleHotelesActivity extends AppCompatActivity {

    Toolbar toolbar;
    public String name,idHotel,uid, phoneCall, WebView;
    DatabaseReference dref;
    TextView address,website,phone,map;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_hoteles);

        // get data for activity Main

        Intent intent = getIntent();
        idHotel = intent.getStringExtra("idHotel");
        name = intent.getStringExtra("name");

        //
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle(name);

        toolbar.setNavigationIcon(android.support.v7.appcompat.R.drawable.abc_ic_ab_back_material);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });

        //
       // address = (TextView) findViewById(R.id.txtAddress);
        phone = (TextView) findViewById(R.id.txtPhone);
        map = (TextView) findViewById(R.id.txtMap);
        website = (TextView) findViewById(R.id.txtWebsite);

        // get data for hotel

        dref = FirebaseDatabase.getInstance().getReference("hoteles");
        Query hotelist = dref.orderByChild("idHotel").equalTo(idHotel);


        hotelist.addChildEventListener(new ChildEventListener() {
            public static final String TAG = "Hoteles";

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                HotelesModel hotelesModel = dataSnapshot.getValue(HotelesModel.class);

                //show info
                if(TextUtils.isEmpty(hotelesModel.getImagen())){
                    Picasso.with(DetalleHotelesActivity.this).load("http://www.dentallink.com.uy/components/com_virtuemart/assets/images/vmgeneral/no-image.jpg").into((ImageView) findViewById(R.id.imageHeader));
                }
                else {

                    Picasso.with(DetalleHotelesActivity.this).load(hotelesModel.getImagen()).into((ImageView) findViewById(R.id.imageHeader));

                }


              //  address.setText(hotelesModel.getAddress().toString());
                phone.setText(hotelesModel.getPhone().toString());
                phoneCall =hotelesModel.getPhone().toString();
                map.setText(hotelesModel.getAddress().toString());
                website.setText(hotelesModel.getWebsite());
                WebView =hotelesModel.getWebsite();
                uid =  dataSnapshot.getKey();

                Log.d(TAG, "onChildAdded: "+ hotelesModel);

            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                HotelesModel hotelesModel = dataSnapshot.getValue(HotelesModel.class);

                //show info

                if(TextUtils.isEmpty(hotelesModel.getImagen())){
                    Picasso.with(DetalleHotelesActivity.this).load("http://www.dentallink.com.uy/components/com_virtuemart/assets/images/vmgeneral/no-image.jpg").into((ImageView) findViewById(R.id.imageHeader));
                }
                else {

                    Picasso.with(DetalleHotelesActivity.this).load(hotelesModel.getImagen()).into((ImageView) findViewById(R.id.imageHeader));

                }


                //  address.setText(hotelesModel.getAddress().toString());
                phone.setText(hotelesModel.getPhone().toString());
                phoneCall =hotelesModel.getPhone().toString();
                map.setText(hotelesModel.getAddress().toString());
                website.setText(hotelesModel.getWebsite());
                WebView =hotelesModel.getWebsite();
                uid =  dataSnapshot.getKey();

                Log.d(TAG, "onChildAdded: "+ hotelesModel);

            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("delete data...");

        //

        findViewById(R.id.fab_update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DetalleHotelesActivity.this, "Clicked add Hotel "+idHotel, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), EditHotelActivity.class);
                intent.putExtra("idHotel",idHotel);
                startActivity(intent);
            }
        });

        findViewById(R.id.fab_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLocationDialog();
            }
        });
    }

    private void showLocationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(DetalleHotelesActivity.this);
        builder.setTitle("Eliminar registro");
        builder.setMessage("¿Esta seguro que desea eliminar el hotel?");

        String positiveText = getString(android.R.string.ok);
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        progressDialog.show();
                        dref.child(uid).removeValue();
                        onBackPressed();
                        progressDialog.dismiss();
                    }
                });

        String negativeText = getString(android.R.string.cancel);
        builder.setNegativeButton(negativeText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // negative button logic
                    }
                });

        AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();
    }

    public void onClicWebview (View v){

        Intent intent = new Intent(getApplicationContext(), WebViewActivity.class);
        intent.putExtra("website", "" + WebView);
        startActivity(intent);
    }

    public void onClicCallPhone(View v) {

        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:"+phoneCall));

        Log.d("POHNE", "onClicCallPhone: " + phoneCall);

        if ( ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startActivity(callIntent);

    }
}
