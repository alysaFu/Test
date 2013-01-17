<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="css/style.css">
<title>Login</title>
</head>
<body>
<jsp:include page="/jsp/header.jsp" flush="true"/>
<form action="LoginServlet" method="post">
	用户名：<input type="text" name="user"><br>
	密码： <input type="password" name="password"><br>
	<%
	String errorMessage = session.getAttribute("Error")!=null?
			session.getAttribute("Error").toString():"";
	%>
	<div class="warning"><%=errorMessage %><br></div>
	<input type="submit" value="登陆">
</form>
<jsp:include page="/jsp/foot.jsp" flush="true"/>
</body>
</html>