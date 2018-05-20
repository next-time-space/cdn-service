package com.nexttimespace.cdnservice.publisher;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.nexttimespace.cdnservice.reader.ReaderUtility;
import com.nexttimespace.cdnservice.utility.UtilityComponent;

@Component
public class MasterPublisher {
	
	@Autowired
	ReaderUtility readerUtility;
	
	@Autowired
	UtilityComponent utilityComponent;
	
	@Autowired
	DirectoryPublisher directoryPublisher;

	
	public boolean publish(InputStream file,String alias, String path) throws IOException {
		String readerKey = utilityComponent.readerKeyByAlias(alias);
		String modifiedPath = path.startsWith("/") ? path : "/" + path;
		boolean isPublishAllowed = Boolean.valueOf(utilityComponent.getConfProperties().getProperty(readerKey + ".allow-publish"));
		if(isPublishAllowed) {
			String type = utilityComponent.getConfProperties().getProperty(readerKey + ".type");
			if(type.equals("directory")) {
				directoryPublisher.publish(file, alias, modifiedPath, readerKey);
			}
			return true;
		} else {
			return false;
		}
	}
	
}
