package de.alphaomega.it.database.converter;

import de.alphaomega.it.utils.LocationSerialization;
import jakarta.persistence.AttributeConverter;
import org.bukkit.Location;

public class LocationConverter implements AttributeConverter<Location, String> {

    @Override
    public String convertToDatabaseColumn(final Location attribute) {
        return new LocationSerialization().getStringFromLocation(attribute);
    }

    @Override
    public Location convertToEntityAttribute(final String dbData) {
        return new LocationSerialization().getLocationFromString(dbData);
    }
}
