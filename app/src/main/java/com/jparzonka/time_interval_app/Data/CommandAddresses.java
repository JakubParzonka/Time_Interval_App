package com.jparzonka.time_interval_app.data;

/**
 * Created by Jakub on 2016-12-29.
 */

enum CommandAddresses {

    SET_S("00"), SET_TRIG("0D"), TIRG_DIV("0E"), SYNTH_N("0F"),
    ADF4360_LOAD("10"), RESET("0C"), CFR1("40"), FTW0("44");
    private String address;

    CommandAddresses(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }
}
