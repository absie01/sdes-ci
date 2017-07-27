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
import uk.gov.hmrc.sdes.event.service.dto.file.FileEventMessage;

@RunWith(SpringRunner.class)
@WebMvcTest(CaptureFileEventAPI.class)
public class CaptureFileEventAPITester {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private EventMessagePublisher service;


	private final static String EMPTY="";
	private final static String ALL_SPACES="        ";
	private final static String NEGATIVE_FILE_SIZE= "-1234567";
	private final static String VALID_FILESIZE="12345678";
	private final static String FILENAME="filename.zip";
	private final static String SRN="srn";
	private final static String DIRECTION="Outbound";
	private final static String VALID_DATE="2015-09-11 11:20:13";
	private final static String INVALID_DATE="11-09-2015 11:20:13";
	private final static String SOURCE_IP="20.13.15.16";
	private final static String INVALID_SOURCE_IP="1.13.15.16";
	private final static String ORIGINAL_FILENAME="ORIG_FILENAME.ZIP";
	private final static String CHECKSUM="XSAJKHDJANJDA==831HDYD8DAJ";
	
	//Filesize
	
	@Test
	public void testBadRequestHTTPStatusWhenFileEventCalledWithNoFileSizeParameter() throws Exception {
		mockMvc.perform(post("/fileevent").param(ParameterNames.FILENAME_PARAMETER, FILENAME)
				.param(ParameterNames.SRN_PARAMETER, SRN)
				.param(ParameterNames.DIRECTION_PARAMETER, DIRECTION)
				.param(ParameterNames.EVENT_DATE_PARAMETER, VALID_DATE)).andExpect(status().isBadRequest());
	}
	
	@Test
	public void testBadRequestHTTPStatusWhenFileEventCalledWithFileSizeLessThanZero() throws Exception {
		mockMvc.perform(post("/fileevent").param(ParameterNames.FILENAME_PARAMETER, FILENAME)
				.param(ParameterNames.SRN_PARAMETER, SRN).param(ParameterNames.FILESIZE_PARAMETER, NEGATIVE_FILE_SIZE)
				.param(ParameterNames.DIRECTION_PARAMETER, DIRECTION)
				.param(ParameterNames.EVENT_DATE_PARAMETER, VALID_DATE)).andExpect(status().isBadRequest());
	}
	
	
	//Filename
	
	@Test
	public void testBadRequestHTTPStatusWhenFileEventCalledWithNoFilenameParameter() throws Exception {
		mockMvc.perform(post("/fileevent")
				.param(ParameterNames.SRN_PARAMETER, SRN).param(ParameterNames.FILESIZE_PARAMETER, VALID_FILESIZE)
				.param(ParameterNames.DIRECTION_PARAMETER, DIRECTION)
				.param(ParameterNames.EVENT_DATE_PARAMETER, VALID_DATE)).andExpect(status().isBadRequest());
	}
	
	@Test
	public void testBadRequestHTTPStatusWhenFileEventCalledWithEmptyFilenameParameter() throws Exception {
		mockMvc.perform(post("/fileevent").param(ParameterNames.FILENAME_PARAMETER, EMPTY)
				.param(ParameterNames.SRN_PARAMETER, SRN).param(ParameterNames.FILESIZE_PARAMETER, VALID_FILESIZE)
				.param(ParameterNames.DIRECTION_PARAMETER, DIRECTION)
				.param(ParameterNames.EVENT_DATE_PARAMETER, VALID_DATE)).andExpect(status().isBadRequest());
	}
	
	@Test
	public void testBadRequestHTTPStatusWhenFileEventCalledWithAllSpacesFilenameParameter() throws Exception {
		mockMvc.perform(post("/fileevent").param(ParameterNames.FILENAME_PARAMETER, ALL_SPACES)
				.param(ParameterNames.SRN_PARAMETER, SRN).param(ParameterNames.FILESIZE_PARAMETER, VALID_FILESIZE)
				.param(ParameterNames.DIRECTION_PARAMETER, DIRECTION)
				.param(ParameterNames.EVENT_DATE_PARAMETER, VALID_DATE)).andExpect(status().isBadRequest());
	}
	
	
	//Date
	
	@Test
	public void testBadRequestHTTPStatusWhenFileEventCalledWithNoDateParameter() throws Exception {
		mockMvc.perform(post("/fileevent").param(ParameterNames.FILENAME_PARAMETER, FILENAME)
				.param(ParameterNames.SRN_PARAMETER, SRN).param(ParameterNames.FILESIZE_PARAMETER, VALID_FILESIZE)
				.param(ParameterNames.DIRECTION_PARAMETER, DIRECTION)).andExpect(status().isBadRequest());
	}
	
