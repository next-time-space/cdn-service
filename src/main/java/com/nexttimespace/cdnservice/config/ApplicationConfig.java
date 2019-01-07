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

import java.util.Optional;

import org.apache.catalina.connector.Connector;
import org.apache.commons.lang3.StringUtils;
import org.apache.coyote.http11.Http11NioProtocol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.Compression;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;

import com.nexttimespace.cdnservice.utility.UtilityComponent;

@Configuration
public class ApplicationConfig implements WebServerFactoryCustomizer<TomcatServletWebServerFactory> {

    @Autowired
    private UtilityComponent utilityComponent;
    
    @Override
    public void customize(TomcatServletWebServerFactory factory) {
        final String keystoreFile = utilityComponent.getConfProperties().getProperty("server.ssl-config.key-store");
        final String keystorePass = utilityComponent.getConfProperties().getProperty("server.ssl-config.key-store-password");
        final String keystoreAlias = utilityComponent.getConfProperties().getProperty("server.ssl-config.key-alias");
        final String truststoreFile = utilityComponent.getConfProperties().getProperty("server.ssl-config.trust-store");
        final String truststorePassword = utilityComponent.getConfProperties().getProperty("server.ssl-config.trust-store-password");
        final String portString = utilityComponent.getConfProperties().getProperty("server.ssl-config.port");
        final String httpPort = utilityComponent.getConfProperties().getProperty("server.http.port");
        String contextPath = utilityComponent.getConfProperties().getProperty("server.contextPath");
        
        if (StringUtils.isNotBlank(portString)) {
            Connector con = new Connector();
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
            factory.addAdditionalTomcatConnectors(con);
        }

        factory.setPort(Integer.parseInt(httpPort));
        if (StringUtils.isNotBlank(contextPath)) {
            contextPath = contextPath.startsWith("/") ? contextPath : "/" + contextPath;
            factory.setContextPath(contextPath);
        }
        getCompressionParams().ifPresent(consumer -> factory.setCompression(consumer));
    }

    private Optional<Compression> getCompressionParams() {
        if (Boolean.valueOf(utilityComponent.getConfProperties().getProperty("server.compression.enabled"))) {
            String mimeType = utilityComponent.getConfProperties().getProperty("server.compression.mime-types");
            String minResponseSize = utilityComponent.getConfProperties().getProperty("server.compression.min-response-size");
            Compression com = new Compression();
            com.setEnabled(true);
            com.setMimeTypes(mimeType.split(","));
            if (StringUtils.isNotBlank(minResponseSize)) {
                com.setMinResponseSize(DataSize.ofBytes(Integer.parseInt(minResponseSize)));
            }

            return Optional.ofNullable(com);
        }
        return Optional.empty();
    }
}
