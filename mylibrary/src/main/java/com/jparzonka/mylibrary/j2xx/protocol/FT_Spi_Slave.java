package com.jparzonka.mylibrary.j2xx.protocol;

import com.jparzonka.mylibrary.j2xx.ft4222.FT_4222_Defines;
import com.jparzonka.mylibrary.j2xx.ft4222.FT_4222_Defines.FT4222_STATUS;
import com.jparzonka.mylibrary.j2xx.ft4222.FT_4222_Defines.GPIO_Tigger;
import com.jparzonka.mylibrary.j2xx.ft4222.FT_4222_Defines.I2C_MasterFlag;
import com.jparzonka.mylibrary.j2xx.interfaces.SpiSlave;

import junit.framework.Assert;

public class FT_Spi_Slave extends SpiSlaveThread {
    private static /* synthetic */ int[] $SWITCH_TABLE$com$ftdi$j2xx$protocol$FT_Spi_Slave$DECODE_STATE = null;
    private static final int FT4222_SPI_SLAVE_SYNC_WORD = 90;
    private static final int SPI_ACK = 132;
    private static final int SPI_MASTER_TRANSFER = 128;
    private static final int SPI_QUERY_VER = 136;
    private static final int SPI_SHART_SLAVE_TRANSFER = 131;
    private static final int SPI_SHORT_MASTER_TRANSFER = 130;
    private static final int SPI_SLAVE_TRANSFER = 129;
    private byte[] mBuffer;
    private int mBufferSize;
    private int mCheckSum;
    private int mCmd;
    private int mCurrentBufferSize;
    private DECODE_STATE mDecodeState;
    private boolean mIsOpened;
    private int mSn;
    private SpiSlave mSpiSlave;
    private SpiSlaveListener mSpiSlaveListener;
    private int mSync;
    private int mWrSn;

    private enum DECODE_STATE {
        STATE_SYNC,
        STATE_CMD,
        STATE_SN,
        STATE_SIZE_HIGH,
        STATE_SIZE_LOW,
        STATE_COLLECT_DATA,
        STATE_CHECKSUM_HIGH,
        STATE_CHECKSUM_LOW
    }

    static /* synthetic */ int[] $SWITCH_TABLE$com$ftdi$j2xx$protocol$FT_Spi_Slave$DECODE_STATE() {
        int[] iArr = $SWITCH_TABLE$com$ftdi$j2xx$protocol$FT_Spi_Slave$DECODE_STATE;
        if (iArr == null) {
            iArr = new int[DECODE_STATE.values().length];
            try {
                iArr[DECODE_STATE.STATE_CHECKSUM_HIGH.ordinal()] = 7;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[DECODE_STATE.STATE_CHECKSUM_LOW.ordinal()] = 8;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[DECODE_STATE.STATE_CMD.ordinal()] = 2;
            } catch (NoSuchFieldError e3) {
            }
            try {
                iArr[DECODE_STATE.STATE_COLLECT_DATA.ordinal()] = 6;
            } catch (NoSuchFieldError e4) {
            }
            try {
                iArr[DECODE_STATE.STATE_SIZE_HIGH.ordinal()] = 4;
            } catch (NoSuchFieldError e5) {
            }
            try {
                iArr[DECODE_STATE.STATE_SIZE_LOW.ordinal()] = 5;
            } catch (NoSuchFieldError e6) {
            }
            try {
                iArr[DECODE_STATE.STATE_SN.ordinal()] = 3;
            } catch (NoSuchFieldError e7) {
            }
            try {
                iArr[DECODE_STATE.STATE_SYNC.ordinal()] = 1;
            } catch (NoSuchFieldError e8) {
            }
            $SWITCH_TABLE$com$ftdi$j2xx$protocol$FT_Spi_Slave$DECODE_STATE = iArr;
        }
        return iArr;
    }

    public FT_Spi_Slave(SpiSlave pSlaveInterface) {
        this.mSpiSlave = pSlaveInterface;
        this.mDecodeState = DECODE_STATE.STATE_SYNC;
    }

    public void registerSpiSlaveListener(SpiSlaveListener pListener) {
        this.mSpiSlaveListener = pListener;
    }

    public int open() {
        if (this.mIsOpened) {
            return 1;
        }
        this.mIsOpened = true;
        this.mSpiSlave.init();
        start();
        return 0;
    }

    public int close() {
        if (!this.mIsOpened) {
            return 3;
        }
        sendMessage(new SpiSlaveRequestEvent(-1, true, null, null, null));
        this.mIsOpened = false;
        return 0;
    }

