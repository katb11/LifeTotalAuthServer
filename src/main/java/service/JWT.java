package service;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;

import java.util.Base64;
import java.util.Date;
import java.util.Map;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.SignatureAlgorithm;

public class JWT {

    private static String SECRET_KEY = System.getenv("JWT_KEY");

    public static String createJWT(String issuer, Map<String,Object> claims, long ttlSeconds) {

        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS512;

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        byte[] apiKeySecretBytes = Base64.getEncoder().encode(SECRET_KEY.getBytes());
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        JwtBuilder builder = Jwts.builder()
                .setIssuedAt(now)
                .setIssuer(issuer)
                .addClaims(claims)
                .signWith(signatureAlgorithm, signingKey);

        if (ttlSeconds >= 0) {
            long expMillis = nowMillis + (ttlSeconds * 1000);
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }

        return builder.compact();
    }
}