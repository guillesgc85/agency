package com.agency.ads.common.enums;

import java.util.Arrays;

public enum LanguageEnum {

    ES("es"),
    PT("pt"),
    EN("en"),
    FR("fr");

    public final String lang;

    LanguageEnum(String lang) {
        this.lang = lang;
    }

    public static LanguageEnum resolve(String lg) {
        return Arrays.stream(LanguageEnum.values())
                .filter(filter -> filter.lang.equals(lg))
                .findFirst().orElse(LanguageEnum.EN);
    }
}