    public int write(byte[] wrBuf) {
        if (!this.mIsOpened) {
            return 3;
        }
        if (wrBuf.length > 65536) {
            return FT4222_STATUS.FT4222_EXCEEDED_MAX_TRANSFER_SIZE;
        }
        int[] sizeTransferred = new int[1];
        int wrSize = wrBuf.length;
        int checksum = getCheckSum(wrBuf, FT4222_SPI_SLAVE_SYNC_WORD, SPI_SLAVE_TRANSFER, this.mWrSn, wrSize);
        byte[] buffer = new byte[(wrBuf.length + 8)];
        int idx = 0 + 1;
        buffer[0] = (byte) 0;
        int i = idx + 1;
        buffer[idx] = (byte) 90;
        idx = i + 1;
        buffer[i] = (byte) -127;
        i = idx + 1;
        buffer[idx] = (byte) this.mWrSn;
        idx = i + 1;
        buffer[i] = (byte) ((wrSize & 65280) >> 8);
        i = idx + 1;
        buffer[idx] = (byte) (wrSize & FT_4222_Defines.CHIPTOP_DEBUG_REQUEST);
        int i2 = 0;
        while (i2 < wrBuf.length) {
            idx = i + 1;
            buffer[i] = wrBuf[i2];
            i2++;
            i = idx;
        }
        idx = i + 1;
        buffer[i] = (byte) ((checksum & 65280) >> 8);
        i = idx + 1;
        buffer[idx] = (byte) (checksum & FT_4222_Defines.CHIPTOP_DEBUG_REQUEST);
        this.mSpiSlave.write(buffer, buffer.length, sizeTransferred);
        if (sizeTransferred[0] != buffer.length) {
            return 4;
        }
        this.mWrSn++;
        if (this.mWrSn >= 256) {
            this.mWrSn = 0;
        }
        return 0;
    }

    private boolean check_valid_spi_cmd(int cmd) {
        if (cmd == SPI_MASTER_TRANSFER || cmd == SPI_SHORT_MASTER_TRANSFER || cmd == SPI_QUERY_VER) {
            return true;
        }
        return false;
    }

    private int getCheckSum(byte[] sendBuf, int sync, int cmd, int sn, int bufsize) {
        int sum = 0;
        if (sendBuf != null) {
            for (byte b : sendBuf) {
                sum += b & FT_4222_Defines.CHIPTOP_DEBUG_REQUEST;
            }
        }
        return ((((sum + sync) + cmd) + sn) + ((65280 & bufsize) >> 8)) + (bufsize & FT_4222_Defines.CHIPTOP_DEBUG_REQUEST);
    }

    private void spi_push_req_ack_queue() {
        byte[] buffer = new byte[8];
        int idx = 0 + 1;
        buffer[0] = (byte) 0;
        int i = idx + 1;
        buffer[idx] = (byte) 90;
        idx = i + 1;
        buffer[i] = (byte) -124;
        i = idx + 1;
        buffer[idx] = (byte) this.mSn;
        idx = i + 1;
        buffer[i] = (byte) 0;
        i = idx + 1;
        buffer[idx] = (byte) 0;
        int checksum = getCheckSum(null, FT4222_SPI_SLAVE_SYNC_WORD, SPI_ACK, this.mSn, 0);
        idx = i + 1;
        buffer[i] = (byte) ((65280 & checksum) >> 8);
        i = idx + 1;
        buffer[idx] = (byte) (checksum & FT_4222_Defines.CHIPTOP_DEBUG_REQUEST);
        this.mSpiSlave.write(buffer, buffer.length, new int[1]);
    }

