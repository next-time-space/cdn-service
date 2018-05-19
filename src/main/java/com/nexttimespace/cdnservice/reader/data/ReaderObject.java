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
