package com.jparzonka.time_interval_app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.jparzonka.mylibrary.j2xx.D2xxManager;

public class MainActivity extends AppCompatActivity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView)findViewById(R.id.textView);
        System.out.println(Boolean.valueOf(String.valueOf(0)));
        try {
            D2xxManager manager = D2xxManager.getInstance(getApplicationContext());
          // System.out.println("Library version: " + D2xxManager.getLibraryVersion());
            
            textView.setText(String.valueOf(D2xxManager.getLibraryVersion()));
        } catch (D2xxManager.D2xxException e) {
            e.printStackTrace();
        }

    }
}
