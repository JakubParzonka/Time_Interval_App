package com.jparzonka.time_interval_app.DTO;

import com.jparzonka.time_interval_app.fragments.FrequencyTriggerSectionFragment;
import com.jparzonka.time_interval_app.fragments.PeriodTriggerSectionFragment;
import com.jparzonka.time_interval_app.fragments.SendDataFragment;
import com.jparzonka.time_interval_app.fragments.TimeIntervalModeFragment;

/**
 * Created by Jakub on 2016-12-26.
 */

public class DTO {

    private String selectedMode;

    private boolean isExternalClockSelected;

    private int secondsTimeIntervals;
    private int milisecondsTimeIntervals;
    private int microsecondsTimeIntervals;
    private int nanosecondsTimeIntervals;
    private int picosecondsTimeIntervals;

    private int outputWidth;

    private boolean isPeriodTriggerSectionSelected;

    private int khzTrigger;
    private int hzTrigger;
    private int mhzTrigger;

    public DTO() {
        SendDataFragment sdf = new SendDataFragment();
        setSelectedMode(sdf.getSelectedMode());
        setExternalClockSelected(sdf.isExternalClockSelected());

        TimeIntervalModeFragment timf = new TimeIntervalModeFragment();
        setSecondsTimeIntervals(timf.getsTI());
        setMilisecondsTimeIntervals(timf.getMsTI());
        setMicrosecondsTimeIntervals(timf.getMicrosTI());
        setNanosecondsTimeIntervals(timf.getNsTI());
        setPicosecondsTimeIntervals(timf.getPsTI());

        setOutputWidth(timf.getOutputWidth());

        if (isPeriodTriggerSectionSelected) {
            PeriodTriggerSectionFragment ptsf = new PeriodTriggerSectionFragment();
            setkHzTrigger(1 / ptsf.getSecondPTS());
            setHzTrigger(1 / ptsf.getMilisecondPTS());
            setmHzTrigger(1 / ptsf.getMicrosecondsPTS());
        } else if (!isPeriodTriggerSectionSelected) {
            FrequencyTriggerSectionFragment ftsf = new FrequencyTriggerSectionFragment();
            setkHzTrigger(ftsf.getkhzFTS());
            setHzTrigger(ftsf.gethzFTS());
            setmHzTrigger(ftsf.getmhzFTS());
        }


    }

    @Override
    public String toString() {
        return "DTO{" +
                "selectedMode='" + selectedMode + '\'' +
                ", isExternalClockSelected=" + isExternalClockSelected +
                ", secondsTimeIntervals=" + secondsTimeIntervals +
                ", milisecondsTimeIntervals=" + milisecondsTimeIntervals +
                ", microsecondsTimeIntervals=" + microsecondsTimeIntervals +
                ", nanosecondsTimeIntervals=" + nanosecondsTimeIntervals +
                ", picosecondsTimeIntervals=" + picosecondsTimeIntervals +
                ", outputWidth=" + outputWidth +
                ", isPeriodTriggerSectionSelected=" + isPeriodTriggerSectionSelected +
                ", khzTrigger=" + khzTrigger +
                ", hzTrigger=" + hzTrigger +
                ", mhzTrigger=" + mhzTrigger +
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

    public int getSecondsTimeIntervals() {
        return secondsTimeIntervals;
    }

    public void setSecondsTimeIntervals(int secondsTimeIntervals) {
        this.secondsTimeIntervals = secondsTimeIntervals;
    }

    public int getMilisecondsTimeIntervals() {
        return milisecondsTimeIntervals;
    }

    public void setMilisecondsTimeIntervals(int milisecondsTimeIntervals) {
        this.milisecondsTimeIntervals = milisecondsTimeIntervals;
    }

    public int getMicrosecondsTimeIntervals() {
        return microsecondsTimeIntervals;
    }

    public void setMicrosecondsTimeIntervals(int microsecondsTimeIntervals) {
        this.microsecondsTimeIntervals = microsecondsTimeIntervals;
    }

    public int getNanosecondsTimeIntervals() {
        return nanosecondsTimeIntervals;
    }

    public void setNanosecondsTimeIntervals(int nanosecondsTimeIntervals) {
        this.nanosecondsTimeIntervals = nanosecondsTimeIntervals;
    }

    public int getPicosecondsTimeIntervals() {
        return picosecondsTimeIntervals;
    }

    public void setPicosecondsTimeIntervals(int picosecondsTimeIntervals) {
        this.picosecondsTimeIntervals = picosecondsTimeIntervals;
    }

    public int getOutputWidth() {
        return outputWidth;
    }

    public void setOutputWidth(int outputWidth) {
        this.outputWidth = outputWidth;
    }

    public int getkHzTrigger() {
        return khzTrigger;
    }

    public void setkHzTrigger(int khzTrigger) {
        this.khzTrigger = khzTrigger;
    }

    public int getHzTrigger() {
        return hzTrigger;
    }

    public void setHzTrigger(int hzTrigger) {
        this.hzTrigger = hzTrigger;
    }

    public int getmHzTrigger() {
        return mhzTrigger;
    }

    public void setmHzTrigger(int mhzTrigger) {
        this.mhzTrigger = mhzTrigger;
    }
}
