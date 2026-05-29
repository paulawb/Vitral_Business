package com.vitral.auth.infraestructure.security;

public final class InputSanitizer {

    private InputSanitizer() {
    }

    public static String clean(String value) {
        if (value == null) {
            return null;
        }
        return value.trim()
                .replace("<", "")
                .replace(">", "")
                .replace("\"", "")
                .replace("'", "");
    }
}
