package onceportal.social.analysis;

import java.io.UnsupportedEncodingException;
import java.util.LinkedHashMap;
import java.util.Map;

public class TFMF extends Analyser{

	private static Map<String, Double> TFMFMap; // 用来计算MF

	
	public TFMF() throws UnsupportedEncodingException{
		TFMFMap = new LinkedHashMap<String, Double>();
	}
	
	
	public TFMF(String ictURL) throws UnsupportedEncodingException {
		super(ictURL);
		TFMFMap = new LinkedHashMap<String, Double>();
	}
	
	
	/**
	 * TFMF处理
	 */
	public void process(){
		computerTFMF();
		sort(TFMFMap);
	}
 	
	
 	/**
 	 * 计算TFMF
 	 */
	private void computerTFMF() {
		for (String word : TFMap.keySet()){
			int length = word.length();
			double tfmf = ((double)TFMap.get(word) / total_words) * 
					      Math.log((double)MFMap.get(word) / total_weibos + 1) / Math.log(2)
					      * length;
			TFMFMap.put(word, tfmf);
		}
	}

	
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		//Test 大众中国
		TFMF tfmf = new TFMF();
		tfmf.statistics(1798750043, true);
		tfmf.process();
		tfmf.exit();
	}

}
