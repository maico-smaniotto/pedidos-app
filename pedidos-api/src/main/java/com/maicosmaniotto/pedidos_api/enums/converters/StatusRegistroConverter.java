package com.maicosmaniotto.pedidos_api.enums.converters;

import java.util.stream.Stream;

import com.maicosmaniotto.pedidos_api.enums.StatusRegistro;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class StatusRegistroConverter implements AttributeConverter<StatusRegistro, Character> {
    
    @Override
    public Character convertToDatabaseColumn(StatusRegistro attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.getValor();
    }

    @Override
    public StatusRegistro convertToEntityAttribute(Character value) {
        if (value == null) {
            return null;
        }
        return Stream.of(StatusRegistro.values())        
            .filter(c -> c.getValor() == value)
            .findFirst()
            .orElseThrow(IllegalArgumentException::new);
    }

    public static StatusRegistro stringToEntityAttribute(String str) {
        if (str == null) {
            return null;
        }
        return Stream.of(StatusRegistro.values())        
            .filter(c -> c.toString().equals(str))
            .findFirst()
            .orElseThrow(IllegalArgumentException::new);
    }
}
