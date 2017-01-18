package com.jparzonka.time_interval_app.fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.jparzonka.time_interval_app.R;

/**
 * Created by Jakub on 2016-12-26.
 */

public class TimeIntervalModeFragment extends Fragment {

    private View view;
    private EditText sTIEditText, msTIEditText, microsTIEditText, nsTIEditText, psTIEditText;
    private static double outputWidth = 0;
    private static FrequencyTriggerSectionFragment frequencyTriggerSectionFragment;
    private static PeriodTriggerSectionFragment periodTriggerSectionFragment;
    private static CheckBox checkBoxA, checkBoxB, checkBoxCW;

    private static boolean isPeriodTriggerSectionSelected;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.ti_mode_layout, container, false);

        sTIEditText = (EditText) view.findViewById(R.id.seconds);
//        sTIEditText.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                sTIEditText.setText("");
//                return false;
//            }
//        });
        msTIEditText = (EditText) view.findViewById(R.id.miliseconds);
        msTIEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                msTIEditText.setText("");
                return false;
            }
        });
        microsTIEditText = (EditText) view.findViewById(R.id.microseconds);
        microsTIEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                microsTIEditText.setText("");
                return false;
            }
        });
        nsTIEditText = (EditText) view.findViewById(R.id.nanoseconds);
        nsTIEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                nsTIEditText.setText("");
                return false;
            }
        });
        psTIEditText = (EditText) view.findViewById(R.id.picoseconds);
        psTIEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                psTIEditText.setText("");
                return false;
            }
        });


        RadioGroup outputWidthRadioGroup = (RadioGroup) view.findViewById(R.id.output_width_radio_group);
        outputWidthRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.ten_radio_button) setOutputWidth(10);
                else if (checkedId == R.id.twenty_radio_button) setOutputWidth(20);
                else if (checkedId == R.id.fifty_radio_button) setOutputWidth(50);
                else if (checkedId == R.id.hundred_radio_button) setOutputWidth(100);
                else
                    Toast.makeText(view.getContext(), "Non of output widths has been selected!", Toast.LENGTH_SHORT).show();
                Log.i("TIMF/checkedId", String.valueOf(checkedId));
            }
        });

        RadioGroup triggerRadioGroup = (RadioGroup) view.findViewById(R.id.trigger_radio_group);
        triggerRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // find which radio button is selected
                if (checkedId == R.id.period_trigger_section_radio_button) {
                    setPeriodTriggerSectionSelected(true);
                    periodTriggerSectionFragment = new PeriodTriggerSectionFragment();
                    FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                    transaction.replace(R.id.trigger_section_frame_layout, periodTriggerSectionFragment).commit();
                    Log.i("TIMF", "fragment replaced on PeriodTriggerSectionFragment");

                } else if (checkedId == R.id.frequency_trigger_section_radio_button) {
                    setPeriodTriggerSectionSelected(false);
                    frequencyTriggerSectionFragment = new FrequencyTriggerSectionFragment();
                    FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
                    transaction.replace(R.id.trigger_section_frame_layout, frequencyTriggerSectionFragment).commit();
                    Log.i("TIMF", "fragment replaced on FrequencyTriggerSectionFragment");
                }
            }
        });

        checkBoxA = (CheckBox) view.findViewById(R.id.signal_A_polarization);
        checkBoxB = (CheckBox) view.findViewById(R.id.signal_B_polarization);
        checkBoxCW = (CheckBox) view.findViewById(R.id.signal_CW_polarization);
        return view;
    }


    public static double getOutputWidth() {
        return outputWidth;
    }

    private void setOutputWidth(double outputWidth) {
        TimeIntervalModeFragment.outputWidth = outputWidth;
    }

    public double getsTI() {
        double sTI = (Integer.parseInt(sTIEditText.getText().toString()));
        Log.i("TIMF", "getsTI: " + sTI);
        return sTI;
    }

    public double getMsTI() {
        double msTI = (Integer.parseInt(msTIEditText.getText().toString()));
        Log.i("TIMF", "getmsTI: " + msTI);
        return msTI;
    }

    public double getMicrosTI() {
        double microsTI = (Integer.parseInt(microsTIEditText.getText().toString()));
        Log.i("TIMF", "getmicrosTI: " + microsTI);
        return microsTI;
    }

    public double getNsTI() {
        double nsTI = (Integer.parseInt(nsTIEditText.getText().toString()));
        Log.i("TIMF", "getnsTI: " + nsTI);
        return nsTI;
    }

    public double getPsTI() {
        double psTI = (Integer.parseInt(psTIEditText.getText().toString()));
        Log.i("TIMF", "getpsTI: " + psTI);
        return psTI;
    }

    public void setPeriodTriggerSectionSelected(boolean periodTriggerSectionSelected) {
        isPeriodTriggerSectionSelected = periodTriggerSectionSelected;
    }

    public static boolean getPeriodTriggerSectionSelected() {
        return isPeriodTriggerSectionSelected;
    }

    public static FrequencyTriggerSectionFragment getFrequencyTriggerSectionFragment() {
        return frequencyTriggerSectionFragment;
    }

    public static PeriodTriggerSectionFragment getPeriodTriggerSectionFragment() {
        return periodTriggerSectionFragment;
    }

    public boolean hasSignal_A_InvertedPolarization() {
        return checkBoxA.isChecked();
    }

    public boolean hasSignal_B_InvertedPolarization() {
        return checkBoxB.isChecked();
    }

    public boolean hasSignal_CW_InvertedPolarization() {
        return checkBoxCW.isChecked();
    }
}
