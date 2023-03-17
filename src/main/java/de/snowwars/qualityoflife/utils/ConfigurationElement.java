package de.snowwars.qualityoflife.utils;

public enum ConfigurationElement {
    AUTOREFILL("features.autorefill"),
    SHULKER("features.shulker"),
    KEEPIMPORTANT("features.keepimportant"),
    DUMPINVENTORY("features.dumpinventory"),
    BACKPACK("features.backpack"),
    STATUS("features.status"),
    AFKSAVE("features.afksave"),
    ARENA("features.arena"),
    VINE("features.vine"),
    FIRST_STATUS("stati.first"),
    SECOND_STATUS("stati.second"),
    CREEPER("features.creeper"),

    ALERT("features.alert");

    private final String path;

    ConfigurationElement(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
