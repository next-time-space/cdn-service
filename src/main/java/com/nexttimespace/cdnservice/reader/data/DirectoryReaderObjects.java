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

package com.nexttimespace.cdnservice.reader.data;

public class DirectoryReaderObjects extends ReaderObject{
	private boolean cachable = false;
	private String directoryPath;
	private String traffic;
	
	public boolean isCachable() {
		return cachable;
	}
	public void setCachable(boolean cachable) {
		this.cachable = cachable;
	}
	public String getDirectoryPath() {
		return directoryPath;
	}
	public void setDirectoryPath(String directoryPath) {
		this.directoryPath = directoryPath;
	}

	public void setTraffic(String traffic) throws Exception {
		this.traffic = traffic;
		setTrafficPercent(traffic);
	}
}
