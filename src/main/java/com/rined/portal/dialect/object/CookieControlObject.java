package com.rined.portal.dialect.object;

import com.rined.portal.utils.CookieUtil;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@SuppressWarnings("unused")
public class CookieControlObject {
    private final HttpServletResponse response;
    private final HttpServletRequest request;

    CookieControlObject(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }

    public boolean isCookieExists(String cookieName) {
        return CookieUtil.isCookieExists(request, cookieName);
    }

    public String pop(String cookieName) {
        Optional<Cookie> cookieOptional = CookieUtil.getCookie(request, cookieName);
        if (cookieOptional.isPresent()) {
            CookieUtil.removeCookieForCurrentPath(request, response, cookieName);
            String value = cookieOptional.get().getValue();
            return CookieUtil.decode(value);
        }
        return "";
    }
}
