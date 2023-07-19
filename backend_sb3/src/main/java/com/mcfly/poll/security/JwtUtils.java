package com.mcfly.poll.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.util.WebUtils;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;

@Component
public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${app.jwtSecret}")
    private String jwtSecret;
    @Value("${app.jwtExpirationInMs}")
    private int jwtExpirationInMs;
    @Value("${app.jwtCookieName}")
    private String jwtCookie;

    public String getJwtFromRequest(HttpServletRequest request) {
        return getJwtFromHeader(request)
                .orElseGet(() -> getJwtFromCookie(request));

    }

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
        final Claims claims = Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(token).getBody();
        return Long.parseLong(claims.getSubject());
    }

    public ResponseCookie generateJwtCookie(Authentication authentication) {
        String jwt = generateToken(authentication);
        return produceJwtCookie(jwt);
    }

    public ResponseCookie produceJwtCookie(String jwt) {
        return ResponseCookie.from(jwtCookie, jwt).path("/api").maxAge(24 * 60 * 60).httpOnly(true).build();
    }

    public ResponseCookie getCleanJwtCookie() {
        return ResponseCookie.from(jwtCookie, null).path("/api").build();
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(authToken).getBody();
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

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    private Optional<String> getJwtFromHeader(HttpServletRequest request) {
        final String bearerToken = request.getHeader("Authorization");
        return StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")
                ? Optional.of(bearerToken.substring(7))
                : Optional.empty();
    }

    private String getJwtFromCookie(HttpServletRequest request) {
        final Cookie cookie = WebUtils.getCookie(request, jwtCookie);
        return cookie == null ? null : cookie.getValue();
    }
}
