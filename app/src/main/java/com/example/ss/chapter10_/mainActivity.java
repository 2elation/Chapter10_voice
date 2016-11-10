package com.example.ss.chapter10_;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.AlarmClock;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class mainActivity extends AppCompatActivity {

    ArrayList<String> result;
    private TextView sentence;
    private ImageView imgCamera;
    private ImageView imgVoice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sentence  = (TextView) findViewById(R.id.tvSentence);
        imgCamera = (ImageView) findViewById(R.id.imgCamera);
        imgVoice  = (ImageView) findViewById(R.id.imgVoice);

        setEvent();
    }

    private void setEvent() {
        /*imgVoice.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                speech();
                return false;
            }
        });*/
        imgVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speech();
            }
        });
    }

    private void speech() {
        Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        i.putExtra(RecognizerIntent.EXTRA_PROMPT,"Say something..");

        try{
            startActivityForResult(i,1);
        }catch (ActivityNotFoundException e){
            Toast.makeText(getApplicationContext(),"Speech is not supported",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 1:
                if(resultCode == RESULT_OK && null != data) {
                    result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    sentence.setText(result.get(0));
                }
                if(result.get(0).contains("open camera")) {
                    try{
                        openCamera();
                    }catch (Exception e){
                        Toast.makeText(mainActivity.this, "Camera is not supported.", Toast.LENGTH_SHORT).show();
                    }
                }else if (result.get(0).contains("open alarm")) {
                    try{
                        openAlarm();
                    }catch (Exception e){
                        Toast.makeText(mainActivity.this, "Alarm is not supported.", Toast.LENGTH_SHORT).show();
                    }
                }else if (result.get(0).contains("phone call")) {
                    try{
                        openPhone();
                    }catch (Exception e){
                        Toast.makeText(mainActivity.this, "Phone is not supported.", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case 2:
                Bitmap bp = (Bitmap) data.getExtras().get("data");
                imgCamera.setImageBitmap(bp);
                break;
        }
    }

    private void openCamera() {
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(i,2);
    }

    public void openAlarm(){
        Intent i = new Intent(AlarmClock.ACTION_SET_ALARM);
        startActivity(i);
    }

    public void openPhone(){
        Intent i = new Intent(Intent.ACTION_DIAL);
        startActivity(i);
    }

    public void createAlarm(String message, int hour, int minutes){
        Intent i = new Intent(AlarmClock.ACTION_SET_ALARM)
                .putExtra(AlarmClock.EXTRA_MESSAGE, message)
                .putExtra(AlarmClock.EXTRA_HOUR, hour)
                .putExtra(AlarmClock.EXTRA_MINUTES, minutes);
        if(i.resolveActivity(getPackageManager()) != null){
            startActivity(i);
        }
    }

    public void dialPhoneNumber(String phoneNumber){
        Intent i = new Intent(Intent.ACTION_DIAL);
        i.setData(Uri.parse("tel: "+ phoneNumber));
        if(i.resolveActivity(getPackageManager()) != null){
            startActivity(i);
        }
    }

}
