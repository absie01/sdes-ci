package uk.gov.hmrc.sdes.event.service.dto.notifications;

import java.util.Arrays;

public enum NotificationType {

    FileReady("FileReady"), 
    FileExpiryWarning("FileExpiryWarning"), 
    FileDeleted("FileExpiryWarning"), 
    FileReceived("FileReceived"), 
    FileProcessed("FileProcessed");

    private final String value;

    private NotificationType(final String value) {
        this.value = value;
    }

    public static NotificationType fromValue(String value) throws IllegalArgumentException {
        for (NotificationType type : values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException(
                "Unknown NotificationType " + value + ", Allowed values are " + Arrays.toString(values()));
    }

}
