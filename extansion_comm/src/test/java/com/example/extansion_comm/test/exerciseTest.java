package com.example.extansion_comm.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.regex.Pattern;

@Slf4j
@Execution(ExecutionMode.CONCURRENT)
public class exerciseTest {

    @Test
    void makeProgramPath(){

        // 기존 경로
        String routePath = "api/v1/users";
        // 대상 경로
        String apiPath = "/v1/users/profile";


        // 각 path를 배열로 분해
        // Pattern.quote는 정규식으로 받아지지 않음
        String[] tmpRt = routePath.split(Pattern.quote("/"));
        String[] tmpAt = apiPath.split(Pattern.quote("/"));
        StringBuilder resultPath = new StringBuilder();


        // routePath 끝에서 부터 apiPath와 비교
        int depth = 0;
        int curIdx = 0;
        for (int i = 0; i < tmpRt.length; i++) {
            if (tmpRt[i].equals(tmpAt[depth])){
                curIdx = depth;
                break;
            }
//            resultPath.append(tmpRt[i]);

            log.debug("for_tmpRt_{} : {}", i, tmpRt[i]);
            log.debug("for_tmpA_{} : {}", depth, tmpAt[depth]);
            depth++;
        }

        log.debug("curIdx : {}", curIdx);



        // route끝에 api앞에 "/"가 있는 경우 한쪽만 "/" 살리기

        // 같은 부분의 배열[i]를 routePath + "/" + apiPath
        for (int i = 0; i < tmpRt.length - curIdx; i++) {
            resultPath.append(tmpRt[i]).append("/");
        }

        if (tmpRt[tmpRt.length-1].equals(Pattern.quote("/")) && tmpAt[0].equals(Pattern.quote("/"))){
            tmpAt = Arrays.copyOfRange(tmpAt, curIdx+1, tmpAt.length);
        }
        resultPath.append(String.join("/", tmpAt));

//        log.debug("resultPath : {}", resultPath.toString());

        // return
        log.debug("resultPath : {}", resultPath);
    }
}
