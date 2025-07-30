package com.example.utils.core;

import com.example.exception.CommonException;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.bouncycastle.crypto.digests.Blake2bDigest;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.security.*;
import java.util.Arrays;
import java.util.Base64;

import static org.bouncycastle.util.Arrays.concatenate;

@Slf4j
@UtilityClass
public class EncryptUtils {

    // Initialization Vector needs 16 bytes
    private static final int GCM_IV_LENGTH = 16;
    // GCM authentication tags bits
    private static final int GCM_TAG_LENGTH = 128;
    // Byte length size of Blake2b digest
    private static final int BLAKE2B_DIGEST_BYTE_LENGTH = 6;

    private static byte[] secretKey;

    /**
     * 암호화 키 설정.
     * <pre>
     *     해당 키는 구동 시 fwk에 의해 자동으로 설정되므로 임의 수정 금지.
     * </pre>
     *
     * @param key 암호화 키
     */
    public static void setKey(String key) {secretKey = key.getBytes();}

    /**
     * 단방향 비밀번호 암호화 처리
     * <pre>
     *     EncryptUtils.encrypt("test password !234")
     *     result : f5abf5fa6bb8b2b9232121be40c73e1b46a47daa6b9ae048638ce410a27b5c96
     * </pre>
     *
     * @param password 평문 비밀번호
     * @return 비밀번호 암호화 결과값
     */
    public static String encrypt(String password) {return encryptSha256(password, true, true);}

    /**
     * sha-256을 이용한 단방향 암호화 처리
     * <pre>
     *     ENcryptUtils.encryptSha256("test password !234", true, false)
     *     result : F5ABF5FA6BB8B2B9232121BE40C73E1B46A47DAA6B9AE048638CE410A27B5C96
     * </pre>
     *
     * @param plainText   암호화 대상 평문
     * @param usingSalt   salt 값 사용 여부
     * @param toLowerCase 대소문자 여부, true면 소문자 출력
     * @return            암호화 결과값
     */
    public static String encryptSha256(String plainText, boolean usingSalt, boolean toLowerCase){

        String returnStr = "";
        MessageDigest msgDigest;

        try{
            msgDigest = MessageDigest.getInstance("SHA-256");
            msgDigest.reset();

            if (usingSalt){
                String salt = "SALT";
                msgDigest.update(salt.getBytes());
            }

            byte[] digest = msgDigest.digest(plainText.getBytes());
            returnStr = Hex.encodeHexString(digest, toLowerCase);

        }catch (NoSuchAlgorithmException | RuntimeException ex){
            throw CommonException.builder().message(ex.getMessage()).cause(ex).build();
        }

        return returnStr;
    }

    /**
     * Blake2b를 이용한 단방향 암호화 처리
     * <pre>
     *     EncryptUtils.encryptNumberByBlake2b(1234567890, 16, '6')
     *     result : 1956842033204306
     * </pre>
     *
     * @param plainNumber 암호화 대상 평문 숫자
     * @param digestSize  결과값 길이. 10-16 사이의 숫자
     * @param padChar     결과값 길이를 맞추기 위한 padding character
     * @return            암호화 결과값. tntwk zoflrxm(0-9)로 이루어진 문자열.
     */
    public static String encryptNumberByBlake2b(long plainNumber, int digestSize, char padChar){

        if (digestSize < 10 || digestSize > 16){
            log.error("illegal argument. digestSize : {}", digestSize);
            throw CommonException.builder().message("illegal argument.").build();
        }

        /*
         * 4-byte    -2,147,483,648 to 2,147,483,647
         * 5-byte    -549,755,813,888 to 549,755,813,887
         * 6-byte    -140,737,488,355,328 to 140,737,488,355,327
         * 7-byte    -36,028,797,018,963,968 to 36,028,797,018,963,967
         * 8-byte    -9,223,372,036,854,775,808 to 9,223,372,036,854,775,807
         */
        int digestByteLength = 6;
        if (digestSize < 12) digestByteLength = 4;
        else if (digestSize < 15) digestByteLength = 5;

        String returnStr = "";
        byte[] resultByte = new byte[Long.BYTES];

        try{
            Blake2bDigest msgDigest = new Blake2bDigest(digestByteLength * 8);
            msgDigest.reset();

            String salt = "EXAMPLE_SALT";

            byte[] plainNumberBytes = longToBytes(plainNumber);
            msgDigest.update(plainNumberBytes, 0, plainNumberBytes.length);

            msgDigest.doFinal(resultByte, Long.BYTES - digestByteLength);
            long returnNumber = bytesToLong(resultByte);

            returnStr = longToFixedLengthString(returnNumber, digestSize, padChar);
        }catch (RuntimeException ex){
            throw CommonException.builder().message(ex.getMessage()).cause(ex).build();
        }

        return returnStr;
    }

