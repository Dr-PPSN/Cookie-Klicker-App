package com.example.cookieclickertest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class Shop extends AppCompatActivity {
    private static final String FILE_SCORE = "score.txt";
    private static final String FILE_SCORE2 = "score2.txt";
    private static final String FILE_AUTO = "auto.txt";
    private static final String FILE_MULTI = "multi.txt";
    private static final String FILE_GRANDMA = "grandma.txt";
    private static final String FILE_BAKERY = "bakery.txt";
    public int digitsFromFile = 0;
    public int score = 0;
    public int score2 = 0;
    public int auto = 0;
    public int multi = 0;
    public int grandma = 0;
    public int bakery = 0;

    //TODO: 2 or 3 New Shop Objects to buy with Billions
    //TODO: button to get Cookies after watching a 30 sec Ad

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        Button button_back = findViewById(R.id.button_back);
        Button button_buy_auto = findViewById(R.id.button_buy_auto);
        Button button_buy_multi = findViewById(R.id.button_buy_multi);
        Button button_buy_grandma = findViewById(R.id.button_buy_grandma);
        Button button_buy_bakery = findViewById(R.id.button_buy_bakery);
        TextView anzahl_auto = findViewById(R.id.anzahl_auto);
        TextView anzahl_multi = findViewById(R.id.anzahl_multi);
        TextView textView_counter_shop = findViewById(R.id.textView_counter_shop);
        TextView anzahl_grandma = findViewById(R.id.anzahl_grandma);
        TextView billions_text2 = findViewById(R.id.billion_text2);
        TextView anzahl_bakery = findViewById(R.id.anzahl_bakery);

        score = loadFile(digitsFromFile, FILE_SCORE);
        score2 = loadFile(digitsFromFile, FILE_SCORE2);
        auto = loadFile(digitsFromFile, FILE_AUTO);
        multi = loadFile(digitsFromFile, FILE_MULTI);
        grandma = loadFile(digitsFromFile, FILE_GRANDMA);
        bakery = loadFile(digitsFromFile, FILE_BAKERY);

        int autoPreis = 1000;
        int multiPreis = 60;
        int grandmaPreis = 2500;
        int bakeryPreis = 1;

        textView_counter_shop.setText((String.valueOf(score)));
        anzahl_auto.setText(String.valueOf(auto));
        anzahl_multi.setText(String.valueOf(multi));
        anzahl_grandma.setText(String.valueOf(grandma));
        anzahl_bakery.setText(String.valueOf(bakery));

        if (auto != 0){
            for (int i = 0; i < auto; i++){
                autoPreis = autoPreis * 3;
            }
        }
        button_buy_auto.setText(String.valueOf(autoPreis));
        if (multi != 0){
            for (int i = 0; i < multi; i++){
                multiPreis = multiPreis * 2;
            }
        }
        button_buy_multi.setText(String.valueOf(multiPreis));
        if (grandma != 0){
            for (int i = 0; i < grandma; i++){
                grandmaPreis = grandmaPreis * 2;
            }
        }
        button_buy_grandma.setText(String.valueOf(grandmaPreis));
        if (bakery != 0){
            for (int i = 0; i < bakery; i++){
                bakeryPreis = bakeryPreis * 2;
            }
        }
        button_buy_bakery.setText(String.valueOf(bakeryPreis));


        if (score2 != 0){
            billions_text2.setVisibility(View.VISIBLE);
            textView_counter_shop.setText((String.valueOf(score2)));
            button_buy_auto.setEnabled(false);
            button_buy_multi.setEnabled(false);
            button_buy_grandma.setEnabled(false);
        }
        else {
            billions_text2.setVisibility(View.INVISIBLE);
        }
        button_buy_grandma.setText(String.valueOf(grandmaPreis));

        button_back.setOnClickListener(v -> {
            // save the current cookie counter value to external file
            saveAll();
            startActivity(new Intent(Shop.this, MainActivity.class));
        });

        button_buy_auto.setOnClickListener(v -> {
            int inputAnzahlAuto = Integer.parseInt(anzahl_auto.getText().toString());
            int buttonAutoText = Integer.parseInt(button_buy_auto.getText().toString());
                if(score >= buttonAutoText) {
                    int x1 = purchaseUpgrade(inputAnzahlAuto, buttonAutoText, score);
                    anzahl_auto.setText(String.valueOf(x1));
                    auto = x1;
                    String buffer = button_buy_auto.getText().toString();
                    int y = Integer.parseInt(buffer) *3;
                    button_buy_auto.setText(String.valueOf(y));
                    textView_counter_shop.setText((String.valueOf(score)));
                }
            saveAll();
        });

        button_buy_multi.setOnClickListener(v -> {
            int inputAnzahlMulti = Integer.parseInt(anzahl_multi.getText().toString());
            int buttonMultiText = Integer.parseInt(button_buy_multi.getText().toString());
            if(score >= buttonMultiText) {
                    int x2 = purchaseUpgrade(inputAnzahlMulti, buttonMultiText, score);
                    anzahl_multi.setText(String.valueOf(x2));
                    multi = x2;
                    String buffer = button_buy_multi.getText().toString();
                    int y = Integer.parseInt(buffer) *2;
                    button_buy_multi.setText(String.valueOf(y));
                    textView_counter_shop.setText((String.valueOf(score)));
                }
            saveAll();
        });

        button_buy_grandma.setOnClickListener(v -> {
            int inputAnzahlGrandma = Integer.parseInt(anzahl_grandma.getText().toString());
                int buttonGrandmaText = Integer.parseInt(button_buy_grandma.getText().toString());
                if (score >= buttonGrandmaText) {
                    int x3 = purchaseUpgrade(inputAnzahlGrandma, buttonGrandmaText, score);
                    anzahl_grandma.setText(String.valueOf(x3));
                    grandma = x3;
                    String buffer = button_buy_grandma.getText().toString();
                    int y = Integer.parseInt(buffer) * 2;
                    button_buy_grandma.setText(String.valueOf(y));
                    textView_counter_shop.setText((String.valueOf(score)));
                }
            saveAll();
        });

        button_buy_bakery.setOnClickListener(v -> {
            // TODO: Fix incrementation of anzahl_bakery ~~ bugged because of score2 vs score issues
            int inputAnzahlBakery = Integer.parseInt(anzahl_bakery.getText().toString());
            int buttonBakeryText = Integer.parseInt(button_buy_bakery.getText().toString());
            if(score2 >= buttonBakeryText){
                score2 = score2 - Integer.parseInt(button_buy_bakery.getText().toString());
                int x4 = purchaseUpgrade(inputAnzahlBakery, buttonBakeryText, score2);
                anzahl_bakery.setText(String.valueOf(x4));
                bakery = x4;
                String buffer = button_buy_bakery.getText().toString();
                int y = Integer.parseInt(buffer) * 2;
                button_buy_bakery.setText(String.valueOf(y));
                textView_counter_shop.setText(String.valueOf(score2));
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
        saveToFile(String.valueOf(score2), FILE_SCORE2);
        saveToFile(String.valueOf(auto),FILE_AUTO);
        saveToFile(String.valueOf(multi),FILE_MULTI);
        saveToFile(String.valueOf(grandma), FILE_GRANDMA);
        saveToFile(String.valueOf(bakery), FILE_BAKERY);
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