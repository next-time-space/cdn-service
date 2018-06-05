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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.nexttimespace.cdnservice.utility.UtilityComponent;

@Component
public class DirectoryPublisher {

	@Autowired
	UtilityComponent utilityComponent;
	public void publish(InputStream file, String alias, String path, String readerKey) throws IOException {
		String directoryPath = utilityComponent.getConfProperties().getProperty(readerKey + ".directory.path");
		directoryPath = directoryPath.endsWith("/") ? directoryPath.substring(0, directoryPath.length() - 1) : directoryPath;
		directoryPath = directoryPath  + path;
		File filetoWrite = new File(directoryPath);
		filetoWrite.getParentFile().mkdirs();
		OutputStream outputStream = new FileOutputStream(filetoWrite);
		IOUtils.copy(file, outputStream);
		outputStream.close();
	}

}
