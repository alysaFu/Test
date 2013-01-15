/**
 * 
 */
package onceportal.social.dataretriver;

import java.io.IOException;

import weibo4j.model.WeiboException;

/**
 * @author WU Yulong
 * 一个定义客户身份验证的接口类，支持用OAuth2.0等不同验证方法实现
 */
public interface IAuthorizer {
	/**
	 * 目前只提供一个方法入口，即验证
	 * @return
	 */
	public boolean authorize() throws Exception;
}
