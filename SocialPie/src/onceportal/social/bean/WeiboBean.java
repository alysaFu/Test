package onceportal.social.bean;

import java.util.Date;


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
	private Date created_at;
	private String text;
	
	public WeiboBean(){
		
	}
	public WeiboBean(long id, long user_id, int repost_count,
			         int comments_count, Date created_at, String text){
		this.id = id;
		this.user_id = user_id;
		this.repost_count = repost_count;
		this.comments_count = comments_count;
		this.created_at = created_at;
		this.text = text;
	}
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
	public Date getCreated_at() {
		return created_at;
	}
	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}
}
