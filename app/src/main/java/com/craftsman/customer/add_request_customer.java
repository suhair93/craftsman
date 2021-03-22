package com.craftsman.customer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.craftsman.R;
import com.craftsman.model.Requests;
import com.craftsman.model.Work;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.UUID;

public class add_request_customer extends AppCompatActivity {
    EditText title, description ;
    ProgressDialog dialog1;
    DatabaseReference mdatabase;
    String Uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_request);
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        dialog1 = new ProgressDialog(add_request_customer.this);
        dialog1.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog1.setMessage("Please Wait ...");
        dialog1.setIndeterminate(true);
        dialog1.setCanceledOnTouchOutside(false);

        title  = (EditText) findViewById(R.id.title);
        description  = (EditText) findViewById(R.id.description);



        SharedPreferences prefs = getSharedPreferences("data", 0);
        Uid = prefs.getString("Uid","");




        findViewById(R.id.Add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    dialog1.show();
                    mdatabase = FirebaseDatabase.getInstance().getReference().child("requests");
                    String key = mdatabase.push().getKey();

                    final Requests request = new Requests(key ,Uid, title.getText().toString(), description.getText().toString()
                            );

                    mdatabase.child(key).setValue(request).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            dialog1.dismiss();
                            Toast.makeText(add_request_customer.this, "Added Successfully", Toast.LENGTH_SHORT).show();
                          Intent i = new Intent(add_request_customer.this , requests_in_customer.class);
                            startActivity(i);


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(add_request_customer.this, "Error " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            dialog1.dismiss();
                        }
                    });

            }

        });

    }







}