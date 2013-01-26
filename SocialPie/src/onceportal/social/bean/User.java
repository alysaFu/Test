package onceportal.social.bean;

/**
 * 一个对应于数据库中user表的java bean类
 * @author WU Yulong
 *
 */
public class User {
	private Integer id;
	private String name;
	private String password;
	private String accessToken;	//OAuth token
	private String expireDate;		//token过期时间
	private long uid;	//该用户微博账号的uid
	private long since_id;	//当前数据库已索引到的最新微博id
	
	public long getUid() {
		return uid;
	}
	public void setUid(long uid) {
		this.uid = uid;
	}
	public long getSince_id() {
		return since_id;
	}
	public void setSince_id(long since_id) {
		this.since_id = since_id;
	}
	public String getExpireDate() {
		return expireDate;
	}
	public void setExpireDate(String expireDate) {
		this.expireDate = expireDate;
	}
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
