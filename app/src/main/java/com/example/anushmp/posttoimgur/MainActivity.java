package com.example.anushmp.posttoimgur;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    ImageView gallery;
    Button opengal;
    Button upload;
    Button uploadvid;
    private String imagePath;
    Button galforvid;
    private String videopath;
    TextView videoid;
    TextView imageid;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gallery = findViewById(R.id.imageView);
        opengal = findViewById(R.id.btnGallery);
        upload = findViewById(R.id.btnUpload);
        uploadvid = findViewById(R.id.uploadvid);
        galforvid = findViewById(R.id.galforvid);
        videoid = findViewById(R.id.videoid);
        imageid = findViewById(R.id.imageid);


        opengal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               if(ispermissiongranted()) {
                   opengallery();
               }else{
                   reqperm();

               }

            }


        });


        galforvid.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(ispermissiongranted()) {
                            opengalleryforvideo();
                        }else{
                            reqperm();

                        }

                    }
                }
        );


        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ApiService spi = Network.getInstance().create(ApiService.class);
                File file = new File(imagePath);


                RequestBody reqb = RequestBody.create(MediaType.parse("*/*"),file);//changed from image

                MultipartBody.Part part = MultipartBody.Part.createFormData("image",file.getName(),reqb);

               spi.uploadimage(part).enqueue(new Callback<Data>() {
                   @Override
                   public void onResponse(Call<Data> call, Response<Data> response) {
                       Toast.makeText(MainActivity.this, "done done done", Toast.LENGTH_SHORT).show();

                       Data dataobj = response.body();
                       Data__1 innerdataobj = dataobj.getData();

                       String link = innerdataobj.getLink();

                       imageid.setText(link);

                   }

                   @Override
                   public void onFailure(Call<Data> call, Throwable t) {

                       Log.d("anushlogsfr", t.getMessage());

                       Toast.makeText(MainActivity.this, "failed", Toast.LENGTH_SHORT).show();

                   }
               });


            }
        });




        uploadvid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ApiService api = Network.getInstance().create(ApiService.class);
                File file = new File(videopath);

                RequestBody reqb = RequestBody.create(MediaType.parse("*/*"), file);

                MultipartBody.Part part = MultipartBody.Part.createFormData("video",file.getName(),reqb);

                api.uploadVideo(part).enqueue(new Callback<Data>() {
                    @Override
                    public void onResponse(Call<Data> call, Response<Data> response) {
                        Toast.makeText(MainActivity.this, "video uploaded", Toast.LENGTH_SHORT).show();

                        Data out = response.body();
                        Data__1 innerobj = out.getData();

                        String setvidid = innerobj.getLink();

                        videoid.setText(setvidid);

                    }

                    @Override
                    public void onFailure(Call<Data> call, Throwable t) {

                        Log.d("anushlogsfr", t.getMessage());

                    }
                });


            }
        });



    }

    private void opengalleryforvideo() {

        Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Video.Media.EXTERNAL_CONTENT_URI);

        resultfromvideogal.launch(intent);


    }

    private void reqperm() {

        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},10);

    }

    private void opengallery() {

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        resultfromgal.launch(intent);// open gal ac


    }

    private ActivityResultLauncher<Intent> resultfromgal = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {


            Uri imageuri = result.getData().getData();

            try {
                InputStream is = getContentResolver().openInputStream(imageuri);
                gallery.setImageBitmap(BitmapFactory.decodeStream(is));

                getpathfromuri(imageuri);


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }


        }
    });


    private ActivityResultLauncher<Intent> resultfromvideogal = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            Uri viduri = result.getData().getData();

            getvideopathfromuri(viduri);
        }
    });


    private Cursor getpathfromuri(Uri selecteduri) {



        String[] filePath = {MediaStore.Images.Media.DATA};
        Cursor c = getContentResolver().query(selecteduri, filePath,
                null, null, null);

        c.moveToFirst();

        int columnIndex = c.getColumnIndex(filePath[0]);

        imagePath = c.getString(columnIndex);

        return c;



    }

    private Cursor getvideopathfromuri(Uri viduri){

        String[] filepath = {MediaStore.Video.Media.DATA};
        Cursor c = getContentResolver().query(viduri,filepath,null,null,null);
        c.moveToFirst();

        int columnIndex = c.getColumnIndex(filepath[0]);

        videopath = c.getString(columnIndex);

        return c;

    }

    private boolean ispermissiongranted() {

        return ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults[0] == PackageManager.PERMISSION_GRANTED){

            opengallery();

        }

    }
}