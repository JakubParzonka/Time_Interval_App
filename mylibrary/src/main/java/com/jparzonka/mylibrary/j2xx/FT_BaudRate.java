package com.jparzonka.mylibrary.j2xx;

final class FT_BaudRate {
    private static final int FT_CLOCK_RATE = 3000000;
    private static final int FT_CLOCK_RATE_HI = 12000000;
    private static final int FT_SUB_INT_0_0 = 0;
    private static final int FT_SUB_INT_0_125 = 49152;
    private static final int FT_SUB_INT_0_25 = 32768;
    private static final int FT_SUB_INT_0_375 = 0;
    private static final int FT_SUB_INT_0_5 = 16384;
    private static final int FT_SUB_INT_0_625 = 16384;
    private static final int FT_SUB_INT_0_75 = 32768;
    private static final int FT_SUB_INT_0_875 = 49152;
    private static final int FT_SUB_INT_MASK = 49152;

    private FT_BaudRate() {
    }

    static byte FT_GetDivisor(int rate, int[] divisors, boolean bm) {
        byte rval = FT_CalcDivisor(rate, divisors, bm);
        if (rval == (byte) -1) {
            return (byte) -1;
        }
        int temp_accuracy;
        if (rval == 0) {
            divisors[FT_SUB_INT_0_375] = (divisors[FT_SUB_INT_0_375] & -49153) + 1;
        }
        int temp_actual = FT_CalcBaudRate(divisors[FT_SUB_INT_0_375], divisors[1], bm);
        int temp_mod;
        if (rate > temp_actual) {
            temp_accuracy = ((rate * 100) / temp_actual) - 100;
            temp_mod = ((rate % temp_actual) * 100) % temp_actual;
        } else {
            temp_accuracy = ((temp_actual * 100) / rate) - 100;
            temp_mod = ((temp_actual % rate) * 100) % rate;
        }
        if (temp_accuracy < 3) {
            rval = (byte) 1;
        } else if (temp_accuracy == 3 && temp_mod == 0) {
            rval = (byte) 1;
        } else {
            rval = (byte) 0;
        }
        return rval;
    }

    private static byte FT_CalcDivisor(int rate, int[] divisors, boolean bm) {
        byte ok = (byte) 1;
        if (rate == 0 || ((FT_CLOCK_RATE / rate) & -16384) > 0) {
            return (byte) -1;
        }
        divisors[FT_SUB_INT_0_375] = FT_CLOCK_RATE / rate;
        divisors[1] = FT_SUB_INT_0_375;
        if (divisors[FT_SUB_INT_0_375] == 1 && ((FT_CLOCK_RATE % rate) * 100) / rate <= 3) {
            divisors[FT_SUB_INT_0_375] = FT_SUB_INT_0_375;
        }
        if (divisors[FT_SUB_INT_0_375] == 0) {
            return (byte) 1;
        }
        int modifier;
        int t = ((FT_CLOCK_RATE % rate) * 100) / rate;
        if (bm) {
            if (t <= 6) {
                modifier = FT_SUB_INT_0_375;
            } else if (t <= 18) {
                modifier = FT_SUB_INT_MASK;
            } else if (t <= 31) {
                modifier = FT_SUB_INT_0_75;
            } else if (t <= 43) {
                modifier = FT_SUB_INT_0_375;
                divisors[1] = 1;
            } else if (t <= 56) {
                modifier = FT_SUB_INT_0_625;
            } else if (t <= 68) {
                modifier = FT_SUB_INT_0_625;
                divisors[1] = 1;
            } else if (t <= 81) {
                modifier = FT_SUB_INT_0_75;
                divisors[1] = 1;
            } else if (t <= 93) {
                modifier = FT_SUB_INT_MASK;
                divisors[1] = 1;
            } else {
                modifier = FT_SUB_INT_0_375;
                ok = (byte) 0;
            }
        } else if (t <= 6) {
            modifier = FT_SUB_INT_0_375;
        } else if (t <= 18) {
            modifier = FT_SUB_INT_MASK;
        } else if (t <= 37) {
            modifier = FT_SUB_INT_0_75;
        } else if (t <= 75) {
            modifier = FT_SUB_INT_0_625;
        } else {
            modifier = FT_SUB_INT_0_375;
            ok = (byte) 0;
        }
        divisors[FT_SUB_INT_0_375] = divisors[FT_SUB_INT_0_375] | modifier;
        return ok;
    }

    private static final int FT_CalcBaudRate(int divisor, int extdiv, boolean bm) {
        if (divisor == 0) {
            return FT_CLOCK_RATE;
        }
        int rate = (-49153 & divisor) * 100;
        if (bm) {
            if (extdiv != 0) {
                switch (divisor & FT_SUB_INT_MASK) {
                    case FT_SUB_INT_0_375 /*0*/:
                        rate += 37;
                        break;
                    case FT_SUB_INT_0_625 /*16384*/:
                        rate += 62;
                        break;
                    case FT_SUB_INT_0_75 /*32768*/:
                        rate += 75;
                        break;
                    case FT_SUB_INT_MASK /*49152*/:
                        rate += 87;
                        break;
                    default:
                        break;
                }
            }
            switch (divisor & FT_SUB_INT_MASK) {
                case FT_SUB_INT_0_625 /*16384*/:
                    rate += 50;
                    break;
                case FT_SUB_INT_0_75 /*32768*/:
                    rate += 25;
                    break;
                case FT_SUB_INT_MASK /*49152*/:
                    rate += 12;
                    break;
                default:
                    break;
            }
        }
        switch (divisor & FT_SUB_INT_MASK) {
            case FT_SUB_INT_0_625 /*16384*/:
                rate += 50;
                break;
            case FT_SUB_INT_0_75 /*32768*/:
                rate += 25;
                break;
            case FT_SUB_INT_MASK /*49152*/:
                rate += 12;
                break;
        }
        return 300000000 / rate;
    }

