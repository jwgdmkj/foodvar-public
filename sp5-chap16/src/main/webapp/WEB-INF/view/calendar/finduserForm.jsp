<%@ page contentType="text/html; charset=utf-8"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tf" tagdir="/WEB-INF/tags" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="EUC-KR">
<title>음식 등록 칸</title>
</head>

<script type="text/javascript" src="http://code.jquery.com/jquery-1.11.3.js"></script>
<script type="text/javascript">
function finduser(foodid, yearNum, monthNum, dateNum){
	
	var foodnum = foodid;
	
	var objParams = {
            foodid : foodid,
            year: yearNum, 
            month: monthNum,
            date : dateNum
      };
        //ajax 호출
      $.ajax({
            url            : "findsamefooduser.co",
            dataType    :    "json",
            contentType :    "application/x-www-form-urlencoded; charset=UTF-8",
            type         :    "post",
            async        :     false, //동기: false, 비동기: ture
            data        :    objParams,
            success     :    function(retVal){

            if(retVal.code != "OK") {
                alert(retVal.message);
            }else{
            	for(var i=0; i<retVal.sameuserList.length; i++) {
            		document.write('<a href="#"> retVal.sameuserList[i].mid + " "</a>');
            	}		               
            }        
        },
        error        :    function(request, status, error){
         	alert("오류가 발생했습니다.");
            console.log("AJAX_ERROR");
        }
      }); 
}
</script>	

<body>
정확한 음식명을 검색해주세요. 음식명은 홈-음식검색기에서 하실 수 있습니다.

<c:if test= "${authInfo.id eq memid}"> 
<form:form modelAttribute="searchfoodObject">
	<p>
		<form:input path="foodname"/>
		<form:errors path="foodname"/>
		</label>
	</p>
	
		<input type="hidden" path = "year" value=${yearNum }>
		<input type="hidden" path = "month" value=${monthNum }>
		<input type="hidden" path = "date" value=${dateNum }>
		<input type="submit" value="<spring:message code="search"/>">
		
</form:form>

	<c:if test = "${! empty sameuserList }">
	<table>
	<c:forEach var="sameuserList" items="${sameuserList }">
	<tr>
		<td>
			<a href="#" onclick = "newCalendar(${sameuserList.mid })" > ${sameuserList.mid }</a>
			<script type="text/javascript">
				function newCalendar(num) {
					opener.document.location.href="http://localhost:8080/sp5-chap16/calendar/calendar/"+num
					self.close();					
				}

			</script>
					</td>
					<!-- <a href="<c:url value="/calendar/calendar/${sameuserList.mid }"/>"> </a>-->
	</c:forEach>
	</table>
 	</c:if> 
</c:if>

<c:if test= "${authInfo.id ne memid}"> 
	음식 검색은 자신의 캘린더에서만 가능합니다.
</c:if>


<br><b>오늘 회원님께서 드신 음식 목록입니다.</b>
	<c:if test="${! empty samefoodList}"> 
	<table>
		<c:forEach var="samefood" items="${samefoodList }">
	<tr>
		<td> ${samefood.mealname } 
		</td>
	</c:forEach>
	</table>
	</c:if>
</body>
</html>