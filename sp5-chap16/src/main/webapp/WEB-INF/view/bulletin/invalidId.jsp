<%@ page contentType="text/html; charset=utf-8"%>
<!DOCTYPE html>
<html>
<head>
<title>에러</title>
</head>
<body>
	잘못된 요청입니다.
	<a href="<c:url value='/bulletin/main'/>">
	<input type="button" value="<spring:message code="go.main"/>"></a>
</body>
</html>