    static final byte FT_GetDivisorHi(int rate, int[] divisors) {
        byte rval = FT_CalcDivisorHi(rate, divisors);
        if (rval == (byte) -1) {
            return (byte) -1;
        }
        int temp_accuracy;
        if (rval == 0) {
            divisors[FT_SUB_INT_0_375] = (divisors[FT_SUB_INT_0_375] & -49153) + 1;
        }
        int temp_actual = FT_CalcBaudRateHi(divisors[FT_SUB_INT_0_375], divisors[1]);
        int temp_mod;
        if (rate > temp_actual) {
            temp_accuracy = ((rate * 100) / temp_actual) - 100;
            temp_mod = ((rate % temp_actual) * 100) % temp_actual;
        } else {
            temp_accuracy = ((temp_actual * 100) / rate) - 100;
            temp_mod = ((temp_actual % rate) * 100) % rate;
        }
        if (temp_accuracy < 3) {
            rval = (byte) 1;
        } else if (temp_accuracy == 3 && temp_mod == 0) {
            rval = (byte) 1;
        } else {
            rval = (byte) 0;
        }
        return rval;
    }

    private static byte FT_CalcDivisorHi(int rate, int[] divisors) {
        byte ok = (byte) 1;
        if (rate == 0 || ((FT_CLOCK_RATE_HI / rate) & -16384) > 0) {
            return (byte) -1;
        }
        divisors[1] = 2;
        if (rate >= 11640000 && rate <= 12360000) {
            divisors[FT_SUB_INT_0_375] = FT_SUB_INT_0_375;
            return (byte) 1;
        } else if (rate < 7760000 || rate > 8240000) {
            divisors[FT_SUB_INT_0_375] = FT_CLOCK_RATE_HI / rate;
            divisors[1] = 2;
            if (divisors[FT_SUB_INT_0_375] == 1 && ((FT_CLOCK_RATE_HI % rate) * 100) / rate <= 3) {
                divisors[FT_SUB_INT_0_375] = FT_SUB_INT_0_375;
            }
            if (divisors[FT_SUB_INT_0_375] == 0) {
                return (byte) 1;
            }
            int modifier;
            int t = ((FT_CLOCK_RATE_HI % rate) * 100) / rate;
            if (t <= 6) {
                modifier = FT_SUB_INT_0_375;
            } else if (t <= 18) {
                modifier = FT_SUB_INT_MASK;
            } else if (t <= 31) {
                modifier = FT_SUB_INT_0_75;
            } else if (t <= 43) {
                modifier = FT_SUB_INT_0_375;
                divisors[1] = divisors[1] | 1;
            } else if (t <= 56) {
                modifier = FT_SUB_INT_0_625;
            } else if (t <= 68) {
                modifier = FT_SUB_INT_0_625;
                divisors[1] = divisors[1] | 1;
            } else if (t <= 81) {
                modifier = FT_SUB_INT_0_75;
                divisors[1] = divisors[1] | 1;
            } else if (t <= 93) {
                modifier = FT_SUB_INT_MASK;
                divisors[1] = divisors[1] | 1;
            } else {
                modifier = FT_SUB_INT_0_375;
                ok = (byte) 0;
            }
            divisors[FT_SUB_INT_0_375] = divisors[FT_SUB_INT_0_375] | modifier;
            return ok;
        } else {
            divisors[FT_SUB_INT_0_375] = 1;
            return (byte) 1;
        }
    }

    private static int FT_CalcBaudRateHi(int divisor, int extdiv) {
        if (divisor == 0) {
            return FT_CLOCK_RATE_HI;
        }
        if (divisor == 1) {
            return 8000000;
        }
        int rate = (-49153 & divisor) * 100;
        if ((extdiv & 65533) != 0) {
            switch (divisor & FT_SUB_INT_MASK) {
                case FT_SUB_INT_0_375 /*0*/:
                    rate += 37;
                    break;
                case FT_SUB_INT_0_625 /*16384*/:
                    rate += 62;
                    break;
                case FT_SUB_INT_0_75 /*32768*/:
                    rate += 75;
                    break;
                case FT_SUB_INT_MASK /*49152*/:
                    rate += 87;
                    break;
                default:
                    break;
            }
        }
        switch (divisor & FT_SUB_INT_MASK) {
            case FT_SUB_INT_0_625 /*16384*/:
                rate += 50;
                break;
            case FT_SUB_INT_0_75 /*32768*/:
                rate += 25;
                break;
            case FT_SUB_INT_MASK /*49152*/:
                rate += 12;
                break;
        }
        return 1200000000 / rate;
    }
}
