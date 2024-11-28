package org.example.final_project.model.validation;

public final class AuthValidation {
    private AuthValidation() {
        throw new UnsupportedOperationException("AuthValidation is a utility class and cannot be instantiated.");
    }

    public static final String BAD_CREDENTIAL = "Invalid username or password.";
    public static final String ACCOUNT_INACTIVE = "Account is inactive.";
    public static final String ACCOUNT_LOCKED = "The account is locked. Please contact support.";
    public static final String ACCOUNT_DISABLED = "The account is disabled.";
    public static final String TOKEN_EXPIRED = "The authentication token has expired.";
    public static final String TOKEN_INVALID = "The provided authentication token is invalid.";
    public static final String UNAUTHORIZED_ACCESS = "Unauthorized access.";
    public static final String ACCOUNT_CONFLICT = "This account is taken by another user";
}
