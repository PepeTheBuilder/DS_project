package org.example.proiect_ds.Utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

public class JwtUtil {

    private static final String SECRET_KEY = "49C2E57DCEC1823F5F1DB2546754E49C2E57DCEC1823F5F1DB2546754E"; // Replace with the same key used for signing

    // Method to extract claims
    public static Claims extractClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    // Method to extract a specific claim
    public static String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    // Validate token
    public static boolean isTokenValid(String token) {
        try {
            extractClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
