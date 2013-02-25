/**
 * 
 */
package onceportal.social.dataretriver;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import onceportal.social.bean.WeiboBean;
import onceportal.social.bean.WeiboUser;
import onceportal.social.dao.WeiboDAO;
import onceportal.social.dao.WeiboUserDAO;
import weibo4j.Timeline;
import weibo4j.model.Paging;
import weibo4j.model.Status;
import weibo4j.model.StatusWarpper;
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
		long weiboCount=0, maxWeiboCount=60;
		int currentPage=1, weiboCountPerPage=50;
		try {
			while (firstFlag || weiboCount < maxWeiboCount) {
				if(firstFlag) firstFlag = false;
				StatusWarpper statusWarpper = tm.getFriendsTimeline(0, 0, new Paging(currentPage, weiboCountPerPage));
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
		} catch (WeiboException e) {
			e.printStackTrace();
		}
	}
	
	public void updateSpecificTimeline(String screen_name) throws Exception{
		if(access_token == null)
			throw new Exception("Please set access_token and owner_uid in WeiboUpdater first!");
		if(owner_uid == 0)
			throw new Exception("owner's uid have not defined!");
		Timeline tm = new Timeline();
		WeiboBean weibo = new WeiboBean();
		tm.client.setToken(access_token);
		boolean firstFlag = true;
		long weiboCount=0, maxWeiboCount=100;
		int currentPage=1, weiboCountPerPage=100;
		int timeOutCount = 0;    //用来进行读取超时计数
		while (timeOutCount <= 3 && weiboCount < maxWeiboCount) {    //若连续3次读取超时，则程序报错，否则重新读取
			try {
				while (firstFlag || weiboCount < maxWeiboCount) {
					if (firstFlag)
						firstFlag = false;

					StatusWarpper statusWarpper;
					statusWarpper = tm.getUserTimelineByName(screen_name, new Paging(currentPage,
							weiboCountPerPage), 0, 1);
					timeOutCount = 0;
					// ***temporary for test
					// FileOutputStream fs = new
					// FileOutputStream("D:\\statusWarpper.ser");
					// ObjectOutputStream os = new ObjectOutputStream(fs);
					// os.writeObject(statusWarpper);
					// ***test end
					// ***temporary for test
					// FileInputStream fins = new
					// FileInputStream("D:\\statusWarpper.ser");
					// ObjectInputStream ins = new ObjectInputStream(fins);
					// statusWarpper = (StatusWarpper)ins.readObject();
					// ***test end
					for (Status s : statusWarpper.getStatuses()) {
						// if(firstFlag){
						// WeiboUserDAO.insert(s.getUser());
						// firstFlag = false;
						// }
						weibo.setId(Long.parseLong(s.getId()));
						weibo.setCreated_at(s.getCreatedAt());
						weibo.setUser_id(Long.parseLong(s.getUser().getId()));
						weibo.setRepost_count(s.getRepostsCount());
						weibo.setComments_count(s.getCommentsCount());
						weibo.setText(s.getText());
						WeiboDAO.insert(weibo);
					}
					System.out.println(weibo.getText());
					weiboCount += statusWarpper.getStatuses().size();
					System.out.println("getTotalNumber" + statusWarpper.getTotalNumber());
					maxWeiboCount = statusWarpper.getTotalNumber();
					System.out.println("weibocount" + weiboCount);
					++currentPage;
					System.out.println("--------------------------------------");

				}
			} catch (Exception e) {
				if (!e.getMessage().equals("Read timed out")) {
					timeOutCount = 4;
					e.printStackTrace();
				}
				else
					++ timeOutCount;
			}
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
