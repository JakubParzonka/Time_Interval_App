package com.jparzonka.time_interval_app.fragments;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.jparzonka.mylibrary.j2xx.D2xxManager;
import com.jparzonka.mylibrary.j2xx.FT_Device;
import com.jparzonka.time_interval_app.R;

import java.util.ArrayList;
import java.util.List;


public class SendDataFragment extends Fragment {
    private View view;
    private static Context deviceContext;
    private D2xxManager ftdiD2xxManager;
    private FT_Device ftDevice = null;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toast.makeText(view.getContext(), "Let's send some data!", Toast.LENGTH_SHORT).show();

    }

    public SendDataFragment() {
    }

    @SuppressLint("ValidFragment")
    public SendDataFragment(D2xxManager ftdiD2xxManager, FT_Device ftDevice) {
        this.ftdiD2xxManager = ftdiD2xxManager;
        this.ftDevice = ftDevice;
    }


    private class TabHandler extends AppCompatActivity {

        public TabHandler() {
            toolbar = (Toolbar) view.findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            viewPager = (ViewPager) view.findViewById(R.id.viewpager);
            setupViewPager(viewPager);

            tabLayout = (TabLayout) view.findViewById(R.id.tabs);
            tabLayout.setupWithViewPager(viewPager);

        }

        private void setupViewPager(ViewPager viewPager) {
            ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
            adapter.addFragment(new TimeIntervalModeFragment(), "ONE");
            adapter.addFragment(new FrequencyModeFragment(), "TWO");
            viewPager.setAdapter(adapter);
        }

        class ViewPagerAdapter extends FragmentPagerAdapter {
            private final List<android.support.v4.app.Fragment> mFragmentList = new ArrayList<>();
            private final List<String> mFragmentTitleList = new ArrayList<>();

            public ViewPagerAdapter(FragmentManager manager) {
                super(manager);
            }

            @Override
            public android.support.v4.app.Fragment getItem(int position) {
                return mFragmentList.get(position);
            }

            @Override
            public int getCount() {
                return mFragmentList.size();
            }

            public void addFragment(android.support.v4.app.Fragment fragment, String title) {
                mFragmentList.add(fragment);
                mFragmentTitleList.add(title);
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return mFragmentTitleList.get(position);
            }
        }
    }
}
