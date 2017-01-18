package com.jparzonka.time_interval_app;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.jparzonka.mylibrary.j2xx.FT_Device;
import com.jparzonka.time_interval_app.data.DTO;
import com.jparzonka.time_interval_app.fragments.FrequencyModeFragment;
import com.jparzonka.time_interval_app.fragments.TimeIntervalModeFragment;

/**
 * Created by Jakub on 2016-12-26.
 */
public class SendDataActivity extends AppCompatActivity {
    private static boolean isExternalClockSelected = false;
    private CheckBox externalClockCheckbox;
    private static String selectedMode = "";
    private static TimeIntervalModeFragment timeIntervalModeFragment;
    private static FrequencyModeFragment frequencyModeFragment;
    private DTO dto;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    public static final String FT_DEVICE = "FT_DEVICE";
    private static FT_Device ftDev = null;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_data_layout);


        try {
            setFtDev(OpenDeviceActivity.getFtDev());
        } catch (NullPointerException ignored) {
            ignored.printStackTrace();
        }

        externalClockCheckbox = (CheckBox) findViewById(R.id.external_clock_checkbox);
        externalClockCheckbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (externalClockCheckbox.isChecked()) {
                    setExternalClockSelected(true);
                    Log.i("SendDataActivity", "setExternalClockSelected is true");
                } else if (!externalClockCheckbox.isChecked()) {
                    setExternalClockSelected(false);
                }
            }
        });
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.mode_radio_group);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                fragmentManager = getFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                // find which radio button is selected
                if (checkedId == R.id.ti_radio_button) {
                    setSelectedMode("TI");
                    timeIntervalModeFragment = new TimeIntervalModeFragment();
                    fragmentTransaction.replace(R.id.operation_mode_fragment, timeIntervalModeFragment).commit();
                    Log.i("SendDataActivity", "fragment replaced on TimeIntervalModeFragment");
                } else if (checkedId == R.id.frequency_radio_button) {
                    setSelectedMode("F");
                    frequencyModeFragment = new FrequencyModeFragment();
                    fragmentTransaction.replace(R.id.operation_mode_fragment, frequencyModeFragment).commit();
                    Log.i("SendDataActivity", "fragment replaced on FrequencyModeFragment");
                }
            }
        });

        Button startButton = (Button) findViewById(R.id.start_button);
        startButton.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                dto = new DTO();
                Log.i("Data", dto.toString());
                //    if(dto.)
                handleSending(dto);
            }
        });

        Button resetButton = (Button) findViewById(R.id.reset_button);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
//                BitSet reset = BitSetHandler.getRESET_Address();
//                byte[] outData = reset.toByteArray();
//                new MenuActivity().sendDataToGenerator(outData);
            }
        });


    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("ShowToast")
    private void handleSending(DTO dto) {
//        BitSetHandler s = new BitSetHandler(dto);
//        sendDataToGenerator(s.getSET_S_Address());
//        sendDataToGenerator(s.getSET_S_DataArray());
//        sendDataToGenerator(s.getSET_TRIG_Address());
//        sendDataToGenerator(s.getSET_TRIG_DataArray());
//        sendDataToGenerator(s.getTRIG_DIV_Address());
//        sendDataToGenerator(s.getTRIG_DIV_DataArray());
//        sendDataToGenerator(s.getSYNTH_N_Address());
//        sendDataToGenerator(s.getSYNTH_N_DataArray());
//        sendDataToGenerator(s.getADF4360_LOAD_Address());
//        sendDataToGenerator(s.getADF4360_LOAD_R_COUNTER_LATCH_DataArray());
//        sendDataToGenerator(s.getADF4360_LOAD_Address());
//        sendDataToGenerator(s.getADF4360_LOAD_CONTROL_LATCH_DataArray());
//        sendDataToGenerator(s.getADF4360_LOAD_Address());
//        sendDataToGenerator(s.getADF4360_LOAD_N_COUNTER_LATCH_DataArray());
//        sendDataToGenerator(s.getRESET_Address());
//        sendDataToGenerator(s.getRESET_POINTLESS_DataArray());
//        sendDataToGenerator(s.getCFR1_Address());
//        sendDataToGenerator(s.getCFR1_DataArray());
//        sendDataToGenerator(s.getFTW0_Address());
//        sendDataToGenerator(s.getFTW0_DataArray());
    }

    public void sendDataToGenerator(byte[] outputData) {
        try {
            for (byte x : outputData) {
                Toast.makeText(this, "SDA/SDTG:" + x + " ", Toast.LENGTH_SHORT).show();
            }
            ftDev.setLatencyTimer((byte) 16);
            int result = ftDev.write(outputData, outputData.length, false);
            Toast.makeText(this, "SDA/SDTG/Data bytes: " + result, Toast.LENGTH_SHORT).show();
        } catch (NullPointerException e) {
            Toast.makeText(this, "SDA/SDTG/ftDev == null", Toast.LENGTH_SHORT).show();
        }
    }


    public static TimeIntervalModeFragment getTimeIntervalModeFragment() {
        return timeIntervalModeFragment;
    }

    public static FrequencyModeFragment getFrequencyModeFragment() {
        return frequencyModeFragment;
    }

    public static boolean getExternalClockSelected() {
        return isExternalClockSelected;
    }

    public void setExternalClockSelected(boolean externalClockSelected) {
        isExternalClockSelected = externalClockSelected;
    }

    public static String getSelectedMode() {
        return selectedMode;
    }

    public void setSelectedMode(String selectedMode) {
        this.selectedMode = selectedMode;
    }

    public static void setFtDev(FT_Device ftDev) {
        SendDataActivity.ftDev = ftDev;
    }

}
