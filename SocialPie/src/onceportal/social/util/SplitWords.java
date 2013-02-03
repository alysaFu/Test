package onceportal.social.util;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import ICTCLAS.I3S.AC.ICTCLAS50;

public class SplitWords {
	static ICTCLAS50 ictclas50;
	static String argu = ".";
	static String usrdir;
	public static boolean init(String base_url) throws UnsupportedEncodingException{
		ictclas50 = new ICTCLAS50();
		argu = base_url;
		usrdir = base_url + "/userdict.txt"; //用户字典路径
		//初始化
		if (ictclas50.ICTCLAS_Init(argu.getBytes("utf-8")) == false)
		{
			System.out.println("Init Fail!");
			return false;
		}
		//设置词性标注集(0 计算所二级标注集，1 计算所一级标注集，2 北大二级标注集，3 北大一级标注集)
		ictclas50.ICTCLAS_SetPOSmap(1);
		//导入用户字典
		int nCount = 0;
		
		byte[] usrdirb = usrdir.getBytes();//将string转化为byte类型
		//导入用户字典,返回导入用户词语个数第一个参数为用户字典路径，第二个参数为用户字典的编码类型
		nCount = ictclas50.ICTCLAS_ImportUserDictFile(usrdirb, 0);
		System.out.println("导入用户词个数" + nCount);
		nCount = 0;
		System.out.println("初始化分词器成功！");
		return true;
	}
	
	/**
	 *  分词
	 * @param sentence
	 * @return
	 */
	public static List<String> split(String sentence) {
		List<String> nouns = new ArrayList<String>();
		try
		{
			//导入用户字典后分词
			byte nativeBytes1[] = ictclas50.ICTCLAS_ParagraphProcess(sentence.getBytes("GB2312"), 2, 1);
			//分词结果
			String nativeStr1 = new String(nativeBytes1, 0, nativeBytes1.length, "GB2312");
			String[] words = nativeStr1.split(" ");
			//过滤名词
			for(String word : words){
				if(word.contains("/n"))
					nouns.add(word.split("/n")[0]);
			}
			return nouns;
		}catch (Exception ex){
			ex.printStackTrace();
			return null;
		}

	}
	public static void exit(){
		//保存用户字典
		ictclas50.ICTCLAS_SaveTheUsrDic();
		//释放分词组件资源
		ictclas50.ICTCLAS_Exit();
		System.out.println("释放分词组件资源成功！");
	}
	
	public static void main(String[] args) throws UnsupportedEncodingException{
		init("E:/apache-tomcat-7.0.20/wtpwebapps/SocialPie/WEB-INF/classes/");
		split("百度一下,什么是效率");
		split("在新浪微博上注册一个账号");
		exit();
	}
}
