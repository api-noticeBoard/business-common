package com.portfolio.common.business.util;

import lombok.experimental.UtilityClass;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.UUID;

import static com.portfolio.common.business.util.StringUtils.bytesToHexString;

/**
 * 암호화 관련 유틸리티 클래스.
 * AES, RSA, SHA, HMAC, Base64 등 다양한 암호화/인코딩 기능을 제공.
 */
@UtilityClass
public class EncryptUtils {
    private static final String AES_ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final String RSA_ALGORITHM = "RSA/ECB/PKCS1Padding";
    private static final int AES_KEY_SIZE = 256;
    private static final int RSA_KEY_SIZE = 2048;
    private static final String HASH_ALGORITHM = "SHA-256";
    private static final String HMAC_ALGORITHM = "HmacSHA256";
    private static final String PBKDF2_ALGORITHM = "PBKDF2WithHmacSHA256";
    private static final int PBKDF2_ITERATIONS = 65536;
    private static final String AES_GCM_ALGORITHM = "AES/GCM/NoPadding";
    private static final int GCM_IV_LENGTH = 12; // 96 bits, GCM 권장 IV 길이
    private static final int GCM_TAG_LENGTH = 128; // 인증 태그 길이(bits)

    // --- 대칭키 암호화 (AES) ---
    /**
     * AES-256 비밀키를 생성.
     * @return 생성된 SecretKey 객체
     * @throws NoSuchAlgorithmException 암호화 알고리즘이 지원되지 않을 경우 발생
     */
    public static SecretKey generateAESKey() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(AES_KEY_SIZE);
        return keyGenerator.generateKey();
    }

    /**
     * 2. 주어진 비밀키(SecretKey)를 Base64 문자열로 인코딩.
     * @param secretKey Base64로 인코딩할 SecretKey 객체
     * @return Base64로 인코딩된 문자열 키
     */
    public static String encodeAESKeyToBase64(SecretKey secretKey) {
        return Base64.getEncoder().encodeToString(secretKey.getEncoded());
    }

    /**
     * Base64로 인코딩된 문자열을 SecretKey 객체로 디코딩.
     * @param encodedKey Base64로 인코딩된 문자열 키
     * @return 디코딩된 SecretKey 객체
     */
    public static SecretKey decodeAESKeyFromBase64(String encodedKey) {
        byte[] decodedkey = Base64.getDecoder().decode(encodedKey);
        return new SecretKeySpec(decodedkey, 0, decodedkey.length, "AES");
    }

    /**
     * 16바이트(128비트) 초기화 벡터(IV)를 생성. (AES/CBC 모드용)
     * @return 생성된 IvParameterSpec 객체
     */
    public static IvParameterSpec generateIv() {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);
    }

    /**
     * 5. 주어진 초기화 벡터(IvParameterSpec)를 Base64 문자열로 인코딩.
     * @param iv Base64로 인코딩할 IvParameterSpec 객체
     * @return Base64로 인코딩된 문자열 IV
     */
    public static String encodeIvToBase64(IvParameterSpec iv) {
        return Base64.getEncoder().encodeToString(iv.getIV());
    }

    /**
     * 6. Base64로 인코딩된 문자열을 IvParameterSpec 객체로 디코딩합니다.
     * @param encodedIv Base64로 인코딩된 문자열 IV
     * @return 디코딩된 IvParameterSpec 객체
     */
    public static IvParameterSpec decodeIvFromBase64(String encodedIv) {
        byte[] decodedIv = Base64.getDecoder().decode(encodedIv);
        return new IvParameterSpec(decodedIv);
    }

    /**
     * 7. AES/CBC/PKCS5Padding 알고리즘을 사용하여 평문을 암호화합니다.
     * @param plainText 암호화할 평문
     * @param key 암호화에 사용할 SecretKey
     * @param iv 암호화에 사용할 초기화 벡터(IV)
     * @return Base64로 인코딩된 암호문
     * @throws Exception 암호화 과정에서 오류 발생 시
     */
    public static String aesEncrypt(String plainText, SecretKey key, IvParameterSpec iv) throws Exception {
        Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    /**
     * 8. AES/CBC/PKCS5Padding 알고리즘을 사용하여 암호문을 복호화합니다.
     * @param cipherText Base64로 인코딩된 암호문
     * @param key 복호화에 사용할 SecretKey
     * @param iv 복호화에 사용할 초기화 벡터(IV)
     * @return 복호화된 평문
     * @throws Exception 복호화 과정에서 오류 발생 시
     */
    public static String aesDecrypt(String cipherText, SecretKey key, IvParameterSpec iv) throws Exception {
        Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key, iv);
        byte[] decodedBytes = Base64.getDecoder().decode(cipherText);
        byte[] decryptedBytes = cipher.doFinal(decodedBytes);
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

    // --- 2. 비대칭키 암호화 (RSA) --
    /**
     * 9. RSA 공개키/개인키 쌍을 생성합니다.
     * @return 생성된 KeyPair 객체
     * @throws NoSuchAlgorithmException 암호화 알고리즘이 지원되지 않을 경우 발생
     */
    public static KeyPair generateRSAKeyPari() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(RSA_KEY_SIZE);
        return keyPairGenerator.generateKeyPair();
    }

    /**
     * 10. 주어진 공개키(PublicKey)를 Base64 문자열로 인코딩합니다.
     * @param publicKey Base64로 인코딩할 PublicKey 객체
     * @return Base64로 인코딩된 문자열 공개키
     */
    public static String encodePublicKeyToBase64(PublicKey publicKey) {
        return Base64.getEncoder().encodeToString(publicKey.getEncoded());
    }

    /**
     * 11. 주어진 개인키(PrivateKey)를 Base64 문자열로 인코딩합니다.
     * @param privateKey Base64로 인코딩할 PrivateKey 객체
     * @return Base64로 인코딩된 문자열 개인키
     */
    public static String encodePrivateKeyToBase64(PrivateKey privateKey) {
        return Base64.getEncoder().encodeToString(privateKey.getEncoded());
    }

    /**
     * 12. Base64로 인코딩된 문자열을 PublicKey 객체로 디코딩합니다.
     * @param encodedPublicKey Base64로 인코딩된 문자열 공개키
     * @return 디코딩된 PublicKey 객체
     * @throws Exception 디코딩 과정에서 오류 발생 시
     */
    public static PublicKey decodePublicKeyFromBase64(String encodedPublicKey) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(encodedPublicKey);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(spec);
    }

    /**
     * 13. Base64로 인코딩된 문자열을 PrivateKey 객체로 디코딩합니다.
     * @param encodedPrivateKey Base64로 인코딩된 문자열 개인키
     * @return 디코딩된 PrivateKey 객체
     * @throws Exception 디코딩 과정에서 오류 발생 시
     */
    public static PrivateKey decodePrivateKeyFromBase64(String encodedPrivateKey) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(encodedPrivateKey);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(spec);
    }

    /**
     * 14. RSA 공개키를 사용하여 평문을 암호화합니다.
     * @param plainText 암호화할 평문
     * @param publicKey 암호화에 사용할 PublicKey
     * @return Base64로 인코딩된 암호문
     * @throws Exception 암호화 과정에서 오류 발생 시
     */
    public static String rsaEncrypt(String plainText, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encryptedBytes = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    /**
     * 15. RSA 개인키를 사용하여 암호문을 복호화합니다.
     * @param cipherText Base64로 인코딩된 암호문
     * @param privateKey 복호화에 사용할 PrivateKey
     * @return 복호화된 평문
     * @throws Exception 복호화 과정에서 오류 발생 시
     */
    public static String rsaDecrypt(String cipherText, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decodedBytes = Base64.getDecoder().decode(cipherText);
        byte[] decryptedBytes = cipher.doFinal(decodedBytes);
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

    /**
     * 16. SHA-256 알고리즘을 사용하여 문자열을 해싱합니다.
     * @param input 해싱할 입력 문자열
     * @return 해싱된 값 (Hex String)
     * @throws NoSuchAlgorithmException 해시 알고리즘이 지원되지 않을 경우 발생
     */
    public static String sha256(String input) throws NoSuchAlgorithmException {
        return hashWithAlgorithm(HASH_ALGORITHM, input);
    }

    /**
     * 17. 솔트(Salt)를 사용하여 SHA-256 해시를 생성합니다. (보안 강화)
     * @param input 해싱할 입력 문자열
     * @param salt 해싱에 사용할 솔트 값
     * @return 솔트가 적용된 해시 값 (Hex String)
     * @throws NoSuchAlgorithmException 해시 알고리즘이 지원되지 않을 경우 발생
     */
    public static String sha256WithSalt(String input, byte[] salt) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(HASH_ALGORITHM);
        md.update(salt);
        byte[] hash = md.digest(input.getBytes(StandardCharsets.UTF_8));
        return bytesToHexString(hash);
    }

    /**
     * 18. 보안 강화를 위한 랜덤 솔트(Salt)를 생성합니다.
     * @param size 생성할 솔트의 바이트 크기 (보통 16)
     * @return 생성된 솔트 바이트 배열
     */
    public static byte[] generateSalt(int size) {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[size];
        random.nextBytes(salt);
        return salt;
    }

    /**
     * 19. 입력 문자열과 해시 값을 비교하여 일치 여부를 확인합니다.
     * @param originStr 원본 문자열
     * @param hashedStr 비교할 해시 문자열 (Hex)
     * @return 일치하면 true, 그렇지 않으면 false
     * @throws NoSuchAlgorithmException 해시 알고리즘이 지원되지 않을 경우 발생
     */
    public static boolean verifySha256(String originStr, String hashedStr) throws NoSuchAlgorithmException {
        String newHash = sha256(originStr);
        return newHash.equals(hashedStr);
    }

    /**
     * 20. 솔트를 사용하여 입력 문자열과 해시 값을 비교합니다.
     * @param originStr 원본 문자열
     * @param hashedStr 비교할 해시 문자열 (Hex)
     * @param salt 해싱에 사용된 솔트
     * @return 일치하면 true, 그렇지 않으면 false
     * @throws NoSuchAlgorithmException 해시 알고리즘이 지원되지 않을 경우 발생
     */
    public static boolean verifySha256WithSalt(String originStr, String hashedStr, byte[] salt) throws NoSuchAlgorithmException {
        String newHash = sha256WithSalt(originStr, salt);
        return newHash.equals(hashedStr);
    }

    // --- 4. HMAC ---
    /**
     * 21. HmacSHA256 알고리즘을 사용하여 메시지 인증 코드를 생성합니다.
     * @param data 인증할 데이터
     * @param key 비밀키
     * @return 생성된 HMAC 값 (Base64 String)
     * @throws Exception HMAC 생성 과정에서 오류 발생 시
     */
    public static String generateHmacSha256(String data, String key) throws Exception {
        return generatehmacWithAlgorithm(HMAC_ALGORITHM, data, key);
    }

    /**
     * 22. 주어진 데이터와 HMAC 값을 비교하여 메시지 무결성을 검증합니다.
     * @param data 원본 데이터
     * @param key 비밀키
     * @param hmac Base64로 인코딩된 HMAC 값
     * @return 유효하면 true, 그렇지 않으면 false
     * @throws Exception 검증 과정에서 오류 발생 시
     */
    public static boolean verifyHmacSha256(String data, String key, String hmac) throws Exception {
        String generatedHmac = generateHmacSha256(data, key);
        return generatedHmac.equals(hmac);
    }

    // --- 5. Base64 인코딩/디코딩 ---
    /**
     * 23. 문자열을 Base64로 인코딩합니다. (UTF-8)
     * @param plainText 인코딩할 문자열
     * @return Base64로 인코딩된 문자열
     */
    public static String base64Encode(String plainText) {
        return base64Encode(plainText, StandardCharsets.UTF_8);
    }

    /**
     * 24. Base64로 인코딩된 문자열을 디코딩합니다. (UTF-8)
     * @param encodedText Base64로 인코딩된 문자열
     * @return 디코딩된 문자열
     */
    public static String base64Decode(String encodedText) {
        return base64Decode(encodedText, StandardCharsets.UTF_8);
    }

    /**
     * 25. 바이트 배열을 Base64 문자열로 인코딩합니다.
     * @param bytes 인코딩할 바이트 배열
     * @return Base64로 인코딩된 문자열
     */
    public static String base64EncodeBytes(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    /**
     * 26. Base64 문자열을 바이트 배열로 디코딩합니다.
     * @param encodedText Base64로 인코딩된 문자열
     * @return 디코딩된 바이트 배열
     */
    public static byte[] base64DecodeToBytes(String encodedText) {
        return Base64.getDecoder().decode(encodedText);
    }

    /**
     * 27. URL-safe Base64로 문자열을 인코딩합니다.
     * URL에 포함될 수 있는 '+', '/' 문자를 '-', '_'로 대체합니다.
     * @param plainText 인코딩할 문자열
     * @return URL-safe Base64로 인코딩된 문자열
     */
    public static String base64UrlSafeEncode(String plainText) {
        return Base64.getUrlEncoder().encodeToString(plainText.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 28. URL-safe Base64로 인코딩된 문자열을 디코딩합니다.
     * @param encodedText URL-safe Base64로 인코딩된 문자열
     * @return 디코딩된 문자열
     */
    public static String base64UrlSefeDecode(String encodedText) {
        byte[] decodedByte = Base64.getUrlDecoder().decode(encodedText);
        return new String(decodedByte, StandardCharsets.UTF_8);
    }

    // --- 6. 디지털 서명 (RSA with SHA256) ---
    /**
     * 29. RSA 개인키를 사용하여 데이터에 대한 디지털 서명을 생성합니다.
     * @param data 서명할 데이터
     * @param privateKey 서명에 사용할 PrivateKey
     * @return Base64로 인코딩된 서명 값
     * @throws Exception 서명 생성 과정에서 오류 발생 시
     */
    public static String createDigitalSignature(String data, PrivateKey privateKey) throws Exception {
        Signature privateSignature = Signature.getInstance("SHA256withRSA");
        privateSignature.initSign(privateKey);
        privateSignature.update(data.getBytes(StandardCharsets.UTF_8));
        byte[] signature = privateSignature.sign();
        return Base64.getEncoder().encodeToString(signature);
    }

    /**
     * 30. RSA 공개키를 사용하여 디지털 서명을 검증합니다.
     * @param data 원본 데이터
     * @param signature Base64로 인코딩된 서명 값
     * @param publicKey 검증에 사용할 PublicKey
     * @return 서명이 유효하면 true, 그렇지 않으면 false
     * @throws Exception 서명 검증 과정에서 오류 발생 시
     */
    public static boolean verifyDigitalSignature(String data, String signature, PublicKey publicKey) throws Exception {
        Signature publicSignature = Signature.getInstance("SHA256withRSA");
        publicSignature.initVerify(publicKey);
        publicSignature.update(data.getBytes(StandardCharsets.UTF_8));
        byte[] signatureBytes = Base64.getDecoder().decode(signature);
        return publicSignature.verify(signatureBytes);
    }

    // --- 7. 비밀번호 기반 키 생성 (PBKDF2) ---
    /**
     * 31. PBKDF2 알고리즘을 사용하여 비밀번호와 솔트로부터 암호화 키를 생성합니다.
     * @param password 비밀번호
     * @param salt 솔트
     * @param keyLength 생성할 키의 비트 길이 (e.g., 256 for AES-256)
     * @return 생성된 SecretKey 객체
     * @throws NoSuchAlgorithmException 알고리즘이 지원되지 않을 경우
     * @throws InvalidKeySpecException 키 스펙이 유효하지 않을 경우
     */
    public static SecretKey generateKeyFromPassword(String password, byte[] salt, int keyLength)
            throws  NoSuchAlgorithmException, InvalidKeySpecException {
        SecretKeyFactory factory = SecretKeyFactory.getInstance(PBKDF2_ALGORITHM);
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, PBKDF2_ITERATIONS, keyLength);
        SecretKey tmp = factory.generateSecret(spec);
        return new SecretKeySpec(tmp.getEncoded(), "AES");
    }

    /**
     * 32. 비밀번호와 솔트를 사용하여 해시값을 생성합니다. (비밀번호 저장용)
     * @param password 비밀번호
     * @param salt 솔트
     * @return Base64로 인코딩된 해시 값
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public static String hashPasswordWithPBKDF2(String password, byte[] salt)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, PBKDF2_ITERATIONS, AES_KEY_SIZE);
        SecretKeyFactory factory = SecretKeyFactory.getInstance(PBKDF2_ALGORITHM);
        byte[] hash = factory.generateSecret(spec).getEncoded();
        return Base64.getEncoder().encodeToString(hash);
    }

    /**
     * 33. 입력된 비밀번호가 저장된 해시와 일치하는지 검증합니다.
     * @param password 검증할 비밀번호
     * @param storedHash Base64로 인코딩된, 저장된 해시 값
     * @param salt 해싱에 사용된 솔트
     * @return 일치하면 true, 그렇지 않으면 false
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public static boolean verifyPasswordWithPBKDF2(String password, String storedHash, byte[] salt)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        String newHash = hashPasswordWithPBKDF2(password, salt);
        return newHash.equals(storedHash);
    }

    // --- 8. 기타 유틸리티 ---
    /**
     * 34. UUID(Universally Unique Identifier)를 생성합니다.
     * @return 생성된 UUID 문자열
     */
    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }

    /**
     * 35. 바이트 배열을 16진수 문자열로 변환합니다.
     * @param bytes 변환할 바이트 배열
     * @return 16진수 문자열
     */
    public static String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    /**
     * 36. 16진수 문자열을 바이트 배열로 변환합니다.
     * @param hexString 변환할 16진수 문자열
     * @return 변환된 바이트 배열
     */
    public static byte[] hexStringToBytes(String hexString) {
        int len = hexString.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4)
                    + Character.digit(hexString.charAt(i + 1), 16));
        }
        return  data;
    }

    /**
     * 37. 안전한 랜덤 바이트 배열을 생성합니다.
     * @param numBytes 생성할 바이트 배열의 길이
     * @return 생성된 랜덤 바이트 배열
     */
    public static byte[] generateSecureRandomBytes(int numBytes) {
        SecureRandom random = new SecureRandom();
        byte[] randomBytes = new byte[numBytes];
        random.nextBytes(randomBytes);
        return randomBytes;
    }

    /**
     * 38. 두 바이트 배열이 동일한지 비교합니다. (Timing-attack 방지)
     * @param a 첫 번째 바이트 배열
     * @param b 두 번째 바이트 배열
     * @return 동일하면 true, 아니면 false
     */
    public static boolean slowEquals(byte[] a, byte[] b) {
        int diff = a.length ^ b.length;
        for (int i = 0; i < a.length && i < b.length; i++) {
            diff |= a[i] ^ b[i];
        }
        return diff == 0;
    }

    /**
     * 39. 객체를 직렬화하여 Base64 문자열로 반환합니다.
     * 주의: 직렬화는 보안 취약점을 유발할 수 있으므로 신뢰할 수 있는 데이터에만 사용해야 합니다.
     * @param object 직렬화할 객체 (java.io.Serializable 구현 필요)
     * @return Base64로 인코딩된 객체 문자열
     * @throws java.io.IOException 직렬화 실패 시
     */
    public static String serializeObjectToBase64(java.io.Serializable object) throws java.io.IOException {
        java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
        java.io.ObjectOutputStream oos = new java.io.ObjectOutputStream(baos);
        oos.writeObject(object);
        oos.close();
        return Base64.getEncoder().encodeToString(baos.toByteArray());
    }

    /**
     * 40. Base64로 인코딩된 문자열을 객체로 역직렬화합니다.
     * 주의: 역직렬화는 보안 취약점을 유발할 수 있으므로 신뢰할 수 있는 데이터에만 사용해야 합니다.
     * @param encodedObject Base64로 인코딩된 객체 문자열
     * @return 역직렬화된 객체
     * @throws java.io.IOException 역직렬화 실패 시
     * @throws ClassNotFoundException 클래스를 찾을 수 없을 때
     */
    public static Object deserializeObjectFromBase64(String encodedObject) throws java.io.IOException, ClassNotFoundException {
        byte[] data = Base64.getDecoder().decode(encodedObject);
        java.io.ObjectInputStream ois = new java.io.ObjectInputStream(new java.io.ByteArrayInputStream(data));
        Object o = ois.readObject();
        ois.close();
        return o;
    }

    /**
     * 51. 다양한 HMAC 알고리즘으로 메시지 인증 코드를 생성합니다. (e.g., HmacSHA1, HmacSHA512)
     * @param algorithm HMAC 알고리즘 이름
     * @param data 인증할 데이터
     * @param key 비밀키
     * @return Base64로 인코딩된 HMAC 값
     * @throws Exception
     */
    public static String generatehmacWithAlgorithm(String algorithm, String data, String key) throws Exception {
        Mac mac = Mac.getInstance(algorithm);
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), algorithm);
        mac.init(secretKey);
        byte[] hmacBytes = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(hmacBytes);
    }

    // --- 14. 추가 유틸리티 ---
    /**
     * 58. 지정된 문자셋(Charset)을 사용하여 문자열을 Base64로 인코딩합니다.
     * @param plainText 인코딩할 문자열
     * @param charset 사용할 문자셋
     * @return Base64로 인코딩된 문자열
     */
    public static String base64Encode(String plainText, Charset charset) {
        return Base64.getEncoder().encodeToString(plainText.getBytes(charset));
    }

    /**
     * 59. 지정된 문자셋(Charset)을 사용하여 Base64 문자열을 디코딩합니다.
     * @param encodedText 디코딩할 Base64 문자열
     * @param charset 사용할 문자셋
     * @return 디코딩된 문자열
     */
    public static String base64Decode(String encodedText, Charset charset) {
        byte[] decodedBytes = Base64.getDecoder().decode(encodedText);
        return new String(decodedBytes, charset);
    }

    /**
     * 62. 지정된 해시 알고리즘을 수행하는 내부 헬퍼 메서드.
     * @param algorithm 해시 알고리즘 이름 (e.g., "SHA-256")
     * @param input 해싱할 입력 문자열
     * @return 해싱된 값 (Hex String)
     * @throws NoSuchAlgorithmException
     */
    private static String hashWithAlgorithm(String algorithm, String input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(algorithm);
        byte[] hash = md.digest(input.getBytes(StandardCharsets.UTF_8));
        return bytesToHexString(hash);
    }
}
