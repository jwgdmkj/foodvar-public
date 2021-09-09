<%@ page contentType="text/html; charset=utf-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html>
<html>
<head>
<title><spring:message code="member.register"/></title>
</head>
<body>
	<h2><spring:message code="member.info"/></h2>
	<form:form action="step3" modelAttribute="registerRequest">
	<p>
		<label><spring:message code="email"/>:<br>
		<form:input path="email"/>
		<form:errors path="email"/>
		</label>
	</p>
	
	<p>
		<label><spring:message code="name"/>:<br>
		<form:input path="name"/>
		<form:errors path="name"/>
		</label>
	</p>
	
	<p>
		<label><spring:message code="password"/>:<br>
		<form:password path="password"/>
		<form:errors path="password" />
		</label>
	</p>
	
	<p>
        <label><spring:message code="password.confirm" />:<br>
        <form:password path="confirmPassword" />
        <form:errors path="confirmPassword"/>
        </label>
    </p>
	<input type="submit" value= "<spring:message code="register.btn"/>">
	</form:form>
</body>
</html>
<!-- spring:message는, 코드값(label.properties)와 일치하는 프로퍼티의 값을 출력한다 -->
<!-- forms:errors태그를 이용해 에러에 해당하는 메세지를 출력해본다.
errrors.rejectValue("emial","required")코드로 emil프로퍼티에 required에러코드 추가,
커맨드객체 이름이 registerRequest라면 다음순서대로 메시지코드를 검색
1.required.registerRequest.email 2.required.email 3.required.String
4.required 이렇게, 에러코드+.+커맨드객체이름+필드명... 순으로 우선순위가 높은 required.email메시지
코드를 사용해 메세지를 출력 -->