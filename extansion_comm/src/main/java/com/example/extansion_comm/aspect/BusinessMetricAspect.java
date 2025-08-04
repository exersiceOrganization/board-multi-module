package com.example.extansion_comm.aspect;

import io.micrometer.core.instrument.Gauge;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import io.micrometer.core.instrument.Metrics;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@Slf4j
@Aspect // common에 있는 aop 사용 시 gradle에서 java-library 및 api 추가
@Component
public class BusinessMetricAspect {

    // Gauge가 측정할 대상 객체 (예: 활성 스레드 수를 저장하는 객체)
    private final AtomicInteger processingGauge = new AtomicInteger(0);

    /**
     * 대상 메서드의 파라미터 key, value값을 Map으로 추출.
     * <pre>
     *     String[] paramNames = {"userId", "amount"};
     *     Object[] args = {"testUser", 10000};
     *
     *     private BusinessMetricAspect aspect;
     *     Map<String, Object> result = aspect.getParameter(joinPoint);
     *     result : {amount=10000, userId=testUser}
     * </pre>
     *
     * @param joinPoint AOP가 가로챈 대상메서드의 실행 정보를 담고 있는 객체
     * @return          서비스 클래스의 이름
     */
    public Map<String, Object> getParam(ProceedingJoinPoint joinPoint){

        // joinPoint와 getSignature()의 반환 값에 대한 null check
        if (joinPoint == null || joinPoint.getSignature() == null) {
            return new HashMap<>();
        }

        // 대상 메서드의 시그니처 정보를 CodeSignature 타입으로 변화
        CodeSignature codeSignature = (CodeSignature) joinPoint.getSignature();
        // 파라미터 이름을 string배열로 가져옴.
        String[] paramNames = codeSignature.getParameterNames();
        // 실제 파라미터 값을 object배열로 가져옴
        Object[] args = joinPoint.getArgs();

        // null check 및 다른 값일 경우 Map 반환
        if (paramNames == null || args == null || paramNames.length != args.length) {
            return new HashMap<>();
        }

        // --- Stream API를 사용한 간결하고 효율적인 로직 ---

        // IntStream.range(0, paramNames.length): 0부터 파라미터 개수-1까지의 정수 Stream을 생성
        return IntStream.range(0, paramNames.length)
                .boxed()    // 기본형(int) Stream을 참조형(Integer) Stream으로 변환
                .collect(Collectors.toMap(
                    // Stream의 index[i]를 Map의 key로 변환
                    i -> paramNames[i]
                    // stream의 인덱스[i]를 Map의 value로 변환
                    , i -> args[i]
                ));
    }

    /**
     * AOP가 가로챈 대상 매서드가 속한 서비스 클래스의 이름을 반환.
     *<pre>
     *     Object result = aspect.getServiceNm(jointPoint);
     *     result : Optional[MyService]
     *</pre>
     *
     * @param joinPoint AOP가 가로챈 대상메서드의 실행 정보를 담고 있는 객체
     * @return          서비스 클래스 명 리턴
     */
    public Object getServiceNm(ProceedingJoinPoint joinPoint){

        // joinPoint와 getSignature()의 반환 값에 대한 null check
        if (joinPoint == null || joinPoint.getSignature() == null) {
            return null;
        }

        // Signature객체를 MethodSignature 타입으로 변환.
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();

        // 대상 메서드가 속한 클래스 객체(Class) 가져옴
        Class<?> declaringClass = methodSignature.getDeclaringType();

        // @Service가 있는 경우 Optional로 받아옴
//        if(declaringClass.isAnnotationPresent(Service.class)){
//            return Optional.of(declaringClass.getSimpleName());
//        }

        // 클래스 객체에서 클래스의 간단한 이름(Simple Name) 추출 반환
        return methodSignature.getDeclaringType().getSimpleName();
    }

    /**
     * customKey라는 이름의 값을 추출하는 역할.
     *
     * @param joinPoint AOP가 가로챈 대상메서드의 실행 정보를 담고 있는 객체
     * @return          서비스 클래스 명 리턴
     */
    public String getCustomKey(ProceedingJoinPoint joinPoint){
        Object customKeyValue = getParam(joinPoint).get("customKey");

        return Optional.ofNullable(customKeyValue)
                .map(Object::toString)  // .map(value -> value.toString()과 동일
                .orElse("business.metrics");
    }

    /**
     * tagsKey라는 이름의 값을 추출하는 역할.
     *
     * @param joinPoint AOP가 가로챈 대상메서드의 실행 정보를 담고 있는 객체
     * @return          서비스 클래스 명 리턴
     */
    public String getCustomTags(ProceedingJoinPoint joinPoint){
        Object tagValue = getParam(joinPoint).get("tags");

        return Optional.ofNullable(tagValue)
                .map(Object::toString)
                .orElse("");
    }

