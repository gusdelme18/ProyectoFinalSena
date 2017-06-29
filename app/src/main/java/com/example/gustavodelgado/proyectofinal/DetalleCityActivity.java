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



        if(TextUtils.isEmpty(imagen)){
            Picasso.with(DetalleCityActivity.this).load("http://www.dentallink.com.uy/components/com_virtuemart/assets/images/vmgeneral/no-image.jpg").into((ImageView) findViewById(R.id.imageHeader));
        }
        else {

            Picasso.with(DetalleCityActivity.this).load(imagen).into((ImageView) findViewById(R.id.imageHeader));

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

        Intent intent = new Intent(getApplicationContext(), SitiosTuristicosActivity.class);
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
