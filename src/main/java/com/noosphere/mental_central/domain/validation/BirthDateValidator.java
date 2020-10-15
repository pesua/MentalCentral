package com.noosphere.mental_central.domain.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.Period;

import static com.noosphere.mental_central.config.Constants.MIN_PATIENT_AGE;

public class BirthDateValidator implements
    ConstraintValidator<BirthDateConstraint, LocalDate> {

    @Override
    public void initialize(BirthDateConstraint constraintAnnotation) {

    }

    @Override
    public boolean isValid(LocalDate birthDate, ConstraintValidatorContext constraintValidatorContext) {
        int age = Period.between(birthDate, LocalDate.now()).getYears();

        return age >= MIN_PATIENT_AGE;
    }
}
