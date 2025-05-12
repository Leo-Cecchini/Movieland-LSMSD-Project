package it.unipi.movieland.model.Enum;

public enum PlatformEnum {
    NETFLIX("NETFLIX"),
    DINSEY("DISNEY+"),
    PARAMOUNT("PARAMOUNT+"),
    AMAZON_PRIME("AMAZON PRIME"),
    APPLE_TV("APPLE TV");

    private final String displayName;

    PlatformEnum(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

}
