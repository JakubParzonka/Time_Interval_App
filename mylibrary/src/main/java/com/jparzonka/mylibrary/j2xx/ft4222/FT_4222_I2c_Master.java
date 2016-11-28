package com.jparzonka.mylibrary.j2xx.ft4222;

import com.jparzonka.mylibrary.j2xx.FT_Device;
import com.jparzonka.mylibrary.j2xx.ft4222.FT_4222_Defines.CHIPTOP_CMD;
import com.jparzonka.mylibrary.j2xx.ft4222.FT_4222_Defines.FT4222_STATUS;
import com.jparzonka.mylibrary.j2xx.ft4222.FT_4222_Defines.SPI_SLAVE_CMD;
import com.jparzonka.mylibrary.j2xx.interfaces.I2cMaster;
import com.jparzonka.mylibrary.j2xx.protocol.SpiSlaveResponseEvent;

public class FT_4222_I2c_Master implements I2cMaster {
    FT_4222_Device mFt4222Dev;
    FT_Device mFtDev;
    int mI2cMasterKbps;

    public FT_4222_I2c_Master(FT_4222_Device ft4222Device) {
        this.mFt4222Dev = ft4222Device;
        this.mFtDev = this.mFt4222Dev.mFtDev;
    }

    int cmdSet(int wValue1, int wValue2) {
        return this.mFtDev.VendorCmdSet(33, (wValue2 << 8) | wValue1);
    }

    int cmdSet(int wValue1, int wValue2, byte[] buf, int datalen) {
        return this.mFtDev.VendorCmdSet(33, (wValue2 << 8) | wValue1, buf, datalen);
    }

    int cmdGet(int wValue1, int wValue2, byte[] buf, int datalen) {
        return this.mFtDev.VendorCmdGet(32, (wValue2 << 8) | wValue1, buf, datalen);
    }

    public int init(int kbps) {
        byte[] clk = new byte[1];
        int ftStatus = this.mFt4222Dev.init();
        if (ftStatus != 0) {
            return ftStatus;
        }
        if (!I2C_Mode_Check()) {
            return FT4222_STATUS.FT4222_I2C_NOT_SUPPORTED_IN_THIS_MODE;
        }
        cmdSet(81, 0);
        ftStatus = this.mFt4222Dev.getClock(clk);
        if (ftStatus != 0) {
            return ftStatus;
        }
        int i2cMP = i2c_master_setup_timer_period(clk[0], kbps);
        ftStatus = cmdSet(5, 1);
        if (ftStatus < 0) {
            return ftStatus;
        }
        this.mFt4222Dev.mChipStatus.function = (byte) 1;
        ftStatus = cmdSet(82, i2cMP);
        if (ftStatus < 0) {
            return ftStatus;
        }
        this.mI2cMasterKbps = kbps;
        return 0;
    }

    public int reset() {
        int ftStatus = I2C_Check(true);
        return ftStatus != 0 ? ftStatus : cmdSet(81, (byte) 1);
    }

    public int read(int deviceAddress, byte[] buffer, int sizeToTransfer, int[] sizeTransferred) {
        return readEx(deviceAddress, 6, buffer, sizeToTransfer, sizeTransferred);
    }

