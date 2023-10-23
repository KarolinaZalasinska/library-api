package model;

public enum Availability {
    AVAILABLE("Available"),
    BORROWED("Borrowed"),
    UNAVAILABLE("Unavailable");

    private final String label;

    Availability(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static Availability fromLabel(String label) {
        for (Availability availability : values()) {
            if (availability.label.equals(label)) {
                return availability;
            }
        }
        throw new IllegalArgumentException("Invalid availability value: " + label);
    }
}
