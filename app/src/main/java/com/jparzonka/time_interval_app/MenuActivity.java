package com.jparzonka.time_interval_app;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jparzonka.mylibrary.j2xx.D2xxManager;
import com.jparzonka.mylibrary.j2xx.FT_Device;
import com.jparzonka.time_interval_app.fragments.InfoPanelFragment;
import com.jparzonka.time_interval_app.fragments.IntroduceFragment;
import com.jparzonka.time_interval_app.log_handler.LogHandler;
import com.jparzonka.time_interval_app.navigation_drawer.NavigationDrawerFragment;
import com.jparzonka.time_interval_app.sending_hanlder.ConnectionHandler;

import net.hockeyapp.android.CrashManager;
import net.hockeyapp.android.CrashManagerListener;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;

public class MenuActivity extends AppCompatActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks {
    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private static final String SERIAL_NUMBER = "FTS9MKOJ";
    private static SendDataActivity sendDataActivity;
    private static ConnectionHandler connectionHandler;
    private static D2xxManager d2xxManager = null;
    private static Context context;
    public static int currentIndex = 0;
    public static int old_index = -1;
    private static Fragment currentFragment = null;
    private CharSequence mTitle;
    private UsbInterface intf;

    private static FT_Device ftDev;
    int openIndex = 0;
    int devCount = -1;
    private static UsbDevice usbDeviceT5300;