    public int readEx(int deviceAddress, int flag, byte[] buffer, int sizeToTransfer, int[] sizeTransferred) {
        short slave_addr = (short) (65535 & deviceAddress);
        short shortSizeToTransfer = (short) sizeToTransfer;
        int[] maxSize = new int[1];
        byte[] headBuf = new byte[4];
        long startTime = System.currentTimeMillis();
        int iTimeout = this.mFtDev.getReadTimeout();
        int ftStatus = I2C_Version_Check(flag);
        if (ftStatus != 0) {
            return ftStatus;
        }
        ftStatus = I2C_Address_Check(deviceAddress);
        if (ftStatus != 0) {
            return ftStatus;
        }
        if (sizeToTransfer < 1) {
            return 6;
        }
        ftStatus = I2C_Check(true);
        if (ftStatus != 0) {
            return ftStatus;
        }
        ftStatus = getMaxTransferSize(maxSize);
        if (ftStatus != 0) {
            return ftStatus;
        }
        if (sizeToTransfer > maxSize[0]) {
            return FT4222_STATUS.FT4222_EXCEEDED_MAX_TRANSFER_SIZE;
        }
        sizeTransferred[0] = 0;
        headBuf[0] = (byte) ((short) ((slave_addr << 1) + 1));
        headBuf[1] = (byte) flag;
        headBuf[2] = (byte) ((shortSizeToTransfer >> 8) & FT_4222_Defines.CHIPTOP_DEBUG_REQUEST);
        headBuf[3] = (byte) (shortSizeToTransfer & FT_4222_Defines.CHIPTOP_DEBUG_REQUEST);
        if (4 != this.mFtDev.write(headBuf, 4)) {
            return FT4222_STATUS.FT4222_FAILED_TO_READ_DEVICE;
        }
        int dataSize = this.mFtDev.getQueueStatus();
        while (dataSize < sizeToTransfer && System.currentTimeMillis() - startTime < ((long) iTimeout)) {
            dataSize = this.mFtDev.getQueueStatus();
        }
        if (dataSize > sizeToTransfer) {
            dataSize = sizeToTransfer;
        }
        ftStatus = this.mFtDev.read(buffer, dataSize);
        sizeTransferred[0] = ftStatus;
        if (ftStatus >= 0) {
            return 0;
        }
        return FT4222_STATUS.FT4222_FAILED_TO_READ_DEVICE;
    }

    public int write(int deviceAddress, byte[] buffer, int sizeToTransfer, int[] sizeTransferred) {
        return writeEx(deviceAddress, 6, buffer, sizeToTransfer, sizeTransferred);
    }

    public int writeEx(int deviceAddress, int flag, byte[] buffer, int sizeToTransfer, int[] sizeTransferred) {
        short slave_addr = (short) deviceAddress;
        short shortSizeToTransfer = (short) sizeToTransfer;
        byte[] transferBuf = new byte[(sizeToTransfer + 4)];
        int[] maxSize = new int[1];
        int ftStatus = I2C_Version_Check(flag);
        if (ftStatus != 0) {
            return ftStatus;
        }
        ftStatus = I2C_Address_Check(deviceAddress);
        if (ftStatus != 0) {
            return ftStatus;
        }
        if (sizeToTransfer < 1) {
            return 6;
        }
        ftStatus = I2C_Check(true);
        if (ftStatus != 0) {
            return ftStatus;
        }
        ftStatus = getMaxTransferSize(maxSize);
        if (ftStatus != 0) {
            return ftStatus;
        }
        if (sizeToTransfer > maxSize[0]) {
            return FT4222_STATUS.FT4222_EXCEEDED_MAX_TRANSFER_SIZE;
        }
        sizeTransferred[0] = 0;
        transferBuf[0] = (byte) ((short) (slave_addr << 1));
        transferBuf[1] = (byte) flag;
        transferBuf[2] = (byte) ((shortSizeToTransfer >> 8) & FT_4222_Defines.CHIPTOP_DEBUG_REQUEST);
        transferBuf[3] = (byte) (shortSizeToTransfer & FT_4222_Defines.CHIPTOP_DEBUG_REQUEST);
        for (int i = 0; i < sizeToTransfer; i++) {
            transferBuf[i + 4] = buffer[i];
        }
        sizeTransferred[0] = this.mFtDev.write(transferBuf, sizeToTransfer + 4) - 4;
        if (sizeToTransfer != sizeTransferred[0]) {
            return 10;
        }
        return 0;
    }

    public int getStatus(int deviceAddress, byte[] controllerStatus) {
        int ftStatus = I2C_Check(true);
        if (ftStatus != 0) {
            return ftStatus;
        }
        if (this.mFtDev.VendorCmdGet(34, 62900, controllerStatus, 1) < 0) {
            return 18;
        }
        return 0;
    }