	@Test
	public void testBadRequestHTTPStatusWhenFileEventCalledWithInvalidDateFormatParameter() throws Exception {
		mockMvc.perform(post("/fileevent").param(ParameterNames.FILENAME_PARAMETER, FILENAME)
				.param(ParameterNames.SRN_PARAMETER, SRN).param(ParameterNames.FILESIZE_PARAMETER, VALID_FILESIZE)
				.param(ParameterNames.DIRECTION_PARAMETER, DIRECTION)
				.param(ParameterNames.EVENT_DATE_PARAMETER, INVALID_DATE)).andExpect(status().isBadRequest());
	}
	
	
	//Direction
	
	@Test
	public void testBadRequestHTTPStatusWhenFileEventCalledWithEmptyDirectionParameter() throws Exception {
		mockMvc.perform(post("/fileevent").param(ParameterNames.FILENAME_PARAMETER, FILENAME)
				.param(ParameterNames.SRN_PARAMETER, SRN).param(ParameterNames.FILESIZE_PARAMETER, VALID_FILESIZE)
				.param(ParameterNames.EVENT_DATE_PARAMETER, VALID_DATE)
				.param(ParameterNames.DIRECTION_PARAMETER, EMPTY)).andExpect(status().isBadRequest());
	}
	
	@Test
	public void testBadRequestHTTPStatusWhenFileEventCalledWithAllSpacesDirectionParameter() throws Exception {
		mockMvc.perform(post("/fileevent").param(ParameterNames.FILENAME_PARAMETER, FILENAME)
				.param(ParameterNames.SRN_PARAMETER, SRN).param(ParameterNames.FILESIZE_PARAMETER, VALID_FILESIZE)
				.param(ParameterNames.EVENT_DATE_PARAMETER, VALID_DATE)
				.param(ParameterNames.DIRECTION_PARAMETER, ALL_SPACES)).andExpect(status().isBadRequest());
	}

	
	//SRN
	
	@Test
	public void testBadRequestHTTPStatusWhenFileEventCalledWithEmptySRNParameter() throws Exception {
		mockMvc.perform(post("/fileevent").param(ParameterNames.FILENAME_PARAMETER, "filename.zip")
				.param(ParameterNames.SRN_PARAMETER, EMPTY).param(ParameterNames.FILESIZE_PARAMETER, VALID_FILESIZE)
				.param(ParameterNames.DIRECTION_PARAMETER, DIRECTION)
				.param(ParameterNames.CHECKSUM, CHECKSUM)
				.param(ParameterNames.SOURCE_IP, SOURCE_IP)
				.param(ParameterNames.EVENT_DATE_PARAMETER, VALID_DATE)
				).andExpect(status().isBadRequest());
	}

	@Test
	public void testBadRequestHTTPStatusWhenFileEventCalledWithAllSpacesSRNParameter() throws Exception {
		mockMvc.perform(post("/fileevent").param(ParameterNames.FILENAME_PARAMETER, FILENAME)
				.param(ParameterNames.SRN_PARAMETER, ALL_SPACES).param(ParameterNames.FILESIZE_PARAMETER, VALID_FILESIZE)
				.param(ParameterNames.DIRECTION_PARAMETER, DIRECTION)
				.param(ParameterNames.EVENT_DATE_PARAMETER, VALID_DATE)).andExpect(status().isBadRequest());
	}
	
	@Test
	public void testBadRequestHTTPStatusWhenFileEventCalledWithNoSRNParameter() throws Exception {
		mockMvc.perform(post("/fileevent").param(ParameterNames.FILENAME_PARAMETER, FILENAME)
				.param(ParameterNames.FILESIZE_PARAMETER, VALID_FILESIZE)
				.param(ParameterNames.DIRECTION_PARAMETER, DIRECTION)
				.param(ParameterNames.CHECKSUM, CHECKSUM)
				.param(ParameterNames.SOURCE_IP, SOURCE_IP)
				.param(ParameterNames.EVENT_DATE_PARAMETER, VALID_DATE)).andExpect(status().isBadRequest());
	}
	
	//Original Filename
	
