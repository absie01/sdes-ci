package uk.gov.hmrc.sdes.event.service.dto.notifications;

import lombok.Builder;
import lombok.Value;

import uk.gov.hmrc.sdes.event.service.dto.Event;

@Value
@Builder
public class NotificationEventMessage implements Event {

    private String srn;
    private String filename;
    private long filesize;
    private NotificationType notificationType;
    private Integer downloadAttempts;
    private AVStatus avcStatus;
    private TransferStatus transferStatus;

}
