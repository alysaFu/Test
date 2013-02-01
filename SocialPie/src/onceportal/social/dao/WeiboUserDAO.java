package onceportal.social.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import onceportal.social.bean.WeiboUser;
import onceportal.social.util.DbManager;
import onceportal.social.util.Parser;
import weibo4j.model.User;

public class WeiboUserDAO {

	private static String TABLE = "tb_s_user";

	/**
	 * 插入一条 微博用户 记录
	 * 
	 * @param w_user
	 * @throws SQLException
	 * @throws Exception
	 */
	public static int insert(WeiboUser w_user) throws SQLException {

		String sql = "INSERT INTO " + TABLE
				+ " VALUES ( ?, ?, ?, ?, ?, ? ,? , ?, ?, ?)";

		return DbManager.executeUpdate(sql, 
				w_user.getId(),
				w_user.getScreen_name(), 
				w_user.getGender(),
				w_user.getFollowers_count(), 
				w_user.getFollowees_count(),
				w_user.getStatus_count(), 
				w_user.getProvince(),
				w_user.getCity(), 
				w_user.getDescription(),
				Parser.Dateparse(w_user.getCreated_at()));

	}

	
	/**
	 * 插入一条 微博用户 记录
	 * 
	 * @param w_user : API获取的微博用户
	 * @throws NumberFormatException 
	 * @throws SQLException
	 * @throws Exception
	 */
	public static int insert(User w_user) throws NumberFormatException, SQLException {

		String sql = "INSERT INTO " + TABLE
				+ " VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		System.out.println(w_user.getCreatedAt());
			return DbManager.executeUpdate(sql, 
					Integer.parseInt(w_user.getId()),
					w_user.getScreenName(),
					w_user.getGender(),
					w_user.getFollowersCount(),
					w_user.getFriendsCount(),
					w_user.getStatusesCount(),
					w_user.getProvince(),
					w_user.getCity(), 
					w_user.getDescription(),
					Parser.Dateparse(w_user.getCreatedAt()));
		
	}
	
	/**
	 * 列出所有的 weibo
	 * 
	 * @return
	 * @throws Exception
	 */
	public static List<WeiboUser> getAllWeiboUsers() throws Exception {

		List<WeiboUser> list = new ArrayList<WeiboUser>();

		String sql = "SELECT * FROM " + TABLE + " ORDER BY id DESC ";

		Connection conn = null;
		PreparedStatement preStmt = null;
		ResultSet rs = null;

		try {
			conn = DbManager.getConnection();
			preStmt = conn.prepareStatement(sql);

			rs = preStmt.executeQuery();

			while (rs.next()) {
				WeiboUser w_user = new WeiboUser();

				w_user.setId(rs.getInt("id"));
				w_user.setScreen_name(rs.getString("screen_name"));
				w_user.setGender((Character) rs.getObject("gender"));
				w_user.setFollowers_count(rs.getInt("followers_count"));
				w_user.setFollowees_count(rs.getInt("followees_count"));
				w_user.setProvince(rs.getInt("province"));
				w_user.setCity(rs.getInt("city"));
				w_user.setStatus_count(rs.getInt("status_count"));
				w_user.setDescription(rs.getString("description"));
				w_user.setCreated_at(rs.getDate("created_at"));

				list.add(w_user);
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

	/**
	 * 更新微博用户信息
	 * 
	 * @param weibo
	 * @throws Exception
	 */
	public static int update(WeiboUser w_user) throws Exception {

		String sql = "UPDATE "+ TABLE + "SET where id=?";
		return DbManager.executeUpdate(sql, w_user.getId());
	}

	/**
	 * 根据id 删除 微博用户
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public static int delete(Integer id) throws Exception {

		String sql = "DELETE FROM " + TABLE + " WHERE id = ? ";

		return DbManager.executeUpdate(sql, id);

	}

	/**
	 * 利用用户id，查找一个微博用户
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public static WeiboUser find(long id) throws Exception {

		String sql = "SELECT * FROM " + TABLE + " WHERE id = ? ";

		Connection conn = null;
		PreparedStatement preStmt = null;
		ResultSet rs = null;

		try {
			conn = DbManager.getConnection();
			preStmt = conn.prepareStatement(sql);
			preStmt.setLong(1, id);

			rs = preStmt.executeQuery();

			if (rs.next()) {
				WeiboUser w_user = new WeiboUser();

				w_user.setId(rs.getInt("id"));
				w_user.setScreen_name(rs.getString("screen_name"));
				w_user.setGender((Character) rs.getObject("gender"));
				w_user.setFollowers_count(rs.getInt("followers_count"));
				w_user.setFollowees_count(rs.getInt("followees_count"));
				w_user.setProvince(rs.getInt("province"));
				w_user.setCity(rs.getInt("city"));
				w_user.setStatus_count(rs.getInt("status_count"));
				w_user.setDescription(rs.getString("description"));
				w_user.setCreated_at(rs.getDate("created_at"));

				return w_user;
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
	
	public static void main(String[] args){
		System.out.println("start");
		User user = new User();
		user.setId("123");
		user.setScreenName("test");
		user.setGender("m");
		user.setProvince(1);
		user.setCity(1);
		user.setDescription("test");
		user.setFriendsCount(233);
		user.setFollowersCount(12322);
		user.setStatusesCount(2342);
		user.setCreatedAt(new java.util.Date());
		try {
			WeiboUserDAO.insert(user);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	System.out.println("stop");
	}

}
