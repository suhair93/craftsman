package com.craftsman.customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import com.craftsman.R;
import com.craftsman.adapter.CraftsmanAdapter;
import com.craftsman.model.User;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class MainActivity   extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawer;

    ArrayList list = new ArrayList<User>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


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

        list.add(new User("ahmed maher","Drawing"));
        list.add(new User("mohammed amjad","Drawing"));
        list.add(new User("abood amjad","Carpentry"));
        final RecyclerView recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setItemViewCacheSize(50);
        recyclerView.setDrawingCacheEnabled(true);
         CraftsmanAdapter nAdapter = new CraftsmanAdapter(MainActivity.this, list);
         recyclerView.setAdapter(nAdapter);


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.home:
           //    HomeActivityNew.this.finish();
                break;
            case R.id.change_Profile:
                //  Intent intent = new Intent(CustomerMainActivity.this, Change_data_customer.class);
                // startActivity(intent);
                break;


            case R.id.search:
                //  Intent intentresturant = new Intent(CustomerMainActivity.this, ResturantActivity.class);
                //   startActivity(intentresturant);
                break;

            //   case R.id.the_nearest_restaurants:
            //     Intent intentnearest = new Intent(CustomerMainActivity.this, NearstResturantActivity.class);
            //   startActivity(intentnearest);
            // break;


            case R.id.logout:

                MainActivity.this.finish();

                break;


        }
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}