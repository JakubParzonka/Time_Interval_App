package com.jparzonka.mylibrary.j2xx.ft4222;

import android.util.Log;

import com.jparzonka.mylibrary.j2xx.D2xxManager;
import com.jparzonka.mylibrary.j2xx.FT_Device;
import com.jparzonka.mylibrary.j2xx.ft4222.FT_4222_Defines.CHIPTOP_CMD;
import com.jparzonka.mylibrary.j2xx.ft4222.FT_4222_Defines.FT4222_STATUS;
import com.jparzonka.mylibrary.j2xx.ft4222.FT_4222_Defines.SPI_SLAVE_CMD;
import com.jparzonka.mylibrary.j2xx.interfaces.SpiMaster;
import com.jparzonka.mylibrary.j2xx.protocol.SpiSlaveResponseEvent;

import junit.framework.Assert;

public class FT_4222_Spi_Master implements SpiMaster {
    private static final String TAG = "FTDI_Device::";
    private FT_4222_Device mFT4222Device;
    private FT_Device mFTDevice;
    private byte[] mPackRdBuf;
    private byte[] mPackWrBuf;

    public FT_4222_Spi_Master(FT_4222_Device pDevice) {
        this.mPackWrBuf = new byte[D2xxManager.FTDI_BREAK_ON];
        this.mPackRdBuf = new byte[D2xxManager.FTDI_BREAK_ON];
        this.mFT4222Device = pDevice;
        this.mFTDevice = pDevice.mFtDev;
    }

    public int init(int ioLine, int clock, int cpol, int cpha, byte ssoMap) {
        chiptop_mgr chipStatus = this.mFT4222Device.mChipStatus;
        int venderSSOMap = 0;
        SPI_MasterCfg config = this.mFT4222Device.mSpiMasterCfg;
        config.ioLine = ioLine;
        config.clock = clock;
        config.cpol = cpol;
        config.cpha = cpha;
        config.ssoMap = ssoMap;
        if (config.ioLine != 1 && config.ioLine != 2 && config.ioLine != 4) {
            return 6;
        }
        this.mFT4222Device.cleanRxData();
        switch (chipStatus.chip_mode) {
            case SpiSlaveResponseEvent.OK /*0*/:
                venderSSOMap = 1;
                break;
            case SpiSlaveResponseEvent.DATA_CORRUPTED /*1*/:
                venderSSOMap = 7;
                break;
            case SpiSlaveResponseEvent.IO_ERROR /*2*/:
                venderSSOMap = 15;
                break;
            case SpiSlaveResponseEvent.RES_SLAVE_READ /*3*/:
                venderSSOMap = 1;
                break;
        }
        if ((config.ssoMap & venderSSOMap) == 0) {
            return 6;
        }
        config.ssoMap = (byte) (config.ssoMap & venderSSOMap);
        if (this.mFTDevice.VendorCmdSet(33, (config.ioLine << 8) | 66) < 0) {
            return 4;
        }
        if (this.mFTDevice.VendorCmdSet(33, (config.clock << 8) | 68) < 0) {
            return 4;
        }
        if (this.mFTDevice.VendorCmdSet(33, (config.cpol << 8) | 69) < 0) {
            return 4;
        }
        if (this.mFTDevice.VendorCmdSet(33, (config.cpha << 8) | 70) < 0) {
            return 4;
        }
        if (this.mFTDevice.VendorCmdSet(33, 67) < 0) {
            return 4;
        }
        if (this.mFTDevice.VendorCmdSet(33, (config.ssoMap << 8) | 72) < 0) {
            return 4;
        }
        if (this.mFTDevice.VendorCmdSet(33, 773) < 0) {
            return 4;
        }
        chipStatus.function = (byte) 3;
        return 0;
    }

    public int setLines(int spiMode) {
        if (this.mFT4222Device.mChipStatus.function != 3) {
            return FT4222_STATUS.FT4222_IS_NOT_SPI_MODE;
        }
        if (spiMode == 0) {
            return 17;
        }
        if (this.mFTDevice.VendorCmdSet(33, (spiMode << 8) | 66) < 0 || this.mFTDevice.VendorCmdSet(33, 330) < 0) {
            return 4;
        }
        this.mFT4222Device.mSpiMasterCfg.ioLine = spiMode;
        return 0;
    }

    public int singleWrite(byte[] writeBuffer, int sizeToTransfer, int[] sizeTransferred, boolean isEndTransaction) {
        return singleReadWrite(new byte[writeBuffer.length], writeBuffer, sizeToTransfer, sizeTransferred, isEndTransaction);
    }

    public int singleRead(byte[] readBuffer, int sizeToTransfer, int[] sizeOfRead, boolean isEndTransaction) {
        return singleReadWrite(readBuffer, new byte[readBuffer.length], sizeToTransfer, sizeOfRead, isEndTransaction);
    }

