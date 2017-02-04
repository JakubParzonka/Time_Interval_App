package com.jparzonka.time_interval_app.data;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.jparzonka.time_interval_app.SendDataActivity;
import com.jparzonka.time_interval_app.fragments.FrequencyModeFragment;
import com.jparzonka.time_interval_app.fragments.TimeIntervalModeFragment;

import java.util.Arrays;
import java.util.Objects;

/**
 * Created by Jakub on 2016-12-26.
 */

public class DataForGenerator {
    public static String TIME_INTERVALS = "TI";
    public static String FREQUENCY = "F";

    private String selectedMode;
    private boolean isExternalClockSelected;
    private double outputWidth;
    private double OCT, DAC;
    private boolean hasSignal_A_InvertedPolarization, hasSignal_B_InvertedPolarization, hasSignal_CW_InvertedPolarization;
    private int frequencyPeriod;
    private byte[] timeInterval, freqencyInMhz, frequencyN, triggerFrequency;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public DataForGenerator() throws NullPointerException {
        SendDataActivity sdf = new SendDataActivity();
        setSelectedMode(SendDataActivity.getSelectedMode());
        setExternalClockSelected(SendDataActivity.getExternalClockSelected());

        if (Objects.equals(SendDataActivity.getSelectedMode(), TIME_INTERVALS)) {
            TimeIntervalModeFragment timf = SendDataActivity.getTimeIntervalModeFragment();
            setTimeInterval(timf.getTimeInterval());
            setOutputWidth(TimeIntervalModeFragment.getOutputWidth());
            setTriggerFrequency(timf.getFrequencyTrigger());
            setOCT();
            setDAC();
            setHasSignal_A_InvertedPolarization(timf.hasSignal_A_InvertedPolarization());
            setHasSignal_B_InvertedPolarization(timf.hasSignal_B_InvertedPolarization());
            setHasSignal_CW_InvertedPolarization(timf.hasSignal_CW_InvertedPolarization());
        } else if (Objects.equals(SendDataActivity.getSelectedMode(), FREQUENCY)) {
            FrequencyModeFragment fmf = SendDataActivity.getFrequencyModeFragment();
            setFreqencyInMhz(fmf.getFrequencyInMHz());
            setFrequencyN(fmf.getFrequencyN());
        }
    }

    @Override
    public String toString() {
        return "DataForGenerator{" +
                "selectedMode='" + selectedMode + '\'' +
                ", isExternalClockSelected=" + isExternalClockSelected +
                ", outputWidth=" + outputWidth +
                ", OCT=" + OCT +
                ", DAC=" + DAC +
                ", hasSignal_A_InvertedPolarization=" + hasSignal_A_InvertedPolarization +
                ", hasSignal_B_InvertedPolarization=" + hasSignal_B_InvertedPolarization +
                ", hasSignal_CW_InvertedPolarization=" + hasSignal_CW_InvertedPolarization +
                ", frequencyPeriod=" + frequencyPeriod +
                ", timeInterval=" + Arrays.toString(timeInterval) +
                ", freqencyInMhz=" + Arrays.toString(freqencyInMhz) +
                ", frequencyN=" + Arrays.toString(frequencyN) +
                ", triggerFrequency=" + Arrays.toString(triggerFrequency) +
                '}';
    }

    public String getSelectedMode() {
        return selectedMode;
    }

    public void setSelectedMode(String selectedMode) {
        this.selectedMode = selectedMode;
    }

    public boolean isExternalClockSelected() {
        return isExternalClockSelected;
    }

    public void setExternalClockSelected(boolean externalClockSelected) {
        isExternalClockSelected = externalClockSelected;
    }


    public double getOCT() {
        return OCT;
    }

    public void setOCT() {
        double f = getFrequencyPeriod();
        if (f != 0) {
            OCT = 3.322 * Math.log10(f / 1039);
            Log.i("DataForGenerator/OCT", String.valueOf(OCT));
        }
    }

    public double getDAC() {
        return DAC;
    }

    public void setDAC() {
        double f = getFrequencyPeriod();
        if (f != 0) {
            DAC = 2048 - ((2078 * Math.pow(2, 10 + getOCT())) / f);
            Log.i("DataForGenerator/DAC", String.valueOf(DAC));
        }
    }

    public boolean isHasSignal_A_InvertedPolarization() {
        return hasSignal_A_InvertedPolarization;
    }

    public void setHasSignal_A_InvertedPolarization(boolean hasSignal_A_InvertedPolarization) {
        this.hasSignal_A_InvertedPolarization = hasSignal_A_InvertedPolarization;
    }

    public boolean isHasSignal_B_InvertedPolarization() {
        return hasSignal_B_InvertedPolarization;
    }

    public void setHasSignal_B_InvertedPolarization(boolean hasSignal_B_InvertedPolarization) {
        this.hasSignal_B_InvertedPolarization = hasSignal_B_InvertedPolarization;
    }

    public boolean isHasSignal_CW_InvertedPolarization() {
        return hasSignal_CW_InvertedPolarization;
    }

    public void setHasSignal_CW_InvertedPolarization(boolean hasSignal_CW_InvertedPolarization) {
        this.hasSignal_CW_InvertedPolarization = hasSignal_CW_InvertedPolarization;
    }

    public byte[] getFreqencyInMhz() {
        return freqencyInMhz;
    }

    public void setFreqencyInMhz(byte[] freqencyInMhz) {
        this.freqencyInMhz = freqencyInMhz;
    }

    public int getFrequencyPeriod() {
        return frequencyPeriod;
    }

    public void setFrequencyPeriod(int frequencyPeriod) {
        this.frequencyPeriod = frequencyPeriod;
    }

    public byte[] getTriggerFrequency() {
        return triggerFrequency;
    }

    public void setTriggerFrequency(byte[] triggerFrequency) {
        this.triggerFrequency = triggerFrequency;
    }

    public double getOutputWidth() {
        return outputWidth;
    }

    public void setOutputWidth(double outputWidth) {
        this.outputWidth = outputWidth;
    }


    public byte[] getTimeInterval() {
        return timeInterval;
    }

    public void setTimeInterval(byte[] timeInterval) {
        this.timeInterval = timeInterval;
    }

    public byte[] getFrequencyN() {
        return frequencyN;
    }

    public void setFrequencyN(byte[] frequencyN) {
        this.frequencyN = frequencyN;
    }
}