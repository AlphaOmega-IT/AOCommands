package de.alphaomega.it.database.converter;

import jakarta.persistence.AttributeConverter;

import java.util.UUID;

public class UUIDConverter implements AttributeConverter<UUID, String> {

    @Override
    public String convertToDatabaseColumn(final UUID attribute) {
        return attribute.toString();
    }

    @Override
    public UUID convertToEntityAttribute(final String dbData) {
        return UUID.fromString(dbData);
    }
}
