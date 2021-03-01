package com.craftsman.craftsman;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.craftsman.R;
import com.craftsman.adapter.Request_in_CraftsmanAdapter;
import com.craftsman.adapter.WorkCraftsmanAdapter;
import com.craftsman.model.Requests;
import com.craftsman.model.Work;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class requests_in_craftsman extends AppCompatActivity {
    ArrayList list = new ArrayList<Requests>();
    DatabaseReference mdatabase;
    ProgressDialog dialog2;
    String Uid = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests_in_craftsman);
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        SharedPreferences prefs = getSharedPreferences("data", 0);
        Uid = prefs.getString("Uid","");

        mdatabase = FirebaseDatabase.getInstance().getReference().child("Requests");
        dialog2 = new ProgressDialog(requests_in_craftsman.this);
        dialog2.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog2.setMessage("Please Wait ... ");
        dialog2.setIndeterminate(true);
        dialog2.setCanceledOnTouchOutside(false);

        // list.add(new Work("Architectural Draftsman","f you want to construct a new building for your business or a beautiful Custom Dream Home. To begin with, hire an architectural draftsman.  ",  23  ));

        // list.add(new Work("Architectural Draftsman","f you want to construct a new building for your business or a beautiful Custom Dream Home. To begin with, hire an architectural draftsman.  ",  23  ));
        final RecyclerView recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setItemViewCacheSize(50);
        recyclerView.setDrawingCacheEnabled(true);
        Request_in_CraftsmanAdapter nAdapter = new Request_in_CraftsmanAdapter(requests_in_craftsman.this, list);
        recyclerView.setAdapter(nAdapter);
        mdatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list.clear();


                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Work work = snapshot.getValue(Work.class);
                    if((work.getUID().equals(Uid)) ) {
                        list.add(work);
                        nAdapter.notifyDataSetChanged();
                    }

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(requests_in_craftsman.this, "no data", Toast.LENGTH_SHORT).show();
            }
        });

    }
}