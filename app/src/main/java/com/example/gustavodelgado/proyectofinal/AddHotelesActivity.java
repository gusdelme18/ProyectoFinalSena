package com.example.gustavodelgado.proyectofinal;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.gustavodelgado.proyectofinal.Model.HotelesModel;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddHotelesActivity extends AppCompatActivity {

    private DatabaseReference mDatabaseReference;
    private EditText editName, editPhone, editWebsite, editImage, editAddress;
    private Button bSubmit;
    public String idCity;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hoteles);


        //initializing database reference
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        editName = (EditText) findViewById(R.id.editText_Name);
        editPhone = (EditText) findViewById(R.id.editText_Phone);
        editWebsite = (EditText) findViewById(R.id.editText_webSite);
        editImage = (EditText) findViewById(R.id.editText_Imagen);
        editAddress = (EditText) findViewById(R.id.editText_Address);

        Intent intent = getIntent();
        idCity = intent.getStringExtra("idCity");
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Saving data...");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(android.support.v7.appcompat.R.drawable.abc_ic_ab_back_material);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });


    }

    public void onClickSaveHotel (View v){

        progressDialog.show();

        // get data for Form
        String uid = mDatabaseReference.child("hoteles").push().getKey();

        HotelesModel hotelesModel =  new HotelesModel(
                editAddress.getText().toString().trim(),
                editName.getText().toString().trim(),
                editPhone.getText().toString().trim(),
                editWebsite.getText().toString().trim(),
                editImage.getText().toString().trim(),
                idCity,
                uid );


        //referring to movies node and setting the values from movie object to that location
        mDatabaseReference.child("hoteles").push().setValue(hotelesModel, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError != null) {
                    System.out.println("Data could not be saved " + databaseError.getMessage());
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Data could not be saved "+ databaseError.getMessage()  , Toast.LENGTH_SHORT).show();

                } else {
                    System.out.println("Data saved successfully.");
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Data saved successfully"  , Toast.LENGTH_SHORT).show();

                    onBackPressed();

                    editAddress.setText(" ");
                    editName.setText(" ");
                    editPhone.setText(" ");
                    editWebsite.setText(" ");
                    editImage.setText(" ");


                }
            }
        });




    }

}
