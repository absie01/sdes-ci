package uk.gov.hmrc.sdes.event.service.api;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;

import uk.gov.hmrc.sdes.event.service.metrics.EventMessagePublisher;
import uk.gov.hmrc.sdes.event.service.dto.notifications.AVStatus;
import uk.gov.hmrc.sdes.event.service.dto.notifications.NotificationEventMessage;
import uk.gov.hmrc.sdes.event.service.dto.notifications.NotificationType;
import uk.gov.hmrc.sdes.event.service.dto.notifications.TransferStatus;

@RestController
@Validated
public class CaptureNotificationEventAPI {

    private final EventMessagePublisher eventMessageSender;

    @Autowired
    public CaptureNotificationEventAPI(EventMessagePublisher eventMessageSender) {
        this.eventMessageSender = eventMessageSender;
    }

    @Timed
    @ExceptionMetered
    @RequestMapping(value = "/notificationevent", method = RequestMethod.POST)
    @ResponseBody
    public String notificationEvent1(@NotEmpty @NotBlank @RequestParam(ParameterNames.SRN_PARAMETER) String srn,
            @NotEmpty @NotBlank @RequestParam(ParameterNames.FILENAME_PARAMETER) String filename,
            @NotNull @Min(0) @RequestParam(ParameterNames.FILESIZE_PARAMETER) Long filesize,
            @NotEmpty @NotBlank @RequestParam(ParameterNames.ACV_STATUS_PARAMETER) String avcStatus,
            @NotEmpty @NotBlank @RequestParam(ParameterNames.TRANSFER_STATUS_PARAMETER) String transferStatus,
            @NotEmpty @NotBlank @RequestParam(ParameterNames.NOTIFICATION_TYPE_PARAMETER) String notificationType,
            @NotNull @Min(0) @RequestParam(ParameterNames.DOWNLOAD_ATTEMPT_PARAMETER) Integer downloadAttempts) {

        eventMessageSender.sendMessage("sdes.exchange", "sdes.notification.key",
                NotificationEventMessage.builder()
                        .srn(srn)
                        .filename(filename)
                        .filesize(filesize)
                        .notificationType(NotificationType.fromValue(notificationType))
                        .downloadAttempts(downloadAttempts)
                        .avcStatus(AVStatus.fromValue(avcStatus))
                        .transferStatus(TransferStatus.fromValue(transferStatus))
                        .build());

        return "Acknowledged";

    }

}
