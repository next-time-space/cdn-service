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

package com.nexttimespace.cdnservice.controller;

import java.util.List;
import java.util.Objects;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.nexttimespace.cdnservice.publisher.MasterPublisher;
import com.nexttimespace.cdnservice.reader.MasterReader;
import com.nexttimespace.cdnservice.reader.ReaderUtility;
import com.nexttimespace.cdnservice.reader.TrafficRouter;
import com.nexttimespace.cdnservice.utility.UtilityComponent;
import com.nexttimespace.cdnservice.utility.UtilityFunctions;


@Controller
public class ServeContentController {
	
	private int httpPort;
	private int httpsPort;
	
	@PostConstruct
	public void setup() {
		httpPort = Integer.parseInt(utilityComponent.getConfProperties().get("server.http.port").toString());
		httpsPort = Integer.parseInt(Objects.toString(utilityComponent.getConfProperties().get("server.ssl-config.port"), "-1"));
	}
	@Autowired
	TrafficRouter trafficManager;
	
	@Autowired
	ReaderUtility readerUtility;
	
	@Autowired
	MasterPublisher masterPublisher;
	
	@Autowired
	UtilityComponent utilityComponent;
	
	private Logger logger = Logger.getLogger(ServeContentController.class);

	@RequestMapping(value = "/publish/{alias}", method=RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<String> publishToDirectory(@PathVariable String alias, @RequestParam("path") String path, HttpServletRequest request, HttpServletResponse response) {
		ResponseEntity<String> responseEntity = null;
		try {
			if(request.getServerPort() == httpsPort) {
				Part filePart = request.getPart("file");
				if(filePart != null && path != null && !path.isEmpty() && alias != null && !alias.isEmpty()) {
					if(masterPublisher.publish(filePart.getInputStream(), alias, path)) {
						responseEntity = new ResponseEntity<String>("Published successfully", HttpStatus.OK);
					} else {
						responseEntity = new ResponseEntity<String>("Publish disabled", HttpStatus.OK);
					}
				} else {
					responseEntity = new ResponseEntity<String>("file or path or alias request attribute missing", HttpStatus.BAD_REQUEST);
				}
				
			} else {
				responseEntity = new ResponseEntity<String>("", HttpStatus.NOT_FOUND);
			}
			
		} catch (Exception e) {
			logger.error("Error on publish: ", e);
			responseEntity = new ResponseEntity<String>("Publish failed due to internal error", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return responseEntity;
	}
	
	@RequestMapping(value = "/**", method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<String> serveContent(HttpServletRequest request, HttpServletResponse response) {
		try {
			if(request.getServerPort() == httpPort) {
				String path = request.getRequestURI().substring(request.getContextPath().length());
				String alias = trafficManager.getReader();
				MasterReader reader = readerUtility.findReader(alias);
				String[] content = reader.getContent(alias, path);
				HttpHeaders responseHeaders = new HttpHeaders();
			    responseHeaders.setContentType(UtilityFunctions.findMediaType(path));
			    List<String[]> headers = reader.getResponseHeader(alias);
			    if(headers != null && !headers.isEmpty()) {
			    	headers.forEach(header -> responseHeaders.add(header[0], header[1]));
			    	
			    }
				return new ResponseEntity<String>(content[0], responseHeaders, HttpStatus.OK);
			} else {
				return new ResponseEntity<String>("", HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			logger.error("Error on servecontent: ", e);
			return new ResponseEntity<String>("", HttpStatus.NOT_FOUND);
		}
	}
	
}
