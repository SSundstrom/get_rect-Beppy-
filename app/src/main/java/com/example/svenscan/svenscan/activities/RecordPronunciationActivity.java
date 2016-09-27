package com.example.svenscan.svenscan.activities;

import android.media.MediaRecorder;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.svenscan.svenscan.R;

import java.io.File;

public class RecordPronunciationActivity extends AppCompatActivity {
    final String PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
    private boolean isRecording = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_pronunciation);

        final MediaRecorder mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setMaxDuration(5000);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        File folder = new File(Environment.getExternalStorageDirectory() +
                File.separator + "SvenScan");
        if (!folder.exists()) {
            folder.mkdir();
        }

        final Button recordButton = (Button) findViewById(R.id.recordButton);
        recordButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                EditText swedishWord = (EditText)findViewById(R.id.swedishWord);
                String filename = swedishWord.getText().toString();
                mediaRecorder.setOutputFile(PATH + "/SvenScan/" + filename + ".3gp");

                try {
                    mediaRecorder.prepare();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (!isRecording) {
                    mediaRecorder.start();
                    isRecording = true;
                } else {
                    mediaRecorder.stop();
                    isRecording = false;
                }
            }
        });
    }
}