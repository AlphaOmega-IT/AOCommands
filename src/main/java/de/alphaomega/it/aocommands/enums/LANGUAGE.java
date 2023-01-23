package de.alphaomega.it.aocommands.enums;

import lombok.Getter;

@Getter
public enum LANGUAGE {

    GERMAN("de_DE"),
    ENGLISH("en_US"),
    BRAZILIAN("pt_BR");

    private final String locale;

    LANGUAGE(final String locale) {
        this.locale = locale;
    }
}
