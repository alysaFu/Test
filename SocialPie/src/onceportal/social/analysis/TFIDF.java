package onceportal.social.analysis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

public class TFIDF extends Analyser{
	private static Map<String,Double> IDFMap; //存储IDF信息
	private static Map<String,Double> TFIDFMap; //存储TFIDF信息
	private String idfURL;
	private static double max = 0;
	
	public TFIDF() throws UnsupportedEncodingException{
		idfURL = new File("").getAbsolutePath()+"/data/IDF.txt";
		IDFMap = new HashMap<String,Double>();
		TFIDFMap = new HashMap<String,Double>();
	}
	
	
	public TFIDF(String idfURL) throws UnsupportedEncodingException{
		IDFMap = new HashMap<String,Double>();
		TFIDFMap = new HashMap<String,Double>();
		this.idfURL = idfURL; 
	}
	
	
	public TFIDF(String ictURL, String idfURL)throws UnsupportedEncodingException{
		super(ictURL);
		this.idfURL = idfURL; 
		IDFMap = new HashMap<String,Double>();
		TFIDFMap = new HashMap<String,Double>();
	}
	
	
	/**
	 * TF-IDF处理
	 */
	public void process(){
		System.out.println("使用TFIDF提取关键字：");
		IDF(idfURL);
		TF_IDF();
		//排序
		sort(TFIDFMap);
	}
	
	/**
	 * 计算TF-IDF
	 */
	private static void TF_IDF(){
		double tfidf = 0;
		for (String word : TFMF.TFMap.keySet()){
			if(IDFMap.containsKey(word))
				tfidf = ((double)TFMF.TFMap.get(word)) * IDFMap.get(word);
			else
				tfidf = (double)TFMF.TFMap.get(word) * max;
			TFIDFMap.put(word, tfidf);
		}
	}
	
	
	/**
	 * 计算IDF
	 * @param url: IDF文件的路径
	 */
	private static void IDF(String url){
		File file = new File(url);
		BufferedReader reader = null;
		String key;
		double value;
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(file),"gbk"));
			String line = null;
			while((line = reader.readLine()) != null){
				key = line.split(" ")[0];
				value = Double.parseDouble(line.split(" ")[1]);
				IDFMap.put(key, Math.log(value));
				max = max > Math.log(value) ? max : Math.log(value);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(reader != null)
				try {
					reader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}
	
	
	public static void main(String[] args){
		try {
			//Test 大众中国
			TFIDF tfidf = new TFIDF();
			tfidf.statistics(1798750043, true);
			tfidf.process();
			tfidf.exit();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
