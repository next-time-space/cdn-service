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

package com.nexttimespace.cdnservice.utility;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Properties;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
public class UtilityComponent {
	
	@Autowired
	ApplicationContext appContext;
	
	@Value("${appversion}")
	private String appVersion;
	
	private Properties confProperties;
	
	public Properties getConfProperties() {
		return confProperties;
	}


	@PostConstruct
	public void setup( ) throws URISyntaxException {
		properties();
	}
	
	public void properties() throws URISyntaxException {
	  YamlPropertiesFactoryBean yaml = new YamlPropertiesFactoryBean();
	  Resource confFileResource = new FileSystemResource(new File(String.format("%s/%s", getExecutablePath(), "conf.yml")));
	  yaml.setResources(confFileResource);
	  confProperties = yaml.getObject();
	}
	
	public String readerKeyByAlias(String alias) {
		int typeArrayIndex = 0;
		String readerKey = "";
		String aliasFromList = "";
		while((aliasFromList = confProperties.getProperty(String.format("repo[%s].alias", typeArrayIndex))) != null) {
			if(aliasFromList.equals(alias)) {
				readerKey = String.format("repo[%s]", typeArrayIndex);
				break;
			}
			typeArrayIndex++;
		}
		return readerKey;
	}
	
	
	public String getExecutablePath() throws URISyntaxException {
		URL jarLocationUrl = UtilityComponent.class.getProtectionDomain().getCodeSource().getLocation();
		String jarLocation;
		try {
			jarLocation = URLDecoder.decode(new File(new File(new File(new File(jarLocationUrl.toString()).getParent()).getParent()).getParent()).getPath().replace("jar:file:\\", ""), "utf-8");
		} catch (UnsupportedEncodingException e) {
			throw new URISyntaxException(jarLocationUrl.toString(), jarLocationUrl.toString());
		}
		jarLocation = jarLocation.replace("jar:file:", "");
		String projectName = "";
		try {
			projectName = appVersion;
		} catch (java.lang.NullPointerException n) {
			// Ignore
			jarLocation = jarLocation.replace("file:\\", "");
		}
		if (projectName != null && !projectName.isEmpty() && !new File(jarLocation + "/" +appVersion).exists()) {
			jarLocation = new File(UtilityComponent.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getPath().replace("\\classes", "");
		}
		jarLocation = jarLocation.replaceAll("\\\\", "/");
		return jarLocation;
	}
}
