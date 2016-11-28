package com.jparzonka.time_interval_app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.jparzonka.mylibrary.j2xx.D2xxManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        System.out.println(Boolean.valueOf(String.valueOf(0)));
        try {
            D2xxManager manager = D2xxManager.getInstance(getApplicationContext());
        } catch (D2xxManager.D2xxException e) {
            e.printStackTrace();
        }

    }
}
