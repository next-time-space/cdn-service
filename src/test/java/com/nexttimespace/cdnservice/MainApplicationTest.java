package com.nexttimespace.cdnservice;

import java.util.Properties;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class MainApplicationTest {
	
	@Test
	public void validateConfigurationTest() {
		Properties properties = null;
		Assertions.assertFalse(MainApplication.validateConfiguration(properties));
		
		properties = TestUtils.getClonedPropety();
		Assertions.assertTrue(MainApplication.validateConfiguration(properties));
	}
	
	@Test
	public void validateConfigurationTestServerConfigNegativeCase() {
		Properties properties = TestUtils.getClonedPropety();
		properties.setProperty("server.http.port", "abc");
		Assertions.assertFalse(MainApplication.validateConfiguration(properties));
		
		properties = TestUtils.getClonedPropety();
		properties.setProperty("server.ssl-config.port", "abc");
		Assertions.assertTrue(MainApplication.validateConfiguration(properties));
		
		properties = TestUtils.getClonedPropety();
		properties.setProperty("server.ssl-config.key-store", "abc");
		Assertions.assertTrue(MainApplication.validateConfiguration(properties));
		
		properties = TestUtils.getClonedPropety();
		properties.setProperty("server.ssl-config.trust-store", "abc");
		Assertions.assertTrue(MainApplication.validateConfiguration(properties));
		
		properties = TestUtils.getClonedPropety();
		properties.setProperty("server.ssl-config.key-store-password", "");
		Assertions.assertTrue(MainApplication.validateConfiguration(properties));
		
		properties = TestUtils.getClonedPropety();
		properties.setProperty("server.ssl-config.trust-store-password", "");
		Assertions.assertTrue(MainApplication.validateConfiguration(properties));
	}
	
	@Test
	public void validateConfigurationTestRepoConfig() {
		Properties properties = TestUtils.getClonedPropety();
		properties.setProperty("repo[0].type", "abc");
		Assertions.assertFalse(MainApplication.validateConfiguration(properties));
		
		properties = TestUtils.getClonedPropety();
		properties.setProperty("repo[0].alias", "");
		Assertions.assertFalse(MainApplication.validateConfiguration(properties));
		
		properties = TestUtils.getClonedPropety();
		properties.setProperty("repo[0].allow-publish", "");
		Assertions.assertFalse(MainApplication.validateConfiguration(properties));
		
		properties = TestUtils.getClonedPropety();
		properties.setProperty("repo[0].allow-publish", "true");
		Assertions.assertTrue(MainApplication.validateConfiguration(properties));
		
		properties = TestUtils.getClonedPropety();
		properties.setProperty("repo[0].allow-publish", "false");
		Assertions.assertTrue(MainApplication.validateConfiguration(properties));
		
		properties = TestUtils.getClonedPropety();
		properties.setProperty("repo[0].traffic", "abc");
		Assertions.assertFalse(MainApplication.validateConfiguration(properties));
		
		properties = TestUtils.getClonedPropety();
		properties.setProperty("repo[0].traffic", "80");
		Assertions.assertFalse(MainApplication.validateConfiguration(properties));
		
		properties = TestUtils.getClonedPropety();
		properties.setProperty("repo[0].directory.path", "abc");
		Assertions.assertFalse(MainApplication.validateConfiguration(properties));
		
		properties = TestUtils.getClonedPropety();
		properties.setProperty("repo[0].response.header", "abc");
		Assertions.assertFalse(MainApplication.validateConfiguration(properties));
		
		properties = TestUtils.getClonedPropety();
		properties.setProperty("repo[0].response.header", "");
		Assertions.assertTrue(MainApplication.validateConfiguration(properties));
		
		properties = TestUtils.getClonedPropety();
		properties.setProperty("repo[0].response.header", "|");
		Assertions.assertTrue(MainApplication.validateConfiguration(properties));
		
		properties = TestUtils.getClonedPropety();
		properties.setProperty("repo[0].response.header", "abc|cdb");
		Assertions.assertFalse(MainApplication.validateConfiguration(properties));
		
		properties = TestUtils.getClonedPropety();
		properties.setProperty("repo[0].response.header", "abc:|cdb:");
		Assertions.assertFalse(MainApplication.validateConfiguration(properties));
		
		properties = TestUtils.getClonedPropety();
		properties.remove("repo[0].response.header");
		Assertions.assertTrue(MainApplication.validateConfiguration(properties));
		
		properties = TestUtils.getClonedPropety();
		properties.setProperty("repo[0].cache-manager.enable", "false");
		Assertions.assertTrue(MainApplication.validateConfiguration(properties));
		
		properties = TestUtils.getClonedPropety();
		properties.setProperty("repo[0].cache-manager.enable", "abc");
		Assertions.assertTrue(MainApplication.validateConfiguration(properties));
		
		properties = TestUtils.getClonedPropety();
		properties.remove("repo[0].cache-manager.enable");
		Assertions.assertTrue(MainApplication.validateConfiguration(properties));
		
		properties = TestUtils.getClonedPropety();
		properties.setProperty("repo[0].cache-manager.type", "abc");
		Assertions.assertFalse(MainApplication.validateConfiguration(properties));
		
		
		properties = TestUtils.getClonedPropety();
		properties.setProperty("repo[0].cache-manager.clear-strategy.type", "abc");
		Assertions.assertFalse(MainApplication.validateConfiguration(properties));
		
		properties = TestUtils.getClonedPropety();
		properties.remove("repo[0].cache-manager.clear-strategy.type");
		Assertions.assertTrue(MainApplication.validateConfiguration(properties));
		
		properties = TestUtils.getClonedPropety();
		properties.setProperty("repo[0].cache-manager.clear-strategy.tic", "abc");
		Assertions.assertFalse(MainApplication.validateConfiguration(properties));
		
	}

	@Test
	public void testValidateCompression() {
	    Properties properties = TestUtils.getClonedPropety();
        properties.setProperty("server.compression.enabled", "true");
        Assertions.assertFalse(MainApplication.validateCompression(properties));
        
        properties.setProperty("server.compression.mime-types", "text/html,image/png");
        Assertions.assertTrue(MainApplication.validateCompression(properties));
        
        properties.setProperty("server.compression.min-response-size", "123");
        Assertions.assertTrue(MainApplication.validateCompression(properties));
        
        properties.setProperty("server.compression.min-response-size", "abc");
        Assertions.assertFalse(MainApplication.validateCompression(properties));
        
        properties.setProperty("server.compression.enabled", "false");
        Assertions.assertTrue(MainApplication.validateCompression(properties));
	}
}
