package com.craftsman.craftsman;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.UiAutomation;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.craftsman.R;
import com.craftsman.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class profile_craftsman extends AppCompatActivity {
    EditText name , Phone;
    TextView email;
    CircleImageView img;
    DatabaseReference mdatabase,Cdatabase;
    StorageReference userImagesRef;
    String link;
    ProgressDialog dialog;
    String Uid;
    String password = "" , type_craftsman = "";
    String city = "" , cvImg = "";
    ArrayList<String> listcities = new ArrayList<String>();
    Spinner cities;
    private static final int PICK_IMG_REQUEST = 7588;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_craftsman);
        name = findViewById(R.id.editName);
        email = findViewById(R.id.editEmail);

        Phone = findViewById(R.id.phone_number);

        img = findViewById(R.id.img);

        Button cvBtn = findViewById(R.id.view);
        cvBtn.setVisibility(View.VISIBLE);
        cities = findViewById(R.id.cities);

        listcities.add("Riyadh");
        listcities.add("Jedah");
        listcities.add("Dammam");
        listcities.add("Tabuk");


        ArrayAdapter<String> adaptercities = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, listcities);
        cities.setAdapter(adaptercities);

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        dialog = new ProgressDialog(this);
        dialog.setMessage(" please wait...");
        dialog.setIndeterminate(true);

        SharedPreferences prefs = getSharedPreferences("data", 0);
        Uid = prefs.getString("Uid","");
        mdatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(Uid);

        Cdatabase = FirebaseDatabase.getInstance().getReference().child("Craftsman").child(Uid);
        get_data();

        findViewById(R.id.img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
            }
        });
        findViewById(R.id.edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit();
            }
        });

        findViewById(R.id.view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(profile_craftsman.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.image_layout);


                ImageView img = dialog.findViewById(R.id.img);
                try {

                    Picasso.get().load(cvImg).into(img);


                } catch (Exception ex) {
                }
                Button dialogButton = (Button) dialog.findViewById(R.id.close);
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

    }

    private void get_data() {

        Cdatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final User user = dataSnapshot.getValue(User.class);
                if(user.getUserType() == 2) {
                    name.setText(user.getFullname());
                    email.setText(user.getEmail());
                    Phone.setText(user.getPhone());

                    password =  user.getPassword();
                    type_craftsman = user.getCategory();
                    try {
                        link = user.getImage();
                        cvImg = user.getCv();
                        Picasso.get().load(link).placeholder(R.drawable.adduser).into(img);


                    } catch (Exception ex) {
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    public void edit(){
       // String key = mdatabase.push().getKey();
        city = listcities.get(cities.getSelectedItemPosition());
        User user = new User(Uid, name.getText().toString(), email.getText().toString(),
                password, Phone.getText().toString(),
                2,  type_craftsman);



        user.setImage(link);

       mdatabase.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                dialog.dismiss();
                Cdatabase.setValue(user);
                Toast.makeText(profile_craftsman.this, "Edit Successfully", Toast.LENGTH_SHORT).show();
                finish();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(profile_craftsman.this, "Error " + e.getMessage(), Toast.LENGTH_SHORT).show();
                dialog.dismiss();

            }
        });
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
            Toast.makeText(profile_craftsman.this, "no image selected :/", Toast.LENGTH_SHORT).show();
        }
    }

    private void upload(Uri uri) {
        final ProgressDialog progressDialog = new ProgressDialog(profile_craftsman.this);
        progressDialog.show();
        final String imageName = UUID.randomUUID().toString() + ".jpg";
        userImagesRef = FirebaseStorage.getInstance().getReference().child("Users").child("image");
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

                            Toast.makeText(profile_craftsman.this, "Uplaod Succeed", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.d("3llomi", "upload Failed " + task.getException().getLocalizedMessage());
                            Toast.makeText(profile_craftsman.this, "Uplaod Failed :( " + task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }


}