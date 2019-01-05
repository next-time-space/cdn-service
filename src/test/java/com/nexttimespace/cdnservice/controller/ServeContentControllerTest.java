package com.nexttimespace.cdnservice.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.util.ReflectionTestUtils;

import com.nexttimespace.cdnservice.TestUtils;
import com.nexttimespace.cdnservice.publisher.MasterPublisher;
import com.nexttimespace.cdnservice.reader.MasterReader;
import com.nexttimespace.cdnservice.reader.ReaderUtility;
import com.nexttimespace.cdnservice.reader.TrafficRouter;
import com.nexttimespace.cdnservice.utility.UtilityComponent;

public class ServeContentControllerTest {

	@Test
	public void publishToDirectoryTest() throws IOException, ServletException {
		ServeContentController contentController = new ServeContentController();
		UtilityComponent utilityComponent = new UtilityComponent();
		MasterPublisher masterPublisher = Mockito.mock(MasterPublisher.class);
		Mockito.doReturn(true).when(masterPublisher).publish(Mockito.any(InputStream.class), Mockito.anyString(), Mockito.anyString());
		
		ReflectionTestUtils.setField(utilityComponent, "confProperties", TestUtils.getClonedPropety());
		
		ReflectionTestUtils.setField(contentController, "utilityComponent", utilityComponent);
		ReflectionTestUtils.setField(contentController, "masterPublisher", masterPublisher);
		
		contentController.setup();
		
		HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
		Part part = Mockito.mock(Part.class);
		Mockito.doReturn(8443).when(request).getServerPort();
		Mockito.doReturn(part).when(request).getPart("file");
		MockHttpServletResponse response = new MockHttpServletResponse();
		
		ResponseEntity<String> publishRes = contentController.publishToDirectory("cdn", "/cdn", request, response);
		Assertions.assertEquals(200, publishRes.getStatusCodeValue());
		Assertions.assertEquals("Published successfully", publishRes.getBody());
	}
	
	@Test
	public void publishToDirectoryTestErrorCase() throws IOException, ServletException {
		ServeContentController contentController = new ServeContentController();
		UtilityComponent utilityComponent = new UtilityComponent();
		MasterPublisher masterPublisher = Mockito.mock(MasterPublisher.class);
		Mockito.doReturn(true).when(masterPublisher).publish(Mockito.any(InputStream.class), Mockito.anyString(), Mockito.anyString());
		
		ReflectionTestUtils.setField(utilityComponent, "confProperties", TestUtils.getClonedPropety());
		
		ReflectionTestUtils.setField(contentController, "utilityComponent", utilityComponent);
		ReflectionTestUtils.setField(contentController, "masterPublisher", masterPublisher);
		
		contentController.setup();
		
		HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
		Part part = Mockito.mock(Part.class);
		Mockito.doReturn(8000).when(request).getServerPort();
		Mockito.doReturn(part).when(request).getPart("file");
		MockHttpServletResponse response = new MockHttpServletResponse();
		
		// Bad port
		ResponseEntity<String> publishRes = contentController.publishToDirectory("cdn", "/cdn", request, response);
		Assertions.assertEquals(404, publishRes.getStatusCodeValue());
		Assertions.assertEquals("", publishRes.getBody());
		
		// No file
		Mockito.doReturn(8443).when(request).getServerPort();
		Mockito.doReturn(null).when(request).getPart("file");
		publishRes = contentController.publishToDirectory("cdn", "/cdn", request, response);
		Assertions.assertEquals(400, publishRes.getStatusCodeValue());
		Assertions.assertEquals("file or path or alias request attribute missing", publishRes.getBody());
		
		// No path
		Mockito.doReturn(part).when(request).getPart("file");
		publishRes = contentController.publishToDirectory("cdn", "", request, response);
		Assertions.assertEquals(400, publishRes.getStatusCodeValue());
		Assertions.assertEquals("file or path or alias request attribute missing", publishRes.getBody());
		
		publishRes = contentController.publishToDirectory("cdn", null, request, response);
		Assertions.assertEquals(400, publishRes.getStatusCodeValue());
		Assertions.assertEquals("file or path or alias request attribute missing", publishRes.getBody());
		
		// No alias
		publishRes = contentController.publishToDirectory("", "/cdn", request, response);
		Assertions.assertEquals(400, publishRes.getStatusCodeValue());
		Assertions.assertEquals("file or path or alias request attribute missing", publishRes.getBody());
		
		publishRes = contentController.publishToDirectory(null, "/cdn", request, response);
		Assertions.assertEquals(400, publishRes.getStatusCodeValue());
		Assertions.assertEquals("file or path or alias request attribute missing", publishRes.getBody());
		
		// publish disabled
		Mockito.doReturn(false).when(masterPublisher).publish(Mockito.any(InputStream.class), Mockito.anyString(), Mockito.anyString());
		publishRes = contentController.publishToDirectory("cdn", "/cdn", request, response);
		Assertions.assertEquals(200, publishRes.getStatusCodeValue());
		Assertions.assertEquals("Publish disabled", publishRes.getBody());
		
		// publish disabled
		Mockito.doThrow(new IOException("wrong")).when(masterPublisher).publish(Mockito.any(InputStream.class), Mockito.anyString(), Mockito.anyString());
		publishRes = contentController.publishToDirectory("cdn", "/cdn", request, response);
		Assertions.assertEquals(500, publishRes.getStatusCodeValue());
		Assertions.assertEquals("Publish failed due to internal error", publishRes.getBody());
		
	}
	
