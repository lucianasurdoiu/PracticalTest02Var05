package ro.pub.cs.systems.pdsd.practicaltest02var05;

public class Myclass {
	
	private String str;
	private DateTime TimeStamp;
	
	
	public Myclass() {
		this.str = null;
		this.TimeStamp   = null;
	}
	
	public void setStr(String str) {
		this.str = str;
	}
	
	public String getStr() {
		return str;
	}
	
	public void setTimeStamp(DateTime TimeStamp) {
		this.TimeStamp = TimeStamp;
	}
	
	public DateTime getTimeStamp() {
		return TimeStamp;
	}

}
