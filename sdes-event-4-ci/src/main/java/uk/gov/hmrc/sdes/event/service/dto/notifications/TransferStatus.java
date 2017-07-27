package uk.gov.hmrc.sdes.event.service.dto.notifications;

import java.util.Arrays;

public enum TransferStatus {

    Passed("Passed"), 
    Failed("Failed"), 
    Partial("Partial");

    private final String value;

    private TransferStatus(final String value) {
        this.value = value;
    }

    public static TransferStatus fromValue(final String value) throws IllegalArgumentException {
        for (TransferStatus type : values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new IllegalArgumentException(
                "Unknown TransferStatus " + value + ", Allowed values are " + Arrays.toString(values()));
    }

}
