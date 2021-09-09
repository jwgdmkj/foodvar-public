<%@ page contentType="text/html; charset=utf-8"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tf" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>
<html>
<head>
<title> 드신 음식은 무엇인가요? </title>

<script>
//////////////httprequest///////////////////////
var httpRequest = null; 
function getXMLHttpRequest(){
    var httpRequest = null;

    if(window.ActiveXObject){
        try{
            httpRequest = new ActiveXObject("Msxml2.XMLHTTP");    
        } catch(e) {
            try{
                httpRequest = new ActiveXObject("Microsoft.XMLHTTP");
            } catch (e2) { httpRequest = null; }
        }
    }
    else if(window.XMLHttpRequest){
        httpRequest = new window.XMLHttpRequest();
    }
    return httpRequest;    
}

function checkFunc(){
    if(httpRequest.readyState == 4){
        // 결과값을 가져온다.
        var resultText = httpRequest.responseText;
        if(resultText == 1){ 
            document.location.reload(); // 상세보기 창 새로고침
        }
    }
}
//////////httprequest 끝//////////////////////

   function registFood(food_num)
   {
      var param="food_num="+food_num;
      httpRequest = getXMLHttpRequest();
      httpRequest.onreadystatechange = checkFunc;
      httpRequest.open("POST", "RegistFood.co", true);    
      httpRequest.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded;charset=EUC-KR'); 
      httpRequest.send(param);
   }
   /*
function next(num){
	
 if(confirm("해당 음식을 드셨나요?") == true)
 {
  registFood(num);
 }
 else
 {
 return false;
 }
}
   */
</script>
</head>

<body>
	<form:form modelAttribute="searchfoodObject">
	
	<p>
		<label><spring:message code="searchfoodPhrase"/>:<br>
		<form:input path="foodname"/>
		<form:errors path="foodname"/>
		</label>
	</p>
	<input type="submit" value="<spring:message code="search"/>">
	
	<c:if test="${! empty foodlist}"> 
	<table>
	
		<c:forEach var="food" items="${foodlist }">
	<tr>
		<td> <a href="#" onclick = "next(${food.num })" > ${food.name }; </a>
		</td>
	</c:forEach>
	
	</table>
 	</c:if> 
 	
	</form:form>
</body>
</html>
<!-- 
<a href="void(0);" onclick="alert('해당 음식을 추가하시겠습니까?'); return false;">
		${food.name}</a>  
		$.ajax({
		 type: "POST", 
		 url : "Calendar/registFood",
		 data : food.serialize(),
		 error : function(error) {
			 console.log("error");
		 }
	 	 success : function(data) {
	 		 console.log("success");
	 	 }
	 });
		
		-->