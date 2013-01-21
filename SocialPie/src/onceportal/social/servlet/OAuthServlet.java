package onceportal.social.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import onceportal.social.bean.User;
import onceportal.social.dao.UserDAO;

import weibo4j.Oauth;
import weibo4j.http.AccessToken;
import weibo4j.model.WeiboException;

/**
 * 读取config.properties中的accessToken,若没有则进行新浪微博OAuth2.0授权验证
 */
//@WebServlet(name="OAuthServlet", urlPatterns={"/AuthorizeServlet"})
public class OAuthServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public OAuthServlet() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request
			, HttpServletResponse response)
					throws ServletException, IOException {
		// TODO Auto-generated method stub

		//检查传入模式。若存在user参数则为登入验证模式；若存在expire参数则为过期
		//验证方式；若存在code则为OAuth回调模式
		String mode = (String)request.getAttribute("mode");
		if(mode!=null && !mode.isEmpty()) {
			if("login".equals(mode)) {
				processLogin(request, response);
			}
			else if("expire".equals(mode)) {
				refreshToken(response);
			}
			else {
				request.getSession().setAttribute("error", "验证模块参数不合法。");
				request.getRequestDispatcher("/jsp/errorHandler.jsp")
				.forward(request, response);
			}
		} 
		else {
			callbackByCode(request, response);
		}
	}

	/**
	 * 处理用户登录，首先查看tb_user中是否有accessToken，没有或者过期则获取
	 * @param request
	 * @param response
	 */
	private void processLogin(HttpServletRequest request, HttpServletResponse response) {
		refreshToken(response);
	}
	
	private void refreshToken(HttpServletResponse response) {
		Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("GMT+8"));
		User user = (User)this.getServletContext().getAttribute("currentUser");
		if(user.getAccessToken()!=null && user.getExpireDate()!=null) {
			//验证AccessToken是否过期
			long expireDate = Long.parseLong(user.getExpireDate());
			if(expireDate < cal.getTimeInMillis()) {	//Token已过期
				oauthValidation(response);
			}
//			long vilidTime = user.get 
//			if(user.getExpireDate())
			try {
				response.sendRedirect(this.getServletContext().getContextPath()+"/MainServlet");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else {
			oauthValidation(response);
		}
	}
	
	/**
	 * 接收新浪微博OAuth平台的回调
	 * @param request
	 * @param response
	 */
	private void callbackByCode(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException{
		String code = request.getParameter("code");
		if(code!=null && !code.isEmpty()) {
			Oauth oauth = new Oauth();
			try {
				Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("GMT+8"));
				AccessToken accessToken = oauth.getAccessTokenByCode(code);
				Long expireDate = cal.getTimeInMillis()+Long.parseLong(accessToken.getExpireIn());
				User user = (User)this.getServletContext().getAttribute("user");
				user.setAccessToken(accessToken.getAccessToken());
				user.setExpireDate(expireDate.toString());
				try {
					UserDAO.save(user);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println(accessToken);
			}
			catch(WeiboException e) {
				throw new ServletException(e.toString());
			}
			try {
				response.sendRedirect("/MainServlet");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	private void oauthValidation(HttpServletResponse response) {
		Oauth oauth = new Oauth();
		try {
			response.sendRedirect(oauth.authorize("code"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WeiboException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		this.doGet(request, response);
	}

}
