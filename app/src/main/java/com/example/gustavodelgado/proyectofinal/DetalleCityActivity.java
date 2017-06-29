package com.example.gustavodelgado.proyectofinal;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.gustavodelgado.proyectofinal.Helpers.ImageLoader;
import com.squareup.picasso.Picasso;

import java.io.InputStream;

public class DetalleCityActivity extends AppCompatActivity {

    Toolbar toolbar;
    CollapsingToolbarLayout collapsingToolbarLayout;
    private AppBarLayout appBarLayout;
    private Menu collapsedMenu;
    private boolean appBarExpanded = true;
    private String id,name,imagen ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_city);

        // get data for activity Main

        Intent intent = getIntent();
        id = intent.getStringExtra("idCity");
        name = intent.getStringExtra("name");
        imagen = intent.getStringExtra("imagen");

        //
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        toolbar.setNavigationIcon(android.support.v7.appcompat.R.drawable.abc_ic_ab_back_material);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);
        collapsingToolbarLayout.setTitle(name);

        // Loader image - will be shown before loading image
        /*int loader = R.drawable.ic_delete;
        ImageView image = (ImageView) findViewById(R.id.imageHeader);
        String image_url = "http://www.cali.gov.co/info/principal/media/bloque202741.jpg";
        ImageLoader imgLoader = new ImageLoader(getApplicationContext());
        imgLoader.DisplayImage(image_url, loader, image);*/

        // Image link from internet


        if(TextUtils.isEmpty(imagen)){
            Picasso.with(DetalleCityActivity.this).load("http://www.dentallink.com.uy/components/com_virtuemart/assets/images/vmgeneral/no-image.jpg").into((ImageView) findViewById(R.id.imageHeader));
        }
        else {

            Picasso.with(DetalleCityActivity.this).load(imagen).into((ImageView) findViewById(R.id.imageHeader));

        }

        //new DownloadImageFromInternet((ImageView) findViewById(R.id.imageHeader))
          //      .execute(imagen);



    }

    private class DownloadImageFromInternet extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;

        public DownloadImageFromInternet(ImageView imageView) {
            this.imageView = imageView;
            Toast.makeText(getApplicationContext(), "Please wait, it may take a few minute...", Toast.LENGTH_SHORT).show();
        }

        protected Bitmap doInBackground(String... urls) {
            String imageURL = urls[0];
            Bitmap bimage = null;
            try {
                InputStream in = new java.net.URL(imageURL).openStream();
                bimage = BitmapFactory.decodeStream(in);

            } catch (Exception e) {
                Log.e("Error Message", e.getMessage());
                e.printStackTrace();
            }
            return bimage;
        }

        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }
    }

    public void onclickHotel(View view){

        Intent intent = new Intent(getApplicationContext(), Hoteles.class);
        intent.putExtra("id", id);
        intent.putExtra("name", name);
        intent.putExtra("category","1");
        startActivity(intent);

    }

    public void onclickSitios(View view){

        Intent intent = new Intent(getApplicationContext(), DetalleCityActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("name", name);
        intent.putExtra("category", "2");
        startActivity(intent);

    }

    public void onclickOperador(View view){

        Intent intent = new Intent(getApplicationContext(), DetalleCityActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("name", name);
        intent.putExtra("category", "3");
        startActivity(intent);

    }


}
