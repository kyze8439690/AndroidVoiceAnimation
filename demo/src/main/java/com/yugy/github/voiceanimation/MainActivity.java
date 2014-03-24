package com.yugy.github.voiceanimation;

import android.app.Activity;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;


public class MainActivity extends Activity implements VoiceView.OnRecordListener{

    private static final String TAG = MainActivity.class.getName();

    private TextView mTextView;
    private VoiceView mVoiceView;
    private MediaRecorder mMediaRecorder;
    private Handler mHandler;

    private boolean mIsRecording = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = (TextView) findViewById(R.id.text);
        mVoiceView = (VoiceView) findViewById(R.id.voiceview);
        mVoiceView.setOnRecordListener(this);

        mHandler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void onRecordStart() {
        Log.d(TAG, "onRecordStart");
        try {
            mMediaRecorder = new MediaRecorder();
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mMediaRecorder.setOutputFile(new File(Environment.getExternalStorageDirectory(), "audio.amr").getAbsolutePath());
            mMediaRecorder.prepare();
            mMediaRecorder.start();
            mIsRecording = true;
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    float radius = (float) Math.log10(Math.max(1, mMediaRecorder.getMaxAmplitude() - 500)) * ScreenUtils.dp2px(MainActivity.this, 20);
                    mTextView.setText(String.valueOf(radius));
                    mVoiceView.animateRadius(radius);
                    if (mIsRecording) {
                        mHandler.postDelayed(this, 50);
                    }
                }
            });
        } catch (IOException e) {
            Toast.makeText(this, "MediaRecorder prepare failed!", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onRecordFinish() {
        Log.d(TAG, "onRecordFinish");
        mIsRecording = false;
        mMediaRecorder.stop();
    }


    @Override
    protected void onDestroy() {
        if(mIsRecording){
            mMediaRecorder.stop();
            mIsRecording = false;
        }
        mMediaRecorder.release();
        super.onDestroy();
    }
}
