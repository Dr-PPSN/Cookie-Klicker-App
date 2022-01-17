package com.example.cookieclickertest;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.*;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;

public class MainActivity extends AppCompatActivity {
    private static final String FILE_SCORE = "score.txt";
    private static final String FILE_SCORE2 = "score2.txt";
    private static final String FILE_AUTO = "auto.txt";
    private static final String FILE_MULTI = "multi.txt";
    private static final String FILE_GRANDMA = "grandma.txt";
    private static final String FILE_SOUND = "sound.txt";
    private static final String FILE_MUSIC = "music.txt";
    private static final String FILE_VIB = "vib.txt";
    private static MediaPlayer mediaPlayerMusic;
    private static MediaPlayer mediaPlayerSound;
    public int digitsFromFile = 0;
    public int auto = 0;
    public int multi = 0;
    public int clickvalue = 1;
    public int grandma = 0;
    public int cheat = 0;
    public int soundCheck = 1;
    public int music = 1;
    public int vib = 1;
    TextView textView_counter;
    TextView textView_counter2;
    TextView billions_text;
    com.google.android.gms.ads.AdView mAdView;

    //TODO: button in Shop activity to get Cookies after watching a 30 sec Ad
    //TODO: maybe better design and colors (add colors in res/values/colors.xml)

