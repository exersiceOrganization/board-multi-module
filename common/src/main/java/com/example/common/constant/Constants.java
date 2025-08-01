package com.example.common.constant;

public class Constants {
    /**
     * log level header
     */
    public static final String FICO_LOG_LEVEL_HEADER = "fico-log-level";

    /**
     * trace propagation header
     */
    public static final String FICO_TRACE_HEADER = "traceparent";

    /**
     * user id to use FicoLogger
     */
    public static final String FICO_USER_ID = "fico-user-id";

    /**
     * trace id to use FicoLogger
     */
    public static final String FICO_TRACE_ID = "fico-trace-id";

    /**
     * RequestContext에 저장하는 span stack key
     */
    public static final String FICO_SPAN_STACK = "fico-span-stack";

    /**
     * RequestContext에 저장하는 root span key
     */
    public static final String FICO_SERVER_SPAN = "fico-server-span";

    /***************** character set **********************/
    /**
     * character set 'EUC_KR'
     */
    public static final String CHARSET_EUC_KR = "EUC-KR";

    /**
     * character set 'UTF-8'
     */
    public static final String CHARSET_UTF_8 = "UTF-8";

    /**
     * character set 'KSC5601'
     */
    public static final String CHARSET_KSC5601 = "KSC5601";

    /***************** error message **********************/
    /**
     * 입력값을 확인하십시오.
     */
    public static final String INPUT_PARAM_ERROR_MESSAGE = "입력값을 확인하십시오.";
}
