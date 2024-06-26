package com.kingmartinien.iutnotifyapi.security;

import com.kingmartinien.iutnotifyapi.repository.TokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtService {

    private final TokenRepository tokenRepository;

    @Value("${application.security.jwt.access-token-expiration}")
    private Long ACCESS_TOKEN_EXPIRATION;
    @Value("${application.security.jwt.refresh-token-expiration}")
    private Long REFRESH_TOKEN_EXPIRATION;
    @Value("${application.security.jwt.secret-key}")
    private String SECRET_KEY;

    public String generateAccessToken(HashMap<String, Object> claims, UserDetails userDetails) {
        return this.generateToken(claims, userDetails, ACCESS_TOKEN_EXPIRATION);
    }

    public String generateRefreshToken(HashMap<String, Object> claims, UserDetails userDetails) {
        return this.generateToken(claims, userDetails, REFRESH_TOKEN_EXPIRATION);
    }

    private String generateToken(HashMap<String, Object> claims, UserDetails userDetails, Long expiration) {
        return Jwts.builder()
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plus(expiration, ChronoUnit.DAYS)))
                .subject(userDetails.getUsername())
                .claims(claims)
                .claim("authorities", userDetails.getAuthorities()
                        .stream().map(GrantedAuthority::getAuthority).toList())
                .signWith(this.getSignInKey())
                .compact();
    }

    public String extractUsername(String accessToken) {
        return this.extractClaim(accessToken, Claims::getSubject);
    }

    public boolean isTokenValid(String accessToken, UserDetails userDetails) {
        String username = this.extractUsername(accessToken);
        boolean isValid = this.tokenRepository.findByAccessTokenOrRefreshToken(accessToken)
                .map(token -> !token.isExpired())
                .orElse(false);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(accessToken) && isValid;
    }

    private boolean isTokenExpired(String jwtToken) {
        return this.extractClaim(jwtToken, Claims::getExpiration).before(Date.from(Instant.now()));
    }

    private <T> T extractClaim(String jwtToken, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(jwtToken);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(this.getSignInKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSignInKey() {
        byte[] bytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(bytes);
    }

}
