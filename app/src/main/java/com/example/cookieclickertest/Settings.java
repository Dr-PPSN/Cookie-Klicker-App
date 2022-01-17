package com.example.cookieclickertest;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class Settings extends AppCompatActivity {
    private static final String FILE_SCORE = "score.txt";
    private static final String FILE_SCORE2 = "score2.txt";
    private static final String FILE_AUTO = "auto.txt";
    private static final String FILE_MULTI = "multi.txt";
    private static final String FILE_GRANDMA = "grandma.txt";
    private static final String FILE_SOUND = "sound.txt";
    private static final String FILE_MUSIC = "music.txt";
    private static final String FILE_VIB = "vib.txt";
    public int auto = 0;
    public int multi = 0;
    public int score = 0;
    public int score2 = 0;
    public int grandma = 0;
    public int sound = 1;
    public int music = 1;
    public int vib = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Button back_button = findViewById(R.id.button_backToGame);
        Button reset_button = findViewById(R.id.button_reset);
        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch sound_switch = findViewById(R.id.switch_sound);
        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch music_switch = findViewById(R.id.switch_music);
        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch vib_switch = findViewById(R.id.switch_vib);

        sound = loadFile(1, FILE_SOUND);
        music = loadFile(1, FILE_MUSIC);
        vib = loadFile(1, FILE_VIB);

        if (sound==0){
            sound_switch.setChecked(false);
        }
        else if (sound==1){
            sound_switch.setChecked(true);
        }

        if (music==0){
            music_switch.setChecked(false);
        }
        else if (music==1){
            music_switch.setChecked(true);
        }

        if (vib==0){
            vib_switch.setChecked(false);
        }
        else if (vib==1){
            vib_switch.setChecked(true);
        }

        back_button.setOnClickListener(view -> {
            Intent intent = new Intent(Settings.this, MainActivity.class);
            startActivity(intent);
        });
        reset_button.setOnClickListener(view -> saveAll());
        sound_switch.setOnClickListener(view -> {
            if (sound_switch.isChecked()){
                sound = 1;
            }
            else {
                sound = 0;
            }
            saveToFile(String.valueOf(sound), FILE_SOUND);
        });
        music_switch.setOnClickListener(view -> {
            if (music_switch.isChecked()){
                music = 1;
            }
            else {
                music = 0;
            }
            saveToFile(String.valueOf(music), FILE_MUSIC);
        });
        vib_switch.setOnClickListener(view -> {
            if (vib_switch.isChecked()){
                vib = 1;
            }
            else {
                vib = 0;
            }
            saveToFile(String.valueOf(vib), FILE_VIB);
        });
    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Settings.this, MainActivity.class);
        startActivity(intent);
    }
    public void saveAll(){
        saveToFile(String.valueOf(score),FILE_SCORE);
        saveToFile(String.valueOf(score2),FILE_SCORE2);
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
}
