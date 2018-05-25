package com.nexttimespace.cdnservice.reader;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import com.nexttimespace.cdnservice.TestUtils;
import com.nexttimespace.cdnservice.reader.DirectoryReader;
import com.nexttimespace.cdnservice.reader.ReaderUtility;
import com.nexttimespace.cdnservice.reader.data.ReaderObject;
import com.nexttimespace.cdnservice.utility.UtilityComponent;

public class ReaderUtilityTest {
	
	@Test
	public void determineReaderTest() throws Exception {
		ReaderUtility readerUtility = new ReaderUtility();
		UtilityComponent utilityComponent = new UtilityComponent();
		DirectoryReader directoryReader = Mockito.mock(DirectoryReader.class);
		Mockito.doNothing().when(directoryReader).setInit();
		List<ReaderObject> ro = new ArrayList<>();
		ro.add(new ReaderObject());
		Mockito.doReturn(ro).when(directoryReader).getReaderObject();
		ReflectionTestUtils.setField(utilityComponent, "confProperties", TestUtils.getClonedPropety());
		ReflectionTestUtils.setField(readerUtility, "utilityComponent", utilityComponent);
		ReflectionTestUtils.setField(readerUtility, "directoryReader", directoryReader);
		
		Assertions.assertEquals(1, readerUtility.determineReader().size());
		
		
		utilityComponent.getConfProperties().setProperty("repo[0].type", "wrong");
		Assertions.assertEquals(1, readerUtility.determineReader().size());
	}
	
	@Test
	public void findReaderTest() {
		ReaderUtility readerUtility = new ReaderUtility();
		DirectoryReader directoryReader = Mockito.mock(DirectoryReader.class);
		
		Mockito.doReturn(true).when(directoryReader).isAliasExist("cdn1");
		Mockito.doReturn(false).when(directoryReader).isAliasExist("cdn1111");
		ReflectionTestUtils.setField(readerUtility, "directoryReader", directoryReader);
		
		Assertions.assertNotNull(readerUtility.findReader("cdn1"));
		
		Assertions.assertNull(readerUtility.findReader("cdn1111"));
	}

}
