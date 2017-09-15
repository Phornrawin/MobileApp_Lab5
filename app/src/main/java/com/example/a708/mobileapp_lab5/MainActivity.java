package com.example.a708.mobileapp_lab5;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.imageprocessors.subfilters.ColorOverlaySubfilter;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView;

    static
    {
        System.loadLibrary("NativeImageProcessor");
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Uri uri = data.getData();

        imageView.setImageURI(uri);
    }

    public void onClickSelect(){
        Log.i("msg", "in onClickSelect method");
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 200);
        }

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 0);

    }
    public void onClickEdit(){
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();
        Bitmap immutableBitmap = Bitmap.createBitmap(drawable.getBitmap());
        Bitmap bitmap = immutableBitmap.copy(Bitmap.Config.ARGB_8888,true);

        Filter myFilter = new Filter();
        myFilter.addSubFilter(new ColorOverlaySubfilter(100, .2f, .2f, .0f));
        Bitmap outputImage = myFilter.processFilter(bitmap);

        imageView.setImageBitmap(outputImage);


    }
    public void initialize(){
        Button btnEdit = (Button) findViewById(R.id.btnEdit);
        Button btnSelect = (Button) findViewById(R.id.btnSelect);
        imageView = (ImageView) findViewById(R.id.imageView);
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickSelect();
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickEdit();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
    }
}
