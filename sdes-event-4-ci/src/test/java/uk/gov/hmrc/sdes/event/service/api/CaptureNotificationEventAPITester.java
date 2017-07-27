package uk.gov.hmrc.sdes.event.service.api;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import uk.gov.hmrc.sdes.event.service.metrics.EventMessagePublisher;
import uk.gov.hmrc.sdes.event.service.dto.notifications.NotificationEventMessage;

@RunWith(SpringRunner.class)
@WebMvcTest(CaptureNotificationEventAPI.class)
public class CaptureNotificationEventAPITester {

    private final static String EMPTY = "";
    private final static String ALL_SPACES = "        ";
    private final static String NEGATIVE_FILE_SIZE = "-1234567";
    private final static String VALID_FILESIZE = "12345678";
    private final static String FILENAME = "filename.zip";
    private final static String SRN = "srn";

    private final static String VALID_AVSTATUS = "Passed";
    private final static String INVALID_AVSTATUS = "FileDone";

    private final static String VALID_TRANSFERSTATUS = "Passed";
    private final static String INVALID_TRANSFERSTATUS = "FileDone";

    private final static String VALID_NOTIFICATION_TYPE = "FileReady";
    private final static String INVALID_NOTIFICATION_TYPE = "FileDone";

    private final static String DOWNLOAD_ATTEMPT = "1";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EventMessagePublisher service;


    //Filesize
    @Test
    public void testBadRequestHTTPStatusWhenFileEventCalledWithNoFileSizeParameter() throws Exception {
        mockMvc.perform(post("/notificationevent")
                .param(ParameterNames.SRN_PARAMETER, SRN)
                .param(ParameterNames.FILENAME_PARAMETER, FILENAME)
                .param(ParameterNames.ACV_STATUS_PARAMETER, VALID_AVSTATUS)
                .param(ParameterNames.TRANSFER_STATUS_PARAMETER, VALID_TRANSFERSTATUS)
                .param(ParameterNames.NOTIFICATION_TYPE_PARAMETER, VALID_NOTIFICATION_TYPE)
                .param(ParameterNames.DOWNLOAD_ATTEMPT_PARAMETER, DOWNLOAD_ATTEMPT)).andExpect(status().isBadRequest());
    }

    @Test
    public void testBadRequestHTTPStatusWhenFileEventCalledWithFileSizeLessThanZero() throws Exception {
        mockMvc.perform(post("/notificationevent")
                .param(ParameterNames.SRN_PARAMETER, SRN)
                .param(ParameterNames.FILENAME_PARAMETER, FILENAME)
                .param(ParameterNames.FILESIZE_PARAMETER, NEGATIVE_FILE_SIZE)
                .param(ParameterNames.ACV_STATUS_PARAMETER, VALID_AVSTATUS)
                .param(ParameterNames.TRANSFER_STATUS_PARAMETER, VALID_TRANSFERSTATUS)
                .param(ParameterNames.NOTIFICATION_TYPE_PARAMETER, VALID_NOTIFICATION_TYPE)
                .param(ParameterNames.DOWNLOAD_ATTEMPT_PARAMETER, DOWNLOAD_ATTEMPT)).andExpect(status().isBadRequest());

    }

    //Filename
    @Test
    public void testBadRequestHTTPStatusWhenFileEventCalledWithNoFilenameParameter() throws Exception {
        mockMvc.perform(post("/notificationevent")
                .param(ParameterNames.SRN_PARAMETER, SRN)
                .param(ParameterNames.FILESIZE_PARAMETER, VALID_FILESIZE)
                .param(ParameterNames.ACV_STATUS_PARAMETER, VALID_AVSTATUS)
                .param(ParameterNames.TRANSFER_STATUS_PARAMETER, VALID_TRANSFERSTATUS)
                .param(ParameterNames.NOTIFICATION_TYPE_PARAMETER, VALID_NOTIFICATION_TYPE)
                .param(ParameterNames.DOWNLOAD_ATTEMPT_PARAMETER, DOWNLOAD_ATTEMPT)).andExpect(status().isBadRequest());
    }

