package com.jparzonka.time_interval_app;

import android.app.Fragment;
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

import com.jparzonka.mylibrary.j2xx.D2xxManager;

public class MenuActivity extends AppCompatActivity {
    public static D2xxManager ftD2xx = null;
    public static int currect_index = 0;
    public static int old_index = -1;
    private static Fragment currentFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            ftD2xx = D2xxManager.getInstance(this);
        } catch (D2xxManager.D2xxException ex) {
            ex.printStackTrace();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        InfoPanelFragment.setParameters(getApplicationContext(), ftD2xx);
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

    public void infoDeviceTextViewOnClick(View view) {
        Fragment fragment = new InfoPanelFragment();
        //fragment.setArguments(getIntent().getExtras());

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id., fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();

//        Intent intent = new Intent(this, InfoPanel.class);
//        startActivity(intent);

    }

    public void send_data_TextViewOnClick(View view) {
        Intent intent = new Intent(this, SendDataActivity.class);
        startActivity(intent);

    }


    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String TAG = "FragL";
            String action = intent.getAction();
            if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {
                Log.i(TAG, "DETACHED...");
            }
        }
    };


}
