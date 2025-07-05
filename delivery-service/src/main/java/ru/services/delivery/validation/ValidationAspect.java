package ru.services.delivery.validation;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.AllArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import java.util.Set;

@Aspect
@Component
@AllArgsConstructor
public class ValidationAspect {
    private final Validator validator;

    @AfterReturning(
            pointcut = "@annotation(ru.services.delivery.validation.ValidateAfterMapping) || " +
                    "execution(* ru.services.store.mapper..*.*(..))",
            returning = "result"
    )
    public void validateAfterMapping(JoinPoint joinPoint, Object result) {
        if (result == null) {
            return;
        }

        Set<ConstraintViolation<Object>> violations = validator.validate(result);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}