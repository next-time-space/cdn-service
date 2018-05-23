package com.nexttimespace.cdnservice.config;

import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.Http11NioProtocol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.nexttimespace.cdnservice.utility.UtilityComponent;

@Configuration
public class ApplicationConfig {
	
	@Autowired
	private UtilityComponent utilityComponent;

	@Bean
	public EmbeddedServletContainerFactory servletContainer() {
	     
	    final String keystoreFile = utilityComponent.getConfProperties().getProperty("server.ssl-config.key-store");
	    final String keystorePass = utilityComponent.getConfProperties().getProperty("server.ssl-config.key-store-password");
	    final String keystoreAlias = utilityComponent.getConfProperties().getProperty("server.ssl-config.key-alias");
	    final String truststoreFile = utilityComponent.getConfProperties().getProperty("server.ssl-config.trust-store");
	    final String truststorePassword = utilityComponent.getConfProperties().getProperty("server.ssl-config.trust-store-password");
	    final String portString = utilityComponent.getConfProperties().getProperty("server.ssl-config.port");
	    final String httpPort = utilityComponent.getConfProperties().getProperty("server.http.port");
	 
	    TomcatEmbeddedServletContainerFactory factory = new TomcatEmbeddedServletContainerFactory();
	    factory.addConnectorCustomizers((TomcatConnectorCustomizer) (Connector con) -> {
	        con.setScheme("https");
	        con.setSecure(true);
	        con.setPort(Integer.parseInt(portString));
	        Http11NioProtocol proto = (Http11NioProtocol) con.getProtocolHandler();
	        proto.setSSLEnabled(true);
	        proto.setClientAuth("true");
	        proto.setKeystoreFile(keystoreFile);
	        proto.setKeystorePass(keystorePass);
	        proto.setTruststoreFile(truststoreFile);
	        proto.setTruststorePass(truststorePassword);
	        proto.setKeyAlias(keystoreAlias);
	    });
	    Connector connector = new Connector(TomcatEmbeddedServletContainerFactory.DEFAULT_PROTOCOL);
        connector.setPort(Integer.parseInt(httpPort));
	    factory.addAdditionalTomcatConnectors(connector);
	    return factory;
	}
}
