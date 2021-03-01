package com.craftsman.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.craftsman.R;
import com.craftsman.model.User;
import com.squareup.picasso.Picasso;
import java.util.List;
import de.hdodenhof.circleimageview.CircleImageView;

public class CraftsmanAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<User> list;
    Context context;

    public CraftsmanAdapter(Context context, List<User> List1) {
        this.context = context;
        this.list = List1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new Holder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_craftsman, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final Holder holder1 = (Holder) holder;

        final User  item = list.get(position);

        holder1.title.setText(item.getFullname());
        holder1.category.setText(item.getCategory());

        try {
          Picasso.get().load(item.getImage()).placeholder(R.drawable.user).into(holder1.img);
        } catch (Exception ex) {
        }


//        holder1.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Intent i = new Intent(context, DetailsActivity.class);
//                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                Bundle b = new Bundle();
//                b.putString("id", orderModel.getUID()+"");
//                b.putString("title", orderModel.getTitle_User() + "");
//                b.putString("image", orderModel.getUpload_image() + "");
//                b.putString("des", orderModel.getDescription_User() + "");
//                b.putString("price", orderModel.getPrice() + "");
//                b.putString("Discount", orderModel.getDiscount() + "");
//                b.putString("Category", orderModel.getCategory() + "");
//                b.putString("UID", orderModel.getUID() + "");
//
//                i.putExtras(b);
//                context.startActivity(i);
//
//            }
//
//        });


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
        TextView title,category;
        ProgressDialog dialog1;
        CircleImageView img;

        public Holder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);

            category = itemView.findViewById(R.id.category);
            img = itemView.findViewById(R.id.img);
            dialog1 = new ProgressDialog(context);
            dialog1.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog1.setMessage("Please Wait ...");
            dialog1.setIndeterminate(true);
            dialog1.setCanceledOnTouchOutside(false);
        }


    }

}






