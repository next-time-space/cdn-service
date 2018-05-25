package com.nexttimespace.cdnservice.reader;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import com.nexttimespace.cdnservice.reader.data.ReaderObject;

public class TrafficRouterTest {

	@Test
	public void setupTest() {
		TrafficRouter trafficRouter = new TrafficRouter();
		
		ReaderObject r1 = new ReaderObject();
		r1.setAlias("cdn1");
		ReaderObject r2 = new ReaderObject();
		r2.setAlias("cdn2");
		
		List<ReaderObject> ros = new ArrayList<>();
		ros.add(r1);
		ros.add(r2);
		
		ReflectionTestUtils.setField(trafficRouter, "readers", ros);
				
		trafficRouter.setup();
		Assertions.assertEquals(2, trafficRouter.readerAndCounter.size());
	}
	
	@Test
	public void getReaderTest() throws Exception {
		TrafficRouter trafficRouter = new TrafficRouter();
		
		ReaderObject r1 = new ReaderObject();
		r1.setAlias("cdn1");
		r1.setTrafficPercent("99");
		ReaderObject r2 = new ReaderObject();
		r2.setTrafficPercent("1");
		r2.setAlias("cdn2");
		
		List<ReaderObject> ros = new ArrayList<>();
		ros.add(r1);
		
		
		ReflectionTestUtils.setField(trafficRouter, "readers", ros);
				
		trafficRouter.setup();
		Assertions.assertFalse(trafficRouter.getReader().isEmpty());
		
		ros.add(r2);
		ReflectionTestUtils.setField(trafficRouter, "readers", ros);
		
		trafficRouter.setup();
		Assertions.assertFalse(trafficRouter.getReader().isEmpty());
		
		r1 = new ReaderObject();
		r2 = new ReaderObject();
		r2.setTrafficPercent("100");
		r2.setAlias("cdn2");
		r1.setTrafficPercent("0");
		r1.setAlias("cdn1");
		ros.clear();
		ros.add(r2);
		ros.add(r1);
		ReflectionTestUtils.setField(trafficRouter, "readers", ros);
		trafficRouter.setup();
		Assertions.assertFalse(trafficRouter.getReader().isEmpty());
		
		r1 = new ReaderObject();
		r2 = new ReaderObject();
		r2.setTrafficPercent("1");
		r1.setTrafficPercent("0");
		r2.setAlias("cdn2");
		r1.setAlias("cdn1");
		ros.clear();
		ros.add(r2);
		ros.add(r1);
		ReflectionTestUtils.setField(trafficRouter, "readers", ros);
		trafficRouter.setup();
		Assertions.assertFalse(trafficRouter.getReader().isEmpty());
		Assertions.assertFalse(trafficRouter.getReader().isEmpty());
		
	}
}
