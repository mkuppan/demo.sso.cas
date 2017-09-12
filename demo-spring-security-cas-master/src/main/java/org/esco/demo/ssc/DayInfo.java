package org.esco.demo.ssc;

import java.io.Serializable;

public class DayInfo implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String curDate;

	public String getCurDate() {
		return curDate;
	}

	public void setCurDate(String curDate) {
		this.curDate = curDate;
	}
	
	public String getCurTime() {
		return curTime;
	}

	public void setCurTime(String curTime) {
		this.curTime = curTime;
	}

	private String curTime;

}
