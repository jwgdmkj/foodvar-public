<%@ page contentType="text/html; charset=utf-8"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tf" tagdir="/WEB-INF/tags" %>
<!DOCTYPE html>
<html>
<head>
<title>게시판 메인</title>
</head>
<body>

	<p>
	<a href="<c:url value='/bulletin/write'/>">
	<input type="button" value="<spring:message code="go.write"/>">
	</a>
	</p>
	
	<p>
  	<c:if test="${! empty bulletins}"> 
	<table>
	<tr>
		<th>번호</th><th>제목</th><th>이름</th><th>조회수</th><th>댓글수</th>
		<th>추천수</th><th>등록일</th>
	</tr>
	
		<c:forEach var="bul" items="${bulletins }">
	<tr>
		<td>${bul.num}</td>
		<td><a href="<c:url value="/bulletin/${bul.num}"/>">
		${bul.title }</a></td>
		<td>${bul.writer }</td>
		<td>${bul.watcher }</td>
		<td>${bul.reply }</td> <td>${bul.recommend }</td>
		<td><tf:formatDateTime value="${bul.registerDateTime}"
			pattern="yyyy-MM-dd"/></td>
	</tr>
	</c:forEach>
	
	</table>
 	</c:if> 
</body>
</html>