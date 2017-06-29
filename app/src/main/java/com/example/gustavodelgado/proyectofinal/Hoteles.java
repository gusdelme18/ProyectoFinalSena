package com.example.gustavodelgado.proyectofinal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gustavodelgado.proyectofinal.Model.HotelesModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;



public class Hoteles extends AppCompatActivity {

    Toolbar toolbar;
    public String name, idCategory,idCity;

    private RecyclerView mRecyclerView;
    private StaggeredGridLayoutManager mLayoutManager;
    private TextView txHoteles;
    private static final String FB_HOTELES = "hoteles";

    //Getting reference to Firebase Database

    DatabaseReference mDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hoteles);


        // get data for activity Main

        Intent intent = getIntent();
        idCity = intent.getStringExtra("id");
        name = intent.getStringExtra("name");
        idCategory = intent.getStringExtra("category");

        //

        findViewById(R.id.fab_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddHotelesActivity.class);
                intent.putExtra("idCity", ""+idCity);
                startActivity(intent);
            }
        });

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Hoteles en "+name);

        toolbar.setNavigationIcon(android.support.v7.appcompat.R.drawable.abc_ic_ab_back_material);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                onBackPressed();
            }
        });

        //Initializing our Recyclerview
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        txHoteles = (TextView) findViewById(R.id.no_hoteles);

        if (mRecyclerView != null) {
            //to enable optimization of recyclerview
            mRecyclerView.setHasFixedSize(true);
        }
        //using staggered grid pattern in recyclerview
        mLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference(FB_HOTELES);
        Query hotelist = mDatabaseReference.orderByChild("idCity").equalTo(idCity);

        System.out.print( mDatabaseReference);


        //Say Hello to our new FirebaseUI android Element, i.e., FirebaseRecyclerAdapter
        FirebaseRecyclerAdapter<HotelesModel,HotelViewHolder> adapter = new FirebaseRecyclerAdapter<HotelesModel, HotelViewHolder>(
                HotelesModel.class,R.layout.items_board_hoteles,HotelViewHolder.class,hotelist) {
            @Override
            protected void populateViewHolder(HotelViewHolder viewHolder, final HotelesModel model, final int position) {

                if(txHoteles.getVisibility()== View.VISIBLE){
                    txHoteles.setVisibility(View.GONE);
                }
                viewHolder.hotelName.setText(model.getName());
                if(TextUtils.isEmpty(model.getImagen())){
                    Picasso.with(Hoteles.this).load("http://www.dentallink.com.uy/components/com_virtuemart/assets/images/vmgeneral/no-image.jpg").into(viewHolder.imHotel);
                }
                else {
                Picasso.with(Hoteles.this).load(model.getImagen()).into(viewHolder.imHotel);
                }

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                       //Log.w( "You clicked on "+position);
                        Intent intent = new Intent(getApplicationContext(), DetalleHotelesActivity.class);
                        intent.putExtra("idHotel", ""+ model.getIdHotel());
                        intent.putExtra("name", model.getName());
                        startActivity(intent);

                    }
                });

            }
        };

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(adapter);
    }



    //ViewHolder for our Firebase UI
    public static class HotelViewHolder extends RecyclerView.ViewHolder{

        TextView hotelName;
        ImageView imHotel;
        View mView;

        public HotelViewHolder(View v) {
            super(v);
            mView = itemView;
            hotelName = (TextView) v.findViewById(R.id.txtNameHotel);
            imHotel = (ImageView) v.findViewById(R.id.coverImageView);
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_hotels_list, menu);
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_map) {
            Intent intent = new Intent(getApplicationContext(), MapsHotelActivity.class);
            intent.putExtra("idCity", ""+ idCity);
            intent.putExtra("name", ""+ name);
            intent.putExtra("FB_CHILD_QUERY", ""+ FB_HOTELES);

            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