    private void sp_slave_parse_and_push_queue(byte[] rdBuf) {
        boolean reset = false;
        boolean dataCorrupted = false;
        for (int i = 0; i < rdBuf.length; i++) {
            int val = rdBuf[i] & FT_4222_Defines.CHIPTOP_DEBUG_REQUEST;
            switch ($SWITCH_TABLE$com$ftdi$j2xx$protocol$FT_Spi_Slave$DECODE_STATE()[this.mDecodeState.ordinal()]) {
                case SpiSlaveResponseEvent.DATA_CORRUPTED /*1*/:
                    if (val == FT4222_SPI_SLAVE_SYNC_WORD) {
                        this.mDecodeState = DECODE_STATE.STATE_CMD;
                        this.mSync = val;
                        break;
                    }
                    reset = true;
                    break;
                case SpiSlaveResponseEvent.IO_ERROR /*2*/:
                    if (check_valid_spi_cmd(val)) {
                        this.mCmd = val;
                    } else {
                        reset = true;
                        dataCorrupted = true;
                    }
                    this.mDecodeState = DECODE_STATE.STATE_SN;
                    break;
                case SpiSlaveResponseEvent.RES_SLAVE_READ /*3*/:
                    this.mSn = val;
                    this.mDecodeState = DECODE_STATE.STATE_SIZE_HIGH;
                    break;
                case FT_4222_Defines.DEBUG_REQ_READ_SFR /*4*/:
                    this.mBufferSize = val * 256;
                    this.mDecodeState = DECODE_STATE.STATE_SIZE_LOW;
                    break;
                case FT_4222_Defines.DEBUG_REQ_READ_OTP_DATA /*5*/:
                    this.mBufferSize += val;
                    this.mCurrentBufferSize = 0;
                    this.mBuffer = new byte[this.mBufferSize];
                    this.mDecodeState = DECODE_STATE.STATE_COLLECT_DATA;
                    break;
                case I2C_MasterFlag.START_AND_STOP /*6*/:
                    this.mBuffer[this.mCurrentBufferSize] = rdBuf[i];
                    this.mCurrentBufferSize++;
                    if (this.mCurrentBufferSize == this.mBufferSize) {
                        this.mDecodeState = DECODE_STATE.STATE_CHECKSUM_HIGH;
                        break;
                    }
                    break;
                case FT4222_STATUS.FT4222_INVALID_BAUD_RATE /*7*/:
                    this.mCheckSum = val * 256;
                    this.mDecodeState = DECODE_STATE.STATE_CHECKSUM_LOW;
                    break;
                case GPIO_Tigger.GPIO_TRIGGER_LEVEL_LOW /*8*/:
                    this.mCheckSum += val;
                    if (this.mCheckSum != getCheckSum(this.mBuffer, this.mSync, this.mCmd, this.mSn, this.mBufferSize)) {
                        dataCorrupted = true;
                    } else if (this.mCmd == SPI_MASTER_TRANSFER) {
                        spi_push_req_ack_queue();
                        if (this.mSpiSlaveListener != null) {
                            this.mSpiSlaveListener.OnDataReceived(new SpiSlaveResponseEvent(3, 0, this.mBuffer, null, null));
                        }
                    }
                    reset = true;
                    break;
            }
            if (dataCorrupted && this.mSpiSlaveListener != null) {
                this.mSpiSlaveListener.OnDataReceived(new SpiSlaveResponseEvent(3, 1, null, null, null));
            }
            if (reset) {
                this.mDecodeState = DECODE_STATE.STATE_SYNC;
                this.mSync = 0;
                this.mCmd = 0;
                this.mSn = 0;
                this.mBufferSize = 0;
                this.mCurrentBufferSize = 0;
                this.mCheckSum = 0;
                this.mBuffer = null;
                reset = false;
                dataCorrupted = false;
            }
        }
    }

    protected boolean pollData() {
        int[] rxSize = new int[1];
        int status = this.mSpiSlave.getRxStatus(rxSize);
        if (rxSize[0] > 0 && status == 0) {
            byte[] rdBuf = new byte[rxSize[0]];
            status = this.mSpiSlave.read(rdBuf, rdBuf.length, rxSize);
            if (status == 0) {
                sp_slave_parse_and_push_queue(rdBuf);
            }
        }
        if (status == 4 && this.mSpiSlaveListener != null) {
            this.mSpiSlaveListener.OnDataReceived(new SpiSlaveResponseEvent(3, 2, this.mBuffer, null, null));
        }
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
        }
        return true;
    }

    protected void requestEvent(SpiSlaveEvent pEvent) {
        if (pEvent instanceof SpiSlaveRequestEvent) {
            switch (pEvent.getEventType()) {
            }
        } else {
            Assert.assertTrue("processEvent wrong type" + pEvent.getEventType(), false);
        }
    }

    protected boolean isTerminateEvent(SpiSlaveEvent pEvent) {
        if (!Thread.interrupted()) {
            return true;
        }
        if (pEvent instanceof SpiSlaveRequestEvent) {
            switch (pEvent.getEventType()) {
                case -1:
                    return true;
            }
        }
        Assert.assertTrue("processEvent wrong type" + pEvent.getEventType(), false);
        return false;
    }
}
