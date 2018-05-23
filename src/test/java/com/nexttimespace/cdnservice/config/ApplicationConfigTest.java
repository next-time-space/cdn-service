package com.nexttimespace.cdnservice.config;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import com.nexttimespace.cdnservice.TestUtils;
import com.nexttimespace.cdnservice.utility.UtilityComponent;

public class ApplicationConfigTest {
	
	ApplicationConfig applicationConfig = new ApplicationConfig();
	@Test
	public void servletContainerTest() {
		UtilityComponent utilityComponent = new UtilityComponent();
		
		ReflectionTestUtils.setField(utilityComponent, "confProperties", TestUtils.getClonedPropety());
		
		ReflectionTestUtils.setField(applicationConfig, "utilityComponent", utilityComponent);
		Assertions.assertNotNull(applicationConfig.servletContainer());
	}

}
