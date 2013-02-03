package onceportal.social.dataretriver;

/**
 * @author furan
 * 
 * 更新微博用户表
 *
 */
public class WeiboUserUpdater {
	private String access_token;
	private long owner_uid;
	private long since_id;
	
	public WeiboUserUpdater(String access_token, long owner_uid, long since_id) {
		this.access_token = access_token;
		this.owner_uid = owner_uid;
		this.since_id = since_id;
	}
}
