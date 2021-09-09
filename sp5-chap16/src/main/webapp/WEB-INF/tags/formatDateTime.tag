<%@ tag body-content = "empty" pageEncoding="utf-8"%>
<%@ tag import="java.time.format.DateTimeFormatter" %>
<%@ tag trimDirectiveWhitespaces="true" %>
<%@ attribute name="value" required="true"
	type="java.time.temporal.TemporalAccessor"%>
<%@ attribute name="pattern" type="java.lang.String" %>
<% if(pattern==null) pattern="yyyy-MM-dd"; %>
<%= DateTimeFormatter.ofPattern(pattern).format(value)%>

<!-- LocalDateTime 값을 워나는 형식으로 출력해주는 커스텀태그 파일. -->