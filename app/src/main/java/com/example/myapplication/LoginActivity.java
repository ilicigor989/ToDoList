package com.example.myapplication;

import android.support.design.widget.TabItem;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class LoginActivity extends AppCompatActivity {


    TabLayout tabLayout;
    ViewPager vp;
    PageAdapter pageAdapter;
    private TabItem quick,facebook,gmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Toolbar toolbar3 = findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar3);
        toolbar3.setTitle("Log In");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tabLayout=findViewById(R.id.tab_layout);
        quick=findViewById(R.id.quick_tab);
        facebook=findViewById(R.id.facebook_tab);
        gmail=findViewById(R.id.gmail_tab);
        vp=findViewById(R.id.view_pager);

        pageAdapter=new PageAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
        vp.setAdapter(pageAdapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                vp.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        vp.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

    }
}
