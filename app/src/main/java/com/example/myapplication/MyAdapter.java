package com.example.myapplication;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private Context context;
    private List<NoteModel> myNotes;
    private  final int layout_first=0;
    private  final int layout_second=1;
    private LayoutInflater inflater;
    private NoteSQL noteSQL;

    public MyAdapter(Context context, List<NoteModel> myNotes, NoteSQL noteSQL) {
        this.context = context;
        this.myNotes = myNotes;
        this.noteSQL=noteSQL;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        this.context=viewGroup.getContext();
        inflater=LayoutInflater.from(context);
        RecyclerView.ViewHolder viewHolder = null;
        switch (i){

             case layout_first:

                 View view1=inflater.inflate(R.layout.item_layout_one,viewGroup,false);
                 viewHolder =new ViewHandlerOne(view1);
                 break;

             case layout_second:

                 View view2=inflater.inflate(R.layout.item_layout_two,viewGroup,false);
                 viewHolder= new ViewHandlerTwo(view2);
                 break;
         }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder viewHolder, final int i) {

        switch (viewHolder.getItemViewType()){

            case layout_first:

                ViewHandlerOne myViewHandlerOne = (ViewHandlerOne)viewHolder;
                myViewHandlerOne.setTitleText(myNotes.get(i).getTitle());
                myViewHandlerOne.setSubtitleText(myNotes.get(i).getSubtitle());


                break;

                case layout_second: {

                    ViewHandlerTwo myViewHandlerTwo = (ViewHandlerTwo) viewHolder;
                    myViewHandlerTwo.setTitleText(myNotes.get(i).getTitle());
                    myViewHandlerTwo.setBitmap(myNotes.get(i).getBitmapPath());

                }
                break;

            default:
        }

    }

    @Override
    public int getItemCount() {
        return myNotes.size();
    }

    @Override
    public int getItemViewType(int position) {

        if (myNotes.get(position).getType()){

            return layout_first;
        }

        else if (!myNotes.get(position).getType()){
            return layout_second;
        }

        return -1;
    }



    public class ViewHandlerOne extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener{


        TextView titleText;
        TextView subtitleText;
        CheckBox checkBox;


        public ViewHandlerOne(@NonNull final View itemView) {
            super(itemView);


            titleText=itemView.findViewById(R.id.titleTextView);
            subtitleText=itemView.findViewById(R.id.subtitleTextView);
            checkBox=itemView.findViewById(R.id.check);
            itemView.setOnLongClickListener(this);
            itemView.setOnClickListener(this);
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (checkBox.isChecked()){
                        checkBox.setText("Task is finished");
                        Toast.makeText(context, "Task is finished", Toast.LENGTH_LONG).show();
                    }
                    else if (!(checkBox.isChecked())){
                        checkBox.setText("Task is in progress");
                        Toast.makeText(context, "Task is in progress", Toast.LENGTH_LONG).show();}

                }
            });

        }

        public void setTitleText(String title) {
            titleText.setText(title);
        }

        public void setSubtitleText(String sub) {
            subtitleText.setText(sub);
        }
        @Override
        public boolean onLongClick(View v) {
            dialogBox(getAdapterPosition());
            return true;
        }
        @Override
        public void onClick(View v) {

            int id;
            String title;
            String subtitle;

            id=myNotes.get(getAdapterPosition()).getId();
            title=myNotes.get(getAdapterPosition()).getTitle();
            subtitle=myNotes.get(getAdapterPosition()).getSubtitle();
            Toast.makeText(context, id + " " + title + " " + subtitle , Toast.LENGTH_SHORT).show();

        }

    }

    public class ViewHandlerTwo extends RecyclerView.ViewHolder implements View.OnLongClickListener,View.OnClickListener {

        TextView titleText;
        public ImageView imageView;

        public ViewHandlerTwo(@NonNull View itemView) {
            super(itemView);

            titleText=itemView.findViewById(R.id.textView2);
            imageView=itemView.findViewById(R.id.imageView2);
            itemView.setOnLongClickListener(this);
            itemView.setOnClickListener(this);

        }
        public void setTitleText(String title) {
            titleText.setText(title);
        }
        public void setBitmap(String path){
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            Bitmap bitmap = BitmapFactory.decodeFile(path,bmOptions);
            imageView.setImageBitmap(bitmap);
        }

        @Override
        public boolean onLongClick(View v) {
            dialogBox(getAdapterPosition());
            return true;
        }
        @Override
        public void onClick(View v) {

            int id;
            String title;
            String subtitle;

            id=myNotes.get(getAdapterPosition()).getId();
            title=myNotes.get(getAdapterPosition()).getTitle();
            subtitle=myNotes.get(getAdapterPosition()).getSubtitle();

            Toast.makeText(context, id + " " + title + " " + subtitle , Toast.LENGTH_SHORT).show();

        }
    }

    public void dialogBox(final int itemPosition){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setMessage("Are you sure you want to DELETE this Note?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        removeAt(itemPosition);
                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    public void removeAt(int position) {

        noteSQL.deleteRow(myNotes.get(position).getId());
        myNotes.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, myNotes.size());

    }




}