	@Test
	public void testBadRequestHTTPStatusWhenFileEventCalledWithNoOriginalFilenameParameter() throws Exception {
		mockMvc.perform(post("/fileevent").param(ParameterNames.FILENAME_PARAMETER, FILENAME)
				.param(ParameterNames.SRN_PARAMETER, SRN)
				.param(ParameterNames.FILESIZE_PARAMETER, VALID_FILESIZE)
				.param(ParameterNames.DIRECTION_PARAMETER, DIRECTION)
				.param(ParameterNames.CHECKSUM, CHECKSUM)
				.param(ParameterNames.SOURCE_IP, SOURCE_IP)
				.param(ParameterNames.EVENT_DATE_PARAMETER, VALID_DATE)).andExpect(status().isOk());
	}
	
	@Test
	public void testBadRequestHTTPStatusWhenFileEventCalledWithEmptyOriginalFilenameParameter() throws Exception {
		mockMvc.perform(post("/fileevent").param(ParameterNames.FILENAME_PARAMETER, FILENAME)
				.param(ParameterNames.SRN_PARAMETER, SRN)
				.param(ParameterNames.FILESIZE_PARAMETER, VALID_FILESIZE)
				.param(ParameterNames.DIRECTION_PARAMETER, DIRECTION)
				.param(ParameterNames.EVENT_DATE_PARAMETER, VALID_DATE)
				.param(ParameterNames.CHECKSUM, CHECKSUM)
				.param(ParameterNames.SOURCE_IP, SOURCE_IP)
				.param(ParameterNames.ORIG_FILENAME, EMPTY)).andExpect(status().isBadRequest());
	}
	
	@Test
	public void testBadRequestHTTPStatusWhenFileEventCalledWithAllSpacesOriginalFilenameParameter() throws Exception {
		mockMvc.perform(post("/fileevent").param(ParameterNames.FILENAME_PARAMETER, FILENAME)
				.param(ParameterNames.SRN_PARAMETER, SRN)
				.param(ParameterNames.FILESIZE_PARAMETER, VALID_FILESIZE)
				.param(ParameterNames.DIRECTION_PARAMETER, DIRECTION)
				.param(ParameterNames.EVENT_DATE_PARAMETER, VALID_DATE)
				.param(ParameterNames.CHECKSUM, CHECKSUM)
				.param(ParameterNames.SOURCE_IP, SOURCE_IP)
				.param(ParameterNames.ORIG_FILENAME, ALL_SPACES)).andExpect(status().isBadRequest());
	}
	
	//Source IP
	public void testBadRequestHTTPStatusWhenFileEventCalledWithInValidSourceIPFormatParameter() throws Exception {
		mockMvc.perform(post("/fileevent").param(ParameterNames.FILENAME_PARAMETER, FILENAME)
				.param(ParameterNames.SRN_PARAMETER, SRN)
				.param(ParameterNames.FILESIZE_PARAMETER, VALID_FILESIZE)
				.param(ParameterNames.DIRECTION_PARAMETER, DIRECTION)
				.param(ParameterNames.EVENT_DATE_PARAMETER, VALID_DATE)
				.param(ParameterNames.ORIG_FILENAME, FILENAME)
				.param(ParameterNames.SOURCE_IP, INVALID_SOURCE_IP )).andExpect(status().isBadRequest());
	}
	
	public void testBadRequestHTTPStatusWhenFileEventCalledWithEmptySourceIPParameter() throws Exception {
		mockMvc.perform(post("/fileevent").param(ParameterNames.FILENAME_PARAMETER, FILENAME)
				.param(ParameterNames.SRN_PARAMETER, SRN)
				.param(ParameterNames.FILESIZE_PARAMETER, VALID_FILESIZE)
				.param(ParameterNames.DIRECTION_PARAMETER, DIRECTION)
				.param(ParameterNames.EVENT_DATE_PARAMETER, VALID_DATE)
				.param(ParameterNames.ORIG_FILENAME, FILENAME)
				.param(ParameterNames.SOURCE_IP, EMPTY )).andExpect(status().isBadRequest());
	}
	
	public void testBadRequestHTTPStatusWhenFileEventCalledWithAllSpacesSourceIPParameter() throws Exception {
		mockMvc.perform(post("/fileevent").param(ParameterNames.FILENAME_PARAMETER, FILENAME)
				.param(ParameterNames.SRN_PARAMETER, SRN)
				.param(ParameterNames.FILESIZE_PARAMETER, VALID_FILESIZE)
				.param(ParameterNames.DIRECTION_PARAMETER, DIRECTION)
				.param(ParameterNames.EVENT_DATE_PARAMETER, VALID_DATE)
				.param(ParameterNames.ORIG_FILENAME, FILENAME)
				.param(ParameterNames.SOURCE_IP, ALL_SPACES )).andExpect(status().isBadRequest());
	}
	
