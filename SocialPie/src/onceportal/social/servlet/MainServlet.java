package onceportal.social.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import onceportal.social.bean.User;
import onceportal.social.dataretriver.WeiboUpdater;

public class MainServlet extends HttpServlet{
	
	private static final long serialVersionUID = 1L;

	/**
	 * Default Constructor
	 */
	public MainServlet() {
		super();
	}
	
	private void weiboUpdate() {
		User owner = (User)this.getServletContext().getAttribute("currentUser");
		WeiboUpdater updater = new WeiboUpdater(owner.getAccessToken(), 
				owner.getUid(), owner.getSince_id());
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter printWriter = response.getWriter();
		printWriter.println("<h1>Hello from MainServlet</h1>");
		
		User owner = (User)this.getServletContext().getAttribute("currentUser");
		WeiboUpdater updater = new WeiboUpdater(owner.getAccessToken(), owner.getUid(), owner.getSince_id());
		try {
			updater.updateOwnerTimeline();
		}catch(Exception e) {
			throw new ServletException(e.getMessage());
		}
		printWriter.println("<h1>Congratulations! update successfully!</h1>");
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException{
		
	}
}
