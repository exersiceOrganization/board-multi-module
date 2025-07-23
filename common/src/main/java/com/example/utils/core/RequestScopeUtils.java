package com.example.utils.core;

import com.example.repository.ThreadRepository;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static org.springframework.web.context.request.RequestContextHolder.*;

/**
 * RequestScopeUtils
 */
public class RequestScopeUtils {

    private RequestScopeUtils(){ throw new IllegalStateException("Utility class"); }

    /**
     * Get an object from RequestAttributes or ThreadRepository
     * <pre>
     * example>
     *      Integer timeout = (Integer) RequestScopeUtils.getAttribute(ONLINE_TIMEOUT_KEY)
     * </pre>
     *
     * @param name 저장된 attribute key name
     * @return key name으로 저장된 객체. 없으면 null
     */
    public static Object getAttribute(String name){
        Object attribute = null;
        RequestAttributes attributes = getRequestAttributes();

        if (attributes == null) {
            attribute = ThreadRepository.get(name);
        }else{
            attribute = attributes.getAttribute(name, RequestAttributes.SCOPE_REQUEST);
        }

        return attribute;
    }

    /**
     * Set an object to RequestAttributes or ThreadRepository
     * <pre>
     * example>
     *      RequestScopeUtils.setAttribute(ONLINE_START_TIME_KEY, startTime)
     * </pre>
     *
     * @param name      저장할 attribute key name
     * @param object    저장할 객체
     */
    public static void setAttribute(String name, Object object){
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();

        if (attributes != null){
            attributes.setAttribute(name, object, RequestAttributes.SCOPE_REQUEST);
        }else{
            ThreadRepository.set(name, object);
        }
    }

    /**
     * Remove all object from ThreadRepository
     * <pre>
     * example>
     *      RequestScopeUtils.removeAllAttribute()
     * </pre>
     */
    public static void removeAllAttribute() { ThreadRepository.removeAll();}

    /**
     * Get ServletRequestAttributes if it is in process Http Request.
     * If not, throw IllegalStateException.
     * <pre>
     * example>
     *      HttpServletRequest httpServletRequest =
     *                  RequestScopeUtils.getServletRequestAttributes().getRequest();
     * </pre>
     *
     * @return  thread local 에 저장된 ServletRequestAttributes 객체
     */
    public static ServletRequestAttributes getServletRequestAttributes(){
        RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
        if (attributes == null)
            throw new IllegalStateException("RequestAttributes is null");
        return (ServletRequestAttributes) attributes;
    }

}