	public void testBadRequestHTTPStatusWhenFileEventCalledNoSourceIPParameter() throws Exception {
		mockMvc.perform(post("/fileevent").param(ParameterNames.FILENAME_PARAMETER, FILENAME)
				.param(ParameterNames.SRN_PARAMETER, SRN)
				.param(ParameterNames.FILESIZE_PARAMETER, VALID_FILESIZE)
				.param(ParameterNames.DIRECTION_PARAMETER, DIRECTION)
				.param(ParameterNames.EVENT_DATE_PARAMETER, VALID_DATE)
				.param(ParameterNames.ORIG_FILENAME, FILENAME)
				).andExpect(status().isOk());
	}

	
	//Successful call
	
	@Test
	public void testBadRequestHTTPStatusWhenFileEventCalledWithRequiredParametersOnly() throws Exception {
		mockMvc.perform(post("/fileevent").param(ParameterNames.FILENAME_PARAMETER, FILENAME)
				.param(ParameterNames.SRN_PARAMETER, SRN)
				.param(ParameterNames.FILESIZE_PARAMETER, VALID_FILESIZE)
				.param(ParameterNames.EVENT_DATE_PARAMETER, VALID_DATE)).andExpect(status().isOk());
		verify(service, times(1)).sendMessage(Matchers.anyString(), Matchers.anyString(), Matchers.<FileEventMessage>anyObject());
	}

	@Test
	public void testOKHTTPStatusWhenFileEventCalledWithAllValidParameters() throws Exception {
		mockMvc.perform(post("/fileevent").param(ParameterNames.FILENAME_PARAMETER, FILENAME)
				.param(ParameterNames.SRN_PARAMETER, SRN).param(ParameterNames.FILESIZE_PARAMETER, VALID_FILESIZE)
				.param(ParameterNames.DIRECTION_PARAMETER, DIRECTION)
				.param(ParameterNames.EVENT_DATE_PARAMETER, VALID_DATE)
				.param(ParameterNames.ORIG_FILENAME, ORIGINAL_FILENAME)
				.param(ParameterNames.SOURCE_IP, SOURCE_IP)
				.param(ParameterNames.CHECKSUM, CHECKSUM)).andExpect(status().isOk());
		verify(service, times(1)).sendMessage(Matchers.anyString(), Matchers.anyString(), Matchers.<FileEventMessage>anyObject());
	}
	
	@Test
	public void testOKHTTPStatusWhenFileEventCalledWithValidParametersButNoIPAddress() throws Exception {
		mockMvc.perform(post("/fileevent").param(ParameterNames.FILENAME_PARAMETER, FILENAME)
				.param(ParameterNames.SRN_PARAMETER, SRN).param(ParameterNames.FILESIZE_PARAMETER, VALID_FILESIZE)
				.param(ParameterNames.DIRECTION_PARAMETER, DIRECTION)
				.param(ParameterNames.EVENT_DATE_PARAMETER, VALID_DATE)
				.param(ParameterNames.ORIG_FILENAME, ORIGINAL_FILENAME)
				.param(ParameterNames.CHECKSUM, CHECKSUM)).andExpect(status().isOk());
		verify(service, times(1)).sendMessage(Matchers.anyString(), Matchers.anyString(), Matchers.<FileEventMessage>anyObject());
	}
	
	@Test
	public void testInternalServerErrorHTTPStatusWhenExceptionInService() throws Exception{
		doThrow(RuntimeException.class).when(service).sendMessage(Matchers.anyString(), Matchers.anyString(), Matchers.<FileEventMessage>anyObject());
		mockMvc.perform(post("/fileevent").param(ParameterNames.FILENAME_PARAMETER, FILENAME)
				.param(ParameterNames.SRN_PARAMETER, SRN).param(ParameterNames.FILESIZE_PARAMETER, VALID_FILESIZE)
				.param(ParameterNames.DIRECTION_PARAMETER, DIRECTION)
				.param(ParameterNames.EVENT_DATE_PARAMETER, VALID_DATE)
				.param(ParameterNames.ORIG_FILENAME, ORIGINAL_FILENAME)
				.param(ParameterNames.CHECKSUM, CHECKSUM)).andExpect(status().isInternalServerError());
		verify(service, times(1)).sendMessage(Matchers.anyString(), Matchers.anyString(), Matchers.<FileEventMessage>anyObject());

	}

	

}
