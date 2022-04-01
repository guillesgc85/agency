package com.agency.ads.app.interceptor;

import com.agency.ads.common.constant.Header;
import com.agency.ads.common.enums.LanguageEnum;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

@Component
public class LocaleInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String language = request.getHeader(Header.LANGUAGE);
        LocaleContextHolder.setLocale(new Locale(LanguageEnum.resolve(language).lang));
        return true;
    }
}
