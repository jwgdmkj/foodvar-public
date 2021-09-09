<%@ page contentType="text/html; charset=utf-8"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<title>
	<spring:message code="delete.title"/></title>
</head>
<body>
	<form:form>
	<p>
	<p>회원삭제를 하실 거면 비밀번호를 입력해주세요</p>
	<label> <spring:message code="realPassword"/>:<br>
	<form:password path="realPassword"/>
	<form:errors path="realPassword"/> </label>
	
	<input type="submit" value="<spring:message code="delete.btn"/>">
	<a href="<c:url value='/main'/>">
		<input type="button" value="<spring:message code="go.main"/>">
	</a>
	</form:form>
</body>
</html>