    public MenuActivity() {
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            d2xxManager = D2xxManager.getInstance(this);
        } catch (D2xxManager.D2xxException ex) {
            ex.printStackTrace();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        MenuActivity.context = getApplicationContext();
        InfoPanelFragment.setParameters(getApplicationContext(), d2xxManager);

        IntentFilter filter = new IntentFilter();
        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        filter.setPriority(500);
        this.registerReceiver(mUsbReceiver, filter);

        if (d2xxManager != null)
            Toast.makeText(this, "MA: D2xxManager not null", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, "MA: D2xxManager is null", Toast.LENGTH_SHORT).show();

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        CrashManager.register(this, BuildConfig.HOCKEYAPP_APP_ID, new CrashManagerListener() {
            @Override
            public boolean shouldAutoUploadCrashes() {
                return true;
            }
        });

//        if (devCount <= 0) {
//            //Toast.makeText(getApplicationContext(), "MA: devCount <= 0", Toast.LENGTH_SHORT).show();
//            createDeviceList();
//        } else {
//            //Toast.makeText(getApplicationContext(), "MA: devCount > 0", Toast.LENGTH_SHORT).show();
//            connectFunction();
//        }

        UsbManager mUsbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
        HashMap<String, UsbDevice> deviceList = mUsbManager.getDeviceList();
        Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();

        while (deviceIterator.hasNext()) {
            UsbDevice device = deviceIterator.next();
            String s = device.getSerialNumber();
            if (Objects.equals(s, SERIAL_NUMBER)) {
                //Toast.makeText(this, "Ciastko", Toast.LENGTH_SHORT).show();
                setUsbDeviceT5300(device);
            }
//            Toast.makeText(this, device.getSerialNumber(), Toast.LENGTH_SHORT).show();
        }
        try {
            //   Toast.makeText(this, "T5300U: " + getUsbDeviceT5300().getSerialNumber(), Toast.LENGTH_SHORT).show();
            intf = usbDeviceT5300.getInterface(0);
            //   Toast.makeText(this, "Interface name: " + intf.getName(), Toast.LENGTH_SHORT).show();
        } catch (NullPointerException e) {
            Toast.makeText(this, "MA: There is no FTDI device attached!", Toast.LENGTH_SHORT).show();
        }
        int i = d2xxManager.createDeviceInfoList(this);
        Toast.makeText(this, "MA: device info list " + String.valueOf(i), Toast.LENGTH_SHORT);

        int devCount = deviceList.size();
        if (devCount > 0) {
            Toast.makeText(this, "MA: devCount > 0", Toast.LENGTH_SHORT).show();
            ftDev = new FT_Device(this, mUsbManager, usbDeviceT5300, intf);
            // ftDev = d2xxManager.openByUsbDevice(getApplicationContext(), usbDeviceT5300);
            //TODO tryOpen rzuca false!!
            Toast.makeText(this, "MA/tryOpen: " + d2xxManager.tryOpen(this, ftDev, null), Toast.LENGTH_SHORT).show();
            //  ftDev = d2xxManager.openByIndex(this, currect_index);
            if (ftDev == null) {
                Toast.makeText(this, "MA: ftDev == null", Toast.LENGTH_SHORT).show();
                return;
            } else {
                //ftDev = d2xxManager.openByUsbDevice(this, usbDeviceT5300);
                Toast.makeText(this, "MA: ftDev != null", Toast.LENGTH_SHORT).show();
            }
            try {
                if (ftDev.isOpen()) {
                    Toast.makeText(this, "MA: ftDev is open", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "MA: tDev is not open", Toast.LENGTH_SHORT).show();
                }
            } catch (NullPointerException e) {
                Toast.makeText(this, "MA: isOpen() throws null :/", Toast.LENGTH_SHORT).show();
            }
        } else Toast.makeText(this, "MA: devCount =< 0", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        new RuntimeException("Test");
    }

    @Override
    protected void onDestroy() {
        this.unregisterReceiver(mUsbReceiver);
        super.onDestroy();
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


    @Override
    public void onNavigationDrawerItemSelected(int position) {
        fragmentManager = getFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        if (position == 0) {
//            sendDataActivity = new SendDataActivity(ftDev);
//            fragmentTransaction.replace(android.R.id.content, sendDataActivity).commit();
        } else if (position == 1) {
            try {
                InfoPanelFragment infoPanelFragment = new InfoPanelFragment(getApplicationContext(), d2xxManager);
                fragmentTransaction.replace(android.R.id.content, infoPanelFragment).commit();
            } catch (Exception e) {
                LogHandler.handleLog(getApplicationContext(), e.getMessage(), 12);
            }
        } else if (position == 2) {
            IntroduceFragment introduceFragment = new IntroduceFragment();
            fragmentTransaction.replace(android.R.id.content, introduceFragment).commit();
        } else if (position == 3) {
            Toast.makeText(this, "NOT READY YET", Toast.LENGTH_SHORT).show();
        }
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
            case 4:
                mTitle = getString(R.string.title_section4);
                break;
        }
    }

//    public void restoreActionBar() {
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
//        actionBar.setDisplayShowTitleEnabled(true);
//        actionBar.setTitle(mTitle);
//    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_home, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MenuActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

    public static SendDataActivity getSendDataActivity() {
        return sendDataActivity;
    }

    public static D2xxManager getd2xxManager() {
        return d2xxManager;
    }

    public static Context getContext() {
        return context;
    }

    private static void setContext(Context context) {
        MenuActivity.context = context;

    }

    public static ConnectionHandler getConnectionHandler() {
        return connectionHandler;
    }

    public static UsbDevice getUsbDeviceT5300() {
        return usbDeviceT5300;
    }

    public static void setUsbDeviceT5300(UsbDevice usbDeviceT5300) {
        MenuActivity.usbDeviceT5300 = usbDeviceT5300;
    }


    public void sendDataToGenerator(byte[] outputData) {
/*      if (outputData != null){
            //  Toast.makeText(getAppContext(), "MA/SDTG: outputData size = " + outputData.length, Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(getAppContext(), "MA/SDTG: outputData is null", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            if (ftDev.isOpen()) {
          //      Toast.makeText(getAppContext(), "MA/SDTG: open device port OK", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getAppContext(), "device not open", Toast.LENGTH_SHORT).show();
                Log.e("j2xx", "SendMessage: device not open");
            }
        } catch (NullPointerException e) {
            Toast.makeText(getAppContext(), "MA/SDTG: isOpen() throws null :/", Toast.LENGTH_SHORT).show();
        }*/
        try {
            // ftDev.setLatencyTimer((byte) 16);
            int result = ftDev.write(outputData);
            Toast.makeText(getAppContext(), "MA/SDTG:Data bytes: " + result, Toast.LENGTH_SHORT).show();
        } catch (NullPointerException e) {
            Toast.makeText(getAppContext(), "MA/SDTG: ftDev == null", Toast.LENGTH_SHORT).show();
        }
    }

    public void setFtDev(FT_Device ftDev) {
        this.ftDev = ftDev;
    }

    public static Context getAppContext() {
        return MenuActivity.context;
    }
}
