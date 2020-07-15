package com.example.myapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    RecyclerView recyclerView;
    List<NoteModel> notes;
    MyAdapter myAdapter;
    private  int NUM_COL;
    private static final int FIRST_ACTIVITY_REQUEST_CODE = 1;
    private static final int SECOND_ACTIVITY_REQUEST_CODE = 2;
    NoteSQL noteSQL;
    ExifInterface exifInterface;

    private DrawerLayout dLayout;

    String filePath;
    String thumbNailFilePath;
    Bitmap bitmap;
    Bitmap rotatedBitmap;
    boolean rightType;
    Dialog editDialog;

    StaggeredGridLayoutManager staggeredGridLayoutManager;

    NoteSQL myNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dLayout=findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle=new ActionBarDrawerToggle(this,dLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        dLayout.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView=findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        noteSQL = new NoteSQL(this);
        editDialog = new Dialog(this);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), AddNoteActivity.class);
                startActivityForResult(intent, FIRST_ACTIVITY_REQUEST_CODE);
            }
        });

        NUM_COL=2;

        notes = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerId);
        myAdapter = new MyAdapter(this, notes,noteSQL);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(NUM_COL, LinearLayout.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        myNote = new NoteSQL(this);
        NoteModel noteModel;

        Cursor cursor = myNote.getData();
        if (cursor.getCount() == 0) {
            Toast.makeText(getApplicationContext(), "there is no data", Toast.LENGTH_LONG).show();
        } else {
            while (cursor.moveToNext()) {
                if (cursor.getInt(4) > 0){

                    rightType=cursor.getInt(4) > 0;
                    noteModel = new NoteModel(cursor.getString(1), cursor.getString(2), true,cursor.getInt(0));
                    notes.add(noteModel);
                }
                else if (cursor.getInt(4) == 0){
                    bitmap = BitmapFactory.decodeFile(cursor.getString(3));
                    noteModel = new NoteModel(cursor.getString(1),cursor.getString(2), cursor.getString(3), false,cursor.getInt(0));
                    notes.add(noteModel);
                }
            }
        }
        recyclerView.setAdapter(myAdapter);

    }

    @Override
    public void onBackPressed() {

        if (dLayout.isDrawerOpen(GravityCompat.START)){
            dLayout.closeDrawer(GravityCompat.START);
        }
        else {
        super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        switch (menuItem.getItemId()){
            case R.id.Profile:
                Intent intent=new Intent(this, ProfileActivity.class);
                startActivity(intent);
                dLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.Register:
                Intent intent2=new Intent(this, RegisterActivity.class);
                startActivity(intent2);
                dLayout.closeDrawer(GravityCompat.START);
                break;
            case R.id.LogIn:
                Intent intent3=new Intent(this, LoginActivity.class);
                startActivity(intent3);
                dLayout.closeDrawer(GravityCompat.START);
                break;

        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.pictureActivity) {

            Intent intent = new Intent(getBaseContext(), AddPictureNoteActicity.class);
            startActivityForResult(intent, SECOND_ACTIVITY_REQUEST_CODE);
            return true;
        }
        if(id == R.id.listView){

            if (NUM_COL == 2){
                NUM_COL=1;
                staggeredGridLayoutManager = new StaggeredGridLayoutManager(NUM_COL, LinearLayout.VERTICAL);
                recyclerView.setLayoutManager(staggeredGridLayoutManager);
                item.setIcon(ContextCompat.getDrawable(this, R.drawable.baseline_view_quilt_black_18dp));
            } else {
                NUM_COL=2;
            staggeredGridLayoutManager = new StaggeredGridLayoutManager(NUM_COL, LinearLayout.VERTICAL);
            recyclerView.setLayoutManager(staggeredGridLayoutManager);
            item.setIcon(ContextCompat.getDrawable(this, R.drawable.baseline_view_list_black_18dp));
            }

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        String title;
        String description;
        boolean type;
        int uniqu;

        if (requestCode == FIRST_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {

            title = data.getStringExtra("title");
            description = data.getStringExtra("description");
            type = data.getBooleanExtra("typeOne", true);
            uniqu=data.getIntExtra("order",0);


            NoteModel noteModel = new NoteModel(title, description, type,uniqu);
            notes.add(noteModel);
            myAdapter.notifyDataSetChanged();

        }
        if (requestCode == SECOND_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {

            title = data.getStringExtra("pictureTitle");
            type = data.getBooleanExtra("typeTwo", true);

            filePath = data.getStringExtra("pics");
            thumbNailFilePath = data.getStringExtra("picsTn");
            bitmap = BitmapFactory.decodeFile(filePath);
            uniqu=data.getIntExtra("order",0);

            NoteModel noteModel = new NoteModel(title, filePath, thumbNailFilePath, type,uniqu);
            notes.add(noteModel);
            myAdapter.notifyDataSetChanged();

        }
    }

    public Bitmap rotatePic(Bitmap bitmap) {
        try {
            exifInterface = new ExifInterface(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
        Matrix matrix2 = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix2.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix2.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix2.setRotate(270);
                break;
            default:
        }
        rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix2, true);
        return rotatedBitmap;

    }

    public void displayMessage(Context context, String message) {

        Toast.makeText(context, message, Toast.LENGTH_LONG).show();


    }



}
