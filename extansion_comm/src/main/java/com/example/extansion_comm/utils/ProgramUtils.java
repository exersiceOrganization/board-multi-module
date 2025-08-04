package com.example.extansion_comm.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@UtilityClass
public class ProgramUtils {

    private static final String PATH_SEPERATOR = "/";
    // 처음, 끝에 "/"를 찾아주는 정규표현식
    private static final Pattern CLEAN_SLASHES = Pattern.compile("^/+|/+$");

    /**
     * 배열을 원하는 크기로 복사.
     * <pre>
     *      ProgranUtils.slice({program, utils, slice}, 1, 3);
     *      result : {utils, slice}
     * </pre>
     *
     * @param strArr    원본 배열
     * @param start     시작 index
     * @param end       종료 index
     * @return          복사한 배열 리턴
     */
    private static String[] slice(String[] strArr, int start, int end){
        // end가 배열길이 초과 시 배열 길이로 방어
        if (strArr.length < end) end = strArr.length;

        return Arrays.copyOfRange(strArr, start, end);
    }

    /**
     * Program의 apiPath와 routePath를 결합해 programPath 생성
     * 요건에 따라 처음이나 끝에 Pattern.quote(PATH_SEPERATOR) 생성 또는 삭제 가능
     * <pre>
     *     ProgramUtils.makeProgramPath("/v1/users/profile/", "/api/v1/users/");
     *     result : "/api/v1/users/profile"
     * </pre>
     *
     * @param apiPath   api 경로
     * @param routePath route 경로
     * @return          결합한 값 리턴
     */
    public static String makeProgramPath(String apiPath, String routePath){
        Assert.notNull(apiPath, "apiPath must be not null");
        Assert.notNull(routePath, "routePath must be not null");

        // 각 path를 배열로 분해
        // Pattern.quote는 정규식으로 받아지지 않음
        String[] tmpRt = routePath.split(PATH_SEPERATOR);
        String[] tmpAt = apiPath.split(PATH_SEPERATOR);

        // api가 Pattern.quote(PATH_SEPERATOR)로 시작하는지 확인
        if (apiPath.startsWith(PATH_SEPERATOR)){
            tmpAt = Arrays.copyOfRange(tmpAt, 1, tmpAt.length);
        }

        // 겹치는 부분 찾기
        int curIdx = 0;
        int minLen = Math.min(tmpRt.length, tmpAt.length);

        for (int i = 0; i <= minLen; i++) {
            String[] routeEnd = Arrays.copyOfRange(tmpRt, tmpRt.length - i, tmpRt.length);
            String[] apiStart = Arrays.copyOfRange(tmpAt, 0, i);

            if (Arrays.equals(routeEnd, apiStart)) curIdx = i;
        }

        // 안겹친 route 가져오기
        String[] nonMatchRoute = Arrays.copyOfRange(tmpRt, 0, tmpRt.length - curIdx);
        // 문자열 결합
        String programPath = String.join(PATH_SEPERATOR, nonMatchRoute) + PATH_SEPERATOR + String.join(PATH_SEPERATOR, tmpAt);

        // 경로중 "//"일 경우 Pattern.quote(PATH_SEPERATOR)로 변경
        if (programPath.contains("//")) programPath = programPath.replaceAll("//", PATH_SEPERATOR);

        return programPath;
    }

    /**
     * uri를 받아 url 생성.
     * <pre>
     *     ProgramUtils.getTestUrl({"http://api.example.com", "v1", "/users/");
     *     result : http://api.example.com/v1/users
     * </pre>
     *
     * @param uri string...으로 받은 uri
     * @return    url로 변환 후 리턴
     */
    public static String getTestUrl(String... uri) {

        return Arrays.stream(uri)
                // null check
                .filter(s -> s != null && !s.trim().isEmpty())
                // 문자열 앞 뒤에 "/"찾아 ""로 변환
                .map(s -> CLEAN_SLASHES.matcher(s).replaceAll(""))
                // String.join이랑 같은 기능
                .collect(Collectors.joining("/"));
    }
}
