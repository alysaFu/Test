package onceportal.social.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import onceportal.social.bean.User;
import onceportal.social.util.DbManager;

/**
 * 实现User对象在数据库上的存取的DAO类
 * @author WU Yulong
 *
 */
public class UserDAO {
	/**
	 * 插入一条 User 记录
	 * 
	 * @param User
	 * @throws Exception
	 */
	public static int insert(User User) throws Exception {

		String sql = "INSERT INTO tb_User VALUES ( NULL, ?, ?, ?, ?, ? ) ";

		return DbManager.executeUpdate(sql, 
				User.getName(), 
				User.getPassword(),
				User.getAccessToken(),
				User.getExpireDate(),
				User.getUid());
	}

	/**
	 * 保存 User
	 * 
	 * @param User
	 * @throws Exception
	 */
	public static int save(User user) throws Exception {

		String sql = "UPDATE tb_User SET name = ?," +
				" password = ?, access_token = ?, expire_date = ?, uid = ?, since_id = ? WHERE id = ? ";
		return DbManager.executeUpdate(sql, user.getName(), user.getPassword(),
				user.getAccessToken(), user.getExpireDate(), user.getId(), user.getUid(), user.getSince_id());
	}

	/**
	 * 删除 User
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public static int delete(Integer id) throws Exception {

		String sql = "DELETE FROM tb_User WHERE id = ? ";

		return DbManager.executeUpdate(sql, id);

	}

	/**
	 * 查找一条 User 记录
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public static User find(String name) throws Exception {

		String sql = "SELECT * FROM tb_User WHERE name = ? ";

		Connection conn = null;
		PreparedStatement preStmt = null;
		ResultSet rs = null;

		try {
			conn = DbManager.getConnection();
			preStmt = conn.prepareStatement(sql);
			preStmt.setString(1, name);

			rs = preStmt.executeQuery();

			if (rs.next()) {
				User user = new User();
				user.setId(rs.getInt("id"));
				user.setName(rs.getString("name"));
				user.setPassword(rs.getString("password"));
				user.setAccessToken(rs.getString("access_token"));
				user.setExpireDate(rs.getString("expire_date"));
				user.setUid(rs.getLong("uid"));
				user.setSince_id(rs.getLong("since_id"));
				return user;
			} else {
				return null;
			}

		} finally {
			if (rs != null)
				rs.close();
			if (preStmt != null)
				preStmt.close();
			if (conn != null)
				conn.close();
		}
	}
	
	/**
	 * 列出所有的 User
	 * 
	 * @return
	 * @throws Exception
	 */
	public static List<User> listUsers() throws Exception {

		List<User> list = new ArrayList<User>();

		String sql = "SELECT * FROM tb_User ORDER BY id DESC ";

		Connection conn = null;
		PreparedStatement preStmt = null;
		ResultSet rs = null;

		try {
			conn = DbManager.getConnection();
			preStmt = conn.prepareStatement(sql);

			rs = preStmt.executeQuery();

			while (rs.next()) {
				User User = new User();
				
				User.setId(rs.getInt("id"));
				User.setName(rs.getString("name"));
				User.setPassword(rs.getString("password"));
				User.setAccessToken(rs.getString("access_token"));
				User.setExpireDate(rs.getString("expire_date"));
				User.setUid(rs.getLong("uid"));
				User.setSince_id(rs.getLong("since_id"));
				
				list.add(User);
			}

		} finally {
			if (rs != null)
				rs.close();
			if (preStmt != null)
				preStmt.close();
			if (conn != null)
				conn.close();
		}
		return list;
	}
}
