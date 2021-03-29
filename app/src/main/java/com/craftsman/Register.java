package com.craftsman;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.craftsman.customer.profile_customer;
import com.craftsman.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Register extends AppCompatActivity {
    Spinner  TypeOfCraftsman ,cities;
    EditText name, email, password, Phone;
    RadioButton radio_Customer, radio_craftsman;
    ProgressDialog mDialog;
    FirebaseAuth mAuth;
    StorageReference userImagesRef;
    String link = "";
    private static final int PICK_IMG_REQUEST = 7588;
    DatabaseReference mdatabase ,mdatabase1 ;
    int user_type = 0;
    Button cv;
    String type_Craftsman = "" ;
    ArrayList<String> ListType = new ArrayList<String>();
    ArrayList<String> listcities = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = findViewById(R.id.editName);
        email = findViewById(R.id.editEmail);
        password = findViewById(R.id.editPassword);
        Phone = findViewById(R.id.phone_number);
        cv = findViewById(R.id.upload);


        findViewById(R.id.buttonLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Register.this, Login.class);
                startActivity(i);
            }
        });


        cities = findViewById(R.id.cities);

        listcities.add("select city");
        listcities.add("Riyadh");
        listcities.add("Jedah");
        listcities.add("Dammam");
        listcities.add("Tabuk");


        ArrayAdapter<String> adaptercities = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, listcities);
        cities.setAdapter(adaptercities);

        TypeOfCraftsman = findViewById(R.id.category);

        ListType.add("select type ");
       ListType.add("Draw");
       ListType.add("Carpentry");
        ArrayAdapter<String> adaptertype = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, ListType);
        TypeOfCraftsman.setAdapter(adaptertype);


        radio_Customer = (RadioButton) findViewById(R.id.radio_C);
        radio_craftsman = (RadioButton) findViewById(R.id.radio_P);
        radio_Customer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    user_type = 1;
                    TypeOfCraftsman.setVisibility(View.GONE);
                    cv.setVisibility(View.GONE);

                }
            }
        });
        radio_craftsman.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    user_type = 2;
                    TypeOfCraftsman.setVisibility(View.VISIBLE);
                    cv.setVisibility(View.VISIBLE);
                }
            }
        });

        findViewById(R.id.buttonRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validtion1())
                    UserRegister(email.getText().toString().trim(),password.getText().toString().trim());
            }
        });

        cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
            }
        });
    }


    private void UserRegister(String Email, String Password) {

        mAuth = FirebaseAuth.getInstance();
        mDialog = new ProgressDialog(this);
        mdatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        mdatabase1 = FirebaseDatabase.getInstance().getReference().child("Craftsman");

        mDialog.setMessage("Creating User please wait...");
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();
        mAuth.createUserWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    //   sendEmailVerification();
                    mDialog.dismiss();
                    User user = new User(task.getResult().getUser().getUid(), name.getText().toString(), Email,
                            password.getText().toString(), Phone.getText().toString(),
                            user_type,  ListType.get(TypeOfCraftsman.getSelectedItemPosition()),listcities.get(cities.getSelectedItemPosition()) ,link  );
                   // user.setCity(listcities.get(cities.getSelectedItemPosition()));


                    mdatabase.child(task.getResult().getUser().getUid()).setValue(user);
                    if(user_type == 2){
                       // user.setCv(link);
                        mdatabase1.child(task.getResult().getUser().getUid()).setValue(user);
                    }

                    mAuth.signOut();
                    Toast.makeText(Register.this, "Successfully", Toast.LENGTH_SHORT).show();
                    //FirebaseAuth.getInstance().signOut();
                    finish();
                } else {
                    mDialog.dismiss();
                    Toast.makeText(Register.this, "error on creating user", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public boolean validtion1() {



        if (TextUtils.isEmpty(name.getText().toString().trim())) {
            Toast.makeText(Register.this, "Enter Name", Toast.LENGTH_SHORT).show();
            name.requestFocus();
            return false;
        } else if (TextUtils.isEmpty(email.getText().toString().trim())) {
            Toast.makeText(Register.this, "Enter Email", Toast.LENGTH_SHORT).show();
            email.requestFocus();
            return false;

        } else if ((password.getText().toString().trim()).length() < 8) {
            Toast.makeText(Register.this, "Password must be greater then 8 digit ", Toast.LENGTH_SHORT).show();
            return false;
        } else if ((!isValidPassword(password.getText().toString().trim()))) {
            Toast.makeText(Register.this, "Password must be  contain Upercase character ,numeric and symbol ", Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(Phone.getText().toString().trim()) || (Phone.getText().toString().trim()).length() != 10 ) {
            Toast.makeText(Register.this, "Enter correct Phone number", Toast.LENGTH_SHORT).show();
            Phone.requestFocus();
            return false;
        } else if (user_type == 0) {
            Toast.makeText(Register.this, "choice User Type", Toast.LENGTH_SHORT).show();
            return false;
        }


        return true;
    }

    public static boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

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
            Toast.makeText(Register.this, "no image selected :/", Toast.LENGTH_SHORT).show();
        }
    }

    private void upload(Uri uri) {
        final ProgressDialog progressDialog = new ProgressDialog(Register.this);
        progressDialog.show();
        final String imageName = UUID.randomUUID().toString() + ".jpg";
        userImagesRef = FirebaseStorage.getInstance().getReference().child("Users").child("cv");
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




                           // Picasso.get().load(link).into(img);


                            Toast.makeText(Register.this, "Uplaod Succeed", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.d("3llomi", "upload Failed " + task.getException().getLocalizedMessage());
                            Toast.makeText(Register                                                 .this, "Uplaod Failed :( " + task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

}
