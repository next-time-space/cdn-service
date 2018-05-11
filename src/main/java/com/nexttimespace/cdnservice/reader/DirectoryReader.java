package com.nexttimespace.cdnservice.reader;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nexttimespace.cdnservice.utility.UtilityComponent;
import com.nexttimespace.cdnservice.utility.UtilityFunctions;

@Component
public class DirectoryReader implements IReader {

	@Autowired
	UtilityComponent utilityComponent;
	
	public String getContent(String path) throws Exception {
		String directory = utilityComponent.getConfProperties().getProperty("reader.directory.path");
		return UtilityFunctions.readFile(directory + path);
	}

}