    /**
     * @MetricsCounter 어노테이션이 붙은 메서드의 실행 전후를 가로채서 비즈니스 지표를 수집.
     * <pre>
     *
     * </pre>
     *
     * @param joinPoint AOP가 가로챈 대상 메서드의 실행 정보를 담고 있는 객체
     * @return 대상 메서드의 반환값
     * @throws Throwable 대상 메서드에서 발생한 예외
     */
    @Around("@annotation(MetricsCounter")
    public Object metricsCounter(ProceedingJoinPoint joinPoint) throws Throwable{

        // joinPoint null check
        if (joinPoint == null) {
            return null;
        }

        // 지표에 사용될 값들을 미리 추출
        final String customKey = getCustomKey(joinPoint);
        final String tags = getCustomTags(joinPoint);
        final String serviceNm = getServiceNm(joinPoint).toString();

        Object result = null;
        try{
            // 대상 메서드를 실항하고 결과 저장
            result = joinPoint.proceed();
        }finally {
            // finally예외 시에도 항상 실행 보장
            // Metrics.counter() 메서드가 TagBuilder패턴 사용 가정
            Metrics.counter(customKey + ".counter.data"
                    , "business_data", tags
                    , "service", serviceNm
                    ).increment();
        }

        return result;
    }

    /**
     * @MetricsSummary 어노테이션이 붙은 메서드의 실행 시간을 요햑 지표로 수집.
     * <pre>
     *
     * </pre>
     *
     * @param joinPoint AOP가 가로챈 대상 메서드의 실행 정보를 담고 있는 객체
     * @return 대상 메서드의 반환값
     * @throws Throwable 대상 메서드에서 발생한 예외
     */
    @Around("@annotation(MetricsSummary")
    public Object metricsSummary(ProceedingJoinPoint joinPoint) throws Throwable{

        // joinPoint null check
        if (joinPoint == null) {
            return null;
        }

        // 지표에 사용될 값들을 미리 추출
        final String customKey = getCustomKey(joinPoint);
        final String tags = getCustomTags(joinPoint);
        final String serviceNm = getServiceNm(joinPoint).toString();

        long startTime = System.currentTimeMillis(); // 메서드 실행 시작 시간 기록
        Object result = null;
        try{
            // 대상 메서드를 실항하고 결과 저장
            result = joinPoint.proceed();
        }finally {
            long duration = System.currentTimeMillis() - startTime; // 실행 시간 계산
            // finally예외 시에도 항상 실행 보장
            // Metrics.summary() 메서드가 시간을 넘겨야 하므로, record()등을 사용 가정
            Metrics.summary(customKey + ".summary.data"
                    , "business_data", tags
                    , "service", serviceNm
            ).record(duration);
        }

        return result;
    }

    // AOP 로직 내부에서 한번만 호출하여 Gauge를 등록
    @PostConstruct  // Spring 애플리케이션 시작 시 한번만 호출되는 예시
    public void initalizeGauge(ProceedingJoinPoint joinPoint) throws Throwable{

        final String customKey = getCustomKey(joinPoint);
        Gauge.builder(customKey + ".gauge.data"
                , this, obj -> processingGauge.doubleValue())
                .register(Metrics.globalRegistry);
    }

    /**
     * @MetricsGauge 어노테이션이 붙은 메서드의 현재 값 지표를 수집.
     * <pre>
     *
     * </pre>
     *
     * @param joinPoint AOP가 가로챈 대상 메서드의 실행 정보를 담고 있는 객체
     * @return 대상 메서드의 반환값
     * @throws Throwable 대상 메서드에서 발생한 예외
     */
    @Around("@annotation(MetricsGauge")
    public Object metricsGauge(ProceedingJoinPoint joinPoint) throws Throwable{

        processingGauge.incrementAndGet();   // 작업 시작 시점, 값 1 증가
        try {
            return joinPoint.proceed();
        }finally {
            processingGauge.decrementAndGet();  // 작업 완료 시점, 값 1 감소
        }
    }

    /**
     * @MetricsTimer 어노테이션이 붙은 메서드 실행 시간 측정.
     * Timer는 실행 시간의 분포, TPS(초당 트랜잭션 수) 등 자세힌 통계 제공
     * <pre>
     *
     * </pre>
     *
     * @param joinPoint AOP가 가로챈 대상 메서드의 실행 정보를 담고 있는 객체
     * @return 대상 메서드의 반환값
     * @throws Throwable 대상 메서드에서 발생한 예외
     */
    @Around("@annotation(MetricsTimer")
    public Object metricsTimer(ProceedingJoinPoint joinPoint) throws Throwable{

        // joinPoint null check
        if (joinPoint == null) {
            return null;
        }

        // 지표에 사용될 값들을 미리 추출
        final String customKey = getCustomKey(joinPoint);
        final String tags = getCustomTags(joinPoint);
        final String serviceNm = getServiceNm(joinPoint).toString();

        long startTime = System.nanoTime(); // 나노초 단위로 정밀 측정

        Object result = null;
        try{
            // 대상 메서드를 실항하고 결과 저장
            result = joinPoint.proceed();
        }finally {
            // finally예외 시에도 항상 실행 보장
            // Metrics.counter() 메서드가 TagBuilder패턴 사용 가정
            long durationNs = System.nanoTime() - startTime;    // 나노초 실행 시간

            Metrics.timer(customKey + ".timer.data"
                    , "business_data", tags
                    , "service", serviceNm
            ).record(durationNs, TimeUnit.NANOSECONDS); // 정확한 나노초 단위로 기록
        }

        return result;
    }
}
