package uk.gov.hmrc.sdes.event.service.dto.file;

import lombok.Builder;
import lombok.Value;

import uk.gov.hmrc.sdes.event.service.dto.Event;

@Value
@Builder
public class FileEventMessage implements Event {

    private String srn;
    private String filename;
    private long filesize;
    private String eventDateTime;
    private String direction;
    private String originalFilename;
    private String sourceIP;
    private String checksum;

}
