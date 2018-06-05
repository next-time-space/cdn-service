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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nexttimespace.cdnservice.reader.data.ReaderObject;
import com.nexttimespace.cdnservice.utility.UtilityComponent;

@Component
public class TrafficRouter {
	
	AtomicInteger totalHit = new AtomicInteger(0);

	Map<String, AtomicInteger> readerAndCounter = new LinkedHashMap<>();
	Map<String, Integer> readerAndPercent = new LinkedHashMap<>();

	@Autowired
	List<ReaderObject> readers;
	
	@Autowired
	UtilityComponent utilityComponent;
	
	@Autowired
	DirectoryReader directoryReader;
	
	@PostConstruct
	public void setup() {
		readers.forEach(reader -> readerAndCounter.put(reader.getAlias(), new AtomicInteger(0)));
	}
	
	
	public String getReader() throws Exception {
		String reader = null;
		if(readers.size() == 1) {
			reader = readers.get(0).getAlias();
		} else {
			reader = decideReaderBy();
		}
		totalHit.incrementAndGet();
		return reader;
	}
	
	private String decideReaderBy() {
		List<String> lifeLeftReaders = getLifeLeftReaders();
		if(lifeLeftReaders.size() == 1) {
			String reader = lifeLeftReaders.get(0);
			readerAndCounter.get(reader).incrementAndGet();
			return reader;
		} else {
			int totalReaders = lifeLeftReaders.size();
			int randomNum = ThreadLocalRandom.current().nextInt(0, totalReaders);
			String firstSelection = lifeLeftReaders.get(randomNum);
			if(readerAndCounter.get(firstSelection) != null) {
				readerAndCounter.get(firstSelection).incrementAndGet();
			}
			return firstSelection;
		}
	}
	
	private void resetCounter() {
		readerAndCounter.forEach((IReader, atomicCounter)-> atomicCounter.set(0));
	}
	
	private List<String> getLifeLeftReaders() {
		List<String> lifeLeftReaders = new ArrayList<>();
		for(ReaderObject reader : readers) {
			if(readerAndCounter.get(reader.getAlias()).get() < reader.getTrafficPercent()) {
				lifeLeftReaders.add(reader.getAlias());
			}
		}
		
		if(lifeLeftReaders.isEmpty()) {
			// may day reset local counter
			resetCounter();
			readers.forEach(reader -> lifeLeftReaders.add(reader.getAlias()));
		}
		
		return lifeLeftReaders;
	}
}
