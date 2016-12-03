package com.jparzonka.mylibrary.j2xx.ft4222;

/* compiled from: FT_4222_Device */
class chiptop_mgr {
    byte[] chip_info_addr;
    byte chip_mode;
    byte clk_ctl;
    byte enable_suspend_out;
    byte enable_wakeup_int;
    byte fs_only;
    byte function;
    byte gpio_mask;
    byte high_speed_chip;
    byte total_ep;
    byte total_if;

    public chiptop_mgr() {
        this.chip_info_addr = new byte[3];
    }

    public chiptop_mgr(byte[] b) {
        this.chip_info_addr = new byte[3];
        formByteArray(b);
    }

    void formByteArray(byte[] b) {
        this.chip_mode = b[0];
        this.high_speed_chip = b[1];
        this.fs_only = b[2];
        this.total_if = b[3];
        this.total_ep = b[4];
        this.clk_ctl = b[5];
        this.function = b[6];
        this.gpio_mask = b[7];
        this.enable_suspend_out = b[8];
        this.enable_wakeup_int = b[9];
        this.chip_info_addr[0] = b[10];
        this.chip_info_addr[1] = b[11];
        this.chip_info_addr[2] = b[12];
    }
}
