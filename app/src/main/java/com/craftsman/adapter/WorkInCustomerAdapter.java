package com.craftsman.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.craftsman.R;
import com.craftsman.craftsman.add_work_craftsman;
import com.craftsman.model.Work;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

public class WorkInCustomerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Work> list;
    Context context;
    DatabaseReference mdatabase;

    SharedPreferences prefs ;
    String UId ;
    public WorkInCustomerAdapter(Context context, List<Work> List1) {
        this.context = context;
        this.list = List1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_work_in_customer, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final Holder holder1 = (Holder) holder;

        final Work item = list.get(position);

        holder1.title.setText(item.getTitle());
        holder1.descrption.setText(item.getDescription());
        holder1.price.setText(Integer.toString(item.getPrice()) + "$");

        try {
           Picasso.get().load(item.getUpload_image()).placeholder(R.drawable.logo).into(holder1.img);
        } catch (Exception ex) {
        }

        holder1.add_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder1.dialog1.show();

                mdatabase = FirebaseDatabase.getInstance().getReference().child("Cart");
             //   String key = mdatabase.push().getKey();



                mdatabase.child(item.getId()).setValue(item).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        holder1.dialog1.dismiss();
                        Toast.makeText(context, "Added Successfully", Toast.LENGTH_SHORT).show();


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Error " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        holder1.dialog1.dismiss();
                    }
                });


            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }


    public class Holder extends RecyclerView.ViewHolder {
        TextView title,descrption,price;
        ProgressDialog dialog1;
        ImageView img;
        Button add_cart;
        public Holder(View itemView) {
            super(itemView);
            dialog1 = new ProgressDialog(context);
            dialog1.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog1.setMessage("Please Wait ...");
            dialog1.setIndeterminate(true);
            dialog1.setCanceledOnTouchOutside(false);

            title = itemView.findViewById(R.id.title);

            descrption = itemView.findViewById(R.id.description);
            price = itemView.findViewById(R.id.price);
            img = itemView.findViewById(R.id.img);
            add_cart = itemView.findViewById(R.id.add);

        }


    }

}






