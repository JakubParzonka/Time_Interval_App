package com.jparzonka.time_interval_app.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.jparzonka.time_interval_app.DTO.DTO;
import com.jparzonka.time_interval_app.R;
import com.jparzonka.time_interval_app.usage.UpdateControler;

/**
 * Created by Jakub on 2016-12-26.
 */

public class FrequencyTriggerSectionFragment extends Fragment {
    private View view;
    public EditText kHzFTS, hzFTS, mHzFTS;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_frequency_trigger_section_layout, container, false);
        kHzFTS = (EditText) view.findViewById(R.id.khz_trigger_section);
        hzFTS = (EditText) view.findViewById(R.id.hz_trigger_section);
        mHzFTS = (EditText) view.findViewById(R.id.mhz_trigger_section);

        kHzFTS.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.i("FTSF", "beforeTextChanged " + s);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.i("FTSF", "onTextChanged " + s);
                UpdateControler.setkHz(Double.parseDouble(s.toString()));
                Log.i("FTSF", "onTextChanged. Values passed");
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.i("FTSF", "afterTextChanged " + s.toString());

            }
        });

        Button startButton = (Button) view.findViewById(R.id.frequency_start_button);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DTO dto = new DTO();
                System.out.println(dto.toString());
            }
        });

        return view;
    }

    @Override
    public View getView() {
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
