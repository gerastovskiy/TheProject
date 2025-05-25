package ru.services.user.authorization;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TokenServiceImpl implements TokenService {
    @Value("${auth.jwt.secret}")
    private String secretKey;

    @Override
    public TokenValidationResult validateToken(String token) {
        try {
            DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256(secretKey))
                    .build()
                    .verify(token);

            String clientId = decodedJWT.getClaim("sub").asString();
            return new TokenValidationResult(true, clientId);

        } catch (JWTVerificationException | NullPointerException e) {
            return new TokenValidationResult(false, null);
        }
    }
}
