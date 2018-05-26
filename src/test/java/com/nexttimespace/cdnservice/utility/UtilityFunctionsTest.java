package com.nexttimespace.cdnservice.utility;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

public class UtilityFunctionsTest {

	@Test
	public void findMediaTypeTest() {
		Assertions.assertEquals(MediaType.TEXT_PLAIN, UtilityFunctions.findMediaType("hello"));
		Assertions.assertEquals("text/css",UtilityFunctions.findMediaType("hello.css").getType() + "/" + UtilityFunctions.findMediaType("hello.css").getSubtype());
		Assertions.assertEquals("text/javascript", UtilityFunctions.findMediaType("hello.js").getType() + "/" + UtilityFunctions.findMediaType("hello.js").getSubtype());
		Assertions.assertEquals("application/json", UtilityFunctions.findMediaType("hello.json").getType() + "/" + UtilityFunctions.findMediaType("hello.json").getSubtype());
		
	}
}
