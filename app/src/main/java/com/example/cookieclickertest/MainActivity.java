package com.example.cookieclickertest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.ParcelFileDescriptor;
import android.os.UserHandle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

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
    TextView textView_counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button_save = findViewById(R.id.button_save);
        Button button_Shop = findViewById(R.id.button_Shop);
        textView_counter = findViewById(R.id.textView_counter);
        ImageButton button_cookie = findViewById(R.id.button_cookie);
        auto = loadFile(digitsFromFile, FILE_AUTO);
        multi = loadFile(digitsFromFile, FILE_MULTI);
        grandma = loadFile(digitsFromFile, FILE_GRANDMA);
        BigInteger BigScore;
        String zwichenString = String.valueOf(loadFile(digitsFromFile, FILE_SCORE));
        textView_counter.setText(zwichenString);
        clickvalue = calculateClickValue(clickvalue,multi);

        button_cookie.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int manualyClick = 1;
                cookieClick(manualyClick);
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

            Toast.makeText(this, "Saved to " + getFilesDir() + "/" + FILE, Toast.LENGTH_LONG).show();
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
        cookieClick(grandmaValue);
    }
}