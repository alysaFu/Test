package onceportal.social.analysis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import onceportal.social.dao.WeiboDAO;
import onceportal.social.util.SplitWords;

class WordInfo {
	public int degree;
	public Double tr_Score;
	public Double getTr_Score() {
		return tr_Score;
	}
	public void setTr_Score(Double tr_Score) {
		this.tr_Score = tr_Score;
	}
	public WordInfo(int degree, Double tr_Score) {
		super();
		this.degree = degree;
		this.tr_Score = tr_Score;
	}
	public void incrementDegree() {
		++degree;
	}
	public String toString() {
        return "["+degree+", "+tr_Score+"]";
    }
}
class PR_Graph {
	public HashMap<String, WordInfo> wordMap;    //HashMap<word, TextRankScore>
	public HashMap<String,HashMap<String, Integer>> edgeMap;    //HashMap<fromWord, HashMap<toWord, edgeWeight>>
	
	public PR_Graph() {
		wordMap = new HashMap<String, WordInfo>();
		edgeMap = new HashMap<String, HashMap<String, Integer>>();
	}	
	public boolean addWord(String word) {
		if(wordMap.containsKey(word)) {
			return false;
		}
		else {
			wordMap.put(word, new WordInfo(0,1.0));
			return true;
		}
	}
	public void addEdge(String word_1, String word_2) throws Exception{
		if(word_1.equals(word_2))
			throw new Exception("Error in TextRank: WordPair construct illegal!");
		else {
			//首先更新wordMap中节点度信息
			if(! (wordMap.containsKey(word_1) && wordMap.containsKey(word_2)))
				throw new Exception("Error in TextRank module: wordMap has missed data.");
			wordMap.get(word_1).incrementDegree();
			wordMap.get(word_2).incrementDegree();
			//插入边
			if(edgeMap.containsKey(word_1)) {    //若word_1已经是fromWord
				if(edgeMap.get(word_1).containsKey(word_2)) {    //若该边已经添加，则权重+1
					//正向边增加
					edgeMap.get(word_1).put(word_2, edgeMap.get(word_1).get(word_2) + 1);
					//反向边增加
					edgeMap.get(word_2).put(word_1, edgeMap.get(word_2).get(word_1) + 1);
				}
				else {
					//正向边添加
					edgeMap.get(word_1).put(word_2, 1);
					//反向边添加
					if(edgeMap.containsKey(word_2))    //若word_2已经是fromWord
						edgeMap.get(word_2).put(word_1, 1);
					else {
						edgeMap.put(word_2, new HashMap<String, Integer>());
						edgeMap.get(word_2).put(word_1, 1);
					}
				}
			}
			else {    //若word_1还不是fromWord
				//正向边添加
				edgeMap.put(word_1, new HashMap<String, Integer>());
				edgeMap.get(word_1).put(word_2, 1);
				//反向边添加
				if(edgeMap.containsKey(word_2)) {    //若word_2已经做过fromWord
					edgeMap.get(word_2).put(word_1, 1);
				}
				else {    //若word_2没有做过fromWord
					edgeMap.put(word_2, new HashMap<String, Integer>());
					edgeMap.get(word_2).put(word_1, 1);
				}
			}
		}
	}
	public ArrayList<Entry<String, Integer>> getAdjacentWordsByWord(String word) {
		if(edgeMap.containsKey(word)) {
			ArrayList<Entry<String, Integer>> res = 
					new ArrayList<Entry<String, Integer>>(edgeMap.get(word).entrySet());
			return res;
		}
		else
			return null;
	}
};

public class TextRank {

	private PR_Graph graph;
	private long userId;
	public TextRank(long userId) {
		this.userId = userId;
		graph = new PR_Graph();
	}
	
	private void constructTextRankGraph(List<String> statusList) throws Exception{
		// 初始化分词器,将配置文件及Data及用户词典等放在该地址下
		SplitWords.init("D:/GitHub_Repository/SocialPie/SocialPie/src");
		for (String status : statusList) {
			// 分词，标注词性，并获取名词
			List<String> nounsList = SplitWords.split(status);
			for(String noun : nounsList) {
				graph.addWord(noun);
			}
			for(String noun_1 : nounsList) 
				for(String noun_2 : nounsList) 
					if(noun_1.compareTo(noun_2) < 0) {    //避免重复加边
						graph.addEdge(noun_1, noun_2);
					}
		}
	}
	public void calTextRank(double damp, double minDiff, int iterBound) throws Exception{
		int iterCount = 0;
		double diff = Double.MAX_VALUE;
		for(; diff > minDiff && iterCount < iterBound; ++iterCount) {
			//***此处需要之前存好所有节点的度信息
			diff = 0;
			for(String word : graph.wordMap.keySet()) {
				if(graph.wordMap.get(word).degree == 0) {
					graph.wordMap.get(word).tr_Score = 0.0;
					continue;
				}
				ArrayList<Entry<String, Integer>> adjacentWords = graph.getAdjacentWordsByWord(word);
				double sum = 0;
				for(Entry<String, Integer> entry : adjacentWords) {
					String adjWord = entry.getKey();
					Integer edgeWeight = entry.getValue();
					double tr_score = graph.wordMap.get(adjWord).tr_Score;
					int degree = graph.wordMap.get(adjWord).degree;
					sum += tr_score / degree * edgeWeight; 
				}
				double new_score = 1 - damp + damp * sum;
				diff += Math.abs(new_score-graph.wordMap.get(word).tr_Score);
				WordInfo newWordInfo = new WordInfo(graph.wordMap.get(word).degree, new_score);
				graph.wordMap.put(word, newWordInfo);
			}
			System.out.println(iterCount+"th iter, diff="+diff);
		}
		System.out.println("TextRank completed:\n"+"Iteration Num:"+iterCount);
	}
	private void calTextRank() throws Exception{
		this.calTextRank(0.85, 0.00001, 1024);
	}
	public void process() throws Exception {
		List<String> statusList = WeiboDAO.getWeiboTextsByUserId(userId);
		constructTextRankGraph(statusList);
		calTextRank();
		getTextRankResult();
	}
	
	public List<Entry<String, Double>> getTextRankResult() {
		
		HashMap<String, Double> TRResultMap = new HashMap<String, Double>();
		for(String word : graph.wordMap.keySet()) {
			TRResultMap.put(word, graph.wordMap.get(word).tr_Score);
		}
		ArrayList<Entry<String, Double>> sortedTRList = 
				new ArrayList<Entry<String, Double>>(TRResultMap.entrySet());

		Collections.sort(sortedTRList, new Comparator<Map.Entry<String, Double>>() {
			public int compare(Map.Entry<String, Double> o1, Map.Entry<String, Double> o2) {
				return o2.getValue().compareTo(o1.getValue());
			}
		});

		for (Map.Entry<String, Double> mapping : sortedTRList)
			System.out.println(mapping.getKey()+":"+mapping.getValue()); 
		return sortedTRList;
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		TextRank tr = new TextRank(1798750043);    //大众中国
		TextRank tr = new TextRank(1772710110);    //我
		try {
			tr.process();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
