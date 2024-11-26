package realtech.util;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class AesEncryptionUtil {
    // AES 키 생성
    private static final String ALGORITHM = "AES";

    
    private static final String SECRET_KEY = "MySecretKey12345"; // 16자리 키 (128-bit)

    /**
     * AES 암호화
     *
     * @param data 암호화할 데이터
     * @return 암호화된 데이터
     * @throws Exception
     */
    public static String encode(String data) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(SECRET_KEY.getBytes(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedBytes = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encryptedBytes); // Base64로 인코딩
    }

    /**
     * AES 복호화
     *
     * @param encryptedData 복호화할 데이터
     * @return 복호화된 데이터
     * @throws Exception
     */
    public static String decode(String encryptedData) throws Exception {
        SecretKeySpec secretKey = new SecretKeySpec(SECRET_KEY.getBytes(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decodedBytes = Base64.getDecoder().decode(encryptedData); // Base64 디코딩
        return new String(cipher.doFinal(decodedBytes));
    }

    /**
     * AES Secret Key 자동 생성 (128-bit)
     * 키 관리 시스템이 없을 경우 사용할 수 있음
     *
     * @return SecretKey
     * @throws Exception
     */
    public static SecretKey generateKey() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
        keyGenerator.init(128); // 128-bit 키
        return keyGenerator.generateKey();
    }
}
