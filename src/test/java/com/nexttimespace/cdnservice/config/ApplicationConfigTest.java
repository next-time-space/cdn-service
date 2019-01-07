package com.nexttimespace.cdnservice.config;

import java.util.Properties;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.test.util.ReflectionTestUtils;

import com.nexttimespace.cdnservice.TestUtils;
import com.nexttimespace.cdnservice.utility.UtilityComponent;

public class ApplicationConfigTest {
	
	ApplicationConfig applicationConfig = new ApplicationConfig();
	@Test
	public void testCustomize() {
		UtilityComponent utilityComponent = new UtilityComponent();
		Properties properties = TestUtils.getClonedPropety();
		ReflectionTestUtils.setField(utilityComponent, "confProperties",properties);
		
		ReflectionTestUtils.setField(applicationConfig, "utilityComponent", utilityComponent);
		TomcatServletWebServerFactory factory = new TomcatServletWebServerFactory();
		applicationConfig.customize(factory);
		Assertions.assertEquals(1, factory.getAdditionalTomcatConnectors().size());
		
		factory = new TomcatServletWebServerFactory();
		properties.setProperty("server.ssl-config.port", "");
		applicationConfig.customize(factory);
        Assertions.assertEquals(0, factory.getAdditionalTomcatConnectors().size());
		
		properties.setProperty("server.contextPath", "/cdn");
		properties.setProperty("server.compression.enabled", "true");
		properties.setProperty("server.compression.mime-types", "text/html,image/png");
		properties.setProperty("server.compression.min-response-size", "1024");
		ReflectionTestUtils.setField(utilityComponent, "confProperties", properties);
		applicationConfig.customize(factory);
		
		properties.setProperty("server.compression.min-response-size", "");
		properties.setProperty("server.contextPath", "cdn");
		applicationConfig.customize(factory);
		Assertions.assertNotNull(factory.getCompression());
		
	}

}
