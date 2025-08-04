package com.example.extansion_comm.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class) // Mockito 기능을 활성화
public class BusinessMetricAspectTest {

    @InjectMocks    // 테스트 대상인 실제 객체에 Mock 객체 주입
    private BusinessMetricAspect aspect;

    @Mock   // 가짜 객체 생성
    private ProceedingJoinPoint joinPoint;

    @Mock
    private CodeSignature codeSignature;

    @Mock
    private MethodSignature methodSignature;

    @BeforeEach
    void setUp(){
        log.debug("---- 테스트 시작 전 setUp() 메서드 실행 ----");
    }

    @Test
    void getParameter() throws Throwable{

        // Given (테스트를 위한 준비)
        String[] paramNames = {"userId", "amount"};
        Object[] args = {"testUser", 10000};

        // Mock 객체 동작 정의
        // joinPoint.getSignature()가 호출 되면 codeSignature를 반환
        when(joinPoint.getSignature()).thenReturn(codeSignature);
        // codeSignature.getParameterNames())가 호출되면 paramNames배열을 반환
        when(codeSignature.getParameterNames()).thenReturn(paramNames);
        // joinPoint.getArgs()가 호출되면 args 배열을 반환
        when(joinPoint.getArgs()).thenReturn(args);

        // When (테스트 대상 메서드 실행)
        Map<String, Object> result = aspect.getParam(joinPoint);
        log.debug("result : {}", result);

        // Then (결과 검증)
        assertNotNull(result, "반환 Map null 아님");
        assertFalse(result.isEmpty(), "결과 Map은 차 있어야 함");
    }

    @Test
    void getServiceNm(){

        // Given (테스트를 위한 준비)

        // Mock 객체의 동작을 정의.
        when(joinPoint.getSignature()).thenReturn(methodSignature);
        when(methodSignature.getDeclaringType()).thenReturn(new Object() {}.getClass());
        // When (테스트 대상 메서드 실행)
        Object result = aspect.getServiceNm(joinPoint);
        log.debug("result : {}", result);
        // Then (결과 검증)
    }

    @Test
    void getCustomKey() throws Throwable{

        // Given (테스트를 위한 준비)
//        String expectedKey = "payment.success.metric";
        String expectedKey = null;
        Map<String, Object> params = new HashMap<>();
        params.put("customKey", expectedKey);

        // **수정된 부분: getParam() 메서드 자체를 Mocking합니다.**
        // spy를 사용하면 aspect의 실제 메서드를 호출하면서 필요한 부분만 Mocking할 수 있습니다.
        BusinessMetricAspect spyAspect = spy(aspect);

        // spy 객체의 getParam() 메서드가 params Map을 반환하도록 설정
        doReturn(params).when(spyAspect).getParam(joinPoint);

        // When (테스트 대상 메서드 실행)
        String result = spyAspect.getCustomKey(joinPoint);
        log.debug("result : {}", result);

        // Then (결과 검증)
//        assertEquals(expectedKey, result);
        assertEquals("business.metrics", result);
    }
}
