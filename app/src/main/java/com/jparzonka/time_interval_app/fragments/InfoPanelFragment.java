package com.jparzonka.time_interval_app.fragments;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.jparzonka.mylibrary.j2xx.D2xxManager;
import com.jparzonka.time_interval_app.R;
import com.jparzonka.time_interval_app.log_handler.LogHandler;

/**
 * Created by Jakub on 2016-12-18.
 */

public class InfoPanelFragment extends Fragment {
    private TextView deviceNameTextView, productIdTextView, serialNumberTextView, deviceNumberTextView, descriptionNumberTextView, errorTextView;
    private Button refreshButton;
    private UsbDevice device = null;
    private static D2xxManager d2xxManager;
    private static Context deviceInformationContext;
    private View view;

    public static void setParameters(Context contextParent, D2xxManager d2xxManager) {
        InfoPanelFragment.d2xxManager = d2xxManager;
        deviceInformationContext = contextParent;
    }

    public InfoPanelFragment() {

    }

    @SuppressLint("ValidFragment")
    public InfoPanelFragment(Context context, D2xxManager d2xxManager) {
        try {
            setD2xxManager(d2xxManager);
            setDeviceInformationContext(context);
        } catch (Exception e) {
            LogHandler.handleLog(view.getContext(), e.getMessage(), 1);
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_info_panel, container, false);
        Toast.makeText(view.getContext(), "Info Panel", Toast.LENGTH_SHORT).show();
        deviceNameTextView = (TextView) view.findViewById(R.id.device_name);
        descriptionNumberTextView = (TextView) view.findViewById(R.id.description_number);
        productIdTextView = (TextView) view.findViewById(R.id.product_id);
        serialNumberTextView = (TextView) view.findViewById(R.id.serial_number);
        deviceNumberTextView = (TextView) view.findViewById(R.id.device_number);
        errorTextView = (TextView) view.findViewById(R.id.error_number);

        refreshButton = (Button) view.findViewById(R.id.refresh_button);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refrestDeviceInformation(v);
            }
        });


        //UsbManager usbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
        IntentFilter filter = new IntentFilter();
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        deviceInformationContext.getApplicationContext().registerReceiver(mUsbPlugEvents, filter);
        Log.i("InfoPanelFragment", "in onCreate");


        return view;

    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i("onStart", "");
        try {
            getDeviceInformation();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            LogHandler.handleLog(view.getContext(), e.getMessage(), 2);
            e.printStackTrace();

        }

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("onResume", "");

        Intent intent = getActivity().getIntent();
        String action = intent.getAction();

        String hotplug = "android.intent.action.MAIN";
        if (hotplug.equals(action)) {

            try {
                getDeviceInformation();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                String s = e.getMessage();
                LogHandler.handleLog(view.getContext(), "120/onResume/InfoPanelFragment" + s, 3);
                if (s != null) {
                    errorTextView.setText(s);
                }
                e.printStackTrace();
            }
        }
    }

    public void getDeviceInformation() throws InterruptedException {
        int devCount = 0;

        devCount = d2xxManager.createDeviceInfoList(deviceInformationContext);
        Log.i("FtdiModeControl",
                "Device number = " + Integer.toString(devCount));
        if (devCount > 0) {
            D2xxManager.FtDeviceInfoListNode[] deviceList = new D2xxManager.FtDeviceInfoListNode[devCount];


            d2xxManager.getDeviceInfoListDetail(devCount);


            deviceList[0] = d2xxManager.getDeviceInfoListDetail(0);

            deviceNumberTextView.setText("Count of devices: " + Integer.toString(devCount));
            //TODO ===> RZUCA NULL'EM
            if (deviceList[0].serialNumber == null) {
                serialNumberTextView.setText("Serial number: " + deviceList[0].serialNumber + "(No Serial number)");
            } else {
                serialNumberTextView.setText("Serial number: " + deviceList[0].serialNumber);
            }

            if (deviceList[0].description == null) {
                descriptionNumberTextView.setText("Device Description: " + deviceList[0].description + "(No Description)");
            } else {
                descriptionNumberTextView.setText("Device Description: " + deviceList[0].description);
            }

//
            productIdTextView.setText("Device ID: " + String.format("%08x", deviceList[0].id));
//            int libVersion = D2xxManager.getLibraryVersion();
//            Library.setText("Library Version: " + convertIntToBcdString(libVersion) );

            // display the chip type for the first device
            switch (deviceList[0].type) {
                case D2xxManager.FT_DEVICE_232B:
                    deviceNameTextView.setText("Device Name : FT232B device");
                    break;

                case D2xxManager.FT_DEVICE_8U232AM:
                    deviceNameTextView.setText("Device Name : FT8U232AM device");
                    break;

                case D2xxManager.FT_DEVICE_UNKNOWN:
                    deviceNameTextView.setText("Device Name : Unknown device");
                    break;

                case D2xxManager.FT_DEVICE_2232:
                    deviceNameTextView.setText("Device Name : FT2232 device");
                    break;

                case D2xxManager.FT_DEVICE_232R:
                    deviceNameTextView.setText("Device Name : FT232R device");
                    break;

                case D2xxManager.FT_DEVICE_2232H:
                    deviceNameTextView.setText("Device Name : FT2232H device");
                    break;

                case D2xxManager.FT_DEVICE_4232H:
                    deviceNameTextView.setText("Device Name : FT4232H device");
                    break;

                case D2xxManager.FT_DEVICE_232H:
                    deviceNameTextView.setText("Device Name : FT232H device");
                    break;

                case D2xxManager.FT_DEVICE_X_SERIES:
                    deviceNameTextView.setText("Device Name : FTDI X_SERIES");
                    break;

                case D2xxManager.FT_DEVICE_4222_0:
                case D2xxManager.FT_DEVICE_4222_1_2:
                case D2xxManager.FT_DEVICE_4222_3:
                    deviceNameTextView.setText("Device Name : FT4222 device");
                    break;

                default:
                    deviceNameTextView.setText("Device Name : FT232B device");
                    break;
            }
        } else {
            deviceNumberTextView.setText("Number of devices: 0");
            deviceNameTextView.setText("Device Name : No device");
            serialNumberTextView.setText("Device Serial Number:");
            descriptionNumberTextView.setText("Device Description:");
//            DeviceLocation.setText("Device Location:");
            productIdTextView.setText("Device ID: ");
//            Library.setText("Library Version: ");

        }


    }

    public void refrestDeviceInformation(View view) {
        try {
            getDeviceInformation();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            String s = e.getMessage();
            LogHandler.handleLog(view.getContext(), s, 4);
            if (s != null) {
                errorTextView.setText(s);
            }
            e.printStackTrace();
        }
    }
    // 16842924
    private BroadcastReceiver mUsbPlugEvents = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {
                try {
                    getDeviceInformation();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    LogHandler.handleLog(view.getContext(), e.getMessage(), 5);
                    String s = e.getMessage();
                    if (s != null) {
                        errorTextView.setText(s);
                    }
                    e.printStackTrace();
                }
            }
        }
    };


    public static D2xxManager getD2xxManager() {
        return d2xxManager;
    }

    private static void setD2xxManager(D2xxManager d2xxManager) {
        InfoPanelFragment.d2xxManager = d2xxManager;
    }

    public static Context getDeviceInformationContext() {
        return deviceInformationContext;
    }

    private static void setDeviceInformationContext(Context deviceInformationContext) {
        InfoPanelFragment.deviceInformationContext = deviceInformationContext;
    }

}
