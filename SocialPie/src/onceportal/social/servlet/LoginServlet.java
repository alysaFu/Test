package onceportal.social.servlet;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import onceportal.social.dao.UserDAO;
import onceportal.social.bean.User;
/**
 * Servlet implementation class LoginServlet
 */
//@WebServlet({ "/LoginServlet" })
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, 
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.getRequestDispatcher("/jsp/login.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, 
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String userName = request.getParameter("user"); 
		if(userName!=null && !userName.isEmpty()) {
			try {
				User user = UserDAO.find(userName);
				if(user == null || !request.getParameter("password")
						.equals(user.getPassword())) {
					HttpSession session = request.getSession();
					session
					.setAttribute("Error", "您输入的用户名和密码不正确。");
					doGet(request, response);
					return;
				}
				else {
					//用户名和密码正确，转入OAuth验证Servlet
					request.setAttribute("mode", "login");
					request.setAttribute("user", user);
					ServletContext context= getServletContext();
					context.getRequestDispatcher("/AuthorizeServlet").forward(request, response);
				}
			} catch(Exception e) {
				throw new ServletException("Exception in UserDAO.");
			}
		}
		else {
			HttpSession session = request.getSession();
			session.setAttribute("Error", "请输入用户名和密码。");
			doGet(request, response);
			return;
		}
	}

}
