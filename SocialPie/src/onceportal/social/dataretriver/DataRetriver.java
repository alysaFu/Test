package onceportal.social.dataretriver;

import java.io.IOException;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.PostMethod;

import weibo4j.Oauth;
import weibo4j.model.WeiboException;
import weibo4j.util.BareBonesBrowserLaunch;

public class DataRetriver {
	
	private String access_token;
	
	
	public boolean authorize() {
		
	}
	public static void main(String[] args) throws IOException {
		HttpClient httpClient = new HttpClient();
		HttpMethod method = new PostMethod("https://api.weibo.com/oauth2/access_token?client_id=3039194912&client_secret=1f33ea3fb731090abc52357d88f61588&grant_type=authorization_code&redirect_uri=http://127.0.0.1&code=8b3db891a8766eece9bb58f1262bb076");
		httpClient.executeMethod(method);
		System.out.println(method.getResponseBodyAsString());
		method.releaseConnection();
	}
}
