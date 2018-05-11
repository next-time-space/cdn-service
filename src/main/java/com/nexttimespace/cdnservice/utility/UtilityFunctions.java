package com.nexttimespace.cdnservice.utility;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.http.MediaType;

public class UtilityFunctions {
	public static String readFile(String filePath) throws IOException {
		return new String(Files.readAllBytes(Paths.get(filePath)));
	}
	
	public static MediaType findMediaType(String path) {
		MediaType mediaType = MediaType.TEXT_PLAIN;
		if(path.endsWith(".js")) {
			mediaType = new MediaType("text","javascript");
		} else if(path.endsWith(".css")) {
			mediaType = new MediaType("text","css");
		}  else if(path.endsWith(".json")) {
			mediaType = MediaType.APPLICATION_JSON;
		} 
		return mediaType;
	}
}