    public int singleReadWrite(byte[] readBuffer, byte[] writeBuffer, int sizeToTransfer, int[] sizeTransferred, boolean isEndTransaction) {
        chiptop_mgr chipStatus = this.mFT4222Device.mChipStatus;
        SPI_MasterCfg spiCfg = this.mFT4222Device.mSpiMasterCfg;
        if (writeBuffer == null || readBuffer == null || sizeTransferred == null) {
            return FT4222_STATUS.FT4222_INVALID_POINTER;
        }
        sizeTransferred[0] = 0;
        if (chipStatus.function != 3 || spiCfg.ioLine != 1) {
            return FT4222_STATUS.FT4222_IS_NOT_SPI_SINGLE_MODE;
        }
        if (sizeToTransfer == 0) {
            return 6;
        }
        if (sizeToTransfer > writeBuffer.length || sizeToTransfer > readBuffer.length) {
            Assert.assertTrue("sizeToTransfer > writeBuffer.length || sizeToTransfer > readBuffer.length", false);
        }
        if (writeBuffer.length != readBuffer.length || writeBuffer.length == 0) {
            Assert.assertTrue("writeBuffer.length != readBuffer.length || writeBuffer.length == 0", false);
        }
        sizeTransferred[0] = sendReadWriteBuffer(this.mFTDevice, writeBuffer, readBuffer, sizeToTransfer);
        if (isEndTransaction) {
            this.mFTDevice.write(null, 0);
        }
        if (sizeTransferred[0] == -1) {
            return 10;
        }
        if (sizeTransferred[0] == -2) {
            return FT4222_STATUS.FT4222_FAILED_TO_READ_DEVICE;
        }
        return 0;
    }

    public int multiReadWrite(byte[] readBuffer, byte[] writeBuffer, int singleWriteBytes, int multiWriteBytes, int multiReadBytes, int[] sizeOfRead) {
        chiptop_mgr chipStatus = this.mFT4222Device.mChipStatus;
        SPI_MasterCfg spiCfg = this.mFT4222Device.mSpiMasterCfg;
        if (multiReadBytes > 0 && readBuffer == null) {
            return FT4222_STATUS.FT4222_INVALID_POINTER;
        }
        if (singleWriteBytes + multiWriteBytes > 0 && writeBuffer == null) {
            return FT4222_STATUS.FT4222_INVALID_POINTER;
        }
        if (multiReadBytes > 0 && sizeOfRead == null) {
            return FT4222_STATUS.FT4222_INVALID_POINTER;
        }
        if (chipStatus.function != 3 || spiCfg.ioLine == 1) {
            return FT4222_STATUS.FT4222_IS_NOT_SPI_MULTI_MODE;
        }
        if (singleWriteBytes > 15) {
            Log.e(TAG, "The maxium single write bytes are 15 bytes");
            return 6;
        }
        byte[] sendData = new byte[((singleWriteBytes + 5) + multiWriteBytes)];
        sendData[0] = (byte) ((singleWriteBytes & 15) | SPI_SLAVE_CMD.SPI_MASTER_TRANSFER);
        sendData[1] = (byte) ((65280 & multiWriteBytes) >> 8);
        sendData[2] = (byte) (multiWriteBytes & FT_4222_Defines.CHIPTOP_DEBUG_REQUEST);
        sendData[3] = (byte) ((65280 & multiReadBytes) >> 8);
        sendData[4] = (byte) (multiReadBytes & FT_4222_Defines.CHIPTOP_DEBUG_REQUEST);
        for (int i = 0; i < singleWriteBytes + multiWriteBytes; i++) {
            sendData[i + 5] = writeBuffer[i];
        }
        sizeOfRead[0] = setMultiReadWritePackage(this.mFTDevice, sendData, readBuffer, multiReadBytes);
        return 0;
    }

    public int reset() {
        if (this.mFTDevice.VendorCmdSet(33, 74) < 0) {
            return 4;
        }
        return 0;
    }

    public int setDrivingStrength(int clkStrength, int ioStrength, int ssoStregth) {
        chiptop_mgr chipStatus = this.mFT4222Device.mChipStatus;
        if (chipStatus.function != (byte) 3 && chipStatus.function != (byte) 4) {
            return FT4222_STATUS.FT4222_IS_NOT_SPI_MODE;
        }
        int actual_strength = ((clkStrength << 4) | (ioStrength << 2)) | ssoStregth;
        int verderFun;
        if (chipStatus.function == (byte) 3) {
            verderFun = 3;
        } else {
            verderFun = 4;
        }
        if (this.mFTDevice.VendorCmdSet(33, (actual_strength << 8) | CHIPTOP_CMD.CHIPTOP_SET_DS_CTL0_REG) < 0 || this.mFTDevice.VendorCmdSet(33, (verderFun << 8) | 5) < 0) {
            return 4;
        }
        return 0;
    }

