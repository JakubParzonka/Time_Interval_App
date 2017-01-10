package com.jparzonka.time_interval_app.data;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.BitSet;

/**
 * Created by Jakub on 2017-01-07.
 */

public class Convert {


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

    public static byte[] hexStringToByteArray(String s) {
        byte[] a = new BigInteger(s, 16).abs()
                .setBit(s.length() << 2).toByteArray();
        byte[] r = new byte[a.length - 1];
        System.arraycopy(a, 1, r, 0, r.length);
        return r;
    }

    public static byte[] toByteArray(BitSet bits) {
        byte[] bytes = new byte[bits.length() / 8 + 1];
        for (int i = 0; i < bits.length(); i++) {
            if (bits.get(i)) {
                bytes[bytes.length - i / 8 - 1] |= 1 << (i % 8);
            }
        }
        return bytes;
    }
}
