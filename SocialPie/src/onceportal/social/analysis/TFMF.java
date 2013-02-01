package onceportal.social.analysis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import onceportal.social.dao.WeiboDAO;
import onceportal.social.util.SplitWords;

public class TFMF {
	private static Map<String, Integer> TFMap 
		= new HashMap<String, Integer>(); // 用来计算TF
	private static Map<String, Integer> MFMap 
		= new HashMap<String, Integer>(); // 用来计算MF
	private static Map<String, Double> TFMFMap 
		= new LinkedHashMap<String, Double>(); // 用来计算MF
	private static int total_words = 0;
	private static int total_weibos = 0;

	public static void process(long user_id) throws Exception {
		preProcess();
		statistics(user_id);
		computerTFMF();
		sort();
	}
	
	
	/**
	 * 预处理
	 */
	private static void preProcess(){
		
	}
	
	
	/**
	 * 统计某用户的词频TFMap和存在该词的微博数MFMap
	 * @param user_id
	 * @throws Exception 
	 */
 	private static void statistics(long user_id) throws Exception{
		List<String> statusList = WeiboDAO.getWeiboTextsByUserId(user_id);
		total_weibos = statusList.size();
		// 初始化分词器,将配置文件及Data及用户词典等放在该地址下
		SplitWords.init("E:/apache-tomcat-7.0.20/wtpwebapps/SocialPie/WEB-INF/classes/");
		for (String status : statusList) {
			// 分词，标注词性，并获取名词
			List<String> nounsList = SplitWords.split(status);
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

 	
 	/**
 	 * 计算TFMF
 	 */
	private static void computerTFMF() {
	
		for (String word : TFMap.keySet()){
			int length = word.length();
			double tfmf = ((double)TFMap.get(word) / total_words) * 
					      Math.log((double)MFMap.get(word) / total_weibos + 1) / Math.log(2)
					      * length;
			TFMFMap.put(word, tfmf);
		}
	}

	
	/**
	 * TFMF按从大到小排序
	 */
 	private static void sort(){
 	    List<Entry<String,Double>> sortedTFMFList 
 	    	= new ArrayList<Entry<String, Double>>(TFMFMap.entrySet());    
        
        Collections.sort(sortedTFMFList, new Comparator<Map.Entry<String, Double>>() {    
            public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {    
                return o2.getValue().compareTo(o1.getValue());    
            }    
        });  
          
        for(Map.Entry<String,Double> mapping : sortedTFMFList)
        	System.out.println(mapping.getKey()+":"+mapping.getValue()); 
 	}
 	
 	
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		process(1798750043);
	}

}
