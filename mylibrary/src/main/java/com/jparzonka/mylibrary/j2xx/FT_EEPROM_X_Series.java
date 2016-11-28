package com.jparzonka.mylibrary.j2xx;

public class FT_EEPROM_X_Series extends FT_EEPROM {
    public byte AC_DriveCurrent;
    public boolean AC_SchmittInput;
    public boolean AC_SlowSlew;
    public byte AD_DriveCurrent;
    public boolean AD_SchmittInput;
    public boolean AD_SlowSlew;
    public short A_DeviceTypeValue;
    public boolean A_LoadD2XX;
    public boolean A_LoadVCP;
    public boolean BCDDisableSleep;
    public boolean BCDEnable;
    public boolean BCDForceCBusPWREN;
    public byte CBus0;
    public byte CBus1;
    public byte CBus2;
    public byte CBus3;
    public byte CBus4;
    public byte CBus5;
    public byte CBus6;
    public boolean FT1248ClockPolarity;
    public boolean FT1248FlowControl;
    public boolean FT1248LSB;
    public int I2CDeviceID;
    public boolean I2CDisableSchmitt;
    public int I2CSlaveAddress;
    public boolean InvertCTS;
    public boolean InvertDCD;
    public boolean InvertDSR;
    public boolean InvertDTR;
    public boolean InvertRI;
    public boolean InvertRTS;
    public boolean InvertRXD;
    public boolean InvertTXD;
    public boolean PowerSaveEnable;
    public boolean RS485EchoSuppress;

    public static final class CBUS {
        static final int BCD_Charge1 = 13;
        static final int BCD_Charge2 = 14;
        static final int BitBang_RD = 19;
        static final int BitBang_WR = 18;
        static final int CLK12MHz = 11;
        static final int CLK24MHz = 10;
        static final int CLK6MHz = 12;
        static final int DRIVE_0 = 6;
        static final int DRIVE_1 = 7;
        static final int GPIO_MODE = 8;
        static final int I2C_RXF = 16;
        static final int I2C_TXE = 15;
        static final int Keep_Awake = 21;
        static final int PWREN = 4;
        static final int RXLED = 1;
        static final int SLEEP = 5;
        static final int TRISTATE = 0;
        static final int TXDEN = 9;
        static final int TXLED = 2;
        static final int TXRXLED = 3;
        static final int Time_Stamp = 20;
        static final int VBUS_Sense = 17;
    }

    public static final class DRIVE_STRENGTH {
        static final byte DRIVE_12mA = (byte) 2;
        static final byte DRIVE_16mA = (byte) 3;
        static final byte DRIVE_4mA = (byte) 0;
        static final byte DRIVE_8mA = (byte) 1;
    }

    public FT_EEPROM_X_Series() {
        this.A_DeviceTypeValue = (short) 0;
        this.A_LoadVCP = false;
        this.A_LoadD2XX = false;
        this.BCDEnable = false;
        this.BCDForceCBusPWREN = false;
        this.BCDDisableSleep = false;
        this.CBus0 = (byte) 0;
        this.CBus1 = (byte) 0;
        this.CBus2 = (byte) 0;
        this.CBus3 = (byte) 0;
        this.CBus4 = (byte) 0;
        this.CBus5 = (byte) 0;
        this.CBus6 = (byte) 0;
        this.FT1248ClockPolarity = false;
        this.FT1248LSB = false;
        this.FT1248FlowControl = false;
        this.InvertTXD = false;
        this.InvertRXD = false;
        this.InvertRTS = false;
        this.InvertCTS = false;
        this.InvertDTR = false;
        this.InvertDSR = false;
        this.InvertDCD = false;
        this.InvertRI = false;
        this.I2CSlaveAddress = 0;
        this.I2CDeviceID = 0;
        this.I2CDisableSchmitt = false;
        this.AD_SlowSlew = false;
        this.AD_SchmittInput = false;
        this.AD_DriveCurrent = (byte) 0;
        this.AC_SlowSlew = false;
        this.AC_SchmittInput = false;
        this.AC_DriveCurrent = (byte) 0;
        this.RS485EchoSuppress = false;
        this.PowerSaveEnable = false;
    }
}
