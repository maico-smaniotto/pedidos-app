package com.maicosmaniotto.pedidos_api.enums.converters;

import com.maicosmaniotto.pedidos_api.enums.TipoPessoa;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.stream.Stream;

@Converter(autoApply = true)
public class TipoPessoaConverter implements AttributeConverter<TipoPessoa, Character> {
    
    @Override
    public Character convertToDatabaseColumn(TipoPessoa attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getValor();
    }

    @Override
    public TipoPessoa convertToEntityAttribute(Character value) {
        if (value == null) {
            return null;
        }
        return Stream.of(TipoPessoa.values())        
            .filter(c -> c.getValor().equals(value))
            .findFirst()
            .orElseThrow(IllegalArgumentException::new);
    }

    public static TipoPessoa stringToEntityAttribute(String str) {
        if (str == null) {
            return null;
        }
        return Stream.of(TipoPessoa.values())        
            .filter(c -> c.toString().equals(str))
            .findFirst()
            .orElseThrow(IllegalArgumentException::new);
    }
}
