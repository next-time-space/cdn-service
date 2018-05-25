package com.nexttimespace.cdnservice.reader.data;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

public class ReaderObjectTest {
	
	@Test
	public void setMethodTest() {
		ReaderObject object = new ReaderObject();
		Executable closureContainingCodeToTest = () -> object.setTrafficPercent("abc");
		Assertions.assertThrows(Exception.class, closureContainingCodeToTest);
	}

}
