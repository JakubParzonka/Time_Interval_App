package com.jparzonka.time_interval_app.data;

import android.util.Log;

import com.jparzonka.time_interval_app.SendDataActivity;
import com.jparzonka.time_interval_app.fragments.FrequencyTriggerSectionFragment;
import com.jparzonka.time_interval_app.fragments.PeriodTriggerSectionFragment;
import com.jparzonka.time_interval_app.fragments.TimeIntervalModeFragment;

/**
 * Created by Jakub on 2016-12-26.
 */

public class DTO {

    private String selectedMode;
    private static boolean isExternalClockSelected;
    private double secondsTimeIntervals;
    private double milisecondsTimeIntervals;
    private double microsecondsTimeIntervals;
    private double nanosecondsTimeIntervals;
    private double picosecondsTimeIntervals;
    private double outputWidth;
    private static boolean isPeriodTriggerSectionSelected;
    private double khzTrigger;
    private double hzTrigger;
    private double mhzTrigger;
    private double OCT, DAC;
    private boolean hasSignal_A_InvertedPolarization, hasSignal_B_InvertedPolarization, hasSignal_CW_InvertedPolarization;
    private double totalValueOfTimeIntervals, totalValueOfTrigger;

    public DTO() throws NullPointerException {
        SendDataActivity sdf = new SendDataActivity();
        setSelectedMode(sdf.getSelectedMode());
        setExternalClockSelected(SendDataActivity.getExternalClockSelected());

        TimeIntervalModeFragment timf = SendDataActivity.getTimeIntervalModeFragment();
        setSecondsTimeIntervals(timf.getsTI());
        setMilisecondsTimeIntervals(timf.getMsTI() / 1.0E+3);
        setMicrosecondsTimeIntervals(timf.getMicrosTI() / 1.0E+6);
        setNanosecondsTimeIntervals(timf.getNsTI() / 1.0E+9);
        setPicosecondsTimeIntervals(timf.getPsTI() / 1.0E+12);
        setOutputWidth(timf.getOutputWidth());

        setIsPeriodTriggerSectionSelected(TimeIntervalModeFragment.getPeriodTriggerSectionSelected());
        Log.i("DTO", "isPeriodTriggerSectionSelected: " + String.valueOf(isPeriodTriggerSectionSelected()));
        if (isPeriodTriggerSectionSelected) {
            PeriodTriggerSectionFragment ptsf = TimeIntervalModeFragment.getPeriodTriggerSectionFragment();
            double s = ptsf.getSecondPTS(), ms = ptsf.getMilisecondPTS() / 1.0E+3, micros = ptsf.getMicrosecondsPTS() / 1.0E+6;
            Log.i("DTO/konstruktor", "s: " + String.valueOf(s));
            Log.i("DTO/konstruktor", "ms: " + String.valueOf(ms));
            Log.i("DTO/konstruktor", "micros: " + String.valueOf(micros));

            if (s == 0) setkHzTrigger(0);
            else setkHzTrigger(1 / s);

            if (ms == 0) setHzTrigger(0);
            else setHzTrigger(1 / ms);

            if (micros == 0) setmHzTrigger(0);
            else setmHzTrigger(1 / micros);

        } else {
            FrequencyTriggerSectionFragment ftsf = TimeIntervalModeFragment.getFrequencyTriggerSectionFragment();
            setkHzTrigger(ftsf.getkHzFTS());
            setHzTrigger(ftsf.getHzFTS());
            setmHzTrigger(ftsf.getmHzFTS());
        }

        setTotalValueOfTimeIntervals(calculateTotalValueOfTimeIntervalsADouble());
        setTotalValueOfTrigger(calculateTotalValueOfFreqencyTriggerADouble());

        setOCT();
        setDAC();

        setHasSignal_A_InvertedPolarization(sdf.hasSignal_A_InvertedPolarization());
        setHasSignal_B_InvertedPolarization(sdf.hasSignal_B_InvertedPolarization());
        setHasSignal_CW_InvertedPolarization(sdf.hasSignal_CW_InvertedPolarization());

        System.out.println(toString());
    }

    private double calculateTotalValueOfTimeIntervalsADouble() {
        double s = getSecondsTimeIntervals(), ms = getMilisecondsTimeIntervals(), micro = getMicrosecondsTimeIntervals(),
                ns = getNanosecondsTimeIntervals(), ps = getPicosecondsTimeIntervals();
//        Log.i("DTO/s", String.valueOf(s));
//        Log.i("DTO/ms", String.valueOf(ms));
//        Log.i("DTO/micros", String.valueOf(micro));
//        Log.i("DTO/ns", String.valueOf(ns));
//        Log.i("DTO/ps", String.valueOf(ps));
        return s + ms + micro + ns + ps;
    }

    private double calculateTotalValueOfFreqencyTriggerADouble() {
        double kHz = getkHzTrigger(), Hz = getHzTrigger(), mHz = getmHzTrigger();
//        Log.i("DTO/kHz", String.valueOf(kHz));
//        Log.i("DTO/Hz", String.valueOf(Hz));
//        Log.i("DTO/mHz", String.valueOf(mHz));
        return kHz + Hz + mHz;
    }

