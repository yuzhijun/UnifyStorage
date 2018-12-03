package com.winning.unifystorage_core.Utils;

public class Constants {
    public static final String EQUAL_TO = "\\w+(\\s)?=(\\s)?[?](\\s)?";
    public static final String GREATER_THAN = "\\w+(\\s)?>(\\s)?[?](\\s)?";
    public static final String LESS_THAN = "\\w+(\\s)?<(\\s)?[?](\\s)?";
    public static final String GREATER_THAN_OR_EQUAL_TO = "\\w+(\\s)?>=(\\s)?[?](\\s)?";
    public static final String LESS_THAN_OR_EQUAL_TO = "\\w+(\\s)?<=(\\s)?[?](\\s)?";
    public static final String CONTAINS = "\\w+(\\s)?contains(\\s)?[?](\\s)?";
    public static final String LIKE = "\\w+(\\s)?like(\\s)?[?](\\s)?";
    public static final String ISNOTNULL = "^[?](\\s)?notnull(\\s)?";
    public static final String NULL = "^[?](\\s)?null(\\s)?";
    public static final String IN = "\\w+(\\s)?in(\\s)?[?](\\s)?";
    public static final String AND_OR = "and|or";

    public static final String[][] patternArray = {{EQUAL_TO, "="}, {GREATER_THAN, ">"}, {LESS_THAN, "<"}, {GREATER_THAN_OR_EQUAL_TO, ">="},
            {LESS_THAN_OR_EQUAL_TO, "<="}, {CONTAINS, "contains"}, {LIKE, "like"}, {ISNOTNULL, "notnull"}, {NULL, "null"}, {IN, "in"}};
}
