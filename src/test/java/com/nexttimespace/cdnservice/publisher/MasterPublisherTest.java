package com.nexttimespace.cdnservice.publisher;

import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import com.nexttimespace.cdnservice.TestUtils;
import com.nexttimespace.cdnservice.utility.UtilityComponent;

public class MasterPublisherTest {
	
	@Test
	public void publishTest() throws IOException {
		DirectoryPublisher directoryPublisher = new DirectoryPublisher();
		MasterPublisher masterPublisher = new MasterPublisher();
		UtilityComponent utilityComponent = new UtilityComponent();
		
		ReflectionTestUtils.setField(utilityComponent, "confProperties", TestUtils.getClonedPropety());
		ReflectionTestUtils.setField(directoryPublisher, "utilityComponent", utilityComponent);
		
		ReflectionTestUtils.setField(masterPublisher, "utilityComponent", utilityComponent);
		ReflectionTestUtils.setField(masterPublisher, "directoryPublisher", directoryPublisher);
		Assertions.assertTrue(masterPublisher.publish(IOUtils.toInputStream("testFile", Charset.defaultCharset()), "cdn1", "/test.js"));
		Assertions.assertTrue(masterPublisher.publish(IOUtils.toInputStream("testFile", Charset.defaultCharset()), "cdn1", "test.js"));
		
		utilityComponent.getConfProperties().setProperty("repo[0].type", "NA");
		Assertions.assertTrue(masterPublisher.publish(IOUtils.toInputStream("testFile", Charset.defaultCharset()), "cdn1", "/test.js"));
		
		
		utilityComponent.getConfProperties().setProperty("repo[0].allow-publish", "false");
		Assertions.assertFalse(masterPublisher.publish(IOUtils.toInputStream("testFile", Charset.defaultCharset()), "cdn1", "/test.js"));
		
		
		
	}

}
