package com.example.svenscan.svenscan;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.media.ImageReader;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.googlecode.tesseract.android.TessBaseAPI;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private OCRtranslator ocr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startOCR();
    }

    private void startOCR() {
        System.out.println("startOCR");

        try {
            saveOCRDataFileToStorage();
        } catch(IOException e) {
            System.out.println(e.getMessage());
        }


        String langPath = getApplicationContext().getFilesDir() + "/";
        System.out.println(langPath);

        System.out.println((new File(langPath + "tessdata/")).listFiles().length);

        ocr = new OCRtranslator(langPath);
    }

    public void seeWord(View view) {
        if(ocr.getTextIfAvailable() != null) {
            //Show the text
        } else {
            //Say that there was no word found
        }
    }

    public void chooseImage(View view) {
        Bitmap picture = BitmapFactory.decodeResource(getResources(), R.drawable.test);
        ocr.setImage(picture);
        ImageView mainView = (ImageView)findViewById((R.id.imageView));
        mainView.setImageBitmap(picture);
    }


    public void saveOCRDataFileToStorage() throws IOException {
        InputStream in = null;
        BufferedOutputStream fout = null;
        try {
            in = getResources().openRawResource(R.raw.swe);
            String path = this.getApplicationContext().getFilesDir() + "/tessdata/";
            File file = new File(path);
            file.mkdirs();

            path += "swe.traineddata";

            fout = new BufferedOutputStream(new FileOutputStream(path, true));

            final byte data[] = new byte[1024];
            int count;
            while ((count = in.read(data, 0, 1024)) != -1) {
                fout.write(data, 0, count);
            }
        } finally {
            if (in != null) {
                in.close();
            }
            if (fout != null) {
                fout.close();
            }
        }
    }
}
