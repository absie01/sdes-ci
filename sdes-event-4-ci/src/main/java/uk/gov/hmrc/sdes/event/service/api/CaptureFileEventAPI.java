package uk.gov.hmrc.sdes.event.service.api;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

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
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;

import uk.gov.hmrc.sdes.event.service.metrics.EventMessagePublisher;
import uk.gov.hmrc.sdes.event.service.dto.file.FileEventMessage;

@RestController
@Validated
public class CaptureFileEventAPI {
    
    private final DateFormat DF = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    private final EventMessagePublisher eventMessageSender;

    @Autowired
    public CaptureFileEventAPI(final EventMessagePublisher eventMessageSender) {
        this.eventMessageSender = eventMessageSender;
    }

    @Timed
    @ExceptionMetered
    @RequestMapping(value = "/fileevent", method = RequestMethod.POST)
    @ResponseBody
    public String fileEvent(@NotNull @NotEmpty @NotBlank @RequestParam(ParameterNames.FILENAME_PARAMETER) String filename,
            @NotNull @NotEmpty @NotBlank @RequestParam(ParameterNames.SRN_PARAMETER) String srn,
            @NotNull @Min(0) @RequestParam(ParameterNames.FILESIZE_PARAMETER) Long filesize,
            @NotNull @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss") @RequestParam(ParameterNames.EVENT_DATE_PARAMETER) Date date,
            @RequestParam(value = ParameterNames.DIRECTION_PARAMETER, required = false) String direction,
            @Pattern(regexp = "^(([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])$", message = "Valid IP Adddress required")
            @RequestParam(value = ParameterNames.SOURCE_IP, required = false) String sourceIP,
            @RequestParam(value = ParameterNames.ORIG_FILENAME, required = false) String originalFilename,
            @RequestParam(value = ParameterNames.CHECKSUM, required = false) String checksum) {

        if ((sourceIP != null && sourceIP.trim().isEmpty())) {
            throw new IllegalArgumentException("Source IP must have a valid IP address or excluded");
        }

        if ((direction != null && direction.trim().isEmpty())) {
            throw new IllegalArgumentException("Direction must be \"OUTBOUND\" or \"INBOUND\" ");
        }

        if (originalFilename != null && originalFilename.trim().isEmpty()) {
            throw new IllegalArgumentException("Original Filename cannot be empty");
        }

        if (checksum != null && checksum.trim().isEmpty()) {
            throw new IllegalArgumentException("Checksum cannot to empty");
        }
        
        final String dateAsString = DF.format(date);

        eventMessageSender.sendMessage("sdes.exchange", "messages.key",
                FileEventMessage.builder()
                        .srn(srn)
                        .filename(filename)
                        .filesize(filesize)
                        .eventDateTime(dateAsString)
                        .direction(direction)
                        .checksum(checksum)
                        .originalFilename(originalFilename)
                        .sourceIP(sourceIP)
                        .build());

        return "Acknowledged";

    }

}
