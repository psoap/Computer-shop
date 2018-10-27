package com.epam.computershop.util;

import java.util.Locale;

//Util class for locale.tld
public final class LocaleUtil {
    private LocaleUtil() {
    }

    public static String getDisplayName(Locale locale) {
        return locale.getDisplayName(locale);
    }

    public static String getTranslatedName(Locale locale, Locale translateLocale) {
        return locale.getDisplayName(translateLocale);
    }
}
