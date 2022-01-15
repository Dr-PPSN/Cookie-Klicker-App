package com.example.cookieclickertest;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.*;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {
    private static final String FILE_SCORE = "score.txt";
    private static final String FILE_SCORE2 = "score2.txt";
    private static final String FILE_AUTO = "auto.txt";
    private static final String FILE_MULTI = "multi.txt";
    private static final String FILE_GRANDMA = "grandma.txt";
    public String WinGame_Text = "Congratulations, you won the game!";
    public int digitsFromFile = 0;
    public int auto = 0;
    public int multi = 0;
    public int clickvalue = 1;
    public int grandma = 0;
    public int cheat = 0;
    TextView textView_counter;
    TextView textView_counter2;

    //TODO: New Shop Objects
    //TODO: App Design
    //TODO: remove both 0 when first time starting the app
    //TODO: resize the animation of the cookie

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button_save = findViewById(R.id.button_save);
        Button button_Shop = findViewById(R.id.button_Shop);
        Button button_reset = findViewById(R.id.button_reset);
        textView_counter = findViewById(R.id.textView_counter);
        textView_counter2 = findViewById(R.id.textView_counter2);
        ImageButton button_cookie = findViewById(R.id.button_cookie);
        auto = loadFile(digitsFromFile, FILE_AUTO);
        multi = loadFile(digitsFromFile, FILE_MULTI);
        grandma = loadFile(digitsFromFile, FILE_GRANDMA);
        textView_counter.setText(String.valueOf(loadFile(digitsFromFile, FILE_SCORE)));
        textView_counter2.setText(String.valueOf(loadFile(digitsFromFile, FILE_SCORE2)));
        clickvalue = calculateClickValue(clickvalue,multi);

        textView_counter.setOnClickListener(view -> {
            cheat++;
            if (cheat == 3){
                textView_counter.setText(String.valueOf(Integer.parseInt((String) textView_counter.getText())+(999999998)));
                cheat = 0;
            }
        });

        button_reset.setOnClickListener(view -> {
            auto = 0;
            multi = 0;
            grandma = 0;
            textView_counter.setText(String.valueOf(0));
            textView_counter2.setText(String.valueOf(0));
            saveAll();
        });

        button_cookie.setOnTouchListener((v, event) -> {
            if(event.getAction() == MotionEvent.ACTION_DOWN) {
                button_cookie.animate().scaleX(1.2f).scaleY(1.2f).setDuration(300).start();
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                button_cookie.animate().scaleX(0.8f).scaleY(0.8f).setDuration(300).start();
            }
            return false;
        });

        button_cookie.setOnClickListener(v -> {
            int manualyClick = 1;
            cookieClick(manualyClick);
        });

        button_save.setOnClickListener(v -> saveAll());
        button_Shop.setOnClickListener(v -> {
            // save the current cookie counter value to external file
            saveAll();
            Intent intent = new Intent(MainActivity.this, Shop.class);
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
    public void cookieClick(int autoClickValue){
        int score = Integer.parseInt((String) textView_counter.getText());
        TextView winGame_Text = findViewById(R.id.winGame_Text);
        //check if score is negative
        if (score < 0){
            winGame_Text.setText(WinGame_Text);
            textView_counter.setText(String.valueOf(999999999));//int max value = 2.000.000.000
            textView_counter2.setText(String.valueOf(999999999));
        }
        else {
            // increment the cookie counter each time the cookie button is clicked
            if (score >= 9){
                if (score >= 99){
                    if (score >= 999){
                        if (score >= 9999){ //9 TSD.
                            if (score >= 99999){ //99 TSD.
                                if (score >= 999999){ //999 TSD.
                                    if (score >= 9999999){ //9 MIO.
                                        if (score >= 99999999){ //99 MIO.
                                            if (score >= 999999999){ //999 MIO.
                                                textView_counter.setText(String.valueOf(Integer.parseInt((String) textView_counter.getText()) + (clickvalue * autoClickValue)));
                                                score = Integer.parseInt((String) textView_counter.getText());
                                                if (score > 999999998) {
                                                    textView_counter2.setText(String.valueOf(Integer.parseInt((String) textView_counter2.getText()) + 1));
                                                    textView_counter.setText(String.valueOf("000000000"));
                                                }
                                            }
                                            else{
                                                textView_counter.setText(String.valueOf(Integer.parseInt((String) textView_counter.getText()) + (clickvalue * autoClickValue)));
                                            }
                                        }
                                        else {
                                            textView_counter.setText("0" + String.valueOf(Integer.parseInt((String) textView_counter.getText()) + (clickvalue * autoClickValue)));
                                        }
                                    }
                                    else {
                                        textView_counter.setText("00" + String.valueOf(Integer.parseInt((String) textView_counter.getText()) + (clickvalue * autoClickValue)));
                                    }
                                }
                                else {
                                    textView_counter.setText("000" + String.valueOf(Integer.parseInt((String) textView_counter.getText()) + (clickvalue * autoClickValue)));
                                }
                            }
                            else {
                                textView_counter.setText("0000" + String.valueOf(Integer.parseInt((String) textView_counter.getText()) + (clickvalue * autoClickValue)));
                            }
                        }
                        else {
                            textView_counter.setText("00000" + String.valueOf(Integer.parseInt((String) textView_counter.getText()) + (clickvalue * autoClickValue)));
                        }
                    }
                    else {
                        textView_counter.setText("000000" + String.valueOf(Integer.parseInt((String) textView_counter.getText()) + (clickvalue * autoClickValue)));
                    }
                }
                else {
                    textView_counter.setText("0000000" + String.valueOf(Integer.parseInt((String) textView_counter.getText()) + (clickvalue * autoClickValue)));
                }
            }
            else {
                textView_counter.setText("00000000" + String.valueOf(Integer.parseInt((String) textView_counter.getText()) + (clickvalue * autoClickValue)));
            }
        }
    }
    public void grandmaFunctionality(int grandma){
        int grandmaValue = 50 * grandma;
        textView_counter.setText(String.valueOf(Integer.parseInt((String) textView_counter.getText())+(grandmaValue)));
    }
}