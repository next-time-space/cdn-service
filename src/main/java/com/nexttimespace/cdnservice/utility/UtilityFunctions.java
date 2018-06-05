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
