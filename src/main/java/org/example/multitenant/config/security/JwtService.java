
package org.example.multitenant.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.example.multitenant.config.datasource.TenantContext;
import org.example.multitenant.model.dto.JwtClaim;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;

@Service
public class JwtService {


    @Value("${application.security.jwt.signing-key}")
    private String signingKey;

    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;


    public JwtClaim extractUserDetails(String token) {
        return extractUserDetails(token, Claims::getSubject, Claims::getAudience);
    }

    private JwtClaim extractUserDetails(String token, Function<Claims, String> function1, Function<Claims, String> function2) {
        final Claims claims = extractAllClaims(token);
        String username = function1.apply(claims);
        String tenantName = function2.apply(claims);

        return new JwtClaim(username, tenantName);
    }

    public String generateAccessToken(String username) {
        return Jwts
                .builder()
                .setClaims(new HashMap<>())
                .setSubject(username)
                .setAudience(TenantContext.getCurrentTenant())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + (jwtExpiration * 60 * 1000)))
                .signWith(createSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Claims extractAllClaims(String token) {

        return Jwts
                .parserBuilder()
                .setSigningKey(createSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key createSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(signingKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
