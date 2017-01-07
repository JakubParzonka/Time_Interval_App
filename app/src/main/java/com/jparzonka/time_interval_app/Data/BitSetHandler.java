package com.jparzonka.time_interval_app.data;

import android.util.Log;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.BitSet;
import java.util.Locale;

/**
 * Created by Jakub on 2016-12-29.
 */

public class BitSetHandler {
    private DTO dto;

    public BitSetHandler(DTO dto) {
        this.dto = dto;
    }

    private BitSet getST(DTO dto) {
        double pulseWidth = dto.getOutputWidth();
        BitSet bs = new BitSet();
        switch ((int) pulseWidth) {
            case 10:
                bs.set(0, false);
                bs.set(1, false);
                return bs;
            case 20:
                bs.set(0, true);
                bs.set(1, false);
                return bs;
            case 50:
                bs.set(0, false);
                bs.set(1, true);
                return bs;
            case 100:
                bs.set(0, true);
                bs.set(1, true);
                return bs;
            default:
                Log.e("BitSetHandler", "Wrong pulse width value!");
                return null;
        }
    }

    public BitSet getSET_S_BitSet() {
        //Adres
        BitSet dataBytes = CommandAddresses.getSET_S_BitSetAddres();
        BitSet bs = getST(dto);
        //ST0
        Log.i("ST0", String.valueOf(bs.get(0)));
        dataBytes.set(16, bs.get(0));
        //ST1
        Log.i("ST1", String.valueOf(bs.get(1)));
        dataBytes.set(17, bs.get(1));
        //XX
        dataBytes.set(18, 20, false);
        //MODE
        if (dto.getSelectedMode() == "TI") {
            dataBytes.set(20, false);
            Log.i("MODE", "TI");
        } else if (dto.getSelectedMode() == "F") {
            dataBytes.set(20, true);
            Log.i("MODE", "FREQUENCY");
        } else Log.e("BitSetHandler", "Wrong selected mode");
        //CK10M_INT
        if (dto.isExternalClockSelected()) {
            Log.i("EXTERNAL CLOCK", "YES");
            dataBytes.set(21, false);
        } else {
            Log.i("EXTERNAL CLOCK", "NO");
            dataBytes.set(21, true);
        }
        //XX
        dataBytes.set(22, 24, false);
        //A2
        Log.i("A - inverted", String.valueOf(dto.isHasSignal_A_InvertedPolarization()));
        dataBytes.set(24, dto.isHasSignal_A_InvertedPolarization());
        //XX
        dataBytes.set(25, 27, false);
        //B2
        Log.i("B - inverted", String.valueOf(dto.isHasSignal_B_InvertedPolarization()));
        dataBytes.set(27, dto.isHasSignal_B_InvertedPolarization());
        //XX
        dataBytes.set(28, 30, false);
        //C2
        Log.i("CW - inverted", String.valueOf(dto.isHasSignal_CW_InvertedPolarization()));
        dataBytes.set(30, dto.isHasSignal_CW_InvertedPolarization());
        //X
        dataBytes.set(31, false);
        //Dopełniam do 3 bajtów danych
        dataBytes.set(32, 40, false);
        Log.i("dataBytes toString", dataBytes.toString());
        Log.i("BitSetHandler/getSET_S", "dataBytes size ->" + dataBytes.size());
        return dataBytes;
    }

    public static BitSet getRESET_BitSet() {
        BitSet dataBytes = CommandAddresses.getRESET_BitSetAddres();
        dataBytes.set(16, 40, false);
        return dataBytes;
    }

    public BitSet getSET_TRIG_BitSet() {
        //Adres
        BitSet dataBytes = CommandAddresses.getSET_TRIG_BitSetAddres();
        //

        return dataBytes;
    }

    public BitSet getTRIG_DIV_BitSet() {
        BitSet dataBytes = CommandAddresses.getTIRG_DIV_BitSetAddres();
        String s = Integer.toBinaryString(8);
        // TODO przenieść to do oddzielnej metody
        char[] data = s.toCharArray();
        int dataSize = data.length;
      //  System.out.println("getTRIG_DIV_BitSet char size: " + dataSize + "\narray:\n");
        for (char c : data) {
            System.out.println(c + " ");
        }

        int lackOfSpace = 4 - dataSize;
        //pierwsza pętla zapełniająca danymi
        for (int i = 0; i < dataSize; i++) {
            if (data[dataSize - i - 1] == '1') {
                dataBytes.set(16 + i, true);
            } else if (data[dataSize - i - 1] == '0') {
                dataBytes.set(16 + i, false);
            } else {
                Log.i("BitSetHandler", "Wrong value in char array");
            }
        }
        dataBytes.set(20, 40, false);
        Log.i("BitSetHandler", "TRIG_DIV: " + dataBytes.toString());
        return dataBytes;
    }

    public void verifyOCT_and_DAC() {
        NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
        DecimalFormat df2 = (DecimalFormat) nf; //new DecimalFormat("###.###");
        double OCT = Double.parseDouble(df2.format(dto.getOCT()));
        int i = (int) OCT;
        Log.i("BitSetHandler", "double OCT = " + OCT);
        Log.i("BitSetHandler", "int OCT = " + i);
        String s = Integer.toBinaryString(i);
        Log.i("BitSetHandler", "binary OCT = " + s);

        double DAC = Double.parseDouble(df2.format(dto.getDAC()));
        i = (int) DAC;
        s = convert(DAC);
        //s = Long.toBinaryString(Long.valueOf(String.valueOf(i)));
        DAC = Double.valueOf(df2.format(DAC));
        Log.i("BitSetHandler", "double DAC = " + DAC);
        Log.i("BitSetHandler", "int DAC = " + i);
        Log.i("BitSetHandler", "binary DAC = " + s);
    }

    public static String convert(double number) {
        int n = 10;  // constant?
        BigDecimal bd = new BigDecimal(number);
        BigDecimal mult = new BigDecimal(2).pow(n);
        bd = bd.multiply(mult);
        BigInteger bi = bd.toBigInteger();
        StringBuilder str = new StringBuilder(bi.toString(2));
        while (str.length() < n + 1) {  // +1 for leading zero
            str.insert(0, "0");
        }
        str.insert(str.length() - n, ".");
        return str.toString();
    }

}
