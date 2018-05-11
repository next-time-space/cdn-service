package com.nexttimespace.cdnservice.reader;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TrafficManager {

	@Autowired
	List<IReader> readers;
	
	public String getContent(String path) throws Exception {
		return readers.get(0).getContent(path);
	}
}
