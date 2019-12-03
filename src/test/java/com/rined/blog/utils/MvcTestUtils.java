package com.rined.blog.utils;

import org.hamcrest.Matcher;

import static org.hamcrest.core.StringContains.containsString;

public class MvcTestUtils {

    public static Matcher<String> containsHref(String hrefPath) {
        return containsString(String.format("href=\"%s\"", hrefPath));
    }

    public static Matcher<String> containsHrefTemplate(String hrefTemplate, Object...data) {
        String template = String.format(hrefTemplate, data);
        return containsString(String.format("href=\"%s\"", template));
    }

    public static Matcher<String> containsHref(String hrefFirstPart, String hrefSecondPart) {
        return containsString(String.format("href=\"%s/%s\"", hrefFirstPart, hrefSecondPart));
    }

    public static Matcher<String> containsFormAction(String action) {
        return containsString(String.format("action=\"%s\"", action));
    }

    public static Matcher<String> containsFormActionTemplate(String hrefTemplate, Object...data) {
        String template = String.format(hrefTemplate, data);
        return containsString(String.format("action=\"%s\"", template));
    }

    public static Matcher<String> containsTagValue(String value) {
        return containsString(String.format(">%s<", value));
    }
}
