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

public class ReaderObject {
	private int trafficPercent = -1;
	private String alias;
	private String[] responseHeader;
	
	public int getTrafficPercent() {
		return trafficPercent;
	}
	public void setTrafficPercent(String traffic) throws Exception {
		try {
			if(trafficPercent == -1) {
				trafficPercent = Integer.parseInt(traffic);
			}
		} catch(NumberFormatException nfe) {
			throw new Exception("Error parsing traffic percentage for Directory reader");
		}
	}
	
	public String[] getResponseHeader() {
		return responseHeader;
	}
	public void setResponseHeader(String[] responseHeader) {
		this.responseHeader = responseHeader;
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
}
