package com.kin1st.teddybearshopping.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.*;
import java.util.function.Function;

@Component
public class JwtUtil {

    private final String SECRET_KEY = "my-secret-key-my-secret-key-my-secret-key"; // ít nhất 32 ký tự
    private final long EXPIRATION = 1000 * 60 * 60; // 1h

    private final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    // sinh token
    public String generateToken(String username, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // lấy username từ token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // lấy toàn bộ claims
    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // validate token
    public boolean validateToken(String token) {
        try {
            Claims claims = extractAllClaims(token);
            Date expiration = claims.getExpiration();

            // ✅ Kiểm tra hạn token
            if (expiration.before(new Date())) {
                System.out.println("Token đã hết hạn");
                return false;
            }

            return true; // Token hợp lệ
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            System.out.println("Token hết hạn: " + e.getMessage());
            return false;
        } catch (io.jsonwebtoken.SignatureException e) {
            System.out.println("Sai chữ ký token: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.out.println("Token không hợp lệ: " + e.getMessage());
            return false;
        }
    }

    // lấy authorities từ role
    public Collection<? extends GrantedAuthority> getAuthorities(String role) {
        String authority = role.startsWith("ROLE_") ? role : "ROLE_" + role;
        return Collections.singletonList(new SimpleGrantedAuthority(authority));
    }
}