    private int setMultiReadWritePackage(FT_Device ftSPIDevice, byte[] wr_buffer, byte[] rd_buffer, int multiReadBytes) {
        if (ftSPIDevice == null || !ftSPIDevice.isOpen()) {
            return -1;
        }
        int ret = sendMultiWriteBuffer(ftSPIDevice, wr_buffer);
        return sendMultiReadBuffer(ftSPIDevice, rd_buffer, multiReadBytes);
    }

    private int sendMultiWriteBuffer(FT_Device ftSPIDevice, byte[] wr_buffer) {
        int packCount = wr_buffer.length / this.mPackWrBuf.length;
        int restCount = wr_buffer.length % this.mPackWrBuf.length;
        int writeIdx = 0;
        int i = 0;
        int j;
        int valRet;
        while (i < packCount) {

            for (j = 0; j < this.mPackWrBuf.length; j++) {
                this.mPackWrBuf[j] = wr_buffer[writeIdx];
                writeIdx++;
            }
            valRet = ftSPIDevice.write(this.mPackWrBuf, this.mPackWrBuf.length);
            if (this.mPackWrBuf.length != valRet) {
                Log.e(TAG, "sendMultiWriteBuffer write error!!!");
                return -1;
            } else if (valRet <= 0) {
                return valRet;
            } else {
                i++;
            }
        }
        if (restCount > 0) {
            for (j = 0; j < restCount; j++) {
                this.mPackWrBuf[j] = wr_buffer[writeIdx];
                writeIdx++;
            }
            valRet = ftSPIDevice.write(this.mPackWrBuf, restCount);
            if (restCount != valRet) {
                Log.e(TAG, "sendMultiWriteBuffer write error!!!");
                return -1;
            } else if (valRet <= 0) {
                return valRet;
            }
        }
        return writeIdx;
    }

    private int sendMultiReadBuffer(FT_Device ftSPIDevice, byte[] rd_buffer, int multiReadBytes) {
        return ftSPIDevice.read(rd_buffer, multiReadBytes);
    }

    private int sendReadWriteBuffer(FT_Device ftDevice, byte[] wr_buffer, byte[] rd_buffer, int sizeToTransfer) {
        int j;
        int packCount = sizeToTransfer / this.mPackWrBuf.length;
        int restCount = sizeToTransfer % this.mPackWrBuf.length;
        int readIdx = 0;
        int writeIdx = 0;
        int valRet;
        for (int i = 0; i < packCount; i++) {
            for (j = 0; j < this.mPackWrBuf.length; j++) {
                this.mPackWrBuf[j] = wr_buffer[writeIdx];
                writeIdx++;
            }
            valRet = setReadWritePackage(ftDevice, this.mPackWrBuf, this.mPackRdBuf, this.mPackWrBuf.length);
            if (valRet <= 0) {
                return valRet;
            }
            for (byte b : this.mPackRdBuf) {
                rd_buffer[readIdx] = b;
                readIdx++;
            }
        }
        if (restCount > 0) {
            for (j = 0; j < restCount; j++) {
                this.mPackWrBuf[j] = wr_buffer[writeIdx];
                writeIdx++;
            }
            valRet = setReadWritePackage(ftDevice, this.mPackWrBuf, this.mPackRdBuf, restCount);
            if (valRet <= 0) {
                return valRet;
            }
            for (j = 0; j < restCount; j++) {
                rd_buffer[readIdx] = this.mPackRdBuf[j];
                readIdx++;
            }
        }
        return readIdx;
    }

    private int setReadWritePackage(FT_Device ftSPIDevice, byte[] wr_buffer, byte[] rd_buffer, int wr_rd_size) {
        int bytesRead = 0;
        if (ftSPIDevice == null || !ftSPIDevice.isOpen()) {
            return -1;
        }
        if (wr_rd_size != ftSPIDevice.write(wr_buffer, wr_rd_size)) {
            Log.e(TAG, "setReadWritePackage write error!!!");
            return -1;
        }
        //PREVIOUS CONDITION (bytesRead < wr_rd_size && 30000 > null)
        while (bytesRead < wr_rd_size) {
            int ret = ftSPIDevice.getQueueStatus();
            if (ret > 0) {
                ret = ftSPIDevice.read(wr_buffer, ret);
                for (int i = 0; i < ret; i++) {
                    if (bytesRead + i < wr_rd_size) {
                        rd_buffer[bytesRead + i] = wr_buffer[i];
                    }
                }
                bytesRead += ret;
            }
            Thread.yield();
        }
        if (wr_rd_size == bytesRead) {
            return bytesRead;
        }
        Log.e(TAG, "SingleReadWritePackage timeout!!!!");
        return -1;
    }
}