	@Test
	public void serveContentTest() throws Exception {
		ServeContentController contentController = new ServeContentController();
		UtilityComponent utilityComponent = new UtilityComponent();
		TrafficRouter trafficManager = Mockito.mock(TrafficRouter.class);
		Mockito.doReturn("cdn").when(trafficManager).getReader();
		
		MasterReader reader = Mockito.mock(MasterReader.class);
		Mockito.doReturn(new String[] {"cdn content", "true"}).when(reader).getContent(Mockito.anyString(), Mockito.anyString());
		List<String[]> headers = new ArrayList<>();
		headers.add(new String[] {"key", "value"});
		Mockito.doReturn(headers).when(reader).getResponseHeader(Mockito.anyString());
		
		ReaderUtility readerUtility = Mockito.mock(ReaderUtility.class);
		Mockito.doReturn(reader).when(readerUtility).findReader(Mockito.anyString());
		
		
		ReflectionTestUtils.setField(utilityComponent, "confProperties", TestUtils.getClonedPropety());
		ReflectionTestUtils.setField(contentController, "utilityComponent", utilityComponent);
		ReflectionTestUtils.setField(contentController, "trafficManager", trafficManager);
		ReflectionTestUtils.setField(contentController, "readerUtility", readerUtility);
		
		contentController.setup();
		
		HttpServletRequest request = Mockito.mock(HttpServletRequest.class);
		Mockito.doReturn(8080).when(request).getServerPort();
		Mockito.doReturn("pop.js").when(request).getRequestURI();
		Mockito.doReturn("").when(request).getContextPath();
		MockHttpServletResponse response = new MockHttpServletResponse();
		
		ResponseEntity<String> readRes = contentController.serveContent(request, response);
		Assertions.assertEquals("cdn content", readRes.getBody());
		Assertions.assertEquals(200, readRes.getStatusCodeValue());
		HttpHeaders resHeader = readRes.getHeaders();
		Assertions.assertEquals("value", resHeader.get("key").get(0));
		
		// No headers
		headers.clear();
		readRes = contentController.serveContent(request, response);
		Assertions.assertEquals("cdn content", readRes.getBody());
		Assertions.assertEquals(200, readRes.getStatusCodeValue());
		Assertions.assertEquals(1, readRes.getHeaders().size());
		
		// No headers
		headers = null;
		readRes = contentController.serveContent(request, response);
		Assertions.assertEquals("cdn content", readRes.getBody());
		Assertions.assertEquals(200, readRes.getStatusCodeValue());
		Assertions.assertEquals(1, readRes.getHeaders().size());

		// Error case - bad port
		Mockito.doReturn(8443).when(request).getServerPort();
		readRes = contentController.serveContent(request, response);
		Assertions.assertEquals("", readRes.getBody());
		Assertions.assertEquals(404, readRes.getStatusCodeValue());
		
		// Error case - exception on read
		Mockito.doReturn(8080).when(request).getServerPort();
		Mockito.doThrow(new Exception("wrong cdn")).when(trafficManager).getReader();
		readRes = contentController.serveContent(request, response);
		Assertions.assertEquals("", readRes.getBody());
		Assertions.assertEquals(404, readRes.getStatusCodeValue());
		
		
	}
}
