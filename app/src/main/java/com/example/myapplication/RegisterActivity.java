package com.example.myapplication;

import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

public class RegisterActivity extends AppCompatActivity {

    RadioButton privateAcc;
    RadioButton businessAcc;
    RadioGroup rg;
    LinearLayout myLayout1;
    LinearLayout myLayout2;

    TextView genderTextView;
    Switch gender;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Toolbar toolbar = findViewById(R.id.register_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        privateAcc = findViewById(R.id.radioButton);
        businessAcc = findViewById(R.id.radioButton2);
        rg = (RadioGroup) findViewById(R.id.radio_group);
        genderTextView=findViewById(R.id.text_gender);
        gender=findViewById(R.id.gender);

        myLayout1 = (LinearLayout) findViewById(R.id.business_lin);
        myLayout2 = (LinearLayout) findViewById(R.id.private_lin);

        account();



        privateAcc.setChecked(true);

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                if (privateAcc.isChecked()) {
                    for (int i = 0; i < myLayout1.getChildCount(); i++) {

                        View view = myLayout1.getChildAt(i);
                        view.setEnabled(false);

                    }
                    for (int i = 0; i < myLayout2.getChildCount(); i++) {

                        View view2 = myLayout2.getChildAt(i);
                        view2.setEnabled(true);

                    }

                }
                    else {
                    for (int i = 0; i < myLayout1.getChildCount(); i++) {

                        View view = myLayout1.getChildAt(i);
                        view.setEnabled(true);

                    }
                    for (int i = 0; i < myLayout2.getChildCount(); i++) {

                        View view2 = myLayout2.getChildAt(i);
                        view2.setEnabled(false);

                    }

                }

                }
        });

        gender.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                // true if the switch is in the On position
                if (isChecked==true){
                genderTextView.setText("Male");
            }
                else {
                    genderTextView.setText("Female");
                }
            }
        });

    }

    public void account(){
        for (int i = 0; i < myLayout1.getChildCount(); i++) {

            View view = myLayout1.getChildAt(i);
            view.setEnabled(false);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
