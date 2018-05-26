package com.nexttimespace.cdnservice.utility;

import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.test.util.ReflectionTestUtils;

import com.nexttimespace.cdnservice.TestUtils;

public class UtilityComponentTest {

	@Test
	public void setupTest() throws URISyntaxException {
		UtilityComponent utilityComponent = new UtilityComponent();
		Executable closureContainingCodeToTest = () -> utilityComponent.setup();
		Assertions.assertThrows(IllegalStateException.class, closureContainingCodeToTest);
	}
	
	@Test
	public void readerKeyByAliasTest() {
		UtilityComponent utilityComponent = new UtilityComponent();
		ReflectionTestUtils.setField(utilityComponent, "confProperties", TestUtils.getClonedPropety());
		Assertions.assertEquals("repo[0]",utilityComponent.readerKeyByAlias("cdn1"));
		
		Assertions.assertEquals("",utilityComponent.readerKeyByAlias("cdn111"));
	}
}
