<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<title>메인</title>
</head>
<body>
	<c:if test="${empty authInfo }">
	<p>환영합니다.</p>
	<p><a href="<c:url value="/register/step1" />">[회원가입하기]</a>
	<a href="<c:url value="/login"/>">[로그인]</a>
	</p>
	</c:if>
	
	<!-- WebMvcConfigurer에서 addViewControllers가 구현 -->
	
	<!-- LoginController은 로그인성공시, HttpSession의 authInfo에 인증정보객체를 저장
	따라서, 밑의 조건의 true가 되어, 이후의 내용이 출력됨. 반대로 로그인하지 안했다면,
	empty authInfo가 참이므로, 회원가입&로그인 기능이 활성화 -->
	<c:if test="${! empty authInfo}">
	<p> ${authInfo.name}님, 환영합니다 </p>
	<p>
		<a href="<c:url value="/edit/changePassword"/>">[비밀번호 변경]</a>
		<a href="<c:url value="/logout"/>">[로그아웃]</a>
		<a href="<c:url value="/edit/withdrawer"/>">[회원탈퇴]</a>
	</p>
	<p>
	<!-- <a href="<c:url value="/bulletin/main"/>"> [게시판으로 이동]</a>  -->	
		<a href="<c:url value="/calendar/calendar/${authInfo.id }"/>"> [달력으로 이동]</a>
		<a href="<c:url value="/calendar/searchfood"/>"> [음식 검색기]</a>
	</p>
	</c:if>
</body>
</html>