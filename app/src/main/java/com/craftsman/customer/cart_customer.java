package com.craftsman.customer;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.UiAutomation;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.craftsman.R;
import com.craftsman.adapter.CartAdapter;
import com.craftsman.adapter.WorkCraftsmanAdapter;
import com.craftsman.model.Work;
import com.craftsman.model.payment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class cart_customer extends AppCompatActivity {
    ArrayList list = new ArrayList<Work>();
    DatabaseReference mdatabase;
    ProgressDialog dialog2;

    String Uid = "" , id_craftsman = "";
    CartAdapter nAdapter;
    RadioButton radio_cash, radio_visa;
    String type_payment = "" , number_card  ;
    EditText home_number, addres;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        home_number = findViewById(R.id.home_number);
        addres = findViewById(R.id.home_address);
        SharedPreferences prefs = getSharedPreferences("data", 0);
        Uid = prefs.getString("Uid","");

        dialog2 = new ProgressDialog(cart_customer.this);
        dialog2.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog2.setMessage("Please Wait ... ");
        dialog2.setIndeterminate(true);
        dialog2.setCanceledOnTouchOutside(false);

           final RecyclerView recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setItemViewCacheSize(50);
        recyclerView.setDrawingCacheEnabled(true);
          nAdapter = new CartAdapter(cart_customer.this, list);
        recyclerView.setAdapter(nAdapter);
        mdatabase = FirebaseDatabase.getInstance().getReference().child("Cart") ;
        mdatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                list.clear();

                   dialog2.show();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Work work = snapshot.getValue(Work.class);

                 //   if(work.getUID().equals(Uid)) {
                        list.add(work);
                        nAdapter.notifyDataSetChanged();
                  //  }
                    dialog2.dismiss();
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                dialog2.dismiss();
                Toast.makeText(cart_customer.this, "no data", Toast.LENGTH_SHORT).show();
            }
        });

        radio_cash = (RadioButton) findViewById(R.id.radio_cash);
        radio_visa = (RadioButton) findViewById(R.id.radio_visa);
        radio_cash.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    type_payment = "cash";


                }
            }
        });
        radio_visa.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    type_payment = "visa";

                    showDialog(cart_customer.this);
                }
            }
        });

        findViewById(R.id.done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                payment pay = new payment();
                pay.setList(list);
                pay.setId(Uid);
                pay.setAddress(addres.getText().toString());
                pay.setType_payment(type_payment);


                mdatabase = FirebaseDatabase.getInstance().getReference().child("payment") ;
                mdatabase.setValue(pay);
                for(int i = 0 ; i <= list.size() ; i ++){
                    FirebaseDatabase.getInstance().getReference().
                            child("Cart").child(Uid).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            Toast.makeText(cart_customer.this, "Delete Successfully", Toast.LENGTH_SHORT).show();
                            list.clear();
                            nAdapter.notifyDataSetChanged();

                        }
                    });

                }

            }
        });
    }

    public void showDialog(Activity activity ){
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.activity_payment);


        EditText card_number = dialog.findViewById(R.id.ca);
        Button dialogButton = (Button) dialog.findViewById(R.id.Add);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                number_card = card_number.getText().toString();
                dialog.dismiss();
            }
        });

        dialog.show();

    }
}