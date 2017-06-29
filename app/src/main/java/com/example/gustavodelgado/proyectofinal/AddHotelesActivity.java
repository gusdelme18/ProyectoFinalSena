package com.example.gustavodelgado.proyectofinal;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.gustavodelgado.proyectofinal.Model.HotelesModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;

public class AddHotelesActivity extends AppCompatActivity {

    private DatabaseReference mDatabaseReference;
    private EditText editName, editPhone, editWebsite, editImage, editAddress, editEmail;

    public String idCity;
    ProgressDialog progressDialog;
    private static final int PICK_IMAGE_REQUEST = 234;
    private static final int CAMERA_REQUEST_CODE = 1;
    public static final int MEDIA_TYPE_IMAGE = 1;

    // Activity request codes
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;

    public static final int MEDIA_TYPE_VIDEO = 2;

    // directory name to store captured images and videos
    private static final String IMAGE_DIRECTORY_NAME = "Hello Camera";
    //a Uri object to store file path
    private Uri imageuri;
    String mCurrentPhotoPath;

    private ImageView imageView;
    private DatabaseReference mDatabase;
    private StorageReference mStorage;

    File photoFile = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hoteles);


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
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Saving data...");

        mStorage = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("hoteles");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
                        photoFile = createImageFile();

                    } catch (IOException ex) {
                        // Error occurred while creating the File

                    }
                    // Continue only if the File was successfully created
                    if (photoFile != null) {
                       // imageuri = FileProvider.getUriForFile(getBaseContext(),"com.example.android.fileprovider",photoFile);

                        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoFile);
                        startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
                    }
                }

            }

        });
    }

    public void onClickSaveHotel (View v){

        progressDialog.show();

        if (imageuri != null) {
            //displaying a progress dialog while upload is going on
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();

            //StorageReference riversRef = storageReference.child("images/pic.jpg");
            StorageReference riversRef = mStorage.child("hoteles").child(imageuri.getLastPathSegment());
            riversRef.putFile(imageuri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //if the upload is successfull
                            //hiding the progress dialog
                            progressDialog.dismiss();

                            //and displaying a success toast
                            Toast.makeText(getApplicationContext(), "File Uploaded ", Toast.LENGTH_LONG).show();

                            Uri downloadUrl =taskSnapshot.getDownloadUrl();
                            String uid = mDatabaseReference.child("hoteles").push().getKey();

                            DatabaseReference newHotel = mDatabase.push();
                            newHotel.child("address").setValue( editAddress.getText().toString().trim());
                            newHotel.child("name").setValue(editName.getText().toString().trim());
                            newHotel.child("phone").setValue(editPhone.getText().toString().trim());
                            newHotel.child("website").setValue(editWebsite.getText().toString().trim());
                            newHotel.child("email").setValue(editEmail.getText().toString().trim());
                            newHotel.child("name").setValue(editName.getText().toString().trim());
                            newHotel.child("imagen").setValue(downloadUrl.toString());
                            newHotel.child("idCity").setValue(idCity);
                            newHotel.child("idHotel").setValue(uid);

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
            //you can display an error toast

            /*/ get data for Form
            String uid = mDatabaseReference.child("hoteles").push().getKey();

            HotelesModel hotelesModel =  new HotelesModel(
                    editAddress.getText().toString().trim(),
                    editName.getText().toString().trim(),
                    editPhone.getText().toString().trim(),
                    editWebsite.getText().toString().trim(),
                    editImage.getText().toString().trim(),
                    idCity,
                    uid,
                    editEmail.getText().toString().trim() );


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


                    }
                }
            });*/
        }

    }

    //handling the image chooser activity result
    @SuppressLint("LongLogTag")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageuri = data.getData();
            Log.e("ExternalStorageimageuri", "Scanned" + imageuri + ":");
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageuri);
                imageView.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {

            Log.e("ExternalStorageimageuri", "Scanned" + imageuri + ":");


            /*Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(imageBitmap);*/

            //File imgFile = new  File(mCurrentPhotoPath);

           // Log.e("ExternalStorageimageuri", "imgFile" + imgFile + ":");

            //if(imgFile.exists()){
                // bimatp factory
                BitmapFactory.Options options = new BitmapFactory.Options();
                // downsizing image as it throws OutOfMemory Exception for larger
                // images
                options.inSampleSize = 8;
                final Bitmap myBitmap = BitmapFactory.decodeFile(photoFile.getPath(),
                        options);
               // Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getPath());
                imageView.setImageBitmap(myBitmap);

            //}


            Log.e("ExternalStorageimageuri", "Scanned" + photoFile + ":");


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
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");


        return mediaFile;
/*


        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + ".png";
        File storageDir =  new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyApplication");

        /**Create the storage directory if it does not exist
        if (! storageDir.exists()){
            if (! storageDir.mkdirs()){
                return null;
            }
        }
        /**Create a media file name
        File file = new File(storageDir.getPath() + File.separator +
                imageFileName);

        File image = File.createTempFile(
                imageFileName,  /* prefix
                ".jpg",         /* suffix
                storageDir      /* directory
        );
        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file://"+image.getAbsolutePath() + "/" + imageFileName;
        Log.e("mCurrentPhotoPath", " " + mCurrentPhotoPath + ":");

        imageuri = Uri.fromFile(file);

        return file;*/
    }

}
