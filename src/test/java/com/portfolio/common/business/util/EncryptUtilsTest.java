package com.portfolio.common.business.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import javax.crypto.AEADBadTagException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.zip.CRC32;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
public class EncryptUtilsTest {
    // 테스트 전체에서 사용할 공통 변수들
    private static SecretKey aesKey;
    private static KeyPair rsaKeyPair;
    private static PublicKey rsaPublicKey;
    private static PrivateKey rsaPrivateKey;
    private static final String PLAIN_TEXT = "이것은 투자증권 업무 공통 모듈 테스트를 위한 평문입니다. (This is a plain text for testing.)";
    private static final String PASSWORD = "MyStrongPassword123!@#";

    @TempDir
    static Path tempDir; // 임시 파일 및 디렉토리를 생성하기 위한 JUnit 5 기능

    /**
     * 모든 테스트가 실행되기 전에 한 번만 호출되어, 테스트에 필요한 키들을 미리 생성합니다.
     * @throws Exception 키 생성 실패 시
     */
    @BeforeAll
    static void setUp() throws Exception {
        // AES 키 생성
        aesKey = EncryptUtils.generateAESKey();  // AES-256 비밀키를 생성
        // RSA 키 쌍 생성
        rsaKeyPair = EncryptUtils.generateRSAKeyPair(); // RSA 공개키/개인키 쌍을 생성
        rsaPublicKey = rsaKeyPair.getPublic();
        rsaPrivateKey = rsaKeyPair.getPrivate();

        assertNotNull(aesKey);
        assertNotNull(rsaKeyPair);
        assertNotNull(rsaPublicKey);
        assertNotNull(rsaPrivateKey);
//        log.info("aesKey : {}", aesKey);
//        log.info("rsaKeyPair : {}", rsaKeyPair);
//        log.info("rsaPublicKey : {}", rsaPublicKey);
//        log.info("rsaPrivateKey : {}", rsaPrivateKey);
    }
    
