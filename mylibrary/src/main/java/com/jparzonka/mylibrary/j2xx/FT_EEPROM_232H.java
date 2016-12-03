package com.jparzonka.mylibrary.j2xx;

public class FT_EEPROM_232H extends FT_EEPROM {
    public byte AL_DriveCurrent;
    public boolean AL_SchmittInput;
    public boolean AL_SlowSlew;
    public byte BL_DriveCurrent;
    public boolean BL_SchmittInput;
    public boolean BL_SlowSlew;
    public byte CBus0;
    public byte CBus1;
    public byte CBus2;
    public byte CBus3;
    public byte CBus4;
    public byte CBus5;
    public byte CBus6;
    public byte CBus7;
    public byte CBus8;
    public byte CBus9;
    public boolean FIFO;
    public boolean FIFOTarget;
    public boolean FT1248;
    public boolean FT1248ClockPolarity;
    public boolean FT1248FlowControl;
    public boolean FT1248LSB;
    public boolean FastSerial;
    public boolean LoadD2XX;
    public boolean LoadVCP;
    public boolean PowerSaveEnable;
    public boolean UART;

    public static final class CBUS {
        static final int CLK15MHz = 11;
        static final int CLK30MHz = 10;
        static final int CLK7_5MHz = 12;
        static final int DRIVE_0 = 6;
        static final int DRIVE_1 = 7;
        static final int GPIO_MODE = 8;
        static final int PWREN = 4;
        static final int RXLED = 2;
        static final int SLEEP = 5;
        static final int TRISTATE = 0;
        static final int TXDEN = 9;
        static final int TXLED = 1;
        static final int TXRXLED = 3;
    }

    public static final class DRIVE_STRENGTH {
        static final byte DRIVE_12mA = (byte) 2;
        static final byte DRIVE_16mA = (byte) 3;
        static final byte DRIVE_4mA = (byte) 0;
        static final byte DRIVE_8mA = (byte) 1;
    }

    public FT_EEPROM_232H() {
        this.AL_SlowSlew = false;
        this.AL_SchmittInput = false;
        this.AL_DriveCurrent = (byte) 0;
        this.BL_SlowSlew = false;
        this.BL_SchmittInput = false;
        this.BL_DriveCurrent = (byte) 0;
        this.CBus0 = (byte) 0;
        this.CBus1 = (byte) 0;
        this.CBus2 = (byte) 0;
        this.CBus3 = (byte) 0;
        this.CBus4 = (byte) 0;
        this.CBus5 = (byte) 0;
        this.CBus6 = (byte) 0;
        this.CBus7 = (byte) 0;
        this.CBus8 = (byte) 0;
        this.CBus9 = (byte) 0;
        this.UART = false;
        this.FIFO = false;
        this.FIFOTarget = false;
        this.FastSerial = false;
        this.FT1248 = false;
        this.FT1248ClockPolarity = false;
        this.FT1248LSB = false;
        this.FT1248FlowControl = false;
        this.PowerSaveEnable = false;
        this.LoadVCP = false;
        this.LoadD2XX = false;
    }
}