    @Override
    public String toString() {
        return "DTO{" +
                "selectedMode='" + selectedMode + '\'' +
                ", secondsTimeIntervals=" + secondsTimeIntervals +
                ", milisecondsTimeIntervals=" + milisecondsTimeIntervals +
                ", microsecondsTimeIntervals=" + microsecondsTimeIntervals +
                ", nanosecondsTimeIntervals=" + nanosecondsTimeIntervals +
                ", picosecondsTimeIntervals=" + picosecondsTimeIntervals +
                ", outputWidth=" + outputWidth +
                ", khzTrigger=" + khzTrigger +
                ", hzTrigger=" + hzTrigger +
                ", mhzTrigger=" + mhzTrigger +
                ", OCT=" + OCT +
                ", DAC=" + DAC +
                ", hasSignal_A_InvertedPolarization=" + hasSignal_A_InvertedPolarization +
                ", hasSignal_B_InvertedPolarization=" + hasSignal_B_InvertedPolarization +
                ", hasSignal_CW_InvertedPolarization=" + hasSignal_CW_InvertedPolarization +
                ", totalValueOfTimeIntervals=" + totalValueOfTimeIntervals +
                ", totalValueOfTrigger=" + totalValueOfTrigger +
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

    public static void setExternalClockSelected(boolean externalClockSelected) {
        isExternalClockSelected = externalClockSelected;
    }

    public double getSecondsTimeIntervals() {
        return secondsTimeIntervals;
    }

    public void setSecondsTimeIntervals(double secondsTimeIntervals) {
        this.secondsTimeIntervals = secondsTimeIntervals;
    }

    public double getMilisecondsTimeIntervals() {
        return milisecondsTimeIntervals;
    }

    public void setMilisecondsTimeIntervals(double milisecondsTimeIntervals) {
        this.milisecondsTimeIntervals = milisecondsTimeIntervals;
    }

    public double getMicrosecondsTimeIntervals() {
        return microsecondsTimeIntervals;
    }

    public void setMicrosecondsTimeIntervals(double microsecondsTimeIntervals) {
        this.microsecondsTimeIntervals = microsecondsTimeIntervals;
    }

    public double getNanosecondsTimeIntervals() {
        return nanosecondsTimeIntervals;
    }

    public void setNanosecondsTimeIntervals(double nanosecondsTimeIntervals) {
        this.nanosecondsTimeIntervals = nanosecondsTimeIntervals;
    }

    public double getPicosecondsTimeIntervals() {
        return picosecondsTimeIntervals;
    }

    public void setPicosecondsTimeIntervals(double picosecondsTimeIntervals) {
        this.picosecondsTimeIntervals = picosecondsTimeIntervals;
    }

    public double getOutputWidth() {
        return outputWidth;
    }

    public void setOutputWidth(double outputWidth) {
        this.outputWidth = outputWidth;
    }

    public double getkHzTrigger() {
        return khzTrigger;
    }

    public void setkHzTrigger(double khzTrigger) {
        this.khzTrigger = khzTrigger;
    }

    public double getHzTrigger() {
        return hzTrigger;
    }

    public void setHzTrigger(double hzTrigger) {
        this.hzTrigger = hzTrigger;
    }

    public double getmHzTrigger() {
        return mhzTrigger;
    }

    public void setmHzTrigger(double mhzTrigger) {
        this.mhzTrigger = mhzTrigger;
    }


    public static boolean isPeriodTriggerSectionSelected() {
        return isPeriodTriggerSectionSelected;
    }

    public static void setIsPeriodTriggerSectionSelected(boolean isPeriodTriggerSectionSelected) {
        DTO.isPeriodTriggerSectionSelected = isPeriodTriggerSectionSelected;
    }

    public void setTotalValueOfTimeIntervals(double totalValueOfTimeIntervals) {
        this.totalValueOfTimeIntervals = totalValueOfTimeIntervals;
    }

    public double getTotalValueOfTimeIntervals() {
        return totalValueOfTimeIntervals;
    }


    public void setTotalValueOfTrigger(double totalValueOfTrigger) {
        this.totalValueOfTrigger = totalValueOfTrigger;
    }

    public double getTotalValueOfFrequencyTrigger() {
        return totalValueOfTrigger;
    }

    public double getOCT() {
        return OCT;
    }

    public void setOCT() {
        double f = getTotalValueOfFrequencyTrigger();
        OCT = 3.322 * Math.log10(f / 1039);
        Log.i("DTO/OCT", String.valueOf(OCT));
    }

    public double getDAC() {
        return DAC;
    }

    public void setDAC() {
        double f = getTotalValueOfFrequencyTrigger();
        DAC = 2048 - ((2078 * Math.pow(2, 10 + getOCT())) / f);
        Log.i("DTO/DAC", String.valueOf(DAC));
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
}
