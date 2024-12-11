package whpu.gomoku.util;

import cn.hutool.core.convert.NumberWithFormat;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.exceptions.ValidateException;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTValidator;
import cn.hutool.jwt.signers.JWTSigner;
import cn.hutool.jwt.signers.JWTSignerUtil;
import org.bouncycastle.util.io.pem.PemReader;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.encrypt.BytesEncryptor;
import org.springframework.security.crypto.encrypt.Encryptors;
import whpu.gomoku.gomokuserver.data.common.GomokuConstant;
import whpu.gomoku.gomokuserver.data.entity.User;
import whpu.gomoku.gomokuserver.exception.authen.InvalidTokenException;
import whpu.gomoku.gomokuserver.exception.authen.TokenExpiredException;

import java.io.FileReader;
import java.io.InputStreamReader;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

public class JwtUtil {
    private final static String algorithm = GomokuConstant.JWT_ALGORITHM;
    private final static String salt = GomokuConstant.JWT_SALT;
    private final static String password = GomokuConstant.JWT_PASSWORD;
    private final static JWTSigner signer;

    static {
        PublicKey publicKey = null;
        PrivateKey privateKey = null;
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(GomokuConstant.JWT_KEY_INSTANCE);
            BytesEncryptor encryptor = Encryptors.stronger(password, salt);

            // 通过 ClassPathResource 获取资源
            ClassPathResource publicKeyFile = new ClassPathResource(GomokuConstant.JWT_PUBLIC_SECRET_KEY_POSITION);
            ClassPathResource privateKeyFile = new ClassPathResource(GomokuConstant.JWT_PRIVATE_SECRET_KEY_POSITION);

            // 使用 InputStreamReader 从类路径资源读取 PEM 文件
            PemReader publicReader = new PemReader(new InputStreamReader(publicKeyFile.getInputStream()));
            PemReader privateReader = new PemReader(new InputStreamReader(privateKeyFile.getInputStream()));

            // 读取公钥和私钥的内容
            byte[] publicBytes = publicReader.readPemObject().getContent();
            byte[] privateBytes = encryptor.decrypt(privateReader.readPemObject().getContent());

            // 生成公钥和私钥对象
            publicKey = keyFactory.generatePublic(new X509EncodedKeySpec(publicBytes));
            privateKey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(privateBytes));
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 创建签名器
        signer = JWTSignerUtil.createSigner(algorithm, new KeyPair(publicKey, privateKey));
    }

    public static String generateToken(User user) {
        return JWT.create()
                .setIssuedAt(DateUtil.date())
                .setExpiresAt(DateUtil.date(System.currentTimeMillis() + GomokuConstant.JWT_EXPIRATION_TIME))
                .setPayload("id", user.getId())
                .setPayload("username", user.getUsername())
                .setPayload("role", user.getRole())
                .setSigner(signer)
                .sign();
    }

    public static void validateToken(String token) throws TokenExpiredException, InvalidTokenException {
        try {
            JWTValidator.of(token).validateAlgorithm(signer);
        } catch (Exception e) {
            throw new InvalidTokenException(e.getMessage(), e);
        }
        try {
            JWTValidator.of(token).validateDate(DateUtil.date());
        } catch (ValidateException validateException) {
            throw new TokenExpiredException(validateException.getMessage(), validateException);
        }
    }

    public static String getUsername(String token) {
        JWT jwt = JWT.of(token);
        return (String) jwt.getPayload("username");
    }

    public static Long getId(String token) {
        JWT jwt = JWT.of(token);
        return  ((NumberWithFormat) jwt.getPayload("id")).longValue();
    }

    public static String getRole(String token) {
        JWT jwt = JWT.of(token);
        return (String) jwt.getPayload("role");
    }

}
