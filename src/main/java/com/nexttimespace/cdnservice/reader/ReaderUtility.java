package com.nexttimespace.cdnservice.reader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.nexttimespace.cdnservice.reader.data.ReaderObject;
import com.nexttimespace.cdnservice.utility.UtilityComponent;

@Component
public class ReaderUtility {
	
	
	@Autowired
	UtilityComponent utilityComponent;
	
	@Autowired
	ApplicationContext applicationContext;
	
	@Autowired
	DirectoryReader directoryReader;
	
	public static class ConfContants {
		public static final String READER_TYPE_DIRECTORY = "directory";
		
	}

	@Bean(name="readers")
	public List<ReaderObject> determineReader() throws Exception {
		List<ReaderObject> readers = new ArrayList<>();
		Properties confProperties = utilityComponent.getConfProperties();
		int typeArrayIndex = 0;
		String type = null;
		while((type = confProperties.getProperty(String.format("repo[%s].type", typeArrayIndex))) != null) {
			if(type.equals("directory")) {
				directoryReader.setInit();
			}
			typeArrayIndex++;
		}
		readers.addAll(directoryReader.getReaderObject());
		return readers;
	}
	
	public MasterReader findReader(String alias) {
		MasterReader reader = null;
		if(directoryReader.isAliasExist(alias)) {
			return directoryReader;
		}
		return reader;
	}
}
