package user_management.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = CodiceFiscaleValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidCodiceFiscale {

    String message() default "Codice fiscale non valido";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
