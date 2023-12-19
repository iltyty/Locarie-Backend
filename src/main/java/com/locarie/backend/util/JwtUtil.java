package com.locarie.backend.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.locarie.backend.domain.entities.UserEntity;
import java.util.Date;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtUtil {

  @Value("${jwt.secret}")
  private String secret;

  @Value("${jwt.expiration}")
  private Long expiration;

  @Value("${jwt.issuer}")
  private String issuer;

  public String generateToken(UserEntity user) {
    Date expireDate = new Date(System.currentTimeMillis() + expiration * 1000);
    Map<String, Object> headers = Map.of("alg", "HS256", "typ", "JWT");
    Map<String, String> payload =
        Map.of("sub", user.getId().toString(), "name", user.getUsername());
    return JWT.create()
        .withHeader(headers)
        .withPayload(payload)
        .withExpiresAt(expireDate)
        .withIssuer(issuer)
        .withIssuedAt(new Date())
        .sign(Algorithm.HMAC256(secret));
  }

  public Map<String, Claim> verifyToken(String token) {
    try {
      return JWT.require(Algorithm.HMAC256(secret))
          .withIssuer(issuer)
          .build()
          .verify(token)
          .getClaims();
    } catch (JWTVerificationException ignored) {
      return null;
    }
  }
}
