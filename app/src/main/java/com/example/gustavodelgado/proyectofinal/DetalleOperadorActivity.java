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

import com.example.gustavodelgado.proyectofinal.Model.OperadoresModel;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

public class DetalleOperadorActivity extends AppCompatActivity {

    Toolbar toolbar;
    public String name,idOperador,uid, phoneCall, WebView, EmailSitio;
    DatabaseReference dref;
    TextView address,website,phone,map, txtEmail;
    ProgressDialog progressDialog;
    private static final String FB_SITIOS_TURISTICOS = "operadores_turisticos";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_operador);

        // get data for activity Main

        Intent intent = getIntent();
        idOperador = intent.getStringExtra("idOperador");
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
        txtEmail = (TextView) findViewById(R.id.txtEmail);

        // get data for

        dref = FirebaseDatabase.getInstance().getReference(FB_SITIOS_TURISTICOS);
        Query Sitiolist = dref.orderByChild("idOperador").equalTo(idOperador);


        Sitiolist.addChildEventListener(new ChildEventListener() {
            public static final String TAG = "Sitio";

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                OperadoresModel operadoresModel = dataSnapshot.getValue(OperadoresModel.class);

                //show info
                if(TextUtils.isEmpty(operadoresModel.getImagen())){
                    Picasso.with(DetalleOperadorActivity.this).load("http://www.dentallink.com.uy/components/com_virtuemart/assets/images/vmgeneral/no-image.jpg").into((ImageView) findViewById(R.id.imageHeader));
                }
                else {

                    Picasso.with(DetalleOperadorActivity.this).load(operadoresModel.getImagen()).into((ImageView) findViewById(R.id.imageHeader));

                }

                phone.setText(operadoresModel.getPhone().toString());
                phoneCall =operadoresModel.getPhone().toString();
                map.setText(operadoresModel.getAddress().toString());
                website.setText(operadoresModel.getWebsite());
                WebView =operadoresModel.getWebsite();
                uid =  dataSnapshot.getKey();
                txtEmail.setText(operadoresModel.getEmail());
                EmailSitio = operadoresModel.getEmail().toString();

                Log.d(TAG, "onChildAdded: "+ operadoresModel);

            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                OperadoresModel operadoresModel = dataSnapshot.getValue(OperadoresModel.class);

                //show info

                if(TextUtils.isEmpty(operadoresModel.getImagen())){
                    Picasso.with(DetalleOperadorActivity.this).load("http://www.dentallink.com.uy/components/com_virtuemart/assets/images/vmgeneral/no-image.jpg").into((ImageView) findViewById(R.id.imageHeader));
                }
                else {

                    Picasso.with(DetalleOperadorActivity.this).load(operadoresModel.getImagen()).into((ImageView) findViewById(R.id.imageHeader));

                }

                phone.setText(operadoresModel.getPhone().toString());
                phoneCall =operadoresModel.getPhone().toString();
                map.setText(operadoresModel.getAddress().toString());
                website.setText(operadoresModel.getWebsite());
                WebView =operadoresModel.getWebsite();
                uid =  dataSnapshot.getKey();
                txtEmail.setText(operadoresModel.getEmail());
                EmailSitio = operadoresModel.getEmail().toString();

                Log.d(TAG, "onChildAdded: "+ operadoresModel);

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
                Intent intent = new Intent(getApplicationContext(), EditOperadorActivity.class);
                intent.putExtra("idOperador",idOperador);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(DetalleOperadorActivity.this);
        builder.setTitle("Eliminar registro");
        builder.setMessage("¿Esta seguro que desea eliminar este operador turistico?");

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

    public void onclicEmail(View v){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{EmailSitio});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
        intent.putExtra(Intent.EXTRA_TEXT, "Message");
        Intent mailer = Intent.createChooser(intent, null);
        startActivity(mailer);

        /*
        Intent testIntent = new Intent(Intent.ACTION_VIEW);
        Uri data = Uri.parse("mailto:?subject=" + "blah blah subject" + "&body=" + "blah blah body" + "&to=" + "sendme@me.com");
        testIntent.setData(data);
        startActivity(testIntent);
         */
    }
}
