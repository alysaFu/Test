package onceportal.social.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import onceportal.social.bean.WeiboBean;
import onceportal.social.util.DbManager;

public class WeiboDAO {
	
	private static String TABLE = "tb_s_weibo"; 
	/**
	 * 插入一条 Weibo 记录
	 * 
	 * @param weibo
	 * @throws Exception
	 */
	public static int insert(WeiboBean weibo) throws Exception {

		String sql = "INSERT INTO "+TABLE+" VALUES ( NULL, ?, ?, ?, ?, ? ) ";

		return DbManager.executeUpdate(sql, 
				weibo.getId(), 
				weibo.getUser_id(),
				weibo.getRepost_count(),
				weibo.getComments_count(),
				weibo.getText());
	}

	/**
	 * 列出某个用户user_id下所有的 weibo
	 * 
	 * @return
	 * @throws Exception
	 */
	public static List<WeiboBean> getWeibosByUser(long uid) throws Exception {
	
		List<WeiboBean> list = new ArrayList<WeiboBean>();
	
		String sql = "SELECT * FROM "+TABLE+" WHERE user_id=? ORDER BY id DESC ";
	
		Connection conn = null;
		PreparedStatement preStmt = null;
		ResultSet rs = null;
	
		try {
			conn = DbManager.getConnection();
			preStmt = conn.prepareStatement(sql);
			preStmt.setLong(1, uid);
			
			rs = preStmt.executeQuery();
	
			while (rs.next()) {
				WeiboBean weibo = new WeiboBean();
				
				weibo.setId(rs.getInt("id"));
				weibo.setUser_id(rs.getLong("user_id"));
				weibo.setRepost_count(rs.getInt("repost_count"));
				weibo.setComments_count(rs.getInt("comment_count"));
				weibo.setText(rs.getString("text"));
				
				list.add(weibo);
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
	 * 列出所有的 weibo
	 * 
	 * @return
	 * @throws Exception
	 */
	public static List<WeiboBean> getAllWeibos() throws Exception {
	
		List<WeiboBean> list = new ArrayList<WeiboBean>();
	
		String sql = "SELECT * FROM "+TABLE+" ORDER BY id DESC ";
	
		Connection conn = null;
		PreparedStatement preStmt = null;
		ResultSet rs = null;
	
		try {
			conn = DbManager.getConnection();
			preStmt = conn.prepareStatement(sql);
			
			rs = preStmt.executeQuery();
	
			while (rs.next()) {
				WeiboBean weibo = new WeiboBean();
				
				weibo.setId(rs.getInt("id"));
				weibo.setUser_id(rs.getLong("user_id"));
				weibo.setRepost_count(rs.getInt("repost_count"));
				weibo.setComments_count(rs.getInt("comment_count"));
				weibo.setText(rs.getString("text"));
				
				list.add(weibo);
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
	 * 保存 weibo
	 * 
	 * @param weibo
	 * @throws Exception
	 */
	public static int modify(WeiboBean weibo) throws Exception {

		String sql = "UPDATE "+TABLE+"SET user_id = ?, repost_count = ?, comment_count = ?, text = ? WHERE id = ? ";
		return DbManager.executeUpdate(sql, weibo.getUser_id(), weibo.getRepost_count(),
				weibo.getComments_count(), weibo.getText(), weibo.getId());
	}

	/**
	 * 删除 weibo
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public static int delete(Integer id) throws Exception {

		String sql = "DELETE FROM "+TABLE+" WHERE id = ? ";

		return DbManager.executeUpdate(sql, id);

	}

	/**
	 * 利用微博id，查找一条 weibo 记录
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public static WeiboBean find(long id) throws Exception {

		String sql = "SELECT * FROM "+TABLE+" WHERE id = ? ";

		Connection conn = null;
		PreparedStatement preStmt = null;
		ResultSet rs = null;

		try {
			conn = DbManager.getConnection();
			preStmt = conn.prepareStatement(sql);
			preStmt.setLong(1, id);

			rs = preStmt.executeQuery();

			if (rs.next()) {
				WeiboBean weibo = new WeiboBean();
				weibo.setId(rs.getInt("id"));
				weibo.setUser_id(rs.getLong("user_id"));
				weibo.setRepost_count(rs.getInt("repost_count"));
				weibo.setComments_count(rs.getInt("comment_count"));
				weibo.setText(rs.getString("text"));
				return weibo;
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
}