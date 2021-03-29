package com.craftsman.customer;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.craftsman.R;
import com.craftsman.adapter.CraftsmanAdapter;
import com.craftsman.model.Requests;
import com.craftsman.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchCraftsman extends AppCompatActivity {
    ArrayList list = new ArrayList<Requests>();
    DatabaseReference mdatabase;
    ProgressDialog dialog2;
    RecyclerView recyclerView;
    ProgressBar progress_bar;
    String Uid = "" , type = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_craftsman);
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        SharedPreferences prefs = getSharedPreferences("data", 0);
        Uid = prefs.getString("Uid","");


        dialog2 = new ProgressDialog(SearchCraftsman.this);
        dialog2.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog2.setMessage("Please Wait ... ");
        dialog2.setIndeterminate(true);
        dialog2.setCanceledOnTouchOutside(false);

        Bundle b = getIntent().getExtras();
        if(b != null){
            type = b.getString("type");
        }
        // list.add(new Work("Architectural Draftsman","f you want to construct a new building for your business or a beautiful Custom Dream Home. To begin with, hire an architectural draftsman.  ",  23  ));

        // list.add(new Work("Architectural Draftsman","f you want to construct a new building for your business or a beautiful Custom Dream Home. To begin with, hire an architectural draftsman.  ",  23  ));
        recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setItemViewCacheSize(50);
        recyclerView.setDrawingCacheEnabled(true);
        CraftsmanAdapter nAdapter = new CraftsmanAdapter(SearchCraftsman.this, list);
        recyclerView.setAdapter(nAdapter);

        mdatabase = FirebaseDatabase.getInstance().getReference().child("Craftsman");
        mdatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list.clear();


                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User u = snapshot.getValue(User.class);

                        list.add(u);
                        nAdapter.notifyDataSetChanged();


                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(SearchCraftsman.this, "no data", Toast.LENGTH_SHORT).show();
            }
        });
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) findViewById(R.id.search_view);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        recyclerView.setHasFixedSize(true);
        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                nAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                nAdapter.getFilter().filter(query);
                return false;
            }
        });

    }
}