package de.alphaomega.it.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.List;
import java.util.Map;

public class LocationSerialization {

    public static Location getLocationFromString(final String location) {
        List<String> locString = List.of(location.split(", "));
        return new Location(Bukkit.getWorld(locString.get(0)), Double.parseDouble(locString.get(1)), Double.parseDouble(locString.get(2)), Double.parseDouble(locString.get(3)), Float.parseFloat(locString.get(4)), Float.parseFloat(locString.get(5)));
    }

    public static String getStringFromLocation(final Location location) {
        Map<String, Object> locationMap;
        locationMap = location.serialize();
        return locationMap.get("world") + ", " + locationMap.get("x") + ", " + locationMap.get("y") + ", " + locationMap.get("z") + ", " + locationMap.get("yaw") + ", " + locationMap.get("pitch");
    }
}
