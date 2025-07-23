package com.example.utils.extension;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * 공통 메세지 utility class
 */
@Component
public class MessageUtils {

    private  static MessageSource messageSource;

    /**
     * messageSource injection
     *
     * @param messageSource
     */
    @Autowired
    public void setMessageSource(@Qualifier("dbMessageSource") MessageSource messageSource) {
        MessageUtils.messageSource = messageSource;
    }

    /**
     * 공통메시지 조회
     * <pre>
     * example>
     *      MessageUtils.getMessage("FWKE0001", "test");
     *      result : test 처리중 오류가 발생했습니다.
     * </pre>
     *
     * @param code  message code
     * @param args  message arguments
     * @return  arguments 가 적용된 message string
     */
    public static String getMessage(String code, String... args) {
        return MessageUtils.messageSource.getMessage(code, args, getLocale());
    }


    /**
     * 공통메시지 조회
     * <pre>
     * example>
     *      MessageUtils.getMessage("FWKE0001", new Locale("ko"), "test");
     *      result : test 처리중 오류가 발생했습니다.
     * </pre>
     *
     * @param code      message code
     * @param locale    message locale
     * @param args      message arguments
     * @return  arguments 가 적용된 message string
     */
    public static String getMessage(String code, Locale locale, String... args) {
        return MessageUtils.messageSource.getMessage(code, args, locale);
    }

    /*
     * spring boot locale 조회.
     * LocaleResolver 를 별도 설정하지 않는다면 AcceptHeaderLocaleResolver bean 이용.
     *
     * @return
     */
    private static Locale getLocale() {
        return LocaleContextHolder.getLocale();
    }
}