    /**
     * 양방향 암호화
     * <pre>
     *     EncryptUtils.encryptAES("test password !234")
     *     result :
     * </pre>
     *
     * @param plainText 암호화 대상 평문
     * @return          암호화된 text
     */
    public static final String encryptAES(String plainText){
        // secretKey가 없어서 임시로 만듬
        setKey("A_VALID_AES_KEY_");

        if (plainText != null && !plainText.isEmpty() && secretKey != null){
            try{
                SecretKeySpec keySpec = new SecretKeySpec(secretKey, "AES");
                Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");

                byte[] iv = getRandomIvBytes();
                GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
                cipher.init(Cipher.ENCRYPT_MODE, keySpec, gcmParameterSpec);
                byte[] encrypted = cipher.doFinal(plainText.getBytes());

               return Base64.getEncoder().encodeToString(concatenate(iv, encrypted));

            }catch (NoSuchAlgorithmException |NoSuchPaddingException
                    | InvalidKeyException | InvalidAlgorithmParameterException
                    | IllegalBlockSizeException | BadPaddingException ex){
                throw CommonException.builder().message(ex.getMessage()).cause(ex).build();
            }
        }

        return plainText;
    }

    /**
     * 양방향 복호화
     * <pre>
     *     EncryptUtils.decryptAES("NjzBxRrKmLfYg/gQE1HM418y7Iw+/7vK/mwpgYoGJeTmSYbxsM1+LJ4MJrow2rha1Zg=")
     *     result : test password !234
     * </pre>
     *
     * @param cipherText 암호화 되어있는 문자열
     * @return           복호화된 text
     */
    public static final String decryptAES(String cipherText){
        // secretKey가 없어서 임시로 만듬
        setKey("A_VALID_AES_KEY_");

        if (cipherText != null && !cipherText.isEmpty() && secretKey != null) {
           try{
               // cipherText에서 iv와 암호화된 text 분리
               byte[] ciphered = Base64.getDecoder().decode(cipherText.getBytes());
               // ciphered에 16자리를 복사해 iv 생성
               // 암호화 모드에서 사용되는 난수 값(같은 평문과 키 사용해도 매번 다른 암호문 생성되어 패턴 노출 막음
               byte[] iv = Arrays.copyOfRange(ciphered, 0, GCM_IV_LENGTH);
               // ciphered에 128자리를 복사해 encrypted 생성
               byte[] encrypted = Arrays.copyOfRange(ciphered, GCM_IV_LENGTH, ciphered.length);

               // secretKey 바이트 배열을 "AES"알고리즘에 사용할 키로 지정.
               // secretKey의 길이가 AES 표준(16, 24, 32byte)을 따라야 함
               SecretKeySpec keySpec = new SecretKeySpec(secretKey, "AES");
               // Cipher 객체 생성: Cipher.getInstance("알고리즘/모드/패딩")
               // 알고리즘: 사용할 암호화 알고리즘("AES", "DES", "RSA"...)
               // 모드: ECB(Electronic Codebook) - 가장단순, 보안취약 (동일한 평문은 항상 동일한 암호문으로 변환)
               //       CBC(Cipher Block Chaining) - 이전 블록 암호문이 다음 블록 암호화에 영향을 줘 보안강화, 초기화 벡터(IV) 필요
               //       GCM(Galois/Counter Mode) - 데이터 기밀, 무결성, 인증까지 동시에 제공, 매우 강력한 현대적인 모드
               // 패딩 : 데이터를 블록 단위 처리 시, 정해진 블록 크기보다 작을 경우 어떻게 채울지에 대한 규칙
               //       NoPadding: 패딩 하지 않음
               //       PKCS5Padding/KCS7Padding: 가장 흔하게 사용 되는 패딩 방식.
               Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
               // 생성할 인증 태그의 길이와 초기화 벡터 값을 Cipher에게 전달
               GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
               // 암호화 모드, key, iv 및 태그 길이를 포함해 Cipher 초기화
               cipher.init(Cipher.DECRYPT_MODE, keySpec, gcmParameterSpec);

               // doFinal() 호출하여 실제 암복호화 수행
               return new String(cipher.doFinal(encrypted));

           }catch (NoSuchAlgorithmException |NoSuchPaddingException
                   | InvalidKeyException | InvalidAlgorithmParameterException
                   | IllegalBlockSizeException | BadPaddingException ex){
               throw CommonException.builder().message(ex.getMessage()).cause(ex).build();
           }
        }

        return cipherText;
    }

    // GCM_IV_LENGTH 길이의 secure random bytes 생성
    private static byte[] getRandomIvBytes(){
        SecureRandom random = new SecureRandom();
        byte[] iv = new byte[GCM_IV_LENGTH];
        random.nextBytes(iv);

        return iv;
    }

    // long을 byte array로 변환
    private static byte[] longToBytes(long num){
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.putLong(num);

        return buffer.array();
    }

    // byte array를 long으로 변환
    private static long bytesToLong(byte[] bytes){
        ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES);
        buffer.put(bytes);
        buffer.rewind();

        return buffer.getLong();
    }

    // long을 입력된 자릿수의 unsigned string으로 변환
    private static String longToFixedLengthString(long src, int length, char padChar){
        String convertStr = Long.toUnsignedString(src);
        int strLength = convertStr.length();
        StringBuilder strBuilder = new StringBuilder(convertStr);

        for (int i = strLength; i < length; i++) {
            strBuilder.append(padChar);
        }

        return strBuilder.toString();
    }
}