    @Test
    public void testBadRequestHTTPStatusWhenFileEventCalledWithEmptyFilenameParameter() throws Exception {
        mockMvc.perform(post("/notificationevent")
                .param(ParameterNames.SRN_PARAMETER, SRN)
                .param(ParameterNames.FILENAME_PARAMETER, EMPTY)
                .param(ParameterNames.FILESIZE_PARAMETER, VALID_FILESIZE)
                .param(ParameterNames.ACV_STATUS_PARAMETER, VALID_AVSTATUS)
                .param(ParameterNames.TRANSFER_STATUS_PARAMETER, VALID_TRANSFERSTATUS)
                .param(ParameterNames.NOTIFICATION_TYPE_PARAMETER, VALID_NOTIFICATION_TYPE)
                .param(ParameterNames.DOWNLOAD_ATTEMPT_PARAMETER, DOWNLOAD_ATTEMPT)).andExpect(status().isBadRequest());
    }

    @Test
    public void testBadRequestHTTPStatusWhenFileEventCalledWithAllSpacesFilenameParameter() throws Exception {
        mockMvc.perform(post("/notificationevent")
                .param(ParameterNames.SRN_PARAMETER, SRN)
                .param(ParameterNames.FILENAME_PARAMETER, ALL_SPACES)
                .param(ParameterNames.FILESIZE_PARAMETER, VALID_FILESIZE)
                .param(ParameterNames.ACV_STATUS_PARAMETER, VALID_AVSTATUS)
                .param(ParameterNames.TRANSFER_STATUS_PARAMETER, VALID_TRANSFERSTATUS)
                .param(ParameterNames.NOTIFICATION_TYPE_PARAMETER, VALID_NOTIFICATION_TYPE)
                .param(ParameterNames.DOWNLOAD_ATTEMPT_PARAMETER, DOWNLOAD_ATTEMPT)).andExpect(status().isBadRequest());
    }

    //Notification Type
    @Test
    public void testBadRequestHTTPStatusWhenFileEventCalledWithNoNotificationParameter() throws Exception {
        mockMvc.perform(post("/notificationevent")
                .param(ParameterNames.SRN_PARAMETER, SRN)
                .param(ParameterNames.FILENAME_PARAMETER, FILENAME)
                .param(ParameterNames.FILESIZE_PARAMETER, VALID_FILESIZE)
                .param(ParameterNames.ACV_STATUS_PARAMETER, VALID_AVSTATUS)
                .param(ParameterNames.TRANSFER_STATUS_PARAMETER, VALID_TRANSFERSTATUS)
                .param(ParameterNames.DOWNLOAD_ATTEMPT_PARAMETER, DOWNLOAD_ATTEMPT)).andExpect(status().isBadRequest());
    }

    @Test
    public void testBadRequestHTTPStatusWhenFileEventCalledWithInvalidNotificationParameter() throws Exception {
        mockMvc.perform(post("/notificationevent")
                .param(ParameterNames.SRN_PARAMETER, SRN)
                .param(ParameterNames.FILENAME_PARAMETER, FILENAME)
                .param(ParameterNames.FILESIZE_PARAMETER, VALID_FILESIZE)
                .param(ParameterNames.ACV_STATUS_PARAMETER, VALID_AVSTATUS)
                .param(ParameterNames.TRANSFER_STATUS_PARAMETER, VALID_TRANSFERSTATUS)
                .param(ParameterNames.NOTIFICATION_TYPE_PARAMETER, INVALID_NOTIFICATION_TYPE)
                .param(ParameterNames.DOWNLOAD_ATTEMPT_PARAMETER, DOWNLOAD_ATTEMPT)).andExpect(status().isBadRequest());
    }

    //Transfer Status
    @Test
    public void testBadRequestHTTPStatusWhenFileEventCalledWithNoTransferStatusParameter() throws Exception {
        mockMvc.perform(post("/notificationevent")
                .param(ParameterNames.SRN_PARAMETER, SRN)
                .param(ParameterNames.FILENAME_PARAMETER, FILENAME)
                .param(ParameterNames.FILESIZE_PARAMETER, VALID_FILESIZE)
                .param(ParameterNames.ACV_STATUS_PARAMETER, VALID_AVSTATUS)
                .param(ParameterNames.NOTIFICATION_TYPE_PARAMETER, VALID_NOTIFICATION_TYPE)
                .param(ParameterNames.DOWNLOAD_ATTEMPT_PARAMETER, DOWNLOAD_ATTEMPT)).andExpect(status().isBadRequest());
    }

