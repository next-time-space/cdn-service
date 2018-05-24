package com.nexttimespace.cdnservice.publisher;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import com.nexttimespace.cdnservice.TestUtils;
import com.nexttimespace.cdnservice.utility.UtilityComponent;

public class DirectoryPublisherTest {
	
	@AfterAll
	public static void deletTmpFile() {
		new File("/tmp/test.js").delete();
	}

	@Test
	public void publishTest() throws IOException {
		DirectoryPublisher directoryPublisher = new DirectoryPublisher();
		UtilityComponent utilityComponent = new UtilityComponent();
		
		ReflectionTestUtils.setField(utilityComponent, "confProperties", TestUtils.getClonedPropety());
		ReflectionTestUtils.setField(directoryPublisher, "utilityComponent", utilityComponent);
		directoryPublisher.publish(IOUtils.toInputStream("testFile", Charset.defaultCharset()), "cdn1", "/test.js", "repo[0]");
		
		utilityComponent.getConfProperties().setProperty("repo[0].directory.path", "/tmp/");
		directoryPublisher.publish(IOUtils.toInputStream("testFile", Charset.defaultCharset()), "cdn1", "test.js", "repo[0]");
		
		
	}
}
