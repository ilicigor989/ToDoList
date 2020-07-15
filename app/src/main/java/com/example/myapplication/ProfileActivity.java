package com.example.myapplication;

import android.support.design.chip.Chip;
import android.support.design.chip.ChipGroup;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProfileActivity extends AppCompatActivity implements OnClickInterface, View.OnClickListener {

    private RecyclerView recyclerView;
    private RecyclerContactsAdapter rca;
    private List<Contacts> contactsList=new ArrayList<>();
    ChipGroup chipGroup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);



        ViewPager vp= findViewById(R.id.add_spot);
        ImageAdapter imageAdapter=new ImageAdapter(this);

        recyclerView=findViewById(R.id.contacts_RV);
        chipGroup=findViewById(R.id.chip_group);


        vp.setAdapter(imageAdapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        getContacts();
        rca=new RecyclerContactsAdapter(this,contactsList);
        recyclerView.setAdapter(rca);


    }

    @Override
    public void onItemSelected(Contacts contacts, View view) {

        String chipEmail=contacts.getEmail();

        if (chipGroup.getChildCount()== 0){
            Chip chip=new Chip(this);
            chip.setText(contacts.getEmail());
            chip.setChipIcon(ContextCompat.getDrawable(this, contacts.getPicId()));
            chip.setCloseIconVisible(true);
            chip.setCheckable(false);
            chip.setClickable(false);
            chip.setOnCloseIconClickListener(this);
            chipGroup.addView(chip);
            chipGroup.setVisibility(View.VISIBLE);
        }
        else {
            for (int i=0; i<chipGroup.getChildCount();i++){
                int count=chipGroup.getChildCount();
                Chip chip2 = (Chip)chipGroup.getChildAt(i);

                if (chip2.getText() == chipEmail){
                    Toast.makeText(getApplicationContext(), "This contact is already added", Toast.LENGTH_SHORT).show();
                    break;
                }
                 if(i == (chipGroup.getChildCount())-1){
                    Chip chip=new Chip(this);
                    chip.setText(contacts.getEmail());
                    chip.setChipIcon(ContextCompat.getDrawable(this, contacts.getPicId()));
                    chip.setCloseIconVisible(true);
                    chip.setCheckable(false);
                    chip.setClickable(false);
                    chip.setOnCloseIconClickListener(this);
                    chipGroup.addView(chip);
                    chipGroup.setVisibility(View.VISIBLE);
                    i++;
                }

            }

        }

    }

    public void getContacts(){

        List<String>names= Arrays.asList(getResources().getStringArray(R.array.names));
        List<String>emails= Arrays.asList(getResources().getStringArray(R.array.emails));
        int[]pictureId={R.drawable.profilna,R.drawable.profil2,R.drawable.profil3,R.drawable.profil4,R.drawable.profil5,R.drawable.profil6,R.drawable.profil7,R.drawable.profil8};

        int count=0;

        for (String Name:names ){

                contactsList.add(new Contacts(Name, emails.get(count), pictureId[count]));

                count++;

            }

        }


    @Override
    public void onClick(View v) {

        Chip chip=(Chip) v;
        chipGroup.removeView(v);



    }
}
