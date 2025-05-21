package com.maicosmaniotto.pedidos_api.enums.validation;

import java.util.Arrays;

import com.maicosmaniotto.pedidos_api.enums.TipoPessoa;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class TipoPessoaStringSubsetValidator implements ConstraintValidator<TipoPessoaStringSubset, String> {
    private TipoPessoa[] subset;

    @Override
    public void initialize(TipoPessoaStringSubset constraint) {
        this.subset = constraint.anyOf();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value == null || Arrays.stream(subset).map(Object::toString).toList().contains(value);
    }
}
