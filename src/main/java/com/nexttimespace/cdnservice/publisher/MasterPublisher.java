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

package com.nexttimespace.cdnservice.publisher;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

	
	public boolean publish(InputStream file, String alias, String path) throws IOException {
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
