package com.jparzonka.time_interval_app;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.jparzonka.mylibrary.j2xx.D2xxManager;

public class MenuActivity extends AppCompatActivity {

    public static D2xxManager ftD2xx = null;
    public static int currect_index = 0;
    public static int old_index = -1;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            ftD2xx = D2xxManager.getInstance(this);
        } catch (D2xxManager.D2xxException ex) {
            ex.printStackTrace();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();

//        FragmentMain fragmentMain = new FragmentMain();
//        fragmentTransaction.replace(android.R.id.content, fragmentMain).commit();


        IntentFilter filter = new IntentFilter();
        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        filter.setPriority(500);

        this.registerReceiver(mUsbReceiver, filter);

    }

    @Override
    protected void onDestroy() {
        this.unregisterReceiver(mUsbReceiver);
        super.onDestroy();
    }


    private void chooseActivity(String activity) {
        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        if (activity == "info") {
            InfoPanelFragment infoPanelFragment = new InfoPanelFragment();
            fragmentTransaction.replace(android.R.id.content, infoPanelFragment).commit();
            Toast.makeText(this, "Info panel", Toast.LENGTH_SHORT).show();
        }
        if (activity == "lorem1") {
            Lorem_Ipsum_1 lorem_ipsum_1 = new Lorem_Ipsum_1();
            fragmentTransaction.replace(android.R.id.content, lorem_ipsum_1).commit();
            Toast.makeText(this, "lorem_ipsum_1", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "No more features. SORRY BRO", Toast.LENGTH_SHORT).show();

        }

    }

    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String TAG = "FragL";
            String action = intent.getAction();
            Log.i(TAG, action);
//            if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {
//                Log.i(TAG, "DETACHED...");
//
//                if (currentFragment != null) {
//                    switch (currect_index) {
//                        case 5:
//                            ((DeviceUARTFragment) currentFragment).notifyUSBDeviceDetach();
//                            break;
//                        default:
//                            //((DeviceInformationFragment)currentFragment).onStart();
//                            break;
//                    }
//                }
//            }
        }
    };

    public void infoDeviceTextViewOnClick(View view) {
        chooseActivity("info");
    }

    public void lorem_ipsum_1_TextViewOnClick(View view) {
        chooseActivity("lorem1");
    }
}
