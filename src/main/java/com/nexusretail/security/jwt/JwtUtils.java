package com.nexusretail.security.jwt;

import com.nexusretail.data.models.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@Slf4j
public class JwtUtils {

    @Value("${jwt.secret:defaultSecretKeyThatShouldBeAtLeast256BitsLongForHS256Algorithm}")
    private String jwtSecret;

    @Value("${jwt.expiration:86400000}")
    private long jwtExpirationMs;

    public long ACCESS_TOKEN_VALID_TIME_MILLIS() {
        return 30 * 60 * 1000L; // 30 minutes
    }

    public long REFRESH_TOKEN_VALID_TIME_MILLIS() {
        return 7 * 24 * 60 * 60 * 1000L; // 7 days
    }

    private static final String BEARER_PREFIX      = "Bearer ";
    private static final String AUTHORIZATION_HEADER = "Authorization";

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    // ------------------------------------------------------------------ //
    //  Token generation
    // ------------------------------------------------------------------ //

    /** Full token with role, userId, username, email claims — used at login */
    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role",     user.getRole().getName());
        claims.put("userId",   user.getId());
        claims.put("username", user.getUsername());
        claims.put("email",    user.getEmail());

        return buildToken(claims, user.getEmail(), jwtExpirationMs);
    }

    /** Short-lived access token by email */
    public String generateToken(String email) {
        return generateToken(email, ACCESS_TOKEN_VALID_TIME_MILLIS());
    }

    /** Long-lived refresh token by email */
    public String generateRefreshToken(String email) {
        return generateToken(email, REFRESH_TOKEN_VALID_TIME_MILLIS());
    }

    private String generateToken(String email, long validTimeMillis) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", email);
        return buildToken(claims, email, validTimeMillis);
    }

    /** Single place that actually builds the JWT */
    private String buildToken(Map<String, Object> claims, String subject, long validTimeMillis) {
        Date now        = new Date();
        Date expiration = new Date(now.getTime() + validTimeMillis);

        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(now)
                .expiration(expiration)
                .signWith(getSigningKey())
                .compact();
    }

    // ------------------------------------------------------------------ //
    //  Claims extraction
    // ------------------------------------------------------------------ //

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public String getEmailFromToken(String token) {
        return getAllClaimsFromToken(token).get("email", String.class);
    }

    public String getRoleFromToken(String token) {
        return getAllClaimsFromToken(token).get("role", String.class);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> resolver) {
        return resolver.apply(getAllClaimsFromToken(token));
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // ------------------------------------------------------------------ //
    //  Validation
    // ------------------------------------------------------------------ //

    public boolean validateToken(String token) {
        try {
            getAllClaimsFromToken(token);
            return true;
        } catch (MalformedJwtException e)     { log.error("Invalid JWT: {}",        e.getMessage()); }
        catch (ExpiredJwtException e)        { log.error("JWT expired: {}",         e.getMessage()); }
        catch (UnsupportedJwtException e)    { log.error("JWT unsupported: {}",     e.getMessage()); }
        catch (IllegalArgumentException e)   { log.error("JWT claims empty: {}",    e.getMessage()); }
        return false;
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String emailFromToken    = getEmailFromToken(token);
        final String usernameFromToken = getUsernameFromToken(token);

        return (emailFromToken != null && emailFromToken.equals(userDetails.getUsername())
                || usernameFromToken != null && usernameFromToken.equals(userDetails.getUsername()))
                && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        try {
            return getClaimFromToken(token, Claims::getExpiration).before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        }
    }

    // ------------------------------------------------------------------ //
    //  Request helpers
    // ------------------------------------------------------------------ //

    public String extractTokenFromRequest(HttpServletRequest request) {
        final String authHeader = request.getHeader(AUTHORIZATION_HEADER);
        if (authHeader != null && authHeader.startsWith(BEARER_PREFIX)) {
            return authHeader.substring(BEARER_PREFIX.length());
        }

        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if ("accessToken".equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    public String extractEmail(String token) {
        try {
            return getEmailFromToken(token);
        } catch (Exception e) {
            log.error("Error extracting email from token: {}", e.getMessage());
            return null;
        }
    }
}