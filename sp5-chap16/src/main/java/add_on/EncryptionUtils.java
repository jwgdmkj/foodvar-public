package add_on;
import java.security.MessageDigest;

//비밀번호 암호화
//회원가입을 하면, 비밀번호 대신 encrypt된 비밀번호를 넣는다.
//비밀번호 변경/로그인 시에, 비번을 입력하면 encrypt된 비밀번호를 통해 대조한다.
public class EncryptionUtils {

    public String encryptSHA256(String s) {
    //	System.out.println(encrypt(s, "SHA-256"));
        return encrypt(s, "SHA-256");
    }

    public static String encryptMD5(String s) {
        return encrypt(s, "MD5");
    }

    public static String encrypt(String s, String messageDigest) {
        try {
            MessageDigest md = MessageDigest.getInstance(messageDigest);
            byte[] passBytes = s.getBytes();
            md.reset();
            byte[] digested = md.digest(passBytes);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < digested.length; i++) sb.append(Integer.toString((digested[i]&0xff) + 0x100, 16).substring(1));
            return sb.toString();
        } catch (Exception e) {
            return s;
        }
    }
}