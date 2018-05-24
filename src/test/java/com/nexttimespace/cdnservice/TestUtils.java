package com.nexttimespace.cdnservice;

import java.util.Properties;

import org.apache.commons.lang3.SerializationUtils;
import org.junit.jupiter.api.BeforeAll;

public class TestUtils {
	private static Properties goodProperty = new Properties();
	static {
		goodProperty.setProperty("server.http.port", "8080");
		goodProperty.setProperty("server.ssl-config.port", "8443");
		goodProperty.setProperty("server.ssl-config.key-store", "pom.xml");
		goodProperty.setProperty("server.ssl-config.trust-store", "pom.xml");
		goodProperty.setProperty("server.ssl-config.key-store-password", "pass");
		goodProperty.setProperty("server.ssl-config.trust-store-password", "pass");
		goodProperty.setProperty("repo[0].type", "directory");
		goodProperty.setProperty("repo[0].alias", "cdn1");
		goodProperty.setProperty("repo[0].allow-publish", "true");
		goodProperty.setProperty("repo[0].traffic", "100");
		goodProperty.setProperty("repo[0].directory.path", "/tmp");
		goodProperty.setProperty("repo[0].response.header", "app:beta|app1:beta1");
		goodProperty.setProperty("repo[0].cache-manager.enable", "true");
		goodProperty.setProperty("repo[0].cache-manager.type", "in-memory");
		goodProperty.setProperty("repo[0].cache-manager.clear-strategy.type", "timer");
		goodProperty.setProperty("repo[0].cache-manager.clear-strategy.tic", "1000");
	}
	
	public static Properties getClonedPropety() {
		return SerializationUtils.clone(goodProperty);
	}
}
