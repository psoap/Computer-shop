package com.epam.computershop.util;

import java.math.BigDecimal;

public class NumberUtil {
    public static final short INVALID_NUMBER = -1;

    private NumberUtil() {
    }

    public static long tryParseLong(String number) {
        try {
            return Long.parseLong(number);
        } catch (NumberFormatException ex) {
            return INVALID_NUMBER;
        }
    }

    public static short tryParseShort(String number) {
        try {
            return Short.parseShort(number);
        } catch (NumberFormatException ex) {
            return INVALID_NUMBER;
        }
    }

    public static BigDecimal tryParseBigDecimal(String number) {
        try {
            return new BigDecimal(number);
        } catch (NumberFormatException ex) {
            return null;
        }
    }
}
