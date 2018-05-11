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
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Component
public class UtilityComponent {
	
	@Autowired
	ApplicationContext appContext;
	
	@Autowired
	ApplicationProperties applicationProperties;
	
	@Value("${appversion}")
	private String appVersion;
	
	private Properties confProperties;
	
	public Properties getConfProperties() {
		return confProperties;
	}

	public void setConfProperties(Properties confProperties) {
		this.confProperties = confProperties;
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
	
	public String getAppNameVersion() {
		return applicationProperties.getAppVersion();
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
			projectName = getAppNameVersion();
		} catch (java.lang.NullPointerException n) {
			// Ignore
			jarLocation = jarLocation.replace("file:\\", "");
		}
		if (projectName != null && !projectName.isEmpty() && !new File(jarLocation + "/" + getAppNameVersion()).exists()) {
			jarLocation = new File(UtilityComponent.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getPath().replace("\\classes", "");
		}
		jarLocation = jarLocation.replaceAll("\\\\", "/");
		return jarLocation;
	}
}
