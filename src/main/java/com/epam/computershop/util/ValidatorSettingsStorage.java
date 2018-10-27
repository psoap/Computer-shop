package com.epam.computershop.util;

import java.util.regex.Pattern;

public final class ValidatorSettingsStorage {
    public static final Pattern REGISTRATION_LOGIN_USERNAME_PATTERN = Pattern.compile("^[a-z][a-z0-9]{4,16}");
    public static final Pattern REGISTRATION_EMAIL_PATTERN = Pattern.compile("^\\w+([-.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$");
    public static final Pattern REGISTRATION_LOGIN_PASSWORD_PATTERN = Pattern.compile("^.{6,32}");

    public static final Pattern DELIVPROF_FULL_NAME_PATTERN = Pattern.compile("^[\\p{Alnum}\\p{IsCyrillic}]{1,32}");
    public static final Pattern DELIVPROF_ADDRESS_PATTERN = Pattern.compile("^[\\s\\w\\p{IsCyrillic},./-]{1,255}");
    public static final Pattern DELIVPROF_PHONE_PATTERN = Pattern.compile("^[+][0-9]{6,12}");

    public static final Pattern CATEGORY_NAME_PATTERN = Pattern.compile("^[\\s\\p{Alnum}\\p{IsCyrillic}]{1,16}");

    public static final Pattern PRODUCT_NAME_PATTERN = Pattern.compile("^[\\wА-Яа-я,\\s.=-]{1,127}");

    public static final Pattern LANG_CODE_NAME_PATTERN = Pattern.compile("^[\\w]{1,16}");

    private ValidatorSettingsStorage() {
    }
}
