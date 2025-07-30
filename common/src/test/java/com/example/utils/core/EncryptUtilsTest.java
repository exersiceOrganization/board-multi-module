package com.example.utils.core;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@Slf4j
@Execution(ExecutionMode.CONCURRENT)
public class EncryptUtilsTest {

    @Test
    void testResult(){
        log.debug("encrypt : {}", EncryptUtils.encrypt("test password !234"));
        log.debug("encryptSha256 : {}", EncryptUtils.encryptSha256("test password !234", true, false));
        log.debug("encryptNumberByBlacke2b : {}", EncryptUtils.encryptNumberByBlake2b(1234567890, 16, '6'));
        log.debug("encryptAES : {}", EncryptUtils.encryptAES("test password !234"));
        log.debug("decryptAES : {}", EncryptUtils.decryptAES("Yb4tXCQwJ6eQBbUd4wXVrgzDrt81BrVlp8YJi6gbQ7ott/jNo8rIj9zaZzWLlYezkI8="));
    }

    @Test
    void testEncryptNumberByBlake2b() {
        long plainNumber = 1234567890;
        String encryptedNumber = EncryptUtils.encryptNumberByBlake2b(plainNumber, 16, '6');
        log.debug("[16] encryptedNumber : {}", encryptedNumber);
        assertEquals(16, encryptedNumber.length());

        plainNumber = 5834873478L;
        encryptedNumber = EncryptUtils.encryptNumberByBlake2b(plainNumber, 10, '0');
        log.debug("[10] encryptedNumber : {}", encryptedNumber);
        assertEquals(10, encryptedNumber.length());

        plainNumber = 5834873478L;
        encryptedNumber = EncryptUtils.encryptNumberByBlake2b(plainNumber, 11, '1');
        log.debug("[11] encryptedNumber : {}", encryptedNumber);
        assertEquals(11, encryptedNumber.length());

        plainNumber = 5834873478L;
        encryptedNumber = EncryptUtils.encryptNumberByBlake2b(plainNumber, 12, '2');
        log.debug("[13] encryptedNumber : {}", encryptedNumber);
        assertEquals(13, encryptedNumber.length());

        plainNumber = 58348734784589534L;
        encryptedNumber = EncryptUtils.encryptNumberByBlake2b(plainNumber, 14, '4');
        log.debug("[14] encryptedNumber : {}", encryptedNumber);
        assertEquals(14, encryptedNumber.length());

        plainNumber = 58348734784589534L;
        encryptedNumber = EncryptUtils.encryptNumberByBlake2b(plainNumber, 15, '5');
        log.debug("[15] encryptedNumber : {}", encryptedNumber);
        assertEquals(15, encryptedNumber.length());
    }
}
