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
