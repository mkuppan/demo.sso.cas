<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
<h1>Demo Landing Page</h1>

<p><a href="secure">Protected Page</a></p>
<%if (request.getUserPrincipal() != null && !request.getUserPrincipal().equals("anonymousUser")) { %>
<p><form action="http://localhost:8090/api/now"><input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"><button type="submit" formmethod="post">Call Time API</button></form></p>
<% } %>
</body>
</html>