    @SuppressLint({"ClickableViewAccessibility", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button_Shop = findViewById(R.id.button_Shop);
        ImageButton button_settings = findViewById(R.id.settings_button);
        textView_counter = findViewById(R.id.textView_counter);
        textView_counter2 = findViewById(R.id.textView_counter2);
        billions_text = findViewById(R.id.billions_text);
        ImageButton button_cookie = findViewById(R.id.button_cookie);
        auto = loadFile(digitsFromFile, FILE_AUTO);
        multi = loadFile(digitsFromFile, FILE_MULTI);
        grandma = loadFile(digitsFromFile, FILE_GRANDMA);
        soundCheck = loadFile(1, FILE_SOUND);
        music = loadFile(1, FILE_MUSIC);
        vib = loadFile(1, FILE_VIB);
        textView_counter.setText(String.valueOf(loadFile(digitsFromFile, FILE_SCORE)));
        textView_counter2.setText(String.valueOf(loadFile(digitsFromFile, FILE_SCORE2)));
        clickvalue = calculateClickValue(clickvalue,multi);
        button_cookie.setScaleX(1.0f);
        button_cookie.setScaleY(1.0f);

        if (music == 1) {
            playBackgroundMusic(R.raw.placeholder_background);
        }

        textView_counter.setOnClickListener(view -> {
            cheat++;
            if (cheat == 3){
                textView_counter.setText(String.valueOf(Integer.parseInt((String) textView_counter.getText())+(999999998)));
                cheat = 0;
            }
        });
        mediaPlayerSound = MediaPlayer.create(MainActivity.this, R.raw.sound_effekt);
        button_cookie.setOnTouchListener((v, event) -> {
            if(event.getAction() == MotionEvent.ACTION_DOWN) {
                button_cookie.animate().scaleX(0.95f).scaleY(0.95f).setDuration(100).start();
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                button_cookie.animate().scaleX(1.0f).scaleY(1.0f).setDuration(100).start();
            }
            return false;
        });

        button_cookie.setOnClickListener(v -> {
            int manualyClick = 1;
            if (soundCheck == 1) {
                mediaPlayerSound.setVolume(1f,1f);
                mediaPlayerSound.start();

            }
            if (vib == 1){
                Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(40);
            }
            cookieClick(manualyClick);
        });

        button_Shop.setOnClickListener(v -> {
            // save the current cookie counter value to external file
            saveAll();
            Intent intent = new Intent(MainActivity.this, Shop.class);
            startActivity(intent);
        });
        button_settings.setOnClickListener(v ->{
            saveAll();
            if (music == 1) {
                mediaPlayerMusic.stop();
            }
            Intent intent = new Intent(MainActivity.this, Settings.class);
            startActivity(intent);
        });

        if (auto > 0) {
            final Handler handler = new Handler();
            final int delay = 1000; // 1000 milliseconds == 1 second

            handler.postDelayed(new Runnable() {
                public void run() {
                    cookieClick(auto);
                    handler.postDelayed(this, delay);
                }
            }, delay);
        }

        if (grandma > 0){
            final Handler grandmaHandler = new Handler();
            final int delay = 1000;

            grandmaHandler.postDelayed(new Runnable(){
                public void run() {
                    grandmaFunctionality(grandma);
                    grandmaHandler.postDelayed(this, delay);
                }
            }, delay);
        }
        if (Integer.parseInt((String) textView_counter2.getText()) == 0){
            billions_text.setVisibility(View.INVISIBLE);
            textView_counter2.setVisibility(View.INVISIBLE);
        }
        //auto-save every 4 sec--------->
        final Handler saveHandler = new Handler();
        final int delay = 4000;
        saveHandler.postDelayed(new Runnable(){
            public void run() {
                saveAll();
                saveHandler.postDelayed(this, delay);
                }
        }, delay);
        //<-----------------------

        //initialize mobile ads----------->
        MobileAds.initialize(this, initializationStatus -> {
        });
        AdRequest adRequest = new AdRequest.Builder().build();
        //<--------------
        //Ad Top Banner------------->
        mAdView = findViewById(R.id.adViewBottomBanner);
        mAdView.loadAd(adRequest);
        //<-------------
    }
    public void saveAll(){
        String score1 = String.valueOf(textView_counter.getText());
        String score2 = String.valueOf(textView_counter2.getText());
        saveToFile(score1,FILE_SCORE);
        saveToFile(score2,FILE_SCORE2);
        saveToFile(String.valueOf(auto),FILE_AUTO);
        saveToFile(String.valueOf(multi),FILE_MULTI);
        saveToFile(String.valueOf(grandma), FILE_GRANDMA);
    }

    private void saveToFile(String data, String FILE){
        FileOutputStream fos = null;
        try {
            fos = openFileOutput(FILE, MODE_PRIVATE);
            fos.write(data.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos!=null){
                try {
                    fos.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }
    public int loadFile(int digitFromFile, String FILE){
        FileInputStream fis = null;

        try {
            fis = openFileInput(FILE);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text;

            while ((text = br.readLine()) != null) {
                sb.append(text);
            }
            //textView_counter.setText(sb.toString());
            Log.i("loadFile()","msg: " + sb.toString());
            digitFromFile = Integer.parseInt(sb.toString());
            return digitFromFile;

        } catch (FileNotFoundException e) {
            Log.i("file","file not found");
            e.printStackTrace();
            return digitFromFile;
        } catch (IOException e) {
            e.printStackTrace();
            return digitFromFile;
        }finally {
            if (fis != null){
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public int calculateClickValue(int clickvalue, int multiplier){

        if(multiplier != 0){
            for(int i = 0; i < multiplier; i++){
                clickvalue = clickvalue * 2;
            }
        }
        return clickvalue;
    }
    @SuppressLint("SetTextI18n")
    public void cookieClick(int autoClickValue) {
        int score = Integer.parseInt((String) textView_counter.getText());
        //check if score is negative
        if (score < 0) {
            textView_counter.setText(String.valueOf(999999999));//int max value = 2.000.000.000
            textView_counter2.setText(String.valueOf(999999999));
        } else {
            // increment the cookie counter each time the cookie button is clicked
            if (score >= 999999999) { //999 MIO.
                    billions_text.setVisibility(View.VISIBLE);
                    textView_counter2.setVisibility(View.VISIBLE);
                    textView_counter2.setText(String.valueOf(Integer.parseInt((String) textView_counter2.getText()) + 1));
                    textView_counter.setText(String.valueOf(0));
                    billions_text.setText("Billion");
            } else {
                textView_counter.setText(String.valueOf(Integer.parseInt((String) textView_counter.getText()) + (clickvalue * autoClickValue)));
            }
        }
    }
    public void grandmaFunctionality(int grandma){
        int grandmaValue = 50 * grandma;
        textView_counter.setText(String.valueOf(Integer.parseInt((String) textView_counter.getText())+(grandmaValue)));
    }
    public void playBackgroundMusic(int id){
        mediaPlayerMusic = MediaPlayer.create(MainActivity.this, id);
        mediaPlayerMusic.setLooping(true);
        mediaPlayerMusic.setVolume(0.1f,0.1f);
        mediaPlayerMusic.start();
    }
}