package user_management.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CodiceFiscaleValidator implements ConstraintValidator<ValidCodiceFiscale, String> {

    private static final String CF_REGEX =
            "^[A-Z]{6}[0-9]{2}[A-Z][0-9]{2}[A-Z][0-9]{3}[A-Z]$";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) return false;
        return value.toUpperCase().matches(CF_REGEX);
    }
}
