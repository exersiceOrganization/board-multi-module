package com.example.extansion_comm.test;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

@Slf4j
@Execution(ExecutionMode.CONCURRENT)
public class ProgramUtils {

    @Test
    void makeProgramPath() {

        // 기존 경로
        String routePath = "/api/v1/users/";
        // 대상 경로
        String apiPath = "/v1/users/profile/";

        String resultPath = com.example.extansion_comm.utils.ProgramUtils.makeProgramPath(apiPath, routePath);
        log.debug("resutPath : {}", resultPath);
    }

    @Test
    void TestUri(){
        String[] uri = {
                "http://api.example.com/",
                "/v1",
                "/users/"
        };

        String result = com.example.extansion_comm.utils.ProgramUtils.getTestUrl(uri);
        log.debug("result : {}", result.toString());

    }
}
