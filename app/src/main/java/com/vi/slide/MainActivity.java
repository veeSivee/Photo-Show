package com.vi.slide;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sangcomz.fishbun.FishBun;
import com.sangcomz.fishbun.define.Define;

import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView tvListFile;
    private ImageView ivImg;
    private Button btnRepeat, btnPlay;
    private RelativeLayout rlFolderCenter;
    private ArrayList<String> pathImage;
    private int countImage = 0;
    private boolean isRepeat = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(isLandscape()){
            setContentView(R.layout.activity_main_landscape);
        } else {
            setContentView(R.layout.activity_main);
        }

        init();
    }

    private void init(){
        tvListFile = (TextView)findViewById(R.id.tv_list_file);
        ivImg = (ImageView)findViewById(R.id.iv_img);
        btnRepeat = (Button)findViewById(R.id.btn_repeat);
        rlFolderCenter = (RelativeLayout)findViewById(R.id.rl_folder_center);
        btnPlay = (Button)findViewById(R.id.btn_play);

        pathImage = new ArrayList<>();
    }

    public void chooseFolder(View view) {

        //openDirectory();
        //openFolder();
        tesFisbun();
    }

    private void openDirectory(){

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,
                "Select Picture"), ConstantVar.REQUEST_IMAGE);
    }

    public void openFolder()
    {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath()
                + "/myFolder/");
        intent.setDataAndType(uri, "text/csv");
        startActivity(Intent.createChooser(intent, "Open folder"));
    }

    private void tesFisbun(){
        FishBun.with(MainActivity.this)
                .setActionBarColor(Color.BLACK, Color.BLUE)
                .setPickerCount(12)
                .setPickerSpanCount(5)
                .setRequestCode(11) //request code is 11. default == Define.ALBUM_REQUEST_CODE(27)
                .setCamera(true)
                .textOnImagesSelectionLimitReached("Limit Reached!")
                .textOnNothingSelected("Nothing Selected")
                .setReachLimitAutomaticClose(true)
                .setAlbumSpanCount(2, 4)
                .startAlbum();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if(resultCode == Activity.RESULT_OK)

            btnPlay.setBackgroundResource(R.drawable.ic_play);
            tvListFile.setVisibility(View.GONE);
            rlFolderCenter.setVisibility(View.GONE);

                switch (requestCode){
                    case ConstantVar.REQUEST_IMAGE:
                        tvListFile.setText(data.getData().toString());
                        break;

                    case 11:
                        List<String> path = data.getStringArrayListExtra(Define.INTENT_PATH);

                        pathImage.clear();
                        pathImage.addAll(path);

                        showImage(pathImage.get(0));
                        break;
        }
    }

    public void playSlide(View view) {
        if(pathImage.size()<=0){
            tvListFile.setText("You don't have image to show..\nClick folder icon to choose images");
        } else {
            fullscreenMode(view);
        }
    }

    private void showImage(String img){
        File imgFile = new  File(img);
        Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
        ivImg.setImageBitmap(myBitmap);
        countImage++;
    }

    public void repeatSlide(View view) {

        if(isRepeat){
            isRepeat = false;
            btnRepeat.setBackgroundResource(R.drawable.ic_repeat_grey);
        } else {
            isRepeat = true;
            btnRepeat.setBackgroundResource(R.drawable.ic_repeat_blue_sky);
        }
    }

    public void fullscreenMode(View view) {

        Intent intent = new Intent(this, FullscreenMode.class);
        intent.putStringArrayListExtra("array",pathImage);
        intent.putExtra("isRepeat",isRepeat);
        startActivity(intent);
    }

    private boolean isLandscape(){
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;
        int width = displaymetrics.widthPixels;

        boolean isLandscape = true;

        if(height > width){
            isLandscape = false;
        }
        return isLandscape;
    }
}
