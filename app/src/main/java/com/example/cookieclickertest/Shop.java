package com.example.cookieclickertest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class Shop extends AppCompatActivity {
    private static final String FILE_SCORE = "score.txt";
    private static final String FILE_AUTO = "auto.txt";
    private static final String FILE_MULTI = "multi.txt";
    private static final String FILE_GRANDMA = "grandma.txt";
    public int digitsFromFile = 0;
    public int score = 0;
    public int auto = 0;
    public int multi = 0;
    public int grandma = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        Button button_back = findViewById(R.id.button_back);
        Button button_buy_auto = findViewById(R.id.button_buy_auto);
        Button button_buy_multi = findViewById(R.id.button_buy_multi);
        Button button_buy_grandma = findViewById(R.id.button_buy_grandma);
        TextView anzahl_auto = findViewById(R.id.anzahl_auto);
        TextView anzahl_multi = findViewById(R.id.anzahl_multi);
        TextView textView_counter_shop = findViewById(R.id.textView_counter_shop);
        TextView anzahl_grandma = findViewById(R.id.anzahl_grandma);

        score = loadFile(digitsFromFile, FILE_SCORE);
        auto = loadFile(digitsFromFile, FILE_AUTO);
        multi = loadFile(digitsFromFile, FILE_MULTI);
        grandma = loadFile(digitsFromFile, FILE_GRANDMA);

        int autoPreis = 1000;
        int multiPreis = 60;
        int grandmaPreis = 2500;

        textView_counter_shop.setText((String.valueOf(score)));
        anzahl_auto.setText(String.valueOf(auto));
        anzahl_multi.setText(String.valueOf(multi));
        anzahl_grandma.setText(String.valueOf(grandma));

        if (auto != 0){
            for (int i = 0; i < auto; i++){
                autoPreis = autoPreis * 3;
            }
            button_buy_auto.setText(String.valueOf(autoPreis));
        }
        else {
            button_buy_auto.setText(String.valueOf(autoPreis));
        }
        if (multi != 0){
            for (int i = 0; i < multi; i++){
                multiPreis = multiPreis * 2;
            }
            button_buy_multi.setText(String.valueOf(multiPreis));
        }
        else {
            button_buy_multi.setText(String.valueOf(multiPreis));
        }
        if (grandma != 0){
            for (int i = 0; i < grandma; i++){
                grandmaPreis = grandmaPreis * 2;
            }
            button_buy_grandma.setText(String.valueOf(grandmaPreis));
        }
        else {
            button_buy_grandma.setText(String.valueOf(grandmaPreis));
        }

        button_back.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // save the current cookie counter value to external file
                startActivity(new Intent(Shop.this, MainActivity.class));
            }
        });

        button_buy_auto.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int inputAnzahlAuto = Integer.parseInt(anzahl_auto.getText().toString());
                int buttenAutoText = Integer.parseInt(button_buy_auto.getText().toString());
                if(score >= buttenAutoText) {
                    int x1 = purchaseUpgrade(inputAnzahlAuto, buttenAutoText, score);
                    anzahl_auto.setText(String.valueOf(x1));
                    auto = x1;
                    String test = button_buy_auto.getText().toString();
                    int y = Integer.parseInt(test) *3;
                    button_buy_auto.setText(String.valueOf(y));
                    textView_counter_shop.setText((String.valueOf(score)));
                }
                saveAll();
            }
        });

        button_buy_multi.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int inputAnzahlMulti = Integer.parseInt(anzahl_multi.getText().toString());
                int buttenMultiText = Integer.parseInt(button_buy_multi.getText().toString());
                if(score >= buttenMultiText) {
                    int x2 = purchaseUpgrade(inputAnzahlMulti, buttenMultiText, score);
                    anzahl_multi.setText(String.valueOf(x2));
                    multi = x2;
                    String test = button_buy_multi.getText().toString();
                    int y = Integer.parseInt(test) *2;
                    button_buy_multi.setText(String.valueOf(y));
                    textView_counter_shop.setText((String.valueOf(score)));
                }
                saveAll();
            }
        });

        button_buy_grandma.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                int inputAnzahlGrandma = Integer.parseInt(anzahl_grandma.getText().toString());
                int buttenGrandmaText = Integer.parseInt(button_buy_grandma.getText().toString());
                if(score >= buttenGrandmaText) {
                    int x3 = purchaseUpgrade(inputAnzahlGrandma, buttenGrandmaText, score);
                    anzahl_grandma.setText(String.valueOf(x3));
                    grandma = x3;
                    String test = button_buy_grandma.getText().toString();
                    int y = Integer.parseInt(test) *2;
                    button_buy_grandma.setText(String.valueOf(y));
                    textView_counter_shop.setText((String.valueOf(score)));
                }
                saveAll();
            }
        });

    }
    public int purchaseUpgrade(int input, int buttonText, int scoreInt){
        if(scoreInt >= buttonText){
            score = score - buttonText;
            return input+1;
        } else {
            return input;
        }

    }
    public void saveAll(){
        saveToFile(String.valueOf(score),FILE_SCORE);
        saveToFile(String.valueOf(auto),FILE_AUTO);
        saveToFile(String.valueOf(multi),FILE_MULTI);
        saveToFile(String.valueOf(grandma), FILE_GRANDMA);
    }

    private void saveToFile(String data, String FILE){
        FileOutputStream fos = null;
        try {
            fos = openFileOutput(FILE, MODE_PRIVATE);
            fos.write(data.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
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
}