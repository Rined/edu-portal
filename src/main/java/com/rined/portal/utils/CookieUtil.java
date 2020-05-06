package com.rined.portal.utils;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

@Slf4j
public final class CookieUtil {
    public static final String UPDATE_COOKIE = "update";

    private static String cookiePathEscape(String value){
        return value.replace(" ", "%20");
    }

    public static String encode(String value){
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            log.warn("Encode problem", e);
            return "";
        }
    }

    public static String decode(String value){
        try {
            return URLDecoder.decode(value, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            log.warn("Decode problem", e);
            return "";
        }
    }

    public static boolean isCookieExists(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (Objects.nonNull(cookies)) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(cookieName)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static Optional<String> getCookieValue(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (Objects.nonNull(cookies)) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals(cookieName)) {
                    return Optional.of(cookie.getValue());
                }
            }
        }
        return Optional.empty();
    }

    public static Optional<Cookie> getCookie(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (Objects.nonNull(cookies)) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(cookieName)) {
                    return Optional.of(cookie);
                }
            }
        }
        return Optional.empty();
    }

    private static void addCookieForPath(HttpServletResponse response, String cookieName, String cookieValue,
                                         String pathTemplate, Object... value) {
        Cookie cookie = new Cookie(cookieName, cookieValue);
        cookie.setPath(cookiePathEscape(String.format(pathTemplate, value)));
        response.addCookie(cookie);
    }

    public static void addTransformCookieValueToPath(HttpServletResponse response,
                                                     String cookieName,
                                                     String cookieValue,
                                                     Function<String, String> transform,
                                                     String pathTemplate, Object... value) {
        addCookieForPath(response, cookieName, transform.apply(cookieValue), pathTemplate, value);
    }

    public static void removeCookieForCurrentPath(HttpServletRequest request, HttpServletResponse response,
                                                  String cookieName) {
        Cookie cookieForDelete = new Cookie(cookieName, null);
        cookieForDelete.setMaxAge(0);
        cookieForDelete.setPath(cookiePathEscape(request.getRequestURI()));
        response.addCookie(cookieForDelete);
    }
}
