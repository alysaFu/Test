package onceportal.social.analysis;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import onceportal.social.dao.WeiboDAO;
import onceportal.social.util.SplitWords;
import onceportal.social.util.OutputStreamWarpper;

class InOut_F {
	public int in_F;
	public int out_F;
	
	public InOut_F(int in_F, int out_F) {
		super();
		this.in_F = in_F;
		this.out_F = out_F;
	}
	public String toString() {
		return "in:"+in_F+" out:"+out_F;
	}
};

public class P_IOLog {

	private long userId;
	private String seed;
	private int seed_F;    //seed词出现的频度
	private int total_F;
	private HashMap<String, InOut_F> inOut_FMap;
	private HashMap<String, Double> IOLogRank;
	ArrayList<Entry<String, Double>> sortedioLogList;
	
	public P_IOLog(long userId) {
		super();
		this.userId = userId;
		seed_F = 0;
		total_F = 0;
	}
	
	/**
	 * 针对种子词统计内外词频
	 */
	private void statistics(List<String> statusList) throws Exception{
		// 初始化分词器,路径参数为配置文件及Data及用户词典的存放路径
		SplitWords.init("D:/GitHub_Repository/SocialPie/SocialPie/src");
		inOut_FMap = new HashMap<String, InOut_F>();
		for(String status : statusList) {
			// 分词，标注词性
			List<String> segList = SplitWords.split(status);
			boolean containSeed = false;
			total_F += segList.size();
			for(String seg : segList) {
				if(seg.equals(seed)) {
					containSeed = true;
					++ seed_F;
				}
			}
//			//对分词去重，即相当于计算MF；若不去重，相当于计算TF，当然，total_F也要修改
//			HashSet<String> segSet = new HashSet<String>(segList);
			for(String seg : segList) {
				if(inOut_FMap.containsKey(seg)) {
					if(containSeed == true)
						++ inOut_FMap.get(seg).in_F;
					else
						++ inOut_FMap.get(seg).out_F;
				}
				else {
					if(containSeed == true)
						inOut_FMap.put(seg, new InOut_F(1,0));
					else
						inOut_FMap.put(seg, new InOut_F(0,1));
				}
			}
		}
	}
	private List<Entry<String, Double>> calP_IOLogRank(double sp) throws Exception{
		IOLogRank = new HashMap<String, Double>();
		
		if(sp < 0 || sp > 1)
			throw new Exception("P_IOLog.calP_IOLogRank: input parameter illegal.");
		for(String word : inOut_FMap.keySet()) {
			int word_F = inOut_FMap.get(word).in_F + inOut_FMap.get(word).out_F;
			double p_in = 1.0 * inOut_FMap.get(word).in_F / seed_F;
			double p_out = 1.0 * (word_F - inOut_FMap.get(word).in_F) / (total_F - seed_F);
			double ioLogRank = Math.log(((1-sp) * p_in + sp) / ((1-sp) * p_out + sp));
			IOLogRank.put(word, ioLogRank);
		}
		
		sortedioLogList = 
				new ArrayList<Entry<String, Double>>(IOLogRank.entrySet());

		Collections.sort(sortedioLogList, new Comparator<Map.Entry<String, Double>>() {
			public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
				return o2.getValue().compareTo(o1.getValue());
			}
		});
		
		return sortedioLogList;
	}
	public void output(PrintWriter writer) {
		for (Map.Entry<String, Double> mapping : sortedioLogList) {
			String s = mapping.getKey()+":"+mapping.getValue();
			writer.println(s);
		}
	}
	public void process(String seed) throws Exception {
		/* 此处判断若seed不在分词词典中，需要将seed加入用户自定义词典*/
		this.seed = seed;
		List<String> statusList = WeiboDAO.getWeiboTextsByUserId(userId);
		statistics(statusList);
		calP_IOLogRank(0.05);
		
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		long userId = 1798750043;
		String seedWord = "大众";
		String resultPath = "D:/WU Yulong/Documents/Study!!!/研究生/研二/Study/毕业之路/实验结果/";
		OutputStreamWarpper streamWarpper = 
				new OutputStreamWarpper(resultPath, userId, "P_IOLog", seedWord);
		P_IOLog iolog = new P_IOLog(userId);
		try {
			iolog.process(seedWord);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		iolog.output(streamWarpper.getWriter());
		streamWarpper.close();
	}

}
