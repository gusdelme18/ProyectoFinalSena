package com.example.gustavodelgado.proyectofinal;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.gustavodelgado.proyectofinal.Model.SitiosTModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddSitiosTuristicosActivity extends AppCompatActivity {

    private DatabaseReference mDatabaseReference;
    private EditText editName, editPhone, editWebsite, editImage, editAddress, editEmail;

    public String idCity;
    ProgressDialog progressDialog;
    private static final int PICK_IMAGE_REQUEST = 234;
    private static final int CAMERA_REQUEST_CODE = 1;
    private static final String FB_SITIOS_TURISTICOS = "sitio_turistico";

    private static final String IMAGE_DIRECTORY_NAME = "Proyecto Sena";
    //a Uri object to store file path
    private Uri imageuri;

    private ImageView imageView;
    private DatabaseReference mDatabase;
    private StorageReference mStorage;

    File photoFile = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sitios_turisticos);


        //initializing database reference
        imageView = (ImageView) findViewById(R.id.imageHeader);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        editName = (EditText) findViewById(R.id.editText_Name);
        editPhone = (EditText) findViewById(R.id.editText_Phone);
        editWebsite = (EditText) findViewById(R.id.editText_webSite);
        editEmail = (EditText) findViewById(R.id.editText_Email);
        editImage = (EditText) findViewById(R.id.editText_Imagen);
        editAddress = (EditText) findViewById(R.id.editText_Address);

        Intent intent = getIntent();
        idCity = intent.getStringExtra("idCity");

        mStorage = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference().child(FB_SITIOS_TURISTICOS);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Añadir Sitios turistico");

        toolbar.setNavigationIcon(android.support.v7.appcompat.R.drawable.abc_ic_ab_back_material);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });


        findViewById(R.id.fab_gallery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
            }
        });

        findViewById(R.id.fab_camera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // Ensure that there's a camera activity to handle the intent
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    // Create the File where the photo should go

                    try {

                        //imageuri = FileProvider.getUriForFile(getBaseContext(),"com.example.android.fileprovider",createImageFile());
                        photoFile =createImageFile();

                        imageuri = FileProvider.getUriForFile(getBaseContext(),
                                "com.example.android.fileprovider",
                                photoFile);
                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageuri);
                        startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);

                    } catch (IOException ex) {
                        // Error occurred while creating the File

                    }
                }
            }

        });
    }

    public void onClickSaveSitio (View v){


        if (imageuri != null) {
            //displaying a progress dialog while upload is going on
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();

            //StorageReference riversRef = storageReference.child("images/pic.jpg");
            StorageReference riversRef = mStorage.child(FB_SITIOS_TURISTICOS).child(imageuri.getLastPathSegment());
            riversRef.putFile(imageuri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //if the upload is successfull
                            //hiding the progress dialog
                            progressDialog.dismiss();

                            //and displaying a success toast
                            Toast.makeText(getApplicationContext(), "Save data", Toast.LENGTH_LONG).show();

                            Uri downloadUrl =taskSnapshot.getDownloadUrl();
                            String uid = mDatabaseReference.child(FB_SITIOS_TURISTICOS).push().getKey();

                            DatabaseReference newSittioT = mDatabase.push();
                            newSittioT.child("address").setValue( editAddress.getText().toString().trim());
                            newSittioT.child("name").setValue(editName.getText().toString().trim());
                            newSittioT.child("phone").setValue(editPhone.getText().toString().trim());
                            newSittioT.child("website").setValue(editWebsite.getText().toString().trim());
                            newSittioT.child("email").setValue(editEmail.getText().toString().trim());
                            newSittioT.child("name").setValue(editName.getText().toString().trim());
                            newSittioT.child("imagen").setValue(downloadUrl.toString());
                            newSittioT.child("idCity").setValue(idCity);
                            newSittioT.child("idSitio").setValue(uid);

                            onBackPressed();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            //if the upload is not successfull
                            //hiding the progress dialog
                            progressDialog.dismiss();

                            //and displaying error message
                            Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //calculating progress percentage
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                            //displaying percentage in progress dialog
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                        }
                    });
        }
        //if there is not any file
        else {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Saving data...");

            progressDialog.show();
            //you can display an error toast

            // get data for Form
            String uid = mDatabaseReference.child(FB_SITIOS_TURISTICOS).push().getKey();

            SitiosTModel sitiosTModel =  new SitiosTModel(
                    editAddress.getText().toString().trim(),
                    editName.getText().toString().trim(),
                    editPhone.getText().toString().trim(),
                    editWebsite.getText().toString().trim(),
                    editImage.getText().toString().trim(),
                    idCity,
                    uid,
                    editEmail.getText().toString().trim() );


            //referring to movies node and setting the values from movie object to that location
            mDatabaseReference.child(FB_SITIOS_TURISTICOS).push().setValue(sitiosTModel, new DatabaseReference.CompletionListener() {
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


                    }
                }
            });
        }

    }

    //handling the image chooser activity result
    @SuppressLint("LongLogTag")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageuri = data.getData();
            Log.e("ExternalStorageimageuri", " " + imageuri + ":");
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageuri);
                imageView.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {

            Log.e("ExternalStorageimageuri", " " + imageuri + ":");


            final Bitmap myBitmap = BitmapFactory.decodeFile(imageuri.getPath());
            imageView.setImageBitmap(myBitmap);


            Log.e("ExternalStoragephotoFile", " " + imageuri + ":");


        }
    }

    private File createImageFile() throws IOException {


        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.e(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }
        else {
            Log.e(IMAGE_DIRECTORY_NAME, "directorio creado  "
                    + IMAGE_DIRECTORY_NAME + " directory");
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + "IMG_" + timeStamp + ".jpg");

        return mediaFile;

    }

}