    @Test
    @DisplayName("AES(CBC) 암호화 및 복호화 테스트")
    void testAesCbcEncryptionDecryption() throws Exception{
        // given
        IvParameterSpec iv = EncryptUtils.generateIv();
        // when
        String aesEncrypt = EncryptUtils.aesEncrypt(PLAIN_TEXT, aesKey, iv);
        // then
        assertNotNull(iv);
        log.info("aesEncrypt : {}", EncryptUtils.aesEncrypt(PLAIN_TEXT, aesKey, iv));
        log.info("aesDecrypt : {}", EncryptUtils.aesDecrypt(aesEncrypt, aesKey, iv));
    }
    @Test
    @DisplayName("AES 키 인코딩 및 디코딩 테스트")
    void testAesKeyEncoding(){
        // given
        String encodeAESKeyToBase64 = EncryptUtils.encodeAESKeyToBase64(aesKey);
        // when, then
        log.info("encodeAESKeyToBase64 : {}", encodeAESKeyToBase64);
        log.info("decodeAESKeyFromBase64 : {}", EncryptUtils.decodeAESKeyFromBase64(encodeAESKeyToBase64));
        assertEquals(aesKey, EncryptUtils.decodeAESKeyFromBase64(encodeAESKeyToBase64));
    }
    @Test
    @DisplayName("RSA 암호화 및 복호화 테스트")
    void testRsaEncryptionDecryption() throws Exception{
        // given
        String rsaEncrypt = EncryptUtils.rsaEncrypt(PLAIN_TEXT, rsaPublicKey);
        // when, then
        log.info("rsaEncrypt : {}", rsaEncrypt);
        log.info("rsaDecrypt : {}", EncryptUtils.rsaDecrypt(rsaEncrypt, rsaPrivateKey));
    }
    @Test
    @DisplayName("RSA 키 인코딩 및 디코딩 테스트")
    void testRsaKeyEncoding() throws Exception{
        // given
        String encodePublicKeyToBase64 = EncryptUtils.encodePublicKeyToBase64(rsaPublicKey);
        String encodePrivateKeyToBase64 = EncryptUtils.encodePrivateKeyToBase64(rsaPrivateKey);
        // when, then
        log.info("encodePublicKeyToBase64 : {}", encodePublicKeyToBase64);
        log.info("encodePrivateKeyToBase64 : {}", encodePrivateKeyToBase64);
        log.info("decodePublicKeyFromBase64 : {}", EncryptUtils.decodePublicKeyFromBase64(encodePublicKeyToBase64));
        log.info("decodePrivateKeyFromBase64 : {}", EncryptUtils.decodePrivateKeyFromBase64(encodePrivateKeyToBase64));
        assertEquals(rsaPublicKey, EncryptUtils.decodePublicKeyFromBase64(encodePublicKeyToBase64));
        assertEquals(rsaPrivateKey, EncryptUtils.decodePrivateKeyFromBase64(encodePrivateKeyToBase64));
    }
    @Test
    @DisplayName("해시(SHA) 함수 테스트")
    void testHashFunctions() throws Exception{
        // given
        String sha256 = EncryptUtils.sha256(PLAIN_TEXT);
//        String sha1 = EncryptUtils.sha1(PLAIN_TEXT);  // warning으로 주석처리
        String sha384 = EncryptUtils.sha384(PLAIN_TEXT);
        String sha512 = EncryptUtils.sha512(PLAIN_TEXT);
        // when, then
        log.info("sha256 : {}", sha256);
//        log.info("sha1 : {}", sha1);
        log.info("sha384 : {}", sha384);
        log.info("sha512 : {}", sha512);
        assertEquals(64, sha256.length());  // SHA-256은 64자리 hex 문자열
//        assertEquals(40, sha1.length());  // SHA-1은 40자리 hex 문자열
        assertEquals(96, sha384.length());  // SHA-384는 96자리 hex 문자열
        assertEquals(128, sha512.length());  // SHA-512는 128자리 hex 문자열
    }
    @Test
    @DisplayName("솔트(Salt)를 사용한 해시 및 검증 테스트")
    void testHashingWithSalt() throws Exception{
        // given
        byte[] salt = EncryptUtils.generateSalt(16);
        String sha256 = EncryptUtils.sha256(PLAIN_TEXT);
        // when
        String sha256WithSalt = EncryptUtils.sha256WithSalt(PLAIN_TEXT, salt);
        // then
        log.info("salt : {}", salt);
        log.info("sha256 : {}", sha256);
        log.info("sha256WithSalt : {}", sha256WithSalt);
        log.info("verifySha256 : {}", EncryptUtils.verifySha256(PLAIN_TEXT, sha256));
        log.info("verifySha256 : {}", EncryptUtils.verifySha256WithSalt(PLAIN_TEXT, sha256WithSalt, salt));
        assertNotNull(sha256);
        assertNotNull(sha256WithSalt);
        assertTrue(EncryptUtils.verifySha256(PLAIN_TEXT, sha256));
        assertTrue(EncryptUtils.verifySha256WithSalt(PLAIN_TEXT, sha256WithSalt, salt));
    }
    @Test
    @DisplayName("HMAC 생성 및 검증 테스트")
    void testHmacFunctions() throws Exception{
        // given
        String key = "my-secret-hmac-key";
        String hmac256 = EncryptUtils.generateHmacSha256(PLAIN_TEXT, key);
        // when
        boolean verifyHmacSha256 = EncryptUtils.verifyHmacSha256(PLAIN_TEXT, key, hmac256);
        boolean verifyHmacWithAlgorithm = EncryptUtils.verifyHmacWithAlgorithm(PLAIN_TEXT, key, hmac256, "HmacSHA256");
        // then
        log.info("verifyHmacSha256 : {}", verifyHmacSha256);
        log.info("verifyHmacWithAlgorithm : {}", verifyHmacWithAlgorithm);
        assertTrue(verifyHmacSha256);
        assertTrue(verifyHmacWithAlgorithm);
    }
    @Test
    @DisplayName("Base64 인코딩 및 디코딩 테스트")
    void testBase64Encoding(){
        // given
        String urlSafeePlainText = "query=value&param=value+/=";
        byte[] textToBytes = PLAIN_TEXT.getBytes(StandardCharsets.UTF_8);
        // when
        String encoded = EncryptUtils.base64Encode(PLAIN_TEXT);
        String decoded = EncryptUtils.base64Decode(encoded);
        String urlSafeEncoded = EncryptUtils.base64UrlSafeEncode(urlSafeePlainText);
        String urlSafeDecoded = EncryptUtils.base64UrlSefeDecode(urlSafeEncoded);
        String base64EncodeBytes = EncryptUtils.base64EncodeBytes(textToBytes);
        byte[] base64DecodeToBytes = EncryptUtils.base64DecodeToBytes(base64EncodeBytes);
        // then
        log.info("PLAIN_TEXT : {}", PLAIN_TEXT);
        log.info("encoded : {}", encoded);
        log.info("decoded : {}", decoded);
        log.info("urlSafeEncoded : {}", urlSafeEncoded);
        log.info("urlSafeePlainText : {}", urlSafeePlainText);
        log.info("urlSafeDecoded : {}", urlSafeDecoded);
        log.info("base64EncodeBytes : {}", base64EncodeBytes);
        log.info("textToBytes : {}", textToBytes);
        log.info("base64DecodeToBytes : {}", EncryptUtils.base64DecodeToBytes(base64EncodeBytes));
        assertEquals(PLAIN_TEXT, decoded);
        assertEquals(urlSafeePlainText, urlSafeDecoded);
        assertArrayEquals(textToBytes, base64DecodeToBytes);
    }
    @Test
    @DisplayName("디지털 서명 생성 및 검증 테스트")
    void testDigitalSignature() throws Exception{
        // given
        String signature = EncryptUtils.createDigitalSignature(PLAIN_TEXT, rsaPrivateKey);
        // when
        boolean checkSignature = EncryptUtils.verifyDigitalSignature(PLAIN_TEXT, signature, rsaPublicKey);
        // then
        log.info("checkSignature : {}", checkSignature);
    }
    @Test
    @DisplayName("PBKDF2 비밀번호 해싱 및 검증 테스트")
    void testPbkdf2PasswordHashing() throws Exception{
        // given
        byte[] salt = EncryptUtils.generateSalt(16);
        SecretKey generateKeyFromPassword1 = EncryptUtils.generateKeyFromPassword(PASSWORD, salt, 16);
        SecretKey generateKeyFromPassword2 = EncryptUtils.generateKeyFromPassword(PASSWORD, salt, 16);
        // when
        String hashedPassword = EncryptUtils.hashPasswordWithPBKDF2(PASSWORD, salt);
        boolean passwordCheck = EncryptUtils.verifyPasswordWithPBKDF2(PASSWORD, hashedPassword, salt);
        // then
        log.info("hashedPassword : {}", hashedPassword);
        log.info("passwordCheck : {}", passwordCheck);
        assertTrue(passwordCheck);
        assertEquals(generateKeyFromPassword1, generateKeyFromPassword2);
    }
    @Test
    @DisplayName("AES(GCM) 암호화 및 복호화 테스트")
    void testAesGcmEncryptionDecryption() throws Exception{
        // given
        byte[] iv = EncryptUtils.generateGcmIv();

        // when
        String encryptedText = EncryptUtils.aesEncryptGCM(PLAIN_TEXT, aesKey, iv);
        String decryptedText = EncryptUtils.aesDecryptGCM(encryptedText, aesKey, iv);
        String tamperedCipherText = encryptedText.substring(0, encryptedText.length() - 2) + "AA";  // 암호문 변조
        // then
        log.info("encryptedText : {}", encryptedText);
        log.info("decryptedText : {}", decryptedText);
        log.info("tamperedCipherText : {}", tamperedCipherText);
        assertEquals(PLAIN_TEXT, decryptedText);
        assertThrows(AEADBadTagException.class, () -> EncryptUtils.aesDecryptGCM(tamperedCipherText, aesKey, iv));
    }
    @Test
    @DisplayName("파일 암호화 및 복호화 테스트")
    void testFileEncryptionDecryption() throws Exception{
        // given
        File originalFile = tempDir.resolve("original.txt").toFile();
        File encryptedFile = tempDir.resolve("encrypted.bin").toFile();
        File decryptedFile = tempDir.resolve("decrypted.txt").toFile();
        Files.write(originalFile.toPath(), PLAIN_TEXT.getBytes(StandardCharsets.UTF_8));

        IvParameterSpec iv = EncryptUtils.generateIv();
        // when
        EncryptUtils.encryptFile(originalFile, encryptedFile, aesKey, iv);
        EncryptUtils.decryptFile(encryptedFile, decryptedFile, aesKey, iv);
        String decryptedFileContent = new String(Files.readAllBytes(decryptedFile.toPath()), StandardCharsets.UTF_8);
        // then
        log.info("encryptedFile  : {}", encryptedFile );
        log.info("decryptedFile  : {}", decryptedFile );
        log.info("decryptedFileContent  : {}", decryptedFileContent );
    }
    @Test
    @DisplayName("AES 편의성 래퍼(Wrapper) 테스트")
    void testAesConvenienceWrapper() throws Exception{
        // given

        // when
        String combined = EncryptUtils.aesEncryptWithCombinedIv(PLAIN_TEXT, aesKey);
        String decrypted = EncryptUtils.aesDecryptWithCombinedIv(combined, aesKey);
        // then
        log.info("combined : {}", combined);
        log.info("decrypted : {}", decrypted);
    }
    @Test
    @DisplayName("키 파일 저장 및 로드 테스트")
    void testKeyFileStorage() throws Exception{
        // given
        Path aesKeyPath = tempDir.resolve("my.aes.key");
        EncryptUtils.saveKeyToFile(aesKey, aesKeyPath.toString());
        SecretKey loadedAesKey = EncryptUtils.loadAESKeyFromFile(aesKeyPath.toString());
        Path pubKeyPath = tempDir.resolve("my.rsa.pub");
        EncryptUtils.saveKeyToFile(rsaPublicKey, pubKeyPath.toString());
        PublicKey loadedPublicKey = EncryptUtils.loadPublicKeyFromFile(pubKeyPath.toString());
        Path privKeyPath = tempDir.resolve("my.rsa.key");
        EncryptUtils.saveKeyToFile(rsaPrivateKey, privKeyPath.toString());
        PrivateKey loadedPrivateKey = EncryptUtils.loadPrivateKeyFromFile(privKeyPath.toString());
        // when
    
        // then
        log.info("loadedAesKey : {}", loadedAesKey);
        log.info("loadedPublicKey : {}", loadedPublicKey);
        log.info("loadedPrivateKey : {}", loadedPrivateKey);
        assertEquals(aesKey, loadedAesKey);
        assertEquals(rsaPublicKey, loadedPublicKey);
        assertEquals(rsaPrivateKey, loadedPrivateKey);
    }
    @Test
    @DisplayName("추가 유틸리티 함수 테스트")
    void testAdditionalUtilities() throws IOException, ClassNotFoundException {
        // given
        String uuid = EncryptUtils.generateUUID();
        byte[] bytes = PLAIN_TEXT.getBytes(StandardCharsets.UTF_8);
        // UUID 정규식 패턴 검증
        Pattern uuidPattern = Pattern.compile("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");
        // when
        String hexString = EncryptUtils.bytesToHexString(bytes);
        byte[] convertedBytes = EncryptUtils.hexStringToBytes(hexString);
        // then
        log.info("convertedBytes : {}", convertedBytes);
        log.info("uuidPatternCheck : {}", uuidPattern.matcher(uuid).matches());
    }
    @Test
    @DisplayName("무작위 비밀번호 생성 테스트")
    void testGenerateRandomPassword(){
        // given
        int passwordLength = 12;
        // when
        String password = EncryptUtils.generateRandomPassword(passwordLength);

        // then
        Pattern pattern = Pattern.compile("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{}\\\\|;:,.<>/?]).+$");
        log.info("password : {}", password);
        log.info("patternCheck : {}", pattern.matcher(password).matches());
        //  길이가 8 미만일 때 예외가 발생하는지 확인
        assertThrows(IllegalArgumentException.class, () -> {
            EncryptUtils.generateRandomPassword(7);
        });
    }
    @Test
    @DisplayName("CRC32 체크섬 계산 테스트")
    void testCalculateCRC32(){
        // given
        String testData = "Hello, World!";
        String testData2 = "Hello, World!\n";
        log.info("testData.length() : {}", testData.length());
        byte[] dataBytes = testData.getBytes(StandardCharsets.UTF_8);
        byte[] dataBytes2 = testData2.getBytes(StandardCharsets.UTF_8);
        log.info("바이트 배열의 길이: {}", dataBytes.length);
        log.info("HEX: {}", Arrays.toString(testData.getBytes()));
        log.info("dataBytes: {}", Arrays.toString(dataBytes));
        log.info("dataBytes2: {}", Arrays.toString(dataBytes2));
        String convert = new String(dataBytes, StandardCharsets.UTF_8);
        log.info("convert : {}", convert);
        log.info("CRC32 impl class: {}", new CRC32().getClass().getName());
        long expectedCRC32Value = 1095204569L;
        // when
        long actualCRC32Value = EncryptUtils.calculateCRC32(dataBytes);
        long actualCRC32Value2 = EncryptUtils.calculateCRC32(dataBytes2);
        // then
        log.info("actualCRC32Value : {}", actualCRC32Value);
        log.info("actualCRC32Value2 : {}", actualCRC32Value2);
        assertEquals(expectedCRC32Value, actualCRC32Value);
    }
}
