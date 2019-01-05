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

package com.nexttimespace.cdnservice;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.SecurityAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ConfigurableApplicationContext;

import com.nexttimespace.cdnservice.utility.UtilityComponent;

@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })
@EnableCaching
public class MainApplication {
	
	private static List<String> repoTypes = Arrays.asList("directory");
	
	private static Logger logger=Logger.getLogger(MainApplication.class);
	public static void main(String[] args) throws URISyntaxException, ClassNotFoundException, SQLException, IOException {
		ConfigurableApplicationContext context = SpringApplication.run(MainApplication.class, args);
		UtilityComponent utilityComponent = context.getBean(UtilityComponent.class);
		try{
			if(!validateConfiguration(utilityComponent.getConfProperties())) {
				System.exit(0);
			} else {
				logger.info("Validation passed");
			}
		} catch(Exception e) {
			logger.error("Error at app startup", e);
			System.exit(0);
		}
	}
	
	public static boolean validateConfiguration(Properties appConf) {
		boolean isValid = true;
		isValid = validateServer(appConf);
		if(isValid) {
			isValid = validateRepo(appConf);
		}
		return isValid;
	}
	
	private static boolean validateServer(Properties appConf) {
		boolean isValid = true;
		if(appConf != null) {
			String httpPort = appConf.get("server.http.port").toString();
			String sslConfError = "";
			if(!StringUtils.isNumeric(httpPort)) {
				isValid = false;
				logger.error("Invalid data for server.http.port");
			}
			
			if(appConf.get("server.ssl-config.port") != null) {
			    if(!StringUtils.isNumeric(appConf.get("server.ssl-config.port").toString())) {
	                sslConfError = "server.ssl-config.port";
	            } else if(!(new File(appConf.get("server.ssl-config.key-store").toString()).exists() && new File(appConf.get("server.ssl-config.key-store").toString()).isFile())) {
	                sslConfError = "server.ssl-config.key-store";
	            } else if(!(new File(appConf.get("server.ssl-config.trust-store").toString()).exists() && new File(appConf.get("server.ssl-config.key-store").toString()).isFile())) {
	                sslConfError = "server.ssl-config.trust-store";
	            } else if(StringUtils.isBlank("server.ssl-config.key-store-password")) {
	                sslConfError = "server.ssl-config.key-store-password";
	            } else if(StringUtils.isBlank("server.ssl-config.trust-store-password")) {
	                sslConfError = "server.ssl-config.trust-store-password";
	            }
			}
			if(!sslConfError.isEmpty()) {
				logger.error("Invalid data for "+ sslConfError + ", publish will be disabled");
			}
		} else {
			isValid = false;
			logger.error("Invalid data for app.conf");
		}
		return isValid;
	}
	public static boolean validateRepo(Properties appConf) {
		boolean isValid = true;
		int totalTraffic = 0;
		if(!repoTypes.contains(appConf.get("repo[0].type"))) {
			isValid = false;	
			logger.error("Invalid data for repo[0].type");
		} else {
			String type = "";
			int typeArrayIndex = 0;
			while((type = appConf.getProperty(String.format("repo[%s].type", typeArrayIndex))) != null) {
				String readerKey = String.format("repo[%s]", typeArrayIndex);
				typeArrayIndex++;
				if(type.equals("directory")) {
					if (StringUtils.isBlank(appConf.getProperty(readerKey + ".alias")) || appConf.getProperty(readerKey + ".alias").contains(" ")) {
						isValid = false;
						logger.error("Invalid data for " + readerKey + ".alias");
						break;
					} else if(!( "true".equals(appConf.getProperty(readerKey + ".allow-publish")) || "false".equals(appConf.getProperty(readerKey + ".allow-publish")))) {
						isValid = false;
						logger.error("Invalid/Missing data for " + readerKey + ".allow-publish");
						break;
					} else if(!StringUtils.isNumeric(appConf.getProperty(readerKey + ".traffic"))) {
						isValid = false;
						logger.error("Invalid data for " + readerKey + ".traffic, only integer allowed");
						break;
					} else if(!(new File(appConf.getProperty(readerKey + ".directory.path")).exists() && (new File(appConf.getProperty(readerKey + ".directory.path")).isDirectory()))) {
						isValid = false;
						logger.error("Invalid data for " + readerKey + ".directory.path, check if directory exist");
						break;
					} else if(!validateResponseHeader(appConf.getProperty(readerKey + ".response.header"))) {
						isValid = false;
						logger.error("Invalid data for " + readerKey + ".response.header, split multiple headers with | and split key:value");
						break;
					} else if(!validateCacheManager(appConf, readerKey)) {
						isValid = false;
						break;
					}
					
					totalTraffic += Integer.parseInt(appConf.getProperty(readerKey + ".traffic"));
				}
			}
		}
		
		if(isValid) {
			if( totalTraffic != 100) {
				isValid = false;
				logger.error("Total traffic is not 100, cannot proceed.");
			}
		}
		return isValid;
	}
	
	private static boolean validateCacheManager(Properties appConf, String readerKey) {
		boolean isValid = true;
		if(appConf.getProperty(readerKey + ".cache-manager.enable") != null) {
			if(appConf.getProperty(readerKey + ".cache-manager.enable").equals("true")) {
				if(!appConf.getProperty(readerKey + ".cache-manager.type").equals("in-memory")) {
					logger.error("Invalid data for " + readerKey + ".cache-manager.type");
					isValid = false;
				} else {
					if(appConf.getProperty(readerKey + ".cache-manager.clear-strategy.type") != null) {
						if(!appConf.getProperty(readerKey + ".cache-manager.clear-strategy.type").equals("timer")) {
							logger.error("Invalid data for " + readerKey + ".cache-manager.clear-strategy.type");
							isValid = false;
						} else if(!StringUtils.isNumeric(appConf.getProperty(readerKey + ".cache-manager.clear-strategy.tic"))) {
							logger.error("Invalid data for " + readerKey + ".cache-manager.clear-strategy.tic");
							isValid = false;
						}
					} else {
						logger.warn("Cache clear configuration not found, however it's not mandatory");
					}
				}
			} else {
				logger.warn("Caching disabled for " + readerKey + ", we storngly recommend to configure cache manager");
			}
		} else {
			logger.warn("Caching disabled or no caching configuration found for " + readerKey + ", we storngly recommend to configure cache manager");
		}
		return isValid;
	}
	
	private static boolean validateResponseHeader(String header) {
		boolean isValid = true;
		if(header != null && !header.isEmpty()) {
			String[] mainString = header.split("\\|");
			if(mainString.length > 0) {
				for(String headerString : mainString) {
					String[] keyValue = headerString.split(":");
					if(keyValue.length <= 1) {
						isValid = false;
						break;
					}
				}
			}
		}
		return isValid;
	}
	
}
