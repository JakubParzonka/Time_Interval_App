package com.jparzonka.mylibrary.j2xx.ft4222;

/* compiled from: FT_4222_Gpio */
class gpio_dev {
    byte[] dat;
    byte dir;
    byte mask;
    dev_ctrl usb;

    public gpio_dev(char fwVer) {
        this.usb = new dev_ctrl(fwVer);
        this.dat = new byte[1];
    }
}
