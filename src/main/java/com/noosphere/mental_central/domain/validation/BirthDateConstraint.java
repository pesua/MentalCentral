package com.noosphere.mental_central.domain.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = BirthDateValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface BirthDateConstraint {
    String message() default "Invalid birth date.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
