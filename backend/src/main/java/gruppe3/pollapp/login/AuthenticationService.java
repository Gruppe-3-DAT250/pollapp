package gruppe3.pollapp.login;

import gruppe3.pollapp.domain.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class AuthenticationService {

    private final Key SECRET_KEY = Keys.hmacShaKeyFor("DDZdF2Z69CMpyoqk6hnvOil5X3bhJKGgDFQeaNKJIkSP3Lni5kt7Sh7tms8Y4wk".getBytes());

    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000))
                .signWith(SECRET_KEY, SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsernameFromToken(String authToken) {
        if (authToken.startsWith("Bearer ")) {
            String token = authToken.substring(7);
            try {
                return Jwts.parserBuilder()
                        .setSigningKey(SECRET_KEY)
                        .build()
                        .parseClaimsJws(token)
                        .getBody()
                        .getSubject();
            } catch (Exception e) {
                return null;
            }
        }
        return null;
    }

    public boolean validateToken(String authToken) {
        if (authToken.startsWith("Bearer ")) {

            String token = authToken.substring(7);
            try {
                Jwts.parserBuilder()
                        .setSigningKey(SECRET_KEY)
                        .build()
                        .parseClaimsJws(token);
                return true;
            } catch (SignatureException | ExpiredJwtException e) {
                return false;
            }
        }
        return false;
    }

}

