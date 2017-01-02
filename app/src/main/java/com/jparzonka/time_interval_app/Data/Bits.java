package com.jparzonka.time_interval_app.data;

import android.util.Log;

import java.util.BitSet;

/**
 * Created by Jakub on 2016-12-30.
 */

public class Bits {

    public static BitSet convert(float val) {
        Log.i("Bits/convert", "float val ->" + val);
        long value = (long) val;
        BitSet bits = new BitSet();
        int index = 0;
        while (value != 0L) {
            if (value % 2L != 0) {
                bits.set(index);
            }
            ++index;
            value = value >>> 1;
        }
        return bits;
    }

    public static long convert(BitSet bits) {
        long value = 0L;
        for (int i = 0; i < bits.length(); ++i) {
            value += bits.get(i) ? (1L << i) : 0L;
        }
        return value;
    }
}