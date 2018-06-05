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
