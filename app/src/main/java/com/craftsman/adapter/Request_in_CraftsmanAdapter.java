package com.craftsman.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.craftsman.R;
import com.craftsman.craftsman.Details_Request;
import com.craftsman.model.Requests;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Request_in_CraftsmanAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Requests> list;
    Context context;
    DatabaseReference mdatabase;

    SharedPreferences prefs ;
    String UId ;
    public Request_in_CraftsmanAdapter(Context context, List<Requests> List1) {
        this.context = context;
        this.list = List1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_request, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final Holder holder1 = (Holder) holder;

        final Requests item = list.get(position);

        holder1.title.setText(item.getTitle());
        holder1.descrption.setText(item.getDescription());
        holder1.category.setText(item.getCategory());


        holder1.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(context, Details_Request.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Bundle b = new Bundle();
                b.putString("id", item.getUID()+"");


                i.putExtras(b);
                context.startActivity(i);

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
        TextView title,descrption,category;

        public Holder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);

            descrption = itemView.findViewById(R.id.description);
            category = itemView.findViewById(R.id.category);



        }


    }

}






