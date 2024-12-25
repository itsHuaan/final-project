package org.example.final_project.validation;

import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;

public final class AuthValidation {
    @Value("${jwt.secret-key}")
    private static String JWT_SECRET;

    private AuthValidation() {
        throw new UnsupportedOperationException("AuthValidation is a utility class and cannot be instantiated.");
    }


    public static final String ACCOUNT_UNAVAILABLE = "Account unavailable.";
    public static final String BAD_CREDENTIAL = "Invalid email or password.";
    public static final String ACCOUNT_INACTIVE = "Account is inactive.";
    public static final String ACCOUNT_LOCKED = "The account is locked. Please contact support.";
    public static final String ACCOUNT_DISABLED = "The account is disabled.";
    public static final String TOKEN_EXPIRED = "The authentication token has expired.";
    public static final String TOKEN_INVALID = "The provided authentication token is invalid.";
    public static final String UNAUTHORIZED_ACCESS = "Unauthorized access.";
    public static final String ACCOUNT_CONFLICT = "This email is already in use.";
    public static final String PASSWORD_INVALID = "Password is incorrect";

    public static boolean tokenValidator(String token) {
        try {
            Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token);
            return true;
        } catch (SignatureException | UnsupportedJwtException | IllegalArgumentException | ExpiredJwtException |
                 MalformedJwtException e) {
            return false;
        }
    }
}
