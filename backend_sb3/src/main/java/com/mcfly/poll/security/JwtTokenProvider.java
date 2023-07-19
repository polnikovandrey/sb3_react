package com.mcfly.poll.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Instant;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Value("${app.jwtExpirationInMs}")
    private int jwtExpirationInMs;

    public String generateToken(Authentication authentication) {
        final UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        final Instant instantNow = Instant.now();
        final Date nowDate = Date.from(instantNow);
        final Date expiryDate = Date.from(instantNow.plusMillis(jwtExpirationInMs));
        final Key key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
        return Jwts.builder()
                .setSubject(Long.toString(userPrincipal.getId()))
                .setIssuedAt(nowDate)
                .setExpiration(expiryDate)
                .signWith(key)
                .compact();
    }

    public Long getUserIdFromJWT(String token) {
        final Key key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
        final Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        return Long.parseLong(claims.getSubject());
    }

    public boolean validateToken(String authToken) {
        try {
            final Key key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(authToken).getBody();
            return true;
        } catch (SignatureException exception) {
            logger.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            logger.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            logger.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            logger.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            logger.error("JWT claims string is empty");
        }
        return false;
    }

}
