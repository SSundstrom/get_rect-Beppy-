package com.example.svenscan.svenscan.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.svenscan.svenscan.SvenScanApplication;
import com.example.svenscan.svenscan.activities.tasks.OCRDecoderAsyncTask;
import com.example.svenscan.svenscan.models.Word;
import com.example.svenscan.svenscan.repositories.FavoriteWordRepository;
import com.example.svenscan.svenscan.repositories.IWordRepository;
import com.example.svenscan.svenscan.utils.SoundManager;
import com.example.svenscan.svenscan.utils.ocr.IOCR;
import com.googlecode.leptonica.android.Pix;
import com.googlecode.leptonica.android.ReadFile;
import com.example.svenscan.svenscan.R;

public class ShowScannedWordActivity extends AppCompatActivity implements OCRDecoderAsyncTask.ITaskCompleteHandler{
    private IOCR ocr;
    private SoundManager soundManager;
    private IWordRepository wordManager;
    private FavoriteWordRepository favoriteWords;
    private Word currentWord;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_word);
        SvenScanApplication app = (SvenScanApplication)getApplication();
        soundManager = new SoundManager(this);
        wordManager = app.getWordRepository();
        favoriteWords = app.getFavoriteWordRepository();
        ocr = app.getOCR();

        if(getIntent().hasExtra("fav")) {

            System.out.println("get word from Fav");
            currentWordFromFavorites();

        } else {

            System.out.println("get word from OCR");
            currentWordFromOCR();

        }

    }

    public void playWord(View view) {
        if (currentWord != null) {
            soundManager.start(currentWord.getSoundID());
        }
    }

    public void favoriteWord(View view) {
        if (currentWord == null) {
            return;
        }

        String word = currentWord.getWord();

        View heart = findViewById(R.id.favorite);
        favoriteWords.toggleFavorite(word, this);

        if (favoriteWords.isFavoriteWord(word)) {
            heart.setBackgroundResource(R.drawable.fav_red);
        } else {
            heart.setBackgroundResource(R.drawable.fav_gray);
        }
    }

    @Override
    public void onOCRComplete(String ocrResult) {

        if (wordManager.containsWord(ocrResult)) {
            currentWord = wordManager.getWordFromID(ocrResult);
//            soundManager.start(currentWord.getSoundID());  // TODO: 2016-10-03 get real sound path
            handleCurrentWord();

        }
        else {
            setContentView(R.layout.no_word_match_view);
            ImageButton imageButton = (ImageButton) findViewById(R.id.imageButton);
            imageButton.setBackgroundResource(R.drawable.redo_button);
            ImageView errorImage = (ImageView) findViewById((R.id.noWordErrorImage));
            errorImage.setBackgroundResource(R.drawable.utropstecken);
            TextView errorText = (TextView) findViewById(R.id.errorText);
            errorText.setText("Tyvärr hittas inte det scannade ordet, försök igen! \n"  + ocrResult);

        }
    }

    public void backToCamera(View view){
        Intent intent = new Intent(this, StartActivity.class);
        startActivity(intent);
    }

    private void currentWordFromFavorites() {
        String wordID = getIntent().getStringExtra("fav").toUpperCase();

        currentWord = wordManager.getWordFromID(wordID);

        Bitmap map = BitmapFactory.decodeResource(getResources(), R.drawable.no_pic);

        setLeftPicture(map);

        handleCurrentWord();
    }

    private void currentWordFromOCR() {

        Bitmap map = BitmapFactory.decodeFile(getIntent().getStringExtra("picture"));
        setLeftPicture(map);

        map = Bitmap.createScaledBitmap(map, 500, 500, false);
        View rootView = findViewById(android.R.id.content);
        Pix picture = ReadFile.readBitmap(map);

        new OCRDecoderAsyncTask(rootView, ocr, this).execute(picture);

    }
    private void handleCurrentWord() {

        Button heart = (Button)findViewById(R.id.favorite);

        if (currentWord != null && favoriteWords.isFavoriteWord(currentWord.getWord())) {
            heart.setBackgroundResource(R.drawable.fav_red);
        } else {
            heart.setBackgroundResource(R.drawable.fav_gray);
        }

        heart.setClickable(true);
        setText(currentWord.getWord());
        soundManager.start(currentWord.getSoundID());  // TODO: 2016-10-03 get real sound path

    }

    private void setLeftPicture(Bitmap map) {

        ImageView mainView = (ImageView) findViewById((R.id.imageView4));
        mainView.setImageBitmap(map);

    }

    private void setText(String word) {
        TextView textBox = ((TextView)findViewById(R.id.wordText));

        if(word != null) {
            textBox.setText(word);
        } else {
            textBox.setText("(null)");
        }
    }
}




