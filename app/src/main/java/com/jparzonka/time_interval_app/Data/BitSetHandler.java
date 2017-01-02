package com.jparzonka.time_interval_app.data;

import android.util.Log;

import java.text.DecimalFormat;
import java.util.BitSet;

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
        dataBytes.set(16, bs.get(0));
        //ST1
        dataBytes.set(17, bs.get(1));
        //XX
        dataBytes.set(18, 19, false);
        //MODE
        if (dto.getSelectedMode() == "TI")
            dataBytes.set(20, false);
        else if (dto.getSelectedMode() == "F")
            dataBytes.set(20, true);
        else Log.e("BitSetHandler", "Wrong selected mode");
        //CK10M_INT
        if (dto.isExternalClockSelected()) {
            dataBytes.set(21, false);
        } else {
            dataBytes.set(21, true);
        }
        //XX
        dataBytes.set(22, 23, false);
        //A2
        dataBytes.set(24, dto.isHasSignal_A_InvertedPolarization());
        //XX
        dataBytes.set(25, 26, false);
        //B2
        dataBytes.set(27, dto.isHasSignal_B_InvertedPolarization());
        //XX
        dataBytes.set(28, 29, false);
        //C2
        dataBytes.set(30, dto.isHasSignal_CW_InvertedPolarization());
        //X
        dataBytes.set(31, false);
        //Dopełniam do 3 bajtów danych
        dataBytes.set(32, 39, false);
        Log.i("dataBytes toString", dataBytes.toString());
        Log.i("BitSetHandler/getSET_S", "dataBytes size ->" + dataBytes.size());

        return dataBytes;
    }

    public BitSet getRESET_BitSet() {
        BitSet dataBytes = CommandAddresses.getRESET_BitSetAddres();
        dataBytes.set(15, 39, false);
        return dataBytes;
    }

    public BitSet getSET_TRIG_BitSet() {
        //Adres
        BitSet dataBytes = CommandAddresses.getSET_TRIG_BitSetAddres();
        //

        return dataBytes;
    }

    public void verifyOCT_and_DAC() {
        double OCT = dto.getOCT();
        DecimalFormat df2 = new DecimalFormat("###.###");
        OCT = Double.valueOf(df2.format(OCT));
        float f = (float) OCT;
        BitSet octBitSet = Bits.convert(f);
        Log.i("BitSetHandler/verifyOCT", "octBitSet toString ->" + octBitSet.toString());
        Log.i("BitSetHandler/verifyOCT", "octBitSet cardinality ->" + octBitSet.cardinality());
        Log.i("BitSetHandler/verifyOCT", "octBitSet size ->" + octBitSet.size());
        Log.i("BitSetHandler", "float OCT = " + f);
        int i = Float.floatToIntBits(f);
        String s = Integer.toBinaryString(i);
        Log.i("BitSetHandler", "OCT = " + s);
        Log.i("BitSetHandler", "OCT = " + OCT);
        Double DAC = dto.getDAC();
        s = Long.toBinaryString(Double.doubleToRawLongBits(DAC));
        DAC = Double.valueOf(df2.format(DAC));
        Log.i("BitSetHandler", "DAC = " + DAC);
        Log.i("BitSetHandler", "DAC = " + s);
        Log.i("BitSetHandler", "DAC = " + DAC);
    }


}
