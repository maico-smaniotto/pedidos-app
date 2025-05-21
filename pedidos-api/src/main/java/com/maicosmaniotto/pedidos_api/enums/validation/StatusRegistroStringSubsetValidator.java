package com.maicosmaniotto.pedidos_api.enums.validation;

import java.util.Arrays;

import com.maicosmaniotto.pedidos_api.enums.StatusRegistro;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class StatusRegistroStringSubsetValidator implements ConstraintValidator<StatusRegistroStringSubset, String> {
    private StatusRegistro[] subset;

    @Override
    public void initialize(StatusRegistroStringSubset constraint) {
        this.subset = constraint.anyOf();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value == null || Arrays.stream(subset).map(Object::toString).toList().contains(value);
    }
}
