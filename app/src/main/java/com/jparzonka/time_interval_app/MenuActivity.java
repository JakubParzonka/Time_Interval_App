package com.jparzonka.time_interval_app;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.jparzonka.mylibrary.j2xx.D2xxManager;
import com.jparzonka.time_interval_app.fragments.InfoPanelFragment;
import com.jparzonka.time_interval_app.fragments.IntroduceFragment;
import com.jparzonka.time_interval_app.fragments.SendDataFragment;
import com.jparzonka.time_interval_app.log_handler.LogHandler;
import com.jparzonka.time_interval_app.navigation_drawer.NavigationDrawerFragment;

import net.hockeyapp.android.CrashManager;
import net.hockeyapp.android.CrashManagerListener;

public class MenuActivity extends AppCompatActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks {
    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;


    public static D2xxManager ftD2xx = null;
    public static int currect_index = 0;
    public static int old_index = -1;

    private static Fragment currentFragment = null;
    private CharSequence mTitle;

    public MenuActivity() {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            ftD2xx = D2xxManager.getInstance(this);
        } catch (D2xxManager.D2xxException ex) {
            ex.printStackTrace();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        LogHandler lg = new LogHandler();
        try {
            InfoPanelFragment.setParameters(getApplicationContext(), ftD2xx);
        } catch (Exception e) {
            LogHandler.handleLog(getApplicationContext(), e.getMessage(), 11);
        }

        IntentFilter filter = new IntentFilter();
        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        filter.setPriority(500);
        this.registerReceiver(mUsbReceiver, filter);

        NavigationDrawerFragment.setActionBar(getSupportActionBar());

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

   /* public void infoDeviceTextViewOnClick(View view) {
        Fragment fragment = new InfoPanelFragment();
        //fragment.setArguments(getIntent().getExtras());

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id., fragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();

//        Intent intent = new Intent(this, InfoPanel.class);
//        startActivity(intent);

    }

    public void sendDataTextViewOnClick(View view) {
        Intent intent = new Intent(this, SendDataActivity.class);
        startActivity(intent);

    }*/


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

        // TODO Czy nie należy przekazywać do konstruktorów contextu oraz D2XXManagera?
        if (position == 0) {
            IntroduceFragment introduceFragment = new IntroduceFragment();
            fragmentTransaction.replace(android.R.id.content, introduceFragment).commit();
        } else if (position == 1) {
            try {
                InfoPanelFragment infoPanelFragment = new InfoPanelFragment(getApplicationContext(), ftD2xx);
                fragmentTransaction.replace(android.R.id.content, infoPanelFragment).commit();
            } catch (Exception e) {
                LogHandler.handleLog(getApplicationContext(), e.getMessage(), 12);
            }
        } else if (position == 2) {
            SendDataFragment sendDataFragment = new SendDataFragment();
            fragmentTransaction.replace(android.R.id.content, sendDataFragment).commit();
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

}
