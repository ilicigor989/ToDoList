package com.example.myapplication;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


public class AddPictureNoteActicity extends AppCompatActivity {

    EditText pictureDes;
    Button picButton;
    ImageView picView;
    private final int PICK_IMAGE_GALLERY = 2;
    private final int PICK_IMAGE_CAMERA = 1;
    private final int CAMERA_PERMISSION_REQUEST = 3;
    private final int GALLERY_PERMISSION_REQUEST = 4;
    static Bitmap bitmap;
    Uri uri;
    Uri pictureUri;
    File pictureFile;
    String myCurrentFilePath;
    ExifInterface exifInterface;
    Bitmap rotatedBitmap;
    public String myThumbNailFilePath;
    String time;
    final static int IMAGE_WIDTH = 250;
    final static int IMAGE_HEIGHT = 300;

    NoteSQL noteSQL;
    SimpleDateFormat sdf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_picture_note_acticity);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        noteSQL = new NoteSQL(this);
        pictureDes = (EditText) findViewById(R.id.pictureText);
        picButton = (Button) findViewById(R.id.button);
        picView = (ImageView) findViewById(R.id.imageView);

        sdf = new SimpleDateFormat("yyyyDDmm_HHmmss");
        time = sdf.format(new Date());

        //pictureDes.addTextChangedListener(enterPictureDescription);

        picButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String root = Environment.getExternalStorageDirectory().toString();
                File myDir = new File(root + "/DCIM" + "/.thumbnails");
                if (!myDir.exists()) {
                    myDir.mkdirs();
                }

                String fname = "Image-" + time + ".jpg";
                File file = new File(myDir, fname);

                try {
                    FileOutputStream out = new FileOutputStream(file);
                    rotateBitmap(decodeBitmapFromFile(myCurrentFilePath)).compress(Bitmap.CompressFormat.JPEG, 90, out);
                    out.flush();
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                myThumbNailFilePath = file.getAbsolutePath();

                String picTitle = pictureDes.getText().toString();

                if(picTitle.length() <= 5){
                    Toast.makeText(getApplicationContext(), "You need at least 6 characters for description", Toast.LENGTH_LONG).show();
                } else {
                    addData(picTitle, myCurrentFilePath, myThumbNailFilePath, false);
                    Toast.makeText(getApplicationContext(), "Note successfully added!", Toast.LENGTH_LONG).show();

                    int id=noteSQL.getID();

                    Intent intent = new Intent(getBaseContext(), MainActivity.class);
                    intent.putExtra("pictureTitle", picTitle);
                    intent.putExtra("pics", myCurrentFilePath);
                    intent.putExtra("picsTn", myThumbNailFilePath);
                    intent.putExtra("typeTwo", false);
                    intent.putExtra("order",id);

                    setResult(RESULT_OK, intent);
                    finish();
                }

            }
        });

        picView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(AddPictureNoteActicity.this);
                final String[] items = new String[]{"Choose from Gallery", "Take a Picture"};
                builder.setTitle("Picture Destination");
                builder.setItems(items, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String[] permission = { Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE};
                        String[] permissionGallery = {Manifest.permission.READ_EXTERNAL_STORAGE};

                        if (items[which].equals("Choose from Gallery")) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                if (ContextCompat.checkSelfPermission(getApplicationContext(),permissionGallery[0])== PackageManager.PERMISSION_GRANTED) {

                                    dialog.dismiss();
                                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                    startActivityForResult(pickPhoto, PICK_IMAGE_GALLERY);

                                } else {

                                    requestPermissions(permissionGallery, GALLERY_PERMISSION_REQUEST);
                                }
                            }
                        }

                        if (items[which].equals("Take a Picture")) {

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                if (ContextCompat.checkSelfPermission(getApplicationContext(),permission[0])== PackageManager.PERMISSION_GRANTED
                                        && ContextCompat.checkSelfPermission(getApplicationContext(),permission[1])== PackageManager.PERMISSION_GRANTED) {
                                    invokeCamera();
                                } else {
                                    requestPermissions(permission, CAMERA_PERMISSION_REQUEST);
                                    invokeCamera();
                                }
                            }

                        }
                    }

                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CAMERA_PERMISSION_REQUEST) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&grantResults.length > 0 && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_SHORT).show();
                invokeCamera();
            }
        }
        else if (requestCode == GALLERY_PERMISSION_REQUEST){

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto, PICK_IMAGE_GALLERY);
            }

        }
    }

    private void invokeCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null)
            try {
                pictureFile = createImageFile();

                if (pictureFile != null) {
                    pictureUri = FileProvider.getUriForFile(this, "com.example.myapplication.provider", pictureFile);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, pictureUri);
                    startActivityForResult(intent, PICK_IMAGE_CAMERA);
                }
            } catch (Exception E) {
            }
    }


    public File createImageFile() {

        File pictureDirectory = new File(Environment.getExternalStorageDirectory(), "MyImages");

        if (!pictureDirectory.exists()) {
            pictureDirectory.mkdirs();
        }

        time = sdf.format(new Date());
        try {
            pictureFile = File.createTempFile("IMG", time + ".jpg", pictureDirectory);
        } catch (IOException e) {
            e.printStackTrace();
        }
        myCurrentFilePath = pictureFile.getAbsolutePath();
        return pictureFile;

    }

    public Bitmap rotateBitmap(Bitmap bitmap) {
        try {
            exifInterface = new ExifInterface(myCurrentFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(270);
                break;
            default:
        }
        rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return rotatedBitmap;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_GALLERY && resultCode == RESULT_OK && data != null) {
            uri = data.getData();
            myCurrentFilePath = getRealPathFromURI(getApplicationContext(), uri);
            picView.setImageURI(uri);
            if (!(uri==null)){
                picButton.setEnabled(true);
            }


        } else if (requestCode == PICK_IMAGE_CAMERA && resultCode == RESULT_OK) {
            uri = Uri.fromFile(new File(myCurrentFilePath));
            decodeBitmapFromFile(myCurrentFilePath);

            bitmap = BitmapFactory.decodeFile(myCurrentFilePath);
            picView.setImageBitmap(bitmap);

            if (!(bitmap ==null)){
                picButton.setEnabled(true);
            }

        }

    }

    private int calcalateInSampleSize(BitmapFactory.Options bmOptions) {

        final int bitmapWidth = bmOptions.outWidth;
        final int bitmapHight = bmOptions.outWidth;
        int scaleFactor = 1;

        if (bitmapWidth > IMAGE_WIDTH || bitmapHight > IMAGE_HEIGHT) {

            final int halfPhotoWidth = bitmapWidth / 2;
            final int halfPhotoHeight = bitmapWidth / 2;
            while (halfPhotoHeight / scaleFactor > IMAGE_HEIGHT || halfPhotoWidth / scaleFactor > IMAGE_WIDTH) {
                scaleFactor *= 2;
            }

        }

        return scaleFactor;
    }

    private Bitmap decodeBitmapFromFile(String file) {

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file, bmOptions);
        bmOptions.inSampleSize = calcalateInSampleSize(bmOptions);
        bmOptions.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(file, bmOptions);
    }

    public void addData(String title, String description, String thumbnailPath, boolean type) {

        noteSQL.insertDataTwo(title, description, thumbnailPath, type);

    }

    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

}
