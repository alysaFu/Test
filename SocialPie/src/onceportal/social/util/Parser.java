package onceportal.social.util;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class Parser {
	private static SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public Parser(){
		sf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
	}
	public static String Dateparse(java.util.Date date){
		return sf.format(date);
	}
    public static String Dateparse(java.sql.Date date){
    	return sf.format(date);
	}
}
