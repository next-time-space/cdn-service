package com.nexttimespace.cdnservice.reader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.nexttimespace.cdnservice.utility.UtilityComponent;

@Component
public class ReaderUtility {
	
	@Autowired
	UtilityComponent utilityComponent;
	
	@Autowired
	DirectoryReader directoryReader;
	
	public static class ConfContants {
		public static final String READER_TYPE_DIRECTORY = "directory";
		
	}

	@Bean(name="readers")
	public List<IReader> determineReader() {
		List<IReader> readers = new ArrayList<>();
		List<String> readerType = Arrays.asList(utilityComponent.getConfProperties().getProperty("reader.type").toLowerCase().split(","));
		if(readerType.contains("directory")) {
			readers.add(directoryReader);
		}
		return readers;
	}
}
