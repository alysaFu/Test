package onceportal.social.analysis;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import onceportal.social.dao.WeiboDAO;
import onceportal.social.util.SplitWords;

public class Analyser {
	protected static Map<String, Integer> TFMap; // 用来计算TF
	protected static Map<String, Integer> MFMap; // 用来计算MF
	private static List<String> nounsList;
	private static List<String> statusList;
	protected static int total_words = 0;
	protected static int total_weibos = 0;
	private String ictURL; //分词器配置文件路径
	
	public Analyser() throws UnsupportedEncodingException{
		ictURL = new File("").getAbsolutePath()+"/data/ictclas";
		SplitWords.init(ictURL);
	}

	public Analyser(String ictURL) throws UnsupportedEncodingException{
		SplitWords.init(ictURL);
	}
	
	/**
	 * 预处理,去掉微博中的话题
	 */
	private String preProcess(String status){
		status = status.replaceAll("#.*?#", "");
		return status;
	}
	
	/**
	 * 统计某用户的词频TFMap
	 * @param user_id
	 * @throws Exception 
	 */
 	public void statistics(long user_id) throws Exception{
 		TFMap = new HashMap<String, Integer>();
		statusList = WeiboDAO.getWeiboTextsByUserId(user_id);
		total_weibos = statusList.size();
		for (String status : statusList) {
			status = preProcess(status);
			// 分词，标注词性，并获取名词
			nounsList = SplitWords.split(status);
			total_words += nounsList.size();
			for (String noun : nounsList) {
				if (TFMap.containsKey(noun))
					TFMap.put(noun, TFMap.get(noun) + 1);
				else
					TFMap.put(noun, 1);
			}
 		}
		//释放分词器
		SplitWords.exit();
	}
 	
 	/**
 	 * 统计某用户的词频TFMap和存在该词的微博数MFMap
 	 * @param user_id
 	 * @param getMF : 是否计算MF
 	 * @throws Exception
 	 */
 	public void statistics(long user_id, boolean getMF) throws Exception{
 		if(!getMF){
 			statistics(user_id);
 			return;
 		}
 		TFMap = new HashMap<String, Integer>();
		MFMap = new HashMap<String, Integer>();
		statusList = WeiboDAO.getWeiboTextsByUserId(user_id);
		total_weibos = statusList.size();
		// 初始化分词器,将配置文件及Data及用户词典等放在该地址下
		for (String status : statusList) {
			status = preProcess(status);
			// 分词，标注词性，并获取名词
			nounsList = SplitWords.split(status);
			List<String> wordInWeibo = new ArrayList<String>();
			total_words += nounsList.size();
			for (String noun : nounsList) {
				if (TFMap.containsKey(noun))
					TFMap.put(noun, TFMap.get(noun) + 1);
				else
					TFMap.put(noun, 1);
				
				if(!wordInWeibo.contains(noun)){
					wordInWeibo.add(noun);
					if(MFMap.containsKey(noun))
					    MFMap.put(noun, MFMap.get(noun) + 1);
					else
						MFMap.put(noun, 1);
				}
			}
 		}
	}
 
 	public void process(){}
 	
 	
 	/**
 	 * 释放分词器
 	 */
 	public void exit(){
 		SplitWords.exit();
 	}
 	
 	/**
	 * Map按从大到小排序
	 */
 	protected void sort(Map<String, Double> map){
 	    List<Entry<String,Double>> sortedList 
 	    	= new ArrayList<Entry<String, Double>>(map.entrySet());    
        
        Collections.sort(sortedList, new Comparator<Map.Entry<String, Double>>() {    
            public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {    
                return o2.getValue().compareTo(o1.getValue());    
            }    
        });  
        int i = 0;
        for(Map.Entry<String,Double> mapping : sortedList){
        	if(i++ > 20)break;
        	System.out.println(mapping.getKey() + ":" + mapping.getValue()); 
        }
 	}
 	
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		
		//Test
		Analyser a = new TFMF();
		a.statistics(1798750043, true);
		a.process();
		a.exit();
		a = new TFMF("E:/apache-tomcat-7.0.20/data/ictclas");
		a.statistics(1798750043, true);
		a.process();
		a.exit();

	}

}
