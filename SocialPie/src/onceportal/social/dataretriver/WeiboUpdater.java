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
	private long owner_uid;
	private long since_id;
	
	public WeiboUpdater(String access_token, long owner_uid, long since_id) {
		this.access_token = access_token;
		this.owner_uid = owner_uid;
		this.since_id = since_id;
	}
	
	public void updateOwnerTimeline() throws Exception{
		if(access_token == null)
			throw new Exception("Please set access_token and owner_uid in WeiboUpdater first!");
		if(owner_uid == 0)
			throw new Exception("owner's uid have not defined!");
		Timeline tm = new Timeline();
		tm.client.setToken(access_token);
		boolean firstFlag = true;
		long weiboCount=0, maxWeiboCount=0;
		int currentPage=1, weiboCountPerPage=200;
		try {
			while (firstFlag || weiboCount < maxWeiboCount) {
				if(firstFlag) firstFlag = false;
				StatusWapper statusWarpper = tm.getFriendsTimeline(0, 0, new Paging(currentPage, weiboCountPerPage));
				maxWeiboCount = statusWarpper.getTotalNumber();
				for (Status s : statusWarpper.getStatuses()) {
					// Log.logInfo(s.toString());
					System.out.println(s.getText()+"\n");
					// WeiboDAO.persist(s);
				}
				weiboCount += statusWarpper.getStatuses().size();
				++currentPage;
				System.out.println("--------------------------------------");
			}
			System.out.println("weiboCount:"+weiboCount+" page:"+currentPage+"\n");
//			System.out.println(statusWarpper.getNextCursor());
//			System.out.println(statusWarpper.getPreviousCursor());
//			System.out.println(statusWarpper.getTotalNumber());
//			System.out.println(statusWarpper.getHasvisible());
		} catch (WeiboException e) {
			e.printStackTrace();
		}
	}
	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}

	public void setOwner_uid(long owner_uid) {
		this.owner_uid = owner_uid;
	}

	public void setSince_id(long since_id) {
		this.since_id = since_id;
	}
}
