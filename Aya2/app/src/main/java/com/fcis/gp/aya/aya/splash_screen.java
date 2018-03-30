package com.fcis.gp.aya.aya;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class splash_screen extends AppCompatActivity {
ImageView splash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Thread th = new Thread(){
            @Override
            public void run() {
                try{
                    sleep(1500);
                   // ImageView imageView=findViewById(R.id.Splash_Image);
                    //Picasso.with(getApplicationContext()).load(R.drawable.splash).into(imageView);
                  //  imageView.setImageResource(R.drawable.splash);
                    Intent i = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(i);
                    finish();
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        };
        th.start();
    }
}
