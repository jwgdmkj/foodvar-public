<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<title>설문조사</title>
</head>
<body>
	<h2> 설문조사</h2>
	<form method="post">
	<!-- 받은 리스트: items(전달받은 attribute명), status: 현 상태-->
	<c:forEach var="q" items="${questions }" varStatus="status">
	<p>
		${status.index+1}.${q.title}<br/> <!--인덱스:0,1,2.. title: 현 val의 내용-->
		
		
		<c:if test="${q.choice}">
			<c:forEach var="option" items="${q.options}">
			<label><input type="radio"
			name="as[${status.index }]" value="${option }">
			${option }</label>
			</c:forEach>
			</c:if>
			
		<!-- text형을 input하는 type. -->	
		<c:if test="${!q.choice }">
			<input type="text" name="as[${status.index }]">
		</c:if>
		</p>
	</c:forEach>
	
	<p>
	<label>응답자나이:<br>
	<input type="text" name= "res.age">
	</label>
	</p>
	
	<input type="submit" value="전송">
	</form>
</body>
</html>