    @Test
    public void testBadRequestHTTPStatusWhenFileEventCalledWithInvalidTransferStatusParameter() throws Exception {
        mockMvc.perform(post("/notificationevent")
                .param(ParameterNames.SRN_PARAMETER, SRN)
                .param(ParameterNames.FILENAME_PARAMETER, FILENAME)
                .param(ParameterNames.FILESIZE_PARAMETER, VALID_FILESIZE)
                .param(ParameterNames.ACV_STATUS_PARAMETER, VALID_AVSTATUS)
                .param(ParameterNames.TRANSFER_STATUS_PARAMETER, INVALID_TRANSFERSTATUS)
                .param(ParameterNames.NOTIFICATION_TYPE_PARAMETER, VALID_NOTIFICATION_TYPE)
                .param(ParameterNames.DOWNLOAD_ATTEMPT_PARAMETER, DOWNLOAD_ATTEMPT)).andExpect(status().isBadRequest());
    }

    //AV Status
    @Test
    public void testBadRequestHTTPStatusWhenFileEventCalledWithNoAVStatusParameter() throws Exception {
        mockMvc.perform(post("/notificationevent")
                .param(ParameterNames.SRN_PARAMETER, SRN)
                .param(ParameterNames.FILENAME_PARAMETER, FILENAME)
                .param(ParameterNames.FILESIZE_PARAMETER, VALID_FILESIZE)
                .param(ParameterNames.TRANSFER_STATUS_PARAMETER, VALID_TRANSFERSTATUS)
                .param(ParameterNames.NOTIFICATION_TYPE_PARAMETER, VALID_NOTIFICATION_TYPE)
                .param(ParameterNames.DOWNLOAD_ATTEMPT_PARAMETER, DOWNLOAD_ATTEMPT)).andExpect(status().isBadRequest());
    }

    @Test
    public void testBadRequestHTTPStatusWhenFileEventCalledWithInvalidAVStatusParameter() throws Exception {
        mockMvc.perform(post("/notificationevent")
                .param(ParameterNames.SRN_PARAMETER, SRN)
                .param(ParameterNames.FILENAME_PARAMETER, FILENAME)
                .param(ParameterNames.FILESIZE_PARAMETER, VALID_FILESIZE)
                .param(ParameterNames.ACV_STATUS_PARAMETER, INVALID_AVSTATUS)
                .param(ParameterNames.TRANSFER_STATUS_PARAMETER, VALID_TRANSFERSTATUS)
                .param(ParameterNames.NOTIFICATION_TYPE_PARAMETER, VALID_NOTIFICATION_TYPE)
                .param(ParameterNames.DOWNLOAD_ATTEMPT_PARAMETER, DOWNLOAD_ATTEMPT)).andExpect(status().isBadRequest());
    }

    //SRN
    @Test
    public void testBadRequestHTTPStatusWhenFileEventCalledWithEmptySRNParameter() throws Exception {
        mockMvc.perform(post("/notificationevent")
                .param(ParameterNames.SRN_PARAMETER, EMPTY)
                .param(ParameterNames.FILENAME_PARAMETER, FILENAME)
                .param(ParameterNames.FILESIZE_PARAMETER, VALID_FILESIZE)
                .param(ParameterNames.ACV_STATUS_PARAMETER, VALID_AVSTATUS)
                .param(ParameterNames.TRANSFER_STATUS_PARAMETER, VALID_TRANSFERSTATUS)
                .param(ParameterNames.NOTIFICATION_TYPE_PARAMETER, VALID_NOTIFICATION_TYPE)
                .param(ParameterNames.DOWNLOAD_ATTEMPT_PARAMETER, DOWNLOAD_ATTEMPT)).andExpect(status().isBadRequest());
    }

