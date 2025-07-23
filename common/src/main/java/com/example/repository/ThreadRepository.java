package com.example.repository;

import java.util.HashMap;
import java.util.Map;

/**
 * ThreadLocal repository for requests not using http.
 * {@code
 * (ex> batch)
 * }
 *
 * @author openlabs
 *
 */
public class ThreadRepository {

    // 부모 TheadLocal의 데이터를 자식에게 물려 줌.
    private static ThreadLocal<Map<String, Object>> threadLocal = new InheritableThreadLocal<Map<String, Object>>() {
        // threadLocal 생성 후 초기화 시켜줌
        @Override protected Map<String, Object> initialValue() {
            return new HashMap<>();
        }
    };

    public static Object get(String key){
        Map<String, Object> map = threadLocal.get();
        return map.get(key);
    }

    public static void set(String key, Object value){
        Map<String, Object> map = threadLocal.get();
        map.put(key, value);
    }

    public static void remove(String key){
        Map<String, Object> map = threadLocal.get();
        map.remove(key);
    }

    public static void removeAll() {threadLocal.remove();}
}