    boolean I2C_Mode_Check() {
        if (this.mFt4222Dev.mChipStatus.chip_mode == 0 || this.mFt4222Dev.mChipStatus.chip_mode == 3) {
            return true;
        }
        return false;
    }

    int I2C_Check(boolean isMaster) {
        if (isMaster) {
            if (this.mFt4222Dev.mChipStatus.function != 1) {
                return FT4222_STATUS.FT4222_IS_NOT_I2C_MODE;
            }
        } else if (this.mFt4222Dev.mChipStatus.function != 2) {
            return FT4222_STATUS.FT4222_IS_NOT_I2C_MODE;
        }
        return 0;
    }

    int I2C_Version_Check(int flag) {
        if (this.mFtDev == null || !this.mFtDev.isOpen()) {
            return 3;
        }
        if (flag == 6 || getFWVersion() >= 'B') {
            return 0;
        }
        return FT4222_STATUS.FT4222_FUN_NOT_SUPPORT;
    }

    int I2C_Address_Check(int deviceAddress) {
        if ((64512 & deviceAddress) > 0) {
            return FT4222_STATUS.FT4222_WRONG_I2C_ADDR;
        }
        return 0;
    }

    private int i2c_master_setup_timer_period(int CLK_CTRL, int kbps) {
        double CLK_PRD;
        switch (CLK_CTRL) {
            case SpiSlaveResponseEvent.DATA_CORRUPTED /*1*/:
                CLK_PRD = 41.666666666666664d;
                break;
            case SpiSlaveResponseEvent.IO_ERROR /*2*/:
                CLK_PRD = 20.833333333333332d;
                break;
            case SpiSlaveResponseEvent.RES_SLAVE_READ /*3*/:
                CLK_PRD = 12.5d;
                break;
            default:
                CLK_PRD = 16.666666666666668d;
                break;
        }
        if (60 <= kbps && kbps <= 100) {
            int TIMER_PRD = (int) ((((1000000.0d / ((double) kbps)) / (8.0d * CLK_PRD)) - 1.0d) + 0.5d);
            if (TIMER_PRD > 127) {
                TIMER_PRD = 127;
            }
            return TIMER_PRD;
        } else if (100 < kbps && kbps <= 400) {
            return ((int) ((((1000000.0d / ((double) kbps)) / (6.0d * CLK_PRD)) - 1.0d) + 0.5d)) | CHIPTOP_CMD.CHIPTOP_WRITE_OTP_TEST_BYTE;
        } else if (400 < kbps && kbps <= FT4222_STATUS.FT4222_DEVICE_NOT_SUPPORTED) {
            return ((int) ((((1000000.0d / ((double) kbps)) / (6.0d * CLK_PRD)) - 1.0d) + 0.5d)) | CHIPTOP_CMD.CHIPTOP_WRITE_OTP_TEST_BYTE;
        } else if (FT4222_STATUS.FT4222_DEVICE_NOT_SUPPORTED >= kbps || kbps > 3400) {
            return 74;
        } else {
            return (((int) ((((1000000.0d / ((double) kbps)) / (6.0d * CLK_PRD)) - 1.0d) + 0.5d)) | SPI_SLAVE_CMD.SPI_MASTER_TRANSFER) & -65;
        }
    }

    int getMaxTransferSize(int[] pMaxSize) {
        pMaxSize[0] = 0;
        int maxBuckSize = this.mFt4222Dev.getMaxBuckSize();
        switch (this.mFt4222Dev.mChipStatus.function) {
            case SpiSlaveResponseEvent.DATA_CORRUPTED /*1*/:
                pMaxSize[0] = maxBuckSize - 4;
                return 0;
            default:
                return 17;
        }
    }

    char getFWVersion() {
        return this.mFt4222Dev.GetVersion();
    }
}
