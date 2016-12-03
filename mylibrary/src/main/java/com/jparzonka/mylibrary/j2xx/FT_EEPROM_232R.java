package com.jparzonka.mylibrary.j2xx;

public class FT_EEPROM_232R extends FT_EEPROM {
    public byte CBus0;
    public byte CBus1;
    public byte CBus2;
    public byte CBus3;
    public byte CBus4;
    public boolean ExternalOscillator;
    public boolean HighIO;
    public boolean InvertCTS;
    public boolean InvertDCD;
    public boolean InvertDSR;
    public boolean InvertDTR;
    public boolean InvertRI;
    public boolean InvertRTS;
    public boolean InvertRXD;
    public boolean InvertTXD;
    public boolean LoadVCP;

    public static final class CBUS {
        static final int BIT_BANG_RD = 12;
        static final int BIT_BANG_WR = 11;
        static final int CLK12MHz = 8;
        static final int CLK24MHz = 7;
        static final int CLK48MHz = 6;
        static final int CLK6MHz = 9;
        static final int IO_MODE = 10;
        static final int PWRON = 1;
        static final int RXLED = 2;
        static final int SLEEP = 5;
        static final int TXDEN = 0;
        static final int TXLED = 3;
        static final int TXRXLED = 4;
    }

    public FT_EEPROM_232R() {
        this.HighIO = false;
        this.ExternalOscillator = false;
        this.InvertTXD = false;
        this.InvertRXD = false;
        this.InvertRTS = false;
        this.InvertCTS = false;
        this.InvertDTR = false;
        this.InvertDSR = false;
        this.InvertDCD = false;
        this.InvertRI = false;
        this.CBus0 = (byte) 0;
        this.CBus1 = (byte) 0;
        this.CBus2 = (byte) 0;
        this.CBus3 = (byte) 0;
        this.CBus4 = (byte) 0;
        this.LoadVCP = false;
    }
}
