package com.example.svenscan.svenscan;

import android.app.Application;
import android.content.SharedPreferences;

import com.example.svenscan.svenscan.utils.ProgressManager;
import com.example.svenscan.svenscan.repositories.FavoriteWordRepository;
import com.example.svenscan.svenscan.repositories.FirebaseMediaRepository;
import com.example.svenscan.svenscan.repositories.FirebaseWordRepository;
import com.example.svenscan.svenscan.repositories.IFavoriteRepository;
import com.example.svenscan.svenscan.repositories.IMediaRepository;
import com.example.svenscan.svenscan.repositories.IWordRepository;
import com.google.firebase.FirebaseApp;
import com.karumi.dexter.Dexter;
import com.example.svenscan.svenscan.utils.ocr.IOCR;
import com.example.svenscan.svenscan.utils.ocr.OCRDecoder;

import java.util.HashSet;

public class SvenScanApplication extends Application {
    private static IWordRepository wordRepository;
    private static FavoriteWordRepository favoriteWordRepository; // todo: bör vara interface?
    private static IOCR ocr;
    private static IMediaRepository mediaRepository;
    private static ProgressManager progressManager;

    public void onCreate() {

        super.onCreate();
        FirebaseApp.initializeApp(this);

        if (wordRepository == null) {
            wordRepository = new FirebaseWordRepository();
        }

        if (favoriteWordRepository == null) {
            favoriteWordRepository = new FavoriteWordRepository();
            recreateFavoriteWords();
        }

        if (ocr == null) {
            ocr = new OCRDecoder(this);
        }

        if (mediaRepository == null) {
            mediaRepository = new FirebaseMediaRepository();
        }

        if(progressManager == null){
            progressManager = new ProgressManager();
            recreatePoints();
        }


        Dexter.initialize(this);
    }

    public IWordRepository getWordRepository() {
        return wordRepository;
    }

    public IOCR getOCR () {
        return ocr;
    }

    public IFavoriteRepository getFavoriteWordRepository() {
        return favoriteWordRepository;
    }

    public IMediaRepository getMediaRepository() {
        return mediaRepository;
    }

    public ProgressManager getPoints(){ return progressManager;}


    public void recreateFavoriteWords(){
        HashSet<String> set = new HashSet<String>();
        SharedPreferences settings = getSharedPreferences("favoriteWords", 0);
        set = (HashSet<String>) settings.getStringSet("favoriteWords", set);
        favoriteWordRepository.addSetToFavorites(set);
    }

    public void recreatePoints(){
        SharedPreferences settings = getSharedPreferences("points", 0);
        int points = settings.getInt("points", 0);
        progressManager.setPoints(points);
    }


}
