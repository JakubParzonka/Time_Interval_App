package com.jparzonka.mylibrary.j2xx.ft4222;

/* compiled from: FT_4222_Device */
class gpio_mgr {
    int[] gpioStatus;
    int[] input;
    byte lastGpioData;

    public gpio_mgr() {
        this.gpioStatus = new int[4];
        this.input = new int[4];
    }
}
