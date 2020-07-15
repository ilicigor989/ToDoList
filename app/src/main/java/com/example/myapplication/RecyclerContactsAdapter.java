package com.example.myapplication;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.view.menu.MenuView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.textclassifier.TextClassification;
import android.widget.TextView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerContactsAdapter extends RecyclerView.Adapter<RecyclerContactsAdapter.MyViewHolder> {

    private Context mContext;
    private List<Contacts> contacts;
    private OnClickInterface onClickInterface;

    public RecyclerContactsAdapter(Context mContext, List<Contacts> contacts) {
        this.mContext = mContext;
        this.contacts = contacts;
        onClickInterface=(ProfileActivity)mContext;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_contact,viewGroup,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder viewHolder, int i) {

        viewHolder.contactEmail.setText(contacts.get(i).getEmail());
        viewHolder.contactName.setText(contacts.get(i).getName());
        viewHolder.contactImage.setImageDrawable(ContextCompat.getDrawable(mContext,contacts.get(i).getPicId()));

    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        CircleImageView contactImage;
        TextView contactName;
        TextView contactEmail;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            contactImage=itemView.findViewById(R.id.item_imageview);
            contactName=itemView.findViewById(R.id.item_name);
            contactEmail=itemView.findViewById(R.id.item_email);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            onClickInterface.onItemSelected(contacts.get(getAdapterPosition()), itemView);



        }
    }
}
