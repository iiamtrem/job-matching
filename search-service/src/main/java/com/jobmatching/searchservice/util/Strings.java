package com.jobmatching.searchservice.util;

public final class Strings {
    private Strings() {}

    public static String safeLower(String s) {
        return s == null ? "" : s.toLowerCase();
    }
}
