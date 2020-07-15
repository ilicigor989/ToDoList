package com.example.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddNoteActivity extends AppCompatActivity {


    NoteSQL noteSQL;
    EditText title;
    EditText subTitle;
    Button addButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        noteSQL = new NoteSQL(this);
         title = (EditText) findViewById(R.id.noteTitle);
         subTitle = (EditText) findViewById(R.id.noteSubtitle);

         addButton = (Button) findViewById(R.id.addButton);
        Button viewButton = (Button) findViewById(R.id.viewButton);

        title.addTextChangedListener(enterNewNote);
        subTitle.addTextChangedListener(enterNewNote);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String newTitle = title.getText().toString();
                String newSubitle = subTitle.getText().toString();
                int id;

                noteSQL.insertDataOne(newTitle,newSubitle,true);

               //addData(newTitle, newSubitle,null,true);
               Toast.makeText(getApplicationContext(),"Note successfully added!", Toast.LENGTH_LONG).show();
               title.setText("");
               subTitle.setText("");


                id=noteSQL.getID();
                Intent intent=new Intent(getBaseContext(), MainActivity.class);
                intent.putExtra("title",newTitle);
                intent.putExtra("description",newSubitle);
                intent.putExtra("typeOne",true);
                intent.putExtra("order",id);
                setResult(RESULT_OK, intent);
                finish();

            }
        });

        viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursor=noteSQL.getData();
                if (cursor.getCount()==0){
                    Toast.makeText(getApplicationContext(), "no data", Toast.LENGTH_LONG).show();
                }
                    while (cursor.moveToNext()){
                        Toast.makeText(getApplicationContext(), cursor.getString(1), Toast.LENGTH_SHORT).show();
                        Toast.makeText(getApplicationContext(), cursor.getString(2), Toast.LENGTH_SHORT).show();

                    }

            }
        });

    }

    private TextWatcher enterNewNote = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            String titleInput=title.getText().toString().trim();
            String subtitleInput=subTitle.getText().toString().trim();

            if (!titleInput.isEmpty() && !subtitleInput.isEmpty() ){
                addButton.setEnabled(true);
            }else addButton.setEnabled(false);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    public void addData(String title, String description,String thumbnailPath, boolean type) {

        noteSQL.insertDataTwo(title, description, thumbnailPath, type);

    }
}
