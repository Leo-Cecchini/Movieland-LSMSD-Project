package it.unipi.movieland.model;

public enum PlatformEnum {
    NETFLIX("NETFLIX"),
    DINSEY("DISNEY+"),
    PARAMOUNT("PARAMOUNT+"),
    AMAZON_PRIME("AMAZON PRIME"),
    APPLE_TV("APPLE TV");


    private final String displayName;

    //constructor
    PlatformEnum(String displayName) {
        this.displayName = displayName;
    }
    //similar to the to-string method
    public String getDisplayName() {
        return displayName;
    }

}
