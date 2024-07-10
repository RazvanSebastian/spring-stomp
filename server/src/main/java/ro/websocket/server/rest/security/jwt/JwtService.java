package ro.websocket.server.rest.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.time.Instant;
import java.time.Period;
import java.util.Date;

public class JwtService {
    private static final String SECRET = "strongSecret123";

    public String generateToken(String username) {
        Instant currentTime = Instant.now();
        return JWT.create()
                .withSubject(username)
                .withAudience("localhost")
                .withIssuer("localhost")
                .withExpiresAt(Date.from(currentTime.plus(Period.ofDays(7))))
                .withIssuedAt(Date.from(currentTime))
                .sign(Algorithm.HMAC256(SECRET));
    }

    public DecodedJWT decodeToken(String token) {
        return JWT.require(Algorithm.HMAC256(SECRET))
                .build()
                .verify(token);

    }
}
