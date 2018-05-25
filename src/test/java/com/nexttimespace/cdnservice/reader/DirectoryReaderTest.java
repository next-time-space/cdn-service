package com.nexttimespace.cdnservice.reader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.SimpleKey;
import org.springframework.test.util.ReflectionTestUtils;

import com.nexttimespace.cdnservice.TestUtils;
import com.nexttimespace.cdnservice.utility.UtilityComponent;

public class DirectoryReaderTest {
	
	@BeforeAll
	public static void sertup() throws FileNotFoundException {
		PrintWriter out = new PrintWriter("/tmp/tmp.js");
		out.write("tmpjs");
		out.close();
	}
	
	@AfterAll
	public static void tearDown() {
		new File("/tmp/tmp.js").delete();
	}
	@Test
	public void setInitTest() throws Exception {
		DirectoryReader directoryReader = new DirectoryReader();
		UtilityComponent utilityComponent = new UtilityComponent();
		TimerCacheCleaner timerCacheCleaner = new TimerCacheCleaner();
		
		ReflectionTestUtils.setField(utilityComponent, "confProperties", TestUtils.getClonedPropety());
		ReflectionTestUtils.setField(directoryReader, "utilityComponent", utilityComponent);
		ReflectionTestUtils.setField(directoryReader, "timerCacheCleaner", timerCacheCleaner);
		directoryReader.setInit();
		Assertions.assertEquals(1, directoryReader.getReaderObject().size());
		directoryReader.setInit();
		Assertions.assertEquals(1, directoryReader.getReaderObject().size());
		
		ReflectionTestUtils.setField(directoryReader, "setupDone", false);
		ReflectionTestUtils.setField(directoryReader, "directoryReaders", new LinkedHashMap<>());
		utilityComponent.getConfProperties().setProperty("repo[0].cache-manager.enable", "false");
		directoryReader.setInit();
		Assertions.assertEquals(1, directoryReader.getReaderObject().size());
		
		ReflectionTestUtils.setField(directoryReader, "setupDone", false);
		ReflectionTestUtils.setField(directoryReader, "directoryReaders", new LinkedHashMap<>());
		utilityComponent.getConfProperties().remove("repo[0].response.header");
		directoryReader.setInit();
		Assertions.assertEquals(1, directoryReader.getReaderObject().size());
		
		ReflectionTestUtils.setField(directoryReader, "setupDone", false);
		ReflectionTestUtils.setField(directoryReader, "directoryReaders", new LinkedHashMap<>());
		utilityComponent.getConfProperties().setProperty("repo[0].cache-manager.enable", "true");
		utilityComponent.getConfProperties().setProperty("repo[0].cache-manager.clear-strategy.type", "API");
		directoryReader.setInit();
		Assertions.assertEquals(1, directoryReader.getReaderObject().size());
		
		ReflectionTestUtils.setField(directoryReader, "directoryReaders", new LinkedHashMap<>());
		ReflectionTestUtils.setField(directoryReader, "setupDone", false);
		utilityComponent.getConfProperties().setProperty("repo[0].type", "wrong");
		directoryReader.setInit();
		Assertions.assertEquals(0, directoryReader.getReaderObject().size());
	}
	
	@Test
	public void getContentTest() throws Exception {
		DirectoryReader directoryReader = new DirectoryReader();
		UtilityComponent utilityComponent = new UtilityComponent();
		TimerCacheCleaner timerCacheCleaner = new TimerCacheCleaner();
		
		ReflectionTestUtils.setField(utilityComponent, "confProperties", TestUtils.getClonedPropety());
		ReflectionTestUtils.setField(directoryReader, "utilityComponent", utilityComponent);
		ReflectionTestUtils.setField(directoryReader, "timerCacheCleaner", timerCacheCleaner);
		directoryReader.setInit();
		String[] out =  directoryReader.getContent("cdn1", "/tmp.js");
		Assertions.assertEquals("tmpjs", out[0]);
		Assertions.assertEquals("1", out[1]);
		
		
		ReflectionTestUtils.setField(directoryReader, "setupDone", false);
		ReflectionTestUtils.setField(directoryReader, "directoryReaders", new LinkedHashMap<>());
		utilityComponent.getConfProperties().setProperty("repo[0].cache-manager.enable", "false");
		directoryReader.setInit();
		out =  directoryReader.getContent("cdn1", "/tmp.js");
		Assertions.assertEquals("tmpjs", out[0]);
		Assertions.assertEquals("0", out[1]);
		
		PrintWriter pw = new PrintWriter("/tmp/tmp.js");
		pw.write("");
		pw.close();
		ReflectionTestUtils.setField(directoryReader, "setupDone", false);
		ReflectionTestUtils.setField(directoryReader, "directoryReaders", new LinkedHashMap<>());
		utilityComponent.getConfProperties().setProperty("repo[0].cache-manager.enable", "true");
		directoryReader.setInit();
		out =  directoryReader.getContent("cdn1", "/tmp.js");
		Assertions.assertEquals("", out[0]);
		Assertions.assertEquals("0", out[1]);
	}
	
	@Test
	public void clearCacheTest() {
		DirectoryReader directoryReader = new DirectoryReader();
		CacheManager cacheManager = Mockito.mock(CacheManager.class);	
		Cache cache = Mockito.mock(Cache.class);
		ConcurrentHashMap<SimpleKey , String[]> map = new ConcurrentHashMap<>();
		map.put(new SimpleKey("cdn1", "/tmp.js"), new String[] { "tmpjs", "1"});
		
		Mockito.doReturn(map).when(cache).getNativeCache();
		Mockito.doReturn(cache).when(cacheManager).getCache(Mockito.anyString());
		ReflectionTestUtils.setField(directoryReader, "cacheManager", cacheManager);
		directoryReader.clearCache("cdn2");
		directoryReader.clearCache("cdn1");
	}

}
