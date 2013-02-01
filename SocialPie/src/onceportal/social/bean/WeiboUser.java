package onceportal.social.bean;

import java.util.Date;

/**
 * @author furan
 * 
 * Mapping the table 'tb_s_user' in database 'SocialPie'
 * 
 */
public class WeiboUser {
	private long id;
	private String screen_name;
	private char gender;
	private int followers_count;
	private int followees_count;
	private int status_count; //微博数
	private int province;
	private int city;
	private String description;
	private Date created_at;
	
	public WeiboUser(){}
	
    public WeiboUser(long id, String screen_name, char gender, int followers_count, int followees_count,
    		         int status_count, int province, int city, String description, Date created_at){
		this.id = id;
		this.screen_name = screen_name;
		this.gender = gender;
		this.followees_count = followees_count;
		this.followers_count = followers_count;
		this.status_count = status_count;
		this.province = province;
		this.city = city;
		this.description = description;
		this.created_at = created_at;
	}
    
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getScreen_name() {
		return screen_name;
	}
	public void setScreen_name(String screen_name) {
		this.screen_name = screen_name;
	}
	public char getGender() {
		return gender;
	}
	public void setGender(char gender) {
		this.gender = gender;
	}
	public int getFollowers_count() {
		return followers_count;
	}
	public void setFollowers_count(int followers_count) {
		this.followers_count = followers_count;
	}
	public int getFollowees_count() {
		return followees_count;
	}
	public void setFollowees_count(int followees_count) {
		this.followees_count = followees_count;
	}
	public int getStatus_count() {
		return status_count;
	}
	public void setStatus_count(int status_count) {
		this.status_count = status_count;
	}
	public int getProvince() {
		return province;
	}
	public void setProvince(int province) {
		this.province = province;
	}
	public int getCity() {
		return city;
	}
	public void setCity(int city) {
		this.city = city;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Date getCreated_at() {
		return created_at;
	}
	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}
}
