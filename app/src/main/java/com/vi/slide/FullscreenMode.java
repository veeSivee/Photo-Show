package com.vi.slide;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;

import jp.wasabeef.blurry.Blurry;

public class FullscreenMode extends AppCompatActivity {

    View clFullscreen;
    ImageView ivImgFullscreen;
    ArrayList<String> listPathImage;
    private CountDownTimer timer;
    private int countImage = 0;
    private boolean isRepeat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_fullscreen_mode);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        init();

        getData();

        startTimer();
    }

    private void init(){
        ivImgFullscreen = (ImageView)findViewById(R.id.iv_img_fullscreen);
        clFullscreen = (View) findViewById(R.id.cl_img);
    }

    private void getData(){
        listPathImage = getIntent().getStringArrayListExtra("array");
        isRepeat = getIntent().getBooleanExtra("isRepeat",false);
    }

    private void startTimer(){
        timer = new CountDownTimer(2000,1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                showImage(listPathImage.get(countImage));

                if(countImage<listPathImage.size()){
                    timer.start();
                } else {
                    countImage = 0;

                    if(isRepeat){
                        timer.start();
                    } else {
                        finish();
                    }
                }
            }
        }.start();

    }


    private void showImage(String img){
        clFullscreen.setBackground(Drawable.createFromPath(img));

        File imgFile = new  File(img);
        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
        ivImgFullscreen.setImageBitmap(myBitmap);
        countImage++;
    }

}
