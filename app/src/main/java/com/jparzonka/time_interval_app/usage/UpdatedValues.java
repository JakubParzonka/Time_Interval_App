package com.jparzonka.time_interval_app.usage;

import android.util.Log;

import com.jparzonka.time_interval_app.DTO.DTO;

import static android.content.ContentValues.TAG;

/**
 * Created by Jakub on 2016-12-29.
 */

public class UpdatedValues {
    private static double kHz, Hz, mHz, s, ms, micros;
    private static boolean isPeriodTriggerSectionSelected;

    public static void setkHz(double kHz) {
        UpdatedValues.kHz = kHz;
    }

    public static void setHz(double hz) {
        UpdatedValues.Hz = hz;
    }

    public static void setmHz(double mHz) {
        UpdatedValues.mHz = mHz;
    }

    public static void setS(double s) {
        UpdatedValues.s = s;
    }

    public static void setMs(double ms) {
        UpdatedValues.ms = ms;
    }

    public static void setMicros(double micros) {
        UpdatedValues.micros = micros;
    }

    public static void setPeriodTriggerSectionSelected() {
        isPeriodTriggerSectionSelected = DTO.isPeriodTriggerSectionSelected();

    }

    public static double getValueToUpdate() {
        //Jeśli true, to dodajemy HZ, jeśli false, to s
        if (isPeriodTriggerSectionSelected) {
            double result = kHz * 1000 + Hz + mHz / 1E+3;
            Log.i(TAG, " result " + result);
            return result;
        } else {
            double result = s + ms / 1E+3 + micros / 1E+6;
            return result;
        }
    }
}
