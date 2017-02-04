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
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.jparzonka.mylibrary.j2xx.FT_Device;
import com.jparzonka.time_interval_app.data.CommandAddresses;
import com.jparzonka.time_interval_app.data.CommandsHandler;
import com.jparzonka.time_interval_app.data.Convert;
import com.jparzonka.time_interval_app.data.DataForGenerator;
import com.jparzonka.time_interval_app.data.FrequencyModeData;
import com.jparzonka.time_interval_app.fragments.FrequencyModeFragment;
import com.jparzonka.time_interval_app.fragments.TimeIntervalModeFragment;

import java.util.Arrays;
import java.util.Objects;

/**
 * Created by Jakub on 2016-12-26.
 */
public class SendDataActivity extends AppCompatActivity {
    private static boolean isExternalClockSelected = false, isADFset = false;
    private CheckBox externalClockCheckbox;
    private static String selectedMode = "";
    private static TimeIntervalModeFragment timeIntervalModeFragment;
    private static FrequencyModeFragment frequencyModeFragment;
    private DataForGenerator dataForGenerator;
    private EditText addressEditText, commandEditText;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    public static final String FT_DEVICE = "FT_DEVICE";
    private static FT_Device ftDev = null;
    private byte[] data;
    private String dataHex, addressHex, commandHex;

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

        addressEditText = (EditText) findViewById(R.id.address_edit_text);
        commandEditText = (EditText) findViewById(R.id.command_edit_text);

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
                    setSelectedMode(DataForGenerator.TIME_INTERVALS);
                    timeIntervalModeFragment = new TimeIntervalModeFragment();
                    fragmentTransaction.replace(R.id.operation_mode_fragment, timeIntervalModeFragment).commit();
                    Log.i("SendDataActivity", "fragment replaced on TimeIntervalModeFragment");
                } else if (checkedId == R.id.frequency_radio_button) {
                    setSelectedMode(DataForGenerator.FREQUENCY);
                    //Inicjalizacja danych dla trybu 'F'
                    FrequencyModeData frequencyModeData = new FrequencyModeData();
                    frequencyModeFragment = new FrequencyModeFragment();
                    fragmentTransaction.replace(R.id.operation_mode_fragment, frequencyModeFragment).commit();
                    Log.i("SendDataActivity", "fragment replaced on FrequencyModeFragment");
                }
                Toast.makeText(getApplicationContext(), "Selected mode: " + selectedMode, Toast.LENGTH_SHORT).show();
            }
        });

        Button resetButton = (Button) findViewById(R.id.reset_button);
        resetButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                dataHex = "";
                data = new byte[]{0x0C, 0x00, 0x00, 0x00, 0x00};
                sendDataToGenerator(data);
                Toast.makeText(v.getContext(), "Urządzenie zostało zresetowane", Toast.LENGTH_SHORT).show();

            }
        });

        Button trigDivButton = (Button) findViewById(R.id.trigdiv_button);
        trigDivButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
