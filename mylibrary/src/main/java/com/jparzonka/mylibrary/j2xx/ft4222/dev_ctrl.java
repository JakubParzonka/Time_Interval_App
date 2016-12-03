package com.jparzonka.mylibrary.j2xx.ft4222;

/* compiled from: FT_4222_Gpio */
class dev_ctrl {
    byte ep_in;
    byte ep_out;
    byte[] proc_io;

    public dev_ctrl(char fwVer) {
        if (fwVer < 'B') {
            this.proc_io = new byte[3];
        } else {
            this.proc_io = new byte[1];
        }
    }
}
