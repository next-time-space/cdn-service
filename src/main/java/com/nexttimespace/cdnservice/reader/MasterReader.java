package com.nexttimespace.cdnservice.reader;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.nexttimespace.cdnservice.reader.data.ReaderObject;

@Component
public abstract class MasterReader {
	public abstract String[] getContent(String alias, String path)  throws Exception;
	public abstract void clearCache(String alias);
	public abstract List<ReaderObject> getReaderObject();
	public List<String> getAllAlias() {
		List<ReaderObject> readers = getReaderObject();
		return readers.stream()
        .map(ReaderObject::getAlias).collect(Collectors.toList());
	}
	
	public ReaderObject findReaderObject(String alias) {
		List<ReaderObject> readers = getReaderObject();
		Optional<ReaderObject> ops = readers.stream().filter(reader -> reader.getAlias().equals(alias)).findFirst();
		return ops.isPresent() ? ops.get() : null;
	}
	
	public boolean isAliasExist(String alias) {
		List<ReaderObject> readers = getReaderObject();
		Optional<ReaderObject> ops = readers.stream().filter(reader -> reader.getAlias().equals(alias)).findFirst();
		return ops.isPresent();
	}
	
	public List<String[]> getResponseHeader(String alias) {
		ReaderObject readerObject = findReaderObject(alias);
		List<String[]> headers = new ArrayList<>();
		if(readerObject != null) {
			String[] responseHeader = readerObject.getResponseHeader();
			if(responseHeader != null) {
				for(String header : responseHeader) {
					headers.add(header.split(":"));
				}
			}
		}
		return headers;
	}	
	
}
