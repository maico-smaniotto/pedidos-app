package com.maicosmaniotto.pedidos_api.enums.validation;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.maicosmaniotto.pedidos_api.enums.TipoPessoa;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

// Validator to be applied on a String property that represents the string value of the enum type; 
// For example in a DTO class where the property is String but the values must be the ones defined in the enum

@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = TipoPessoaStringSubsetValidator.class)
public @interface TipoPessoaStringSubset {
    TipoPessoa[] anyOf();
    String message() default "deve ser um destes: {anyOf}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}