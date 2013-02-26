package onceportal.social.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class OutputStreamWarpper {
	
	private String resultPath;
	private PrintWriter pw;
	
	private long targetUserId;
	private String AlgoName;
	private String targetWord;
	
	public OutputStreamWarpper(String resultPath, long targetUserId, String algoName, String targetWord) {
		super();
		this.resultPath = resultPath;
		this.targetUserId = targetUserId;
		this.targetWord = targetWord;
		AlgoName = algoName;
		if(!resultPath.endsWith("/"))
			resultPath += "/"; 
		File file = new File(resultPath+"基于_"+targetUserId+"_"+targetWord+"_的"+AlgoName+"结果.txt");
		try {
//			System.setProperty("file.encoding", "UTF8");
			pw = new PrintWriter(new FileWriter(file),true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public OutputStreamWarpper(String resultPath, long targetUserId, String algoName) {
		super();
		this.resultPath = resultPath;
		this.targetUserId = targetUserId;
		AlgoName = algoName;
		File file = new File(resultPath+"基于_"+targetUserId+"_的"+AlgoName+"结果.txt");
		try {
			pw = new PrintWriter(new FileWriter(file),true);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public PrintWriter getWriter() {
		return pw;
	}
	public void close() {
		pw.close();
	}
}
