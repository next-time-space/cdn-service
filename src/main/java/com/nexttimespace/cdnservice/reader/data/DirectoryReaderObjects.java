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
	public String getTraffic() {
		return traffic;
	}
	public void setTraffic(String traffic) throws Exception {
		this.traffic = traffic;
		setTrafficPercent(traffic);
	}
}
