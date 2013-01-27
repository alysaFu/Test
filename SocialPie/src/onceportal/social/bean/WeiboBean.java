package onceportal.social.bean;


/**
 * 用来封装微博内容的bean类
 * @author WU Yulong
 *
 */
public class WeiboBean {
	
	private long id;
	private long user_id;
	private int repost_count;
	private int comments_count;
	private String text;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getUser_id() {
		return user_id;
	}
	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}
	public int getRepost_count() {
		return repost_count;
	}
	public void setRepost_count(int repost_count) {
		this.repost_count = repost_count;
	}
	public int getComments_count() {
		return comments_count;
	}
	public void setComments_count(int comments_count) {
		this.comments_count = comments_count;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
}
