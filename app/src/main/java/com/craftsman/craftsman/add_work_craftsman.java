package com.craftsman.craftsman;

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

public class add_work_craftsman extends AppCompatActivity {
    EditText title, description, price;
    ProgressDialog dialog1;
    DatabaseReference mdatabase;
    String Uid;

    String link;
    StorageReference userImagesRef;
    ImageView img;

    private static final int PICK_IMG_REQUEST = 7588;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_work);
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        dialog1 = new ProgressDialog(add_work_craftsman.this);
        dialog1.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog1.setMessage("Please Wait ...");
        dialog1.setIndeterminate(true);
        dialog1.setCanceledOnTouchOutside(false);

        title  = (EditText) findViewById(R.id.title);
        description  = (EditText) findViewById(R.id.description);
        price = (EditText) findViewById(R.id.price);
        img = (ImageView) findViewById(R.id.img);

        SharedPreferences prefs = getSharedPreferences("data", 0);
        Uid = prefs.getString("Uid","");

        findViewById(R.id.upload_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
            }
        });


        findViewById(R.id.Add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Validion()) {
                    dialog1.show();
                    mdatabase = FirebaseDatabase.getInstance().getReference().child("Works");
                    String key = mdatabase.push().getKey();

                    final Work work = new Work(key ,Uid, title.getText().toString(), description.getText().toString()
                            , Integer.parseInt(price.getText().toString()), link );

                    mdatabase.child(key).setValue(work).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            dialog1.dismiss();
                            Toast.makeText(add_work_craftsman.this, "Added Successfully", Toast.LENGTH_SHORT).show();
                            finish();

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(add_work_craftsman.this, "Error " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            dialog1.dismiss();
                        }
                    });
                }
            }

        });

    }
    public boolean Validion() {

        if (title.getText().toString().trim().equals("")) {
            title.setError("needed *");
            title.requestFocus();
            return false;
        }
        if (description.getText().toString().trim().equals("")) {
            description.setError("needed *");
            description.requestFocus();
            return false;
        }
        if (price.getText().toString().trim().equals("")) {
            price.setError("needed *");
            price.requestFocus();
            return false;
        }


        return true;
    }

    private void pickImage() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, PICK_IMG_REQUEST);
    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);


        if (resultCode == RESULT_OK) {

            final Uri imageUri = data.getData();
            upload(imageUri);


        } else {
            Toast.makeText(add_work_craftsman.this, "no image selected :/", Toast.LENGTH_SHORT).show();
        }
    }



    private void upload(Uri uri) {
        final ProgressDialog progressDialog = new ProgressDialog(add_work_craftsman.this);
        progressDialog.show();
        final String imageName = UUID.randomUUID().toString() + ".jpg";
        userImagesRef = FirebaseStorage.getInstance().getReference().child("userUid").child("imagesRes");
        userImagesRef.child(imageName).putFile(uri)
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        int progress = (int) ((100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount());
                        progressDialog.setMessage(progress + "");
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                        progressDialog.dismiss();

                        if (task.isSuccessful()) {
                            //     String link = task.getResult().toString();

                            Task<Uri> urlTask = task.getResult().getStorage().getDownloadUrl();
                            while (!urlTask.isSuccessful()) ;
                            Uri downloadUrl = urlTask.getResult();

                            link = String.valueOf(downloadUrl);
                            String path = task.getResult().getStorage().getPath();



                            Picasso.get().load(link).into(img);
                            img.setVisibility(View.VISIBLE);

                            Toast.makeText(add_work_craftsman.this, "Uplaod Succeed", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.d("3llomi", "upload Failed " + task.getException().getLocalizedMessage());
                            Toast.makeText(add_work_craftsman.this, "Uplaod Failed :( " + task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }


}