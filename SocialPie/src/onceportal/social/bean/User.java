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
