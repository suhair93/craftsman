package com.craftsman;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.craftsman.craftsman.home_craftsman;
import com.craftsman.customer.MainActivity;
import com.craftsman.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    EditText Email ,Password ;
    String email, password;
    ProgressDialog dialog,dialog2;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListner;
    FirebaseUser mUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        findViewById(R.id.buttonRegister).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login.this, Register.class);
                startActivity(i);
            }
        });


        Email = (EditText) findViewById(R.id.editEmail);
        Password = (EditText) findViewById(R.id.editPassword);
        dialog = new ProgressDialog(this);
        dialog.setMessage("Login in please wait...");
        dialog.setIndeterminate(true);

        dialog2 = new ProgressDialog(this);
        dialog2.setMessage(" please wait...");
        dialog2.setIndeterminate(true);

        mAuth = FirebaseAuth.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();


        findViewById(R.id.buttonForget).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText taskEditText = new EditText(Login.this);
                AlertDialog dialog = new AlertDialog.Builder(Login.this)
                        .setTitle("Forgot Password")
                        .setMessage("Enter Your Email Address?")
                        .setView(taskEditText)
                        .setPositiveButton("Reset", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                String task = String.valueOf(taskEditText.getText());
                                if (!task.equals("")) {
                                    dialog2.show();
                                    FirebaseAuth.getInstance().sendPasswordResetEmail(task)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        dialog2.dismiss();
                                                        Toast.makeText(Login.this, "Email sent.", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        dialog2.dismiss();
                                                        Toast.makeText(Login.this, "Email Error", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                } else {
                                    Toast.makeText(Login.this, "plz Add Email Address", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog.show();
            }
        });



        findViewById(R.id.buttonLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userSign();
            }
        });
    }


    private void userSign() {
        email = Email.getText().toString().trim();
        password = Password.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            Email.setError("needed *");
            Email.requestFocus();
            Toast.makeText(Login.this, "Enter the correct Email", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(password)) {
            Password.setError("needed *");
            Password.requestFocus();
            Toast.makeText(Login.this, "Enter the correct password", Toast.LENGTH_SHORT).show();
            return;
        }

        dialog.show();

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {

                    dialog.dismiss();

                    Toast.makeText(Login.this, "Email Or password are wrong  ", Toast.LENGTH_SHORT).show();
                } else {
                    final FirebaseUser users = FirebaseAuth.getInstance().getCurrentUser();
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                User user = snapshot.getValue(User.class);
                                dialog.dismiss();
                                if (user.getEmail().equals(email)) {
                                    databaseReference.child(user.getId()).child("password").setValue(password);
                                    SharedPreferences.Editor editor = getSharedPreferences("data", 0).edit();
                                    editor.putString("Uid", user.getId());
                                    editor.putString("email", email);
                                    editor.putString("Category", user.getCategory());
                                    editor.putString("phone", user.getPhone());
                                    editor.putString("photo", user.getImage());
                                    editor.putString("City", user.getCity());
                                    editor.putInt("user_type", user.getUserType());

                                    editor.apply();



                                    if (user.getUserType() == 1) {
                                        Intent intent = new Intent(Login.this, MainActivity.class);
                                        intent.putExtra(user.getEmail(), email);
                                        intent.putExtra("Uid", users.getUid());
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                    } else {
                                        Intent intent = new Intent(Login.this, home_craftsman.class);
                                        intent.putExtra(user.getEmail(), email);
                                        intent.putExtra("Uid", users.getUid());
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                    }

                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            dialog.dismiss();

                            Toast.makeText(Login.this, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        });

    }
    @Override
    protected void onStart() {
        super.onStart();
        //removeAuthSateListner is used  in onStart function just for checking purposes,it helps in logging you out.
        mAuth.removeAuthStateListener(mAuthListner);

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuthListner != null) {
            mAuth.removeAuthStateListener(mAuthListner);
        }

    }

    @Override
    public void onBackPressed() {
        Login.super.finish();
    }

}