package com.craftsman.customer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.craftsman.R;
import com.craftsman.adapter.WorkCraftsmanAdapter;
import com.craftsman.adapter.WorkInCustomerAdapter;
import com.craftsman.craftsman.add_work_craftsman;
import com.craftsman.model.Work;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class works_craftsman_in_customer extends AppCompatActivity {
    ArrayList list = new ArrayList<Work>();
    DatabaseReference mdatabase;
    ProgressDialog dialog2;
    String Uid = "" , id_craftsman = "";
    WorkInCustomerAdapter nAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_works_in_customer);
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Bundle b = getIntent().getExtras();
        if(b != null){
            id_craftsman = b.getString("id");
        }
        SharedPreferences prefs = getSharedPreferences("data", 0);
        Uid = prefs.getString("Uid","");

        dialog2 = new ProgressDialog(works_craftsman_in_customer.this);
        dialog2.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog2.setMessage("Please Wait ... ");
        dialog2.setIndeterminate(true);
        dialog2.setCanceledOnTouchOutside(false);

           final RecyclerView recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setItemViewCacheSize(50);
        recyclerView.setDrawingCacheEnabled(true);
          nAdapter = new WorkInCustomerAdapter(works_craftsman_in_customer.this, list);
        recyclerView.setAdapter(nAdapter);
        mdatabase = FirebaseDatabase.getInstance().getReference().child("Works") ;
        mdatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list.clear();

                   dialog2.show();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Work work = snapshot.getValue(Work.class);

                    if(work.getUID().equals(id_craftsman)) {
                        list.add(work);
                        nAdapter.notifyDataSetChanged();
                    }
                    dialog2.dismiss();
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                dialog2.dismiss();
                Toast.makeText(works_craftsman_in_customer.this, "no data", Toast.LENGTH_SHORT).show();
            }
        });

    }
}