package com.noosphere.mental_central.security;

/**
 * Constants for Spring Security authorities.
 */
public final class AuthoritiesConstants {

    public static final String ADMIN = "ROLE_ADMIN";

    public static final String ANONYMOUS = "ROLE_ANONYMOUS";

    public static final String RECEPTION = "ROLE_RECEPTION";

    public static final String DOCTOR = "ROLE_DOCTOR";

    private AuthoritiesConstants() {
    }
}
