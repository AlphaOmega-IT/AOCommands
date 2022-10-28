package de.alphaomega.it.utils;

public class InputCheck {

    public static boolean isFullNumber(final String input) {
        return input.matches("^\\d*$");
    }

}
