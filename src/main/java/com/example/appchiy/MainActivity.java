package com.example.appchiy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import java.io.IOException;
import java.util.Random;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class MainActivity extends AppCompatActivity {
    private Button startbtn, stopbtn, playbtn, pausebtn, resumebtn,record_video,play_video,stop_video;
    private MediaRecorder mRecorder;
    private MediaPlayer mPlayer;
    private static final String LOG_TAG = "AudioRecording";
    private static String mFileName = null, mp4file=null;
    private static int id = 0;
    public static final int REQUEST_AUDIO_PERMISSION_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startbtn = (Button)findViewById(R.id.btnRecord);
        pausebtn = (Button)findViewById(R.id.btnPause);
        resumebtn = (Button)findViewById(R.id.btnResume);
        stopbtn = (Button)findViewById(R.id.btnStop);
        playbtn = (Button)findViewById(R.id.btnPlay);

        playbtn.setEnabled(false);
        resumebtn.setEnabled(false);
        pausebtn.setEnabled(false);
        stopbtn.setEnabled(false);

        startbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CheckPermissions()) {
                    id=1;
                    startbtn.setEnabled(false);
                    playbtn.setEnabled(false);
                    resumebtn.setEnabled(false);
                    pausebtn.setEnabled(true);
                    stopbtn.setEnabled(true);

                    Random random= new Random();
                    mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
                    mFileName += "/AudioRecording"+ random.nextInt(100)+".3gp";

                    mRecorder = new MediaRecorder();
                    mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                    mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                    mRecorder.setOutputFile(mFileName);
                    try {
                        mRecorder.prepare();
                    } catch (IOException e) {
                        Log.e(LOG_TAG, "prepare() failed");
                    }
                    mRecorder.start();
                    Toast.makeText(getApplicationContext(), "Recording Started", Toast.LENGTH_LONG).show();
                }
                else
                {
                    RequestPermissions();
                }
            }
        });

        pausebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startbtn.setEnabled(false);
                playbtn.setEnabled(false);
                resumebtn.setEnabled(true);
                pausebtn.setEnabled(false);
                stopbtn.setEnabled(true);


                if (id == 1) {
                    mRecorder.pause();
                    Toast.makeText(getApplicationContext(), "Recording Paused", Toast.LENGTH_LONG).show();
                }
                if(id == 2){
                    mPlayer.pause();
                    Toast.makeText(getApplicationContext(), "Playing Audio Paused", Toast.LENGTH_SHORT).show();
                }
            }
        });

        resumebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startbtn.setEnabled(false);
                playbtn.setEnabled(false);
                resumebtn.setEnabled(false);
                pausebtn.setEnabled(true);
                stopbtn.setEnabled(true);


               if(id == 1){
                   mRecorder.resume();
                   Toast.makeText(getApplicationContext(), "Recording Resumed", Toast.LENGTH_LONG).show();
               }
               if(id == 2){
                   mPlayer.start();
                   Toast.makeText(getApplicationContext(), "Playing Audio Paused", Toast.LENGTH_SHORT).show();
               }

            }
        });

        stopbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startbtn.setEnabled(true);
                playbtn.setEnabled(true);
                resumebtn.setEnabled(false);
                pausebtn.setEnabled(false);
                stopbtn.setEnabled(false);


                if(id == 1){
                    mRecorder.stop();
                    mRecorder.release();
                    mRecorder = null;
                    Toast.makeText(getApplicationContext(), "Recording Stopped", Toast.LENGTH_LONG).show();
                }
                if(id == 2){
                    mPlayer.release();
                    mPlayer = null;
                    Toast.makeText(getApplicationContext(),"Playing Audio Stopped", Toast.LENGTH_SHORT).show();
                }
            }
        });

        playbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                id=2;
                startbtn.setEnabled(false);
                playbtn.setEnabled(false);
                resumebtn.setEnabled(false);
                pausebtn.setEnabled(true);
                stopbtn.setEnabled(true);


                mPlayer = new MediaPlayer();
                try {
                    mPlayer.setDataSource(mFileName);
                    mPlayer.prepare();
                    mPlayer.start();
                    Toast.makeText(getApplicationContext(), "Recording Started Playing", Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    Log.e(LOG_TAG, "prepare() failed");
                }
            }
        });

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_AUDIO_PERMISSION_CODE:
                if (grantResults.length> 0) {
                    boolean permissionToRecord = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean permissionToStore = grantResults[1] ==  PackageManager.PERMISSION_GRANTED;
                    if (permissionToRecord && permissionToStore) {
                        Toast.makeText(getApplicationContext(), "Permission Granted", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(),"Permission Denied",Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    public boolean CheckPermissions() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }

    private void RequestPermissions() {
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                RECORD_AUDIO, WRITE_EXTERNAL_STORAGE}, REQUEST_AUDIO_PERMISSION_CODE);
    }
}