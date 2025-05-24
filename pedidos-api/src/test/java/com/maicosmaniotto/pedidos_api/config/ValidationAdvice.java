package com.maicosmaniotto.pedidos_api.config;

import java.lang.reflect.Method;
import java.util.Set;

import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.executable.ExecutableValidator;

public class ValidationAdvice implements MethodBeforeAdvice {

    private static final ExecutableValidator executableValidator;

    static {
        LocalValidatorFactoryBean factory = new LocalValidatorFactoryBean();
        factory.afterPropertiesSet();
        executableValidator = factory.getValidator().forExecutables();
        factory.close();
    }

    @Override
    public void before(@NonNull Method method, @NonNull Object[] args, @Nullable Object target) throws Throwable {
        Set<ConstraintViolation<Object>> violations = executableValidator.validateParameters(target, method, args);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

}
