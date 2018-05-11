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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.nexttimespace.cdnservice.reader.TrafficManager;
import com.nexttimespace.cdnservice.utility.UtilityFunctions;


@Controller
public class ServeContentController {
	
	@Autowired
	TrafficManager trafficManager;

	@RequestMapping(value = "/**", method=RequestMethod.GET)
	@ResponseBody
	public ResponseEntity<String> serveContent(HttpServletRequest request, HttpServletResponse response) {
		try {
			String path = request.getRequestURI();
			String content = trafficManager.getContent(path);
			HttpHeaders responseHeaders = new HttpHeaders();
		    responseHeaders.setContentType(UtilityFunctions.findMediaType(path));
			return new ResponseEntity<String>(content, responseHeaders, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>("", HttpStatus.NOT_FOUND);
		}
	}
	
}
