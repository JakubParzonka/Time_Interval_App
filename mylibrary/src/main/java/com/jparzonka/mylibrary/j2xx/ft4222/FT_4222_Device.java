package com.jparzonka.mylibrary.j2xx.ft4222;

import com.jparzonka.mylibrary.j2xx.FT_Device;
import com.jparzonka.mylibrary.j2xx.interfaces.Gpio;
import com.jparzonka.mylibrary.j2xx.interfaces.I2cMaster;
import com.jparzonka.mylibrary.j2xx.interfaces.I2cSlave;
import com.jparzonka.mylibrary.j2xx.interfaces.SpiMaster;
import com.jparzonka.mylibrary.j2xx.interfaces.SpiSlave;
import com.jparzonka.mylibrary.j2xx.protocol.SpiSlaveResponseEvent;

public class FT_4222_Device {
    protected String TAG;
    protected chiptop_mgr mChipStatus;
    protected FT_Device mFtDev;
    protected gpio_mgr mGpio;
    protected SPI_MasterCfg mSpiMasterCfg;
    protected char version;

    public FT_4222_Device(FT_Device ftDev) {
        this.TAG = "FT4222";
        this.mFtDev = ftDev;
        this.mChipStatus = new chiptop_mgr();
        this.mSpiMasterCfg = new SPI_MasterCfg();
        this.mGpio = new gpio_mgr();
    }

    public int init() {
        byte[] buf = new byte[13];
        if (this.mFtDev.VendorCmdGet(32, 1, buf, 13) != 13) {
            return 18;
        }
        this.mChipStatus.formByteArray(buf);
        byte[] bVer = new byte[12];
        if (this.mFtDev.VendorCmdGet(32, 0, bVer, 12) < 0) {
            return 18;
        }
        if (bVer[2] == (byte) 1) {
            this.version = 'A';
            return 0;
        } else if (bVer[2] == (byte) 2) {
            this.version = 'B';
            return 0;
        } else if (bVer[2] < 3) {
            return 0;
        } else {
            this.version = 'C';
            return 0;
        }
    }

    public int setClock(byte clk) {
        if (clk == this.mChipStatus.clk_ctl) {
            return 0;
        }
        int ftStatus = this.mFtDev.VendorCmdSet(33, (clk << 8) | 4);
        if (ftStatus != 0) {
            return ftStatus;
        }
        this.mChipStatus.clk_ctl = clk;
        return ftStatus;
    }

    public int getClock(byte[] clk) {
        if (this.mFtDev.VendorCmdGet(32, 4, clk, 1) < 0) {
            return 18;
        }
        this.mChipStatus.clk_ctl = clk[0];
        return 0;
    }

    public boolean cleanRxData() {
        int ret = this.mFtDev.getQueueStatus();
        if (ret > 0) {
            byte[] rd_tmp_buf = new byte[ret];
            if (this.mFtDev.read(rd_tmp_buf, ret) != rd_tmp_buf.length) {
                return false;
            }
        }
        return true;
    }

    protected int getMaxBuckSize() {
        if (this.mChipStatus.fs_only != 0) {
            return 64;
        }
        switch (this.mChipStatus.chip_mode) {
            case SpiSlaveResponseEvent.DATA_CORRUPTED /*1*/:
            case SpiSlaveResponseEvent.IO_ERROR /*2*/:
                return 256;
            default:
                return 512;
        }
    }

    public boolean isFT4222Device() {
        if (this.mFtDev != null) {
            switch (this.mFtDev.getDeviceInfo().bcdDevice & 65280) {
                case 5888:
                    this.mFtDev.getDeviceInfo().type = 12;
                    return true;
                case 6144:
                    this.mFtDev.getDeviceInfo().type = 10;
                    return true;
                case 6400:
                    this.mFtDev.getDeviceInfo().type = 11;
                    return true;
            }
        }
        return false;
    }

    public I2cMaster getI2cMasterDevice() {
        if (isFT4222Device()) {
            return new FT_4222_I2c_Master(this);
        }
        return null;
    }

    public I2cSlave getI2cSlaveDevice() {
        if (isFT4222Device()) {
            return new FT_4222_I2c_Slave(this);
        }
        return null;
    }

    public SpiMaster getSpiMasterDevice() {
        if (isFT4222Device()) {
            return new FT_4222_Spi_Master(this);
        }
        return null;
    }

    public SpiSlave getSpiSlaveDevice() {
        if (isFT4222Device()) {
            return new FT_4222_Spi_Slave(this);
        }
        return null;
    }

    public Gpio getGpioDevice() {
        if (isFT4222Device()) {
            return new FT_4222_Gpio(this);
        }
        return null;
    }

    public char GetVersion() {
        return this.version;
    }
}
