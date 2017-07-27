package uk.gov.hmrc.sdes.event.service;

import uk.gov.hmrc.sdes.event.service.metrics.EventMessagePublisher;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.amqp.AmqpException;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import uk.gov.hmrc.sdes.event.service.dto.file.FileEventMessage;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ContextConfiguration
@TestPropertySource("classpath:EventMessagePublisherTest.properties")
public class EventMessagePublisherTester {

    private final static long FILESIZE = 12345678L;
    private final static String FILENAME = "filename.zip";
    private final static String SRN = "srn";
    private final static String DIRECTION = "Outbound";
    private final static String VALID_DATE = "2015-09-11 11:20:13";
    private final static String SOURCE_IP = "20.13.15.16";
    private final static String ORIGINAL_FILENAME = "ORIG_FILENAME.ZIP";
    private final static String CHECKSUM = "XSAJKHDJANJDA==831HDYD8DAJ";

    @Mock
    private EventMessagePublisher service;

    @Test(expected = AmqpException.class)
    public void testErrorWithInvalidExchangeName() {
        final FileEventMessage message = FileEventMessage.builder()
                .srn(SRN)
                .filename(FILENAME)
                .filesize(FILESIZE)
                .eventDateTime(VALID_DATE)
                .direction(DIRECTION)
                .checksum(CHECKSUM)
                .originalFilename(ORIGINAL_FILENAME)
                .sourceIP(SOURCE_IP)
                .build();

        doThrow(AmqpException.class).when(service).sendMessage("invalidExchangeName", "message.key", message);
        service.sendMessage("invalidExchangeName", "message.key", message);
        verify(service, times(1)).sendMessage("invalidExchangeName", "message.key", message);
    }

    @Test(expected = AmqpException.class)
    public void testErrorWithInvalidRoutingKey() {
        final FileEventMessage message = FileEventMessage.builder()
                .srn(SRN)
                .filename(FILENAME)
                .filesize(FILESIZE)
                .eventDateTime(VALID_DATE)
                .direction(DIRECTION)
                .checksum(CHECKSUM)
                .originalFilename(ORIGINAL_FILENAME)
                .sourceIP(SOURCE_IP)
                .build();

        doThrow(AmqpException.class).when(service).sendMessage("sdes.exchange", "invalidRoutingKey", message);
        service.sendMessage("sdes.exchange", "invalidRoutingKey", message);
        verify(service, times(1)).sendMessage("invalidExchangeName", "message.key", message);
    }

    @Test
    public void testMessagePlacedOnQueueWithCorrectExchangeNameAndRoutingKey() {
        final FileEventMessage message = FileEventMessage.builder()
                .srn(SRN)
                .filename(FILENAME)
                .filesize(FILESIZE)
                .eventDateTime(VALID_DATE)
                .direction(DIRECTION)
                .checksum(CHECKSUM)
                .originalFilename(ORIGINAL_FILENAME)
                .sourceIP(SOURCE_IP)
                .build();

        service.sendMessage("sdes.exchange", "message.key", message);
        verify(service, times(1)).sendMessage("sdes.exchange", "message.key", message);
    }

}
