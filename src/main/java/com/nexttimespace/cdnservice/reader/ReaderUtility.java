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

package com.nexttimespace.cdnservice.reader;

import java.util.ArrayList;
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
