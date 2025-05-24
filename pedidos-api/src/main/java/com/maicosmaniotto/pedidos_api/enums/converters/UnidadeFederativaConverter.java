package com.maicosmaniotto.pedidos_api.enums.converters;

import com.maicosmaniotto.pedidos_api.enums.UnidadeFederativa;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.stream.Stream;

@Converter(autoApply = true)
public class UnidadeFederativaConverter implements AttributeConverter<UnidadeFederativa, String> {
    
    @Override
    public String convertToDatabaseColumn(UnidadeFederativa attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getValor();
    }

    @Override
    public UnidadeFederativa convertToEntityAttribute(String value) {
        if (value == null) {
            return null;
        }
        return Stream.of(UnidadeFederativa.values())        
            .filter(c -> c.getValor().equals(value))
            .findFirst()
            .orElseThrow(IllegalArgumentException::new);
    }

    public static UnidadeFederativa stringToEntityAttribute(String str) {
        if (str == null) {
            return null;
        }
        return Stream.of(UnidadeFederativa.values())        
            .filter(c -> c.toString().equals(str))
            .findFirst()
            .orElseThrow(IllegalArgumentException::new);
    }
}
