/*
 * Copyright 2018 Next Time Space.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.nexttimespace.cdnservice.config;

import org.apache.catalina.connector.Connector;
import org.apache.commons.lang3.StringUtils;
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
	    String contextPath = utilityComponent.getConfProperties().getProperty("server.contextPath");
	    
	    TomcatEmbeddedServletContainerFactory factory = new TomcatEmbeddedServletContainerFactory();
	    if(StringUtils.isNotBlank(portString)) {
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
	    }
	    
	    Connector connector = new Connector(TomcatEmbeddedServletContainerFactory.DEFAULT_PROTOCOL);
        connector.setPort(Integer.parseInt(httpPort));
	    factory.addAdditionalTomcatConnectors(connector);
	    if(StringUtils.isNotBlank(contextPath)) {
	        contextPath = contextPath.startsWith("/") ? contextPath : "/" + contextPath;
	        factory.setContextPath(contextPath);
	    }
	    

	    return factory;
	}
}
