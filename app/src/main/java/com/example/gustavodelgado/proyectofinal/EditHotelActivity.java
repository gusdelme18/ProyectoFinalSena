package com.example.gustavodelgado.proyectofinal;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gustavodelgado.proyectofinal.Model.HotelesModel;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import java.util.Map;

public class EditHotelActivity extends AppCompatActivity {


    public String idCity,idHotel,uid;
    DatabaseReference dref;
    private EditText editName, editPhone, editWebsite, editImage, editAddress,editEmail;
    ProgressDialog progressDialog;
    DatabaseReference mDatabaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_hotel);

        Intent intent = getIntent();
        idHotel = intent.getStringExtra("idHotel");
        Toast.makeText(EditHotelActivity.this, "Clicked add Hotel "+idHotel, Toast.LENGTH_SHORT).show();


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Actualizar");

        toolbar.setNavigationIcon(android.support.v7.appcompat.R.drawable.abc_ic_ab_back_material);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });


        editName = (EditText) findViewById(R.id.editText_Name);
        editPhone = (EditText) findViewById(R.id.editText_Phone);
        editWebsite = (EditText) findViewById(R.id.editText_webSite);
        editImage = (EditText) findViewById(R.id.editText_Imagen);
        editAddress = (EditText) findViewById(R.id.editText_Address);
        editEmail =(EditText) findViewById(R.id.editText_Address);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Update data...");

        //initializing database reference
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("hoteles");
        Query hotelist = mDatabaseReference.orderByChild("idHotel").equalTo(idHotel);


        hotelist.addChildEventListener(new ChildEventListener() {
            public static final String TAG = "Hoteles";

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                HotelesModel hotelesModel = dataSnapshot.getValue(HotelesModel.class);

                //show info
              /*  if(TextUtils.isEmpty(hotelesModel.getImagen())){
                    Picasso.with(EditHotelActivity.this).load("http://www.dentallink.com.uy/components/com_virtuemart/assets/images/vmgeneral/no-image.jpg").into((ImageView) findViewById(R.id.imageHeader));
                }
                else {

                    Picasso.with(EditHotelActivity.this).load(hotelesModel.getImagen()).into((ImageView) findViewById(R.id.imageHeader));

                }*/

                editName.setText(hotelesModel.getName().toString());
                editPhone.setText(hotelesModel.getPhone().toString());
                editWebsite.setText(hotelesModel.getWebsite().toString());
                editImage.setText(hotelesModel.getImagen().toString());
                editAddress.setText(hotelesModel.getAddress().toString());
                uid =  dataSnapshot.getKey();
                idCity = hotelesModel.getIdCity();


            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                HotelesModel hotelesModel = dataSnapshot.getValue(HotelesModel.class);

             /*  if(TextUtils.isEmpty(hotelesModel.getImagen())){
                    Picasso.with(EditHotelActivity.this).load("http://www.dentallink.com.uy/components/com_virtuemart/assets/images/vmgeneral/no-image.jpg").into((ImageView) findViewById(R.id.imageHeader));
                }
                else {

                    Picasso.with(EditHotelActivity.this).load(hotelesModel.getImagen()).into((ImageView) findViewById(R.id.imageHeader));

                }*/

                editName.setText(hotelesModel.getName().toString());
                editPhone.setText(hotelesModel.getPhone().toString());
                editWebsite.setText(hotelesModel.getWebsite().toString());
                editImage.setText(hotelesModel.getImagen().toString());
                editAddress.setText(hotelesModel.getAddress().toString());
                uid = dataSnapshot.getKey();
                idCity = hotelesModel.getIdCity();

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
    }

    public void onClickUpdate(View v){
        progressDialog.show();


        HotelesModel hotelesModel =  new HotelesModel(
                editAddress.getText().toString().trim(),
                editName.getText().toString().trim(),
                editPhone.getText().toString().trim(),
                editWebsite.getText().toString().trim(),
                editImage.getText().toString().trim(),
                idCity,
                idHotel,
                editEmail.getText().toString().trim() );

        Map<String, Object> postValues = hotelesModel.toMap();


        //referring to movies node and setting the values from movie object to that location

        mDatabaseReference.child(uid).updateChildren(postValues, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {
                    System.out.println("Data could not be saved " + databaseError.getMessage());
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Data could not be saved "+ databaseError.getMessage()  , Toast.LENGTH_SHORT).show();

                } else {
                    System.out.println("Data saved successfully.");
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Data update successfully"  , Toast.LENGTH_SHORT).show();

                    onBackPressed();
                }
            }
        });

    }
}
