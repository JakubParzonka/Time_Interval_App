package com.jparzonka.time_interval_app.usage;

import android.os.AsyncTask;
import android.util.Log;

import com.jparzonka.time_interval_app.fragments.FrequencyTriggerSectionFragment;
import com.jparzonka.time_interval_app.fragments.PeriodTriggerSectionFragment;
import com.jparzonka.time_interval_app.fragments.TimeIntervalModeFragment;

/**
 * Created by Jakub on 2016-12-29.
 */

public class UpdateControler extends AsyncTask<Object, Integer, Double> {

    private TimeIntervalModeFragment timeIntervalModeFragment;
    private FrequencyTriggerSectionFragment frequencyTriggerSectionFragment;
    private PeriodTriggerSectionFragment periodTriggerSectionFragment;
    private boolean hasTheValueChanged, isPeriodTriggerSectionSelected;

    public UpdateControler(TimeIntervalModeFragment timeIntervalModeFragment, FrequencyTriggerSectionFragment frequencyTriggerSectionFragment, PeriodTriggerSectionFragment periodTriggerSectionFragment) {
        setTimeIntervalModeFragment(timeIntervalModeFragment);
        setFrequencyTriggerSectionFragment(frequencyTriggerSectionFragment);
        setPeriodTriggerSectionFragment(periodTriggerSectionFragment);
        Log.i("UpdateControler", String.valueOf(isPeriodTriggerSectionSelected));
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Double doInBackground(Object... params) {
        return null;
    }

    @Override
    protected void onPostExecute(Double aDouble) {

    }


    public TimeIntervalModeFragment getTimeIntervalModeFragment() {
        return timeIntervalModeFragment;
    }

    public void setTimeIntervalModeFragment(TimeIntervalModeFragment timeIntervalModeFragment) {
        this.timeIntervalModeFragment = timeIntervalModeFragment;
    }

    public FrequencyTriggerSectionFragment getFrequencyTriggerSectionFragment() {
        return frequencyTriggerSectionFragment;
    }

    public void setFrequencyTriggerSectionFragment(FrequencyTriggerSectionFragment frequencyTriggerSectionFragment) {
        this.frequencyTriggerSectionFragment = frequencyTriggerSectionFragment;
    }

    public PeriodTriggerSectionFragment getPeriodTriggerSectionFragment() {
        return periodTriggerSectionFragment;
    }

    public void setPeriodTriggerSectionFragment(PeriodTriggerSectionFragment periodTriggerSectionFragment) {
        this.periodTriggerSectionFragment = periodTriggerSectionFragment;
    }
}
