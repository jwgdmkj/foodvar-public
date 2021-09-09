<%@ page contentType="text/html; charset=utf-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html>
<html>
<head>
<title> <spring:message code="login.title"/></title>
</head>
<body>
	<p>
	<spring:message code="login.done"/>
	</p>
	<p>
	<a href="<c:url value='/main'/>">
		[<spring:message code="go.main" />]
	</a>
	</p>
</body>
</html>

<!-- a href랑 code는 무엇인가? c:url 어쩌군 무엇? property에서 나옴 -->
<!-- 이를 하고나면, 남은건 컨트롤러와 스프링을 빈으로 등록하는 것
MemberConfig와 ControllerConfig설정파일에 앞서, 작성한
AuthService와 LoginController을 빈으로 등록 -->