package ro.websocket.server.rest.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.AllArgsConstructor;

import java.time.Instant;
import java.time.Period;
import java.util.Date;

@AllArgsConstructor
public class JwtService {
    private final String jwtSecret;

    public String generateToken(String username) {
        Instant currentTime = Instant.now();
        return JWT.create()
                .withSubject(username)
                .withAudience("localhost")
                .withIssuer("localhost")
                .withExpiresAt(Date.from(currentTime.plus(Period.ofDays(7))))
                .withIssuedAt(Date.from(currentTime))
                .sign(Algorithm.HMAC256(jwtSecret));
    }

    public DecodedJWT decodeToken(String token) {
        return JWT.require(Algorithm.HMAC256(jwtSecret))
                .build()
                .verify(token);

    }
}
