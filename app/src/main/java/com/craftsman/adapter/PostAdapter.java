package com.craftsman.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.craftsman.MessageActivity;
import com.craftsman.R;
import com.craftsman.model.Post;
import com.craftsman.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Post> list;
    Context context;
    String  iduser;
    int  userType;

    public PostAdapter(Context context, List<Post> List1 , String iduser) {
        this.context = context;
        this.list = List1;
        this.iduser = iduser;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_post, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final Holder holder1 = (Holder) holder;
        SharedPreferences prefs = context.getSharedPreferences("data", 0);

        userType = prefs.getInt("user_type",0);


        final Post  item = list.get(position);
        holder1.title.setText(item.getTitle());
        holder1.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MessageActivity.class);
                intent.putExtra("userid", iduser);
                context.startActivity(intent);
            }
        });

        if(userType == 1){
           holder1.accept.setVisibility(View.VISIBLE);
            holder1.reject.setVisibility(View.VISIBLE);
        }else{
            holder1.accept.setVisibility(View.GONE);
            holder1.reject.setVisibility(View.GONE);
        }

        holder1.reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder1.dialog1.show();
                FirebaseDatabase.getInstance().getReference().
                        child("Post").child(item.getId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        holder1.dialog1.dismiss();
                        Toast.makeText(context, "reject Successfully", Toast.LENGTH_SHORT).show();
                        notifyDataSetChanged();
                        notifyItemRemoved(position);
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
        TextView title ;
        ProgressDialog dialog1;

        ImageButton accept, reject ;

        public Holder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            accept = itemView.findViewById(R.id.accept);
            reject = itemView.findViewById(R.id.reject);


            dialog1 = new ProgressDialog(context);
            dialog1.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog1.setMessage("Please Wait ...");
            dialog1.setIndeterminate(true);
            dialog1.setCanceledOnTouchOutside(false);
        }


    }

}