    @Test
    public void testBadRequestHTTPStatusWhenFileEventCalledWithAllSpacesSRNParameter() throws Exception {
        mockMvc.perform(post("/notificationevent")
                .param(ParameterNames.SRN_PARAMETER, ALL_SPACES)
                .param(ParameterNames.FILENAME_PARAMETER, FILENAME)
                .param(ParameterNames.FILESIZE_PARAMETER, VALID_FILESIZE)
                .param(ParameterNames.ACV_STATUS_PARAMETER, VALID_AVSTATUS)
                .param(ParameterNames.TRANSFER_STATUS_PARAMETER, VALID_TRANSFERSTATUS)
                .param(ParameterNames.NOTIFICATION_TYPE_PARAMETER, VALID_NOTIFICATION_TYPE)
                .param(ParameterNames.DOWNLOAD_ATTEMPT_PARAMETER, DOWNLOAD_ATTEMPT)).andExpect(status().isBadRequest());
    }

    @Test
    public void testBadRequestHTTPStatusWhenFileEventCalledWithNoSRNParameter() throws Exception {
        mockMvc.perform(post("/notificationevent")
                .param(ParameterNames.FILENAME_PARAMETER, FILENAME)
                .param(ParameterNames.FILESIZE_PARAMETER, VALID_FILESIZE)
                .param(ParameterNames.ACV_STATUS_PARAMETER, VALID_AVSTATUS)
                .param(ParameterNames.TRANSFER_STATUS_PARAMETER, VALID_TRANSFERSTATUS)
                .param(ParameterNames.NOTIFICATION_TYPE_PARAMETER, VALID_NOTIFICATION_TYPE)
                .param(ParameterNames.DOWNLOAD_ATTEMPT_PARAMETER, DOWNLOAD_ATTEMPT)).andExpect(status().isBadRequest());
    }

    //Successful call
    @Test
    public void testOKHTTPStatusWhenFileEventCalledWithValidParameters() throws Exception {
        mockMvc.perform(post("/notificationevent")
                .param(ParameterNames.SRN_PARAMETER, SRN)
                .param(ParameterNames.FILENAME_PARAMETER, FILENAME)
                .param(ParameterNames.FILESIZE_PARAMETER, VALID_FILESIZE)
                .param(ParameterNames.ACV_STATUS_PARAMETER, VALID_AVSTATUS)
                .param(ParameterNames.TRANSFER_STATUS_PARAMETER, VALID_TRANSFERSTATUS)
                .param(ParameterNames.NOTIFICATION_TYPE_PARAMETER, VALID_NOTIFICATION_TYPE)
                .param(ParameterNames.DOWNLOAD_ATTEMPT_PARAMETER, DOWNLOAD_ATTEMPT)).andExpect(status().isOk());

        verify(service, times(1)).sendMessage(Matchers.anyString(), Matchers.anyString(), Matchers.<NotificationEventMessage>anyObject());
    }

    @Test
    public void test500HTTPStatusWhenExceptionInService() throws Exception {
        doThrow(RuntimeException.class).when(service).sendMessage(Matchers.anyString(), Matchers.anyString(), Matchers.<NotificationEventMessage>anyObject());
        mockMvc.perform(post("/notificationevent")
                .param(ParameterNames.SRN_PARAMETER, SRN)
                .param(ParameterNames.FILENAME_PARAMETER, FILENAME)
                .param(ParameterNames.FILESIZE_PARAMETER, VALID_FILESIZE)
                .param(ParameterNames.ACV_STATUS_PARAMETER, VALID_AVSTATUS)
                .param(ParameterNames.TRANSFER_STATUS_PARAMETER, VALID_TRANSFERSTATUS)
                .param(ParameterNames.NOTIFICATION_TYPE_PARAMETER, VALID_NOTIFICATION_TYPE)
                .param(ParameterNames.DOWNLOAD_ATTEMPT_PARAMETER, DOWNLOAD_ATTEMPT)).andExpect(status().isInternalServerError());

        verify(service, times(1)).sendMessage(Matchers.anyString(), Matchers.anyString(), Matchers.<NotificationEventMessage>anyObject());
    }

}
