package com.nexttimespace.cdnservice.reader;

import java.util.LinkedHashMap;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import com.nexttimespace.cdnservice.TestUtils;
import com.nexttimespace.cdnservice.utility.UtilityComponent;

public class MasterReaderTest {
	
	@Test
	public void getAllAliasTest() throws Exception {
		DirectoryReader directoryReader = new DirectoryReader();
		UtilityComponent utilityComponent = new UtilityComponent();
		TimerCacheCleaner timerCacheCleaner = new TimerCacheCleaner();
		
		ReflectionTestUtils.setField(utilityComponent, "confProperties", TestUtils.getClonedPropety());
		ReflectionTestUtils.setField(directoryReader, "utilityComponent", utilityComponent);
		ReflectionTestUtils.setField(directoryReader, "timerCacheCleaner", timerCacheCleaner);
		directoryReader.setInit();
		MasterReader masterReader = directoryReader;
		Assertions.assertEquals(1, masterReader.getAllAlias().size());
	}

	
	@Test
	public void findReaderObjectTest() throws Exception {
		DirectoryReader directoryReader = new DirectoryReader();
		UtilityComponent utilityComponent = new UtilityComponent();
		TimerCacheCleaner timerCacheCleaner = new TimerCacheCleaner();
		
		ReflectionTestUtils.setField(utilityComponent, "confProperties", TestUtils.getClonedPropety());
		ReflectionTestUtils.setField(directoryReader, "utilityComponent", utilityComponent);
		ReflectionTestUtils.setField(directoryReader, "timerCacheCleaner", timerCacheCleaner);
		directoryReader.setInit();
		MasterReader masterReader = directoryReader;
		Assertions.assertNotNull(masterReader.findReaderObject("cdn1"));
		Assertions.assertNull(masterReader.findReaderObject("cdn111"));
	}
	
	@Test
	public void isAliasExistTest() throws Exception {
		DirectoryReader directoryReader = new DirectoryReader();
		UtilityComponent utilityComponent = new UtilityComponent();
		TimerCacheCleaner timerCacheCleaner = new TimerCacheCleaner();
		
		ReflectionTestUtils.setField(utilityComponent, "confProperties", TestUtils.getClonedPropety());
		ReflectionTestUtils.setField(directoryReader, "utilityComponent", utilityComponent);
		ReflectionTestUtils.setField(directoryReader, "timerCacheCleaner", timerCacheCleaner);
		directoryReader.setInit();
		MasterReader masterReader = directoryReader;
		Assertions.assertTrue(masterReader.isAliasExist("cdn1"));
		Assertions.assertFalse(masterReader.isAliasExist("cdn111"));
	}
	
	@Test
	public void getResponseHeaderTest() throws Exception {
		DirectoryReader directoryReader = new DirectoryReader();
		UtilityComponent utilityComponent = new UtilityComponent();
		TimerCacheCleaner timerCacheCleaner = new TimerCacheCleaner();
		
		ReflectionTestUtils.setField(utilityComponent, "confProperties", TestUtils.getClonedPropety());
		ReflectionTestUtils.setField(directoryReader, "utilityComponent", utilityComponent);
		ReflectionTestUtils.setField(directoryReader, "timerCacheCleaner", timerCacheCleaner);
		directoryReader.setInit();
		MasterReader masterReader = directoryReader;
		Assertions.assertFalse(masterReader.getResponseHeader("cdn1").isEmpty());
		Assertions.assertTrue(masterReader.getResponseHeader("cdn111").isEmpty());
		
		ReflectionTestUtils.setField(directoryReader, "setupDone", false);
		ReflectionTestUtils.setField(directoryReader, "directoryReaders", new LinkedHashMap<>());
		utilityComponent.getConfProperties().remove("repo[0].response.header");
		directoryReader.setInit();
		Assertions.assertTrue(masterReader.getResponseHeader("cdn1").isEmpty());
	}

}
