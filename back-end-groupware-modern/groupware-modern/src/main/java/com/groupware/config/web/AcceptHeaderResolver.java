package com.groupware.config.web;

import jakarta.servlet.http.Cookie;
import org.springframework.context.i18n.LocaleContext;
import org.springframework.context.i18n.TimeZoneAwareLocaleContext;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.util.WebUtils;

import java.util.Locale;
import java.util.TimeZone;

public class AcceptHeaderResolver extends SessionLocaleResolver {
    @Override
    public Locale resolveLocale(jakarta.servlet.http.HttpServletRequest request) {
        String lang = request.getParameter("lang");
        if (StringUtils.isEmpty(lang)) {
            Cookie langCookie = WebUtils.getCookie((jakarta.servlet.http.HttpServletRequest) request, "lang");
            if (langCookie == null) {
                if (!StringUtils.isEmpty(request.getHeader("Accept-Language"))) {
                    lang = request.getHeader("Accept-Language");
                } else {
                    return super.resolveLocale((jakarta.servlet.http.HttpServletRequest) request);
                }
            } else {
                lang = langCookie.getValue();
            }
        }
        return Locale.forLanguageTag(lang);
    }

    @Override
    public LocaleContext resolveLocaleContext(jakarta.servlet.http.HttpServletRequest request) {
        return new TimeZoneAwareLocaleContext() {
            public Locale getLocale() {
                Locale locale = resolveLocale(request);
                if (locale == null) {
                    locale = determineDefaultLocale((jakarta.servlet.http.HttpServletRequest) request);
                }

                return locale;
            }

            @Nullable
            public TimeZone getTimeZone() {
                TimeZone timeZone = (TimeZone)WebUtils.getSessionAttribute((jakarta.servlet.http.HttpServletRequest) request, TIME_ZONE_SESSION_ATTRIBUTE_NAME);
                if (timeZone == null) {
                    timeZone = determineDefaultTimeZone(request);
                }

                return timeZone;
            }
        };
    }
}
