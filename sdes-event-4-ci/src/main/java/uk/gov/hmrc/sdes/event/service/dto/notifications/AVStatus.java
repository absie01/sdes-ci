package uk.gov.hmrc.sdes.event.service.dto.notifications;

import java.util.Arrays;

public enum AVStatus {

    Passed("Passed"), Failed("Failed"), NotRun("NotRun");
    private final String value;

    private AVStatus(final String value) {
        this.value = value;
    }

    public static AVStatus fromValue(String value) throws IllegalArgumentException {
        for (AVStatus status : values()) {
            if (status.value.equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException(
                "Unknown AVStatus " + value + ", Allowed values are " + Arrays.toString(values()));
    }

}