//                dataHex = "0E";
//                byte[] address = Convert.hexStringToByteArray(dataHex);
//                Toast.makeText(v.getContext(), "Address: " + dataHex, Toast.LENGTH_SHORT).show();
//                sendDataToGenerator(data);
//                byte[] data = {0x02, 0x00, 0x00, 0x00};
//                Toast.makeText(v.getContext(), "Data: " + dataHex, Toast.LENGTH_SHORT).show();
//
//                sendDataToGenerator(ArrayUtils.addAll(address, data));
            }
        });

        Button adfButton = (Button) findViewById(R.id.loremipsum);
        adfButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                try {
                    addressHex = String.valueOf(addressEditText.getText());
                    commandHex = String.valueOf(commandEditText.getText());
                    StringBuilder s = new StringBuilder(addressHex);
                    s.append(commandHex);
                    String x = s.toString();
                    data = Convert.hexStringToByteArray(x);
                    Toast.makeText(v.getContext(), "DATA: " + s, Toast.LENGTH_SHORT).show();
                    sendDataToGenerator(data);

//                    data = Convert.hexStringToByteArray(commandHex);
//                    Toast.makeText(v.getContext(), "Data: " + commandHex, Toast.LENGTH_SHORT).show();
//                    sendDataToGenerator(data);

                } catch (NumberFormatException nfe) {
                    Toast.makeText(v.getContext(), "Wprowadź poprawny formay danych", Toast.LENGTH_SHORT).show();
                }
            }
        });


        Button startButton = (Button) findViewById(R.id.start_button);
        startButton.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                if (Objects.equals(selectedMode, DataForGenerator.TIME_INTERVALS)) {
                    dataForGenerator = new DataForGenerator();
                    Log.i("Data", dataForGenerator.toString());
                    Toast.makeText(v.getContext(), dataForGenerator.toString(), Toast.LENGTH_LONG).show();
                    handleSending(dataForGenerator);
                } else if (Objects.equals(selectedMode, DataForGenerator.FREQUENCY)) {
                    dataForGenerator = new DataForGenerator();
                    Log.i("Data", dataForGenerator.toString());
                    Toast.makeText(v.getContext(), dataForGenerator.toString(), Toast.LENGTH_LONG).show();
                    handleSending(dataForGenerator);
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("ShowToast")
    private void handleSending(DataForGenerator dataForGenerator) {
        CommandsHandler s = new CommandsHandler(dataForGenerator);
        if (!isADFset) {
            setSynthesizer(s);
            isADFset = true;
        }
        Toast.makeText(this, "SDA/handleSending/isADFset = " + isADFset, Toast.LENGTH_SHORT).show();

//        Toast.makeText(getApplicationContext(), "Selected mode in handleSending: \n" + selectedMode, Toast.LENGTH_SHORT).show();

        if (selectedMode.equals(DataForGenerator.TIME_INTERVALS)) {
            Toast.makeText(this, "SDA/handleSending/TI_MODE ", Toast.LENGTH_SHORT).show();
            handleTimeIntervalMode(s);
        } else if (selectedMode.equals(DataForGenerator.FREQUENCY)) {
            Toast.makeText(this, "SDA/handleSending/F_MODE ", Toast.LENGTH_SHORT).show();
            handleFrequencyMode(s, dataForGenerator);
        } else {
            throw new NullPointerException("Coś się pokićkało z trybami!");
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void handleFrequencyMode(CommandsHandler s, DataForGenerator dfg) {
        Toast.makeText(this, "SDA/handleFrequencyMode ", Toast.LENGTH_SHORT).show();
        // SET_S
//        byte[] sets = new byte[]{0x00, 0x31, 0x22, 0x00, 0x00};
//        sendDataToGenerator(sets);
        sendDataToGenerator(s.getSET_S());
        // WRITE_FTWO
//        byte[] ftw0 = new byte[]{0x44, (byte) 0xFF, (byte) 0x7C, (byte) 0x50, 0x07};
//        sendDataToGenerator(ftw0);
        sendDataToGenerator(Convert.addByteArray(Convert.hexStringToByteArray(CommandAddresses.FTW0.getAddress()), dfg.getFreqencyInMhz()));
        // SYNTH_N
//        byte[] synth = new byte[]{0x0F, 0x0C, 0x00, 0x00, 0x00};
//        sendDataToGenerator(synth);
        sendDataToGenerator(Convert.addByteArray(Convert.hexStringToByteArray(CommandAddresses.SYNTH_N.getAddress()), dfg.getFrequencyN()));
        // TRIG_DIV
//        byte[] trig = new byte[]{0x0E, 0x00, 0x00, 0x00, 0x00};
//        sendDataToGenerator(trig);
        sendDataToGenerator(s.getTRIG_DIV());
    }

    private void setSynthesizer(CommandsHandler s) {
        Toast.makeText(this, "SDA/setSynthesizer ", Toast.LENGTH_SHORT).show();
        sendDataToGenerator(s.getADF4360_LOAD_R_COUNTER_LATCH());
        sendDataToGenerator(s.getADF4360_LOAD_CONTROL_LATCH());
        sendDataToGenerator(s.getADF4360_LOAD_N_COUNTER_LATCH());
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void handleTimeIntervalMode(CommandsHandler s) {
        Toast.makeText(this, "SDA/handleTimeIntervalMode ", Toast.LENGTH_SHORT).show();
        sendDataToGenerator(s.getSET_S());
        sendDataToGenerator(s.getSET_TRIG());
        sendDataToGenerator(s.getTRIG_DIV());
        sendDataToGenerator(s.getSYNTH_N(dataForGenerator.getTimeInterval()));
        sendDataToGenerator(s.getRESET());
        sendDataToGenerator(s.getCFR1());
        sendDataToGenerator(s.getFTW0());
    }


    public void sendDataToGenerator(byte[] outputData) {
        Log.i("SDA/SDTG", Arrays.toString(outputData));

        try {
//            for (byte x : outputData) {
//                Toast.makeText(this, "SDA/SDTG:" + x + " ", Toast.LENGTH_SHORT).show();
//            }
////          Toast.makeText(this, "SDA/SDTG:" + Arrays.toString(outputData) + " ", Toast.LENGTH_SHORT).show();
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
