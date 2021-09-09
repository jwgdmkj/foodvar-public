<%@ page contentType="text/html; charset=utf-8"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<title>글쓰기</title>
</head>
<body>
<!-- editer가 true일 땐, 전달받은 커맨드객체의 title과 content를 삽입 -->
<form:form>
 <p>
		<label><spring:message code="bulletin.title"/>:<br>
		<c:choose>
			<c:when test="${editer == true }">
				<form:input path="title" value="${command.title }"
				style="width:400px;height:30px;font-size:20px;"/>
				<form:errors path="title"/>
			</c:when>
			<c:otherwise>
				<form:input path="title" 
				style="width:400px;height:30px;font-size:20px;"/>
				<form:errors path="title"/>
			</c:otherwise>
		</c:choose>

	
		</label>
</p>
	<p>
		<label><spring:message code="bulletin.content"/>:<br>
		<c:choose>
			<c:when test="${editer == true }">
				<form:textarea path="content" cols="50" rows="10"></form:textarea>
				<form:errors path="content"/>
			</c:when>
			<c:otherwise>
				<form:textarea path="content" cols="50" rows="10"
				value="${command.content }"></form:textarea>
				<form:errors path="content"/>
			</c:otherwise>
		</c:choose>
		</label>
	</p>
	<p>
		<input type="submit" value="<spring:message code="go.bulletinmain"/>">
		<a href="<c:url value='/bulletin/main'/>">
		<input type="button" value="<spring:message code="go.main"/>"></a>
	</p>
</form:form>
</body>
</html>