package com.epam.computershop.util;

import java.util.regex.Pattern;

public class Validator {
    private Validator() {
    }

    public static boolean validate(final Pattern pattern, String data) {
        return pattern.matcher(data).matches();
    }
}