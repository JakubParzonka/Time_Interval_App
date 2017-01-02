package com.jparzonka.time_interval_app.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.jparzonka.time_interval_app.data.DTO;
import com.jparzonka.time_interval_app.R;

/**
 * Created by Jakub on 2016-12-26.
 */

public class FrequencyTriggerSectionFragment extends Fragment {
    private View view;
    private EditText kHzFTS, hzFTS, mHzFTS;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_frequency_trigger_section_layout, container, false);
        kHzFTS = (EditText) view.findViewById(R.id.khz_trigger_section);
        hzFTS = (EditText) view.findViewById(R.id.hz_trigger_section);
        mHzFTS = (EditText) view.findViewById(R.id.mhz_trigger_section);

        Button startButton = (Button) view.findViewById(R.id.frequency_start_button);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DTO dto = new DTO();
                System.out.println(dto.toString());
            }
        });
        kHzFTS.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                kHzFTS.setText("");
                return false;
            }
        });
        hzFTS.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hzFTS.setText("");
                return false;
            }
        });
        mHzFTS.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mHzFTS.setText("");
                return false;
            }
        });
        return view;
    }

    public int getkHzFTS() {
        int kHzFT = (Integer.parseInt(kHzFTS.getText().toString()));
        Log.i("FTSF", "getkHzFTS: " + kHzFT);
        return kHzFT;
    }

    public int getHzFTS() {
        int HzFT = (Integer.parseInt(hzFTS.getText().toString()));
        Log.i("FTSF", "getHzFTS: " + HzFT);
        return HzFT;
    }

    public int getmHzFTS() {
        int mHzFT = (Integer.parseInt(mHzFTS.getText().toString()));
        Log.i("FTSF", "getmHzFTS: " + mHzFT);
        return mHzFT;
    }


}
