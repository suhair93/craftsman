package com.craftsman.craftsman;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.craftsman.R;
import com.craftsman.adapter.PostAdapter;
import com.craftsman.adapter.WorkCraftsmanAdapter;
import com.craftsman.model.Post;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Details_Request extends AppCompatActivity {

    TextView title,descrption,category;
    EditText add_Offer;
    ArrayList list = new ArrayList<Post>();
    DatabaseReference mdatabase;
    ProgressDialog dialog2;
    PostAdapter nAdapter;
    String Uid = "" , id = "" ,idUser = "" ;
    int userType = 0   ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_details);
        SharedPreferences prefs = getSharedPreferences("data", 0);
        Uid = prefs.getString("Uid","");
        userType = prefs.getInt("user_type",0);
        title = findViewById(R.id.title);
        descrption = findViewById(R.id.description);
        Bundle b = getIntent().getExtras();
        if(b != null){
            id = b.getString("id","");
            idUser =  b.getString("idUser","");
            title.setText( b.getString("title",""));
            descrption.setText( b.getString("descrption",""));


        }


        dialog2 = new ProgressDialog(Details_Request.this);
        dialog2.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog2.setMessage("Please Wait ... ");
        dialog2.setIndeterminate(true);
        dialog2.setCanceledOnTouchOutside(false);

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if(userType == 1){
            findViewById(R.id.add_view).setVisibility(View.GONE);
        }else{
            findViewById(R.id.add_view).setVisibility(View.VISIBLE);
        }

        mdatabase = FirebaseDatabase.getInstance().getReference().child("Post");
        add_Offer =  findViewById(R.id.addOffer);

        findViewById(R.id.add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String key = mdatabase.push().getKey();

                final Post post = new Post(key ,Uid, id, title.getText().toString()  );
                mdatabase.child(key).setValue(post).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        Toast.makeText(Details_Request.this, "Added Successfully", Toast.LENGTH_SHORT).show();
                        nAdapter.notifyDataSetChanged();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Details_Request.this, "Error " + e.getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });

        // list.add(new Work("Architectural Draftsman","f you want to construct a new building for your business or a beautiful Custom Dream Home. To begin with, hire an architectural draftsman.  ",  23  ));

        // list.add(new Work("Architectural Draftsman","f you want to construct a new building for your business or a beautiful Custom Dream Home. To begin with, hire an architectural draftsman.  ",  23  ));
        final RecyclerView recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setItemViewCacheSize(50);
        recyclerView.setDrawingCacheEnabled(true);
        nAdapter = new PostAdapter(Details_Request.this, list, idUser) ;
        recyclerView.setAdapter(nAdapter);
        mdatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list.clear();


                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Post post = snapshot.getValue(Post.class);
                    if((id.equals(post.getRequest())) ) {
                        list.add(post);
                        nAdapter.notifyDataSetChanged();
                    }

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(Details_Request.this, "no data", Toast.LENGTH_SHORT).show();
            }
        });


    }
}