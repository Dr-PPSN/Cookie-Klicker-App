package com.example.cookieclickertest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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

public class MainActivity extends AppCompatActivity {
    private static final String FILE_SCORE = "score.txt";
    private static final String FILE_AUTO = "auto.txt";
    private static final String FILE_MULTI = "multi.txt";
    private static final String FILE_GRANDMA = "grandma.txt";
    public int digitsFromFile = 0;
    public int auto = 0;
    public int multi = 0;
    public int clickvalue = 1;
    public int grandma = 0;
    public int cheat = 0;
    TextView textView_counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button_save = findViewById(R.id.button_save);
        Button button_Shop = findViewById(R.id.button_Shop);
        Button button_reset = findViewById(R.id.button_reset);
        textView_counter = findViewById(R.id.textView_counter);
        ImageButton button_cookie = findViewById(R.id.button_cookie);
        auto = loadFile(digitsFromFile, FILE_AUTO);
        multi = loadFile(digitsFromFile, FILE_MULTI);
        grandma = loadFile(digitsFromFile, FILE_GRANDMA);
        String zwichenString = String.valueOf(loadFile(digitsFromFile, FILE_SCORE));
        textView_counter.setText(zwichenString);
        clickvalue = calculateClickValue(clickvalue,multi);

        textView_counter.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                cheat++;
                if (cheat == 3){
                    textView_counter.setText(String.valueOf(Integer.valueOf((String) textView_counter.getText())+(10000)));
                    cheat = 0;
                }
            }
        });

        button_reset.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                auto = 0;
                multi = 0;
                grandma = 0;
                textView_counter.setText(String.valueOf(0));
                saveAll();
            }
        });

        button_cookie.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN) {
                    buttonGross();
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    buttonKlein();
                }
                return false;
            }
        });

        button_save.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                saveAll();
            }
        });
        button_Shop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // save the current cookie counter value to external file
                saveAll();
                Intent intent = new Intent(MainActivity.this, Shop.class);
                startActivity(intent);
            }
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
        String data = String.valueOf(textView_counter.getText());
        saveToFile(data,FILE_SCORE);
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
            digitFromFile = Integer.valueOf(sb.toString());
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
    public void cookieClick(int autoClickValue){
        // increment the cookie counter by 1 each time the cookie button is clicked
        textView_counter.setText(String.valueOf(Integer.valueOf((String) textView_counter.getText())+(clickvalue*autoClickValue)));
    }
    public void grandmaFunctionality(int grandma){
        int grandmaValue = 50 * grandma;
        textView_counter.setText(String.valueOf(Integer.valueOf((String) textView_counter.getText())+(grandmaValue)));
    }
    public void buttonGross(){
        ImageButton button_cookie = findViewById(R.id.button_cookie);
        button_cookie.animate().setStartDelay(1);
        button_cookie.animate().scaleYBy((float) 0.8).setDuration(300);
        button_cookie.animate().scaleXBy((float) 0.8).setDuration(300);
    }
    public void buttonKlein(){
        ImageButton button_cookie = findViewById(R.id.button_cookie);
        button_cookie.animate().setStartDelay(1);
        button_cookie.animate().scaleYBy((float) -0.4).setDuration(300);
        button_cookie.animate().scaleXBy((float) -0.4).setDuration(300);
    }
}