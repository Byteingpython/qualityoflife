package de.snowwars.qualityoflife.alert;

public enum AlertType {
    DURABILITY("durability", "WARNING: Low durability!");
    private final String message;
    private final String name;
    AlertType(String name, String message) {
        this.message = message;
        this.name = name;
    }
    public String getMessage() {
        return message;
    }
    public String getName() {
        return name;
    }


}
