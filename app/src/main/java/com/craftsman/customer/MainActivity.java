package com.craftsman.customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.craftsman.R;
import com.craftsman.adapter.CraftsmanAdapter;
import com.craftsman.adapter.WorkCraftsmanAdapter;
import com.craftsman.craftsman.works_craftsman;
import com.craftsman.model.User;
import com.craftsman.model.Work;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity   extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawer;

    ArrayList list = new ArrayList<User>();
    DatabaseReference mdatabase;
    ProgressDialog dialog2;
    String Uid = "" , City = "";
    CraftsmanAdapter nAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences prefs = getSharedPreferences("data", 0);
        Uid = prefs.getString("Uid","");
        City= prefs.getString("City", "");
        dialog2 = new ProgressDialog(MainActivity.this);
        dialog2.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog2.setMessage("Please Wait ... ");
        dialog2.setIndeterminate(true);
        dialog2.setCanceledOnTouchOutside(false);

        findViewById(R.id.drawing).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(MainActivity.this,CraftsmanList.class);
                i.putExtra("type", "Draw");
                startActivity(i);

            }
        });

        findViewById(R.id.carpentry).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this,CraftsmanList.class);
                i.putExtra("type", "Carpentry");
                startActivity(i);
            }
        });

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nvView);
        navigationView.setNavigationItemSelectedListener(this);


        findViewById(R.id.menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(Gravity.LEFT);

            }
        });


        final RecyclerView recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setItemViewCacheSize(50);
        recyclerView.setDrawingCacheEnabled(true);
          nAdapter = new CraftsmanAdapter(MainActivity.this, list);
         recyclerView.setAdapter(nAdapter);
        mdatabase = FirebaseDatabase.getInstance().getReference().child("Craftsman") ;
        mdatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list.clear();

                dialog2.show();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);

                    if(user.getCity().equals(City)) {


                        list.add(user);
                        nAdapter.notifyDataSetChanged();
                    }
                    dialog2.dismiss();
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                dialog2.dismiss();
                Toast.makeText(MainActivity.this, "no data", Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.home:

                break;
            case R.id.change_Profile:
               Intent intent = new Intent(MainActivity.this, profile_customer.class);
                startActivity(intent);
                break;
            case R.id.add:
                Intent add  = new Intent(MainActivity.this, add_request_customer.class);
                startActivity(add);
                break;

            case R.id.MyRequest:
               Intent in  = new Intent(MainActivity.this, requests_in_customer.class);
                 startActivity(in);
                break;
            case R.id.cart:
                  Intent i = new Intent(MainActivity.this, cart_customer.class);
                   startActivity(i);
                break;



            case R.id.logout:

                MainActivity.this.finish();

                break;


        }
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}