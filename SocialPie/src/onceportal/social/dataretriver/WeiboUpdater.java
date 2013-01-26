/**
 * 
 */
package onceportal.social.dataretriver;

import weibo4j.Timeline;
import weibo4j.model.Paging;
import weibo4j.model.Status;
import weibo4j.model.StatusWapper;
import weibo4j.model.WeiboException;

/**
 * @author WU Yulong
 * 更新用户相关的微博信息
 */
public class WeiboUpdater {
	
	private String access_token;
	private String owner_uid;
	private long since_id;
	
	public WeiboUpdater(String access_token, String owner_uid, long since_id) {
		this.access_token = access_token;
		this.owner_uid = owner_uid;
		this.since_id = since_id;
	}
	
	public void updateOwnerTimeline() throws Exception{
		if(access_token == null || owner_uid == null)
			throw new Exception("Please set access_token and owner_uid in WeiboUpdater first!");
		Timeline tm = new Timeline();
		tm.client.setToken(access_token);
		try {
			StatusWapper statusWarpper = tm.getFriendsTimeline(0, 0, new Paging(since_id));
			for(Status s : statusWarpper.getStatuses()){
//				Log.logInfo(s.toString());
				System.out.println(s.toString());
//				WeiboDAO.persist(s);
			}
			System.out.println(statusWarpper.getNextCursor());
			System.out.println(statusWarpper.getPreviousCursor());
			System.out.println(statusWarpper.getTotalNumber());
			System.out.println(statusWarpper.getHasvisible());
		} catch (WeiboException e) {
			e.printStackTrace();
		}
	}
	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	public void setOwner_uid(String owner_uid) {
		this.owner_uid = owner_uid;
	}

	public void setSince_id(long since_id) {
		this.since_id = since_id;
	}
}
