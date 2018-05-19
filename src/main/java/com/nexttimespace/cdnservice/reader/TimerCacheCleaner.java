package com.nexttimespace.cdnservice.reader;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

@Component
public class TimerCacheCleaner {
	
	@Autowired
	CacheManager cacheManager;

	public class CacheClearRunner implements Runnable{
		MasterReader masterReader;
		String alias;
		public CacheClearRunner(MasterReader masterReader, String alias) {
			this.masterReader = masterReader;
			this.alias = alias;
		}
		@Override
		public void run() {
			masterReader.clearCache(alias);
		}
	}
	
	public void createCacheClearJob(MasterReader masterReader, String alias, Long time) {
		ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
		CacheClearRunner cacheClearRunner = new CacheClearRunner(masterReader, alias);
		scheduler.scheduleAtFixedRate(cacheClearRunner, time, time, TimeUnit.MILLISECONDS);
	}
}
