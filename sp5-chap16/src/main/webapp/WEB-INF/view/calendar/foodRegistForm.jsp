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
function next(foodnum, yearNum, monthNum, dateNum, mealname, kcal){
	if(confirm("해당 음식을 드셨나요?") == true) {
		var check = false;
	/*	var mealarray = document.getElementsByName('chk_info');
		for(var i=0; i<mealarray.length; i++) {
			if(mealarray[0].checked == true) meal = "Break";
			else if(mealarray[1].checked == true) meal = "Lunch";
			else if(mealarray[2].checked == true) meal = "Dinner";
			else if(mealarray[3].checked == true) meal = "Snank";
			else if(mealarray[4].checked == true) meal = "Midnight";
		} */
		meal = $('input[name="chk_info"]:checked').val();
		
        //값 셋팅
        var objParams = {
                foodnum : foodnum,
                meal : meal,
                year: yearNum, 
                month: monthNum,
                date : dateNum, 
                mealname : mealname,
                kcal : kcal
        };
        //ajax 호출
        $.ajax({
            url            : "foodRegistAction.co",
            dataType    :    "json",
            contentType :    "application/x-www-form-urlencoded; charset=UTF-8",
            type         :    "post",
            async        :     false, //동기: false, 비동기: ture
            data        :    objParams,
            success     :    function(retVal){

                if(retVal.code != "OK") {
                    alert(retVal.message);
                }else{
                    check = true; alert("등록되었습니다!");                              
                }
                
            },
            error        :    function(request, status, error){
            	alert("끼니를 선택해 주세요.");
                console.log("AJAX_ERROR");
            }
        });
	}
	else {
		return false;
	}
}

</script>

<body>

<table border="1">
    <tr align = "center"> <td colspan = "2"><b>오늘 먹은 것은</b></td></tr>
    
    <tr align = "center"><td colspan = "2" ><b>아침 - 칼로리</b></td></tr>
    <c:forEach var="scheduleList_1" items="${scheduleList_1 }">
	<tr>
		<c:if test= "${year eq scheduleList_1.year && month eq scheduleList_1.month
		&& date eq scheduleList_1.date }" > 
		<td> ${scheduleList_1.mealname } </td> <td> ${scheduleList_1.kcal } </td>
		</c:if>
	</tr>
	</c:forEach>

    <tr align = "center"><td colspan = "2" ><b>점심 - 칼로리</b></td></tr>
    <c:forEach var="scheduleList_2" items="${scheduleList_2 }">
	<tr>
		<c:if test= "${year eq scheduleList_2.year && month eq scheduleList_2.month
		&& date eq scheduleList_2.date }" > 
		<td> ${scheduleList_2.mealname } </td>  <td> ${scheduleList_2.kcal } </td>
		</c:if>
	</c:forEach>
	
    <tr align = "center"><td colspan = "2" ><b>저녁 - 칼로리</b></td></tr>
    <c:forEach var="scheduleList_3" items="${scheduleList_3 }">
	<tr>
		<c:if test= "${year eq scheduleList_3.year && month eq scheduleList_3.month
		&& date eq scheduleList_3.date }" > 
		<td> ${scheduleList_3.mealname } </td>  <td> ${scheduleList_3.kcal } </td>
		</c:if>
	</tr>
	</c:forEach>
	
	<tr align = "center"><td colspan = "2" ><b>간식 - 칼로리</b></td></tr>
    <c:forEach var="scheduleList_4" items="${scheduleList_4 }">
	<tr>
		<c:if test= "${year eq scheduleList_4.year && month eq scheduleList_4.month
		&& date eq scheduleList_4.date }" > 
		<td> ${scheduleList_4.mealname } </td>  <td> ${scheduleList_4.kcal } </td>
		</c:if>
	</c:forEach>
	
	<tr align = "center"><td colspan = "2" ><b>야식 - 칼로리</b></td></tr>
    <c:forEach var="scheduleList_5" items="${scheduleList_5 }">
	<tr>
		<c:if test= "${year eq scheduleList_5.year && month eq scheduleList_5.month
		&& date eq scheduleList_5.date }" > 
		<td> ${scheduleList_5.mealname } </td>  <td> ${scheduleList_5.kcal } </td>
		</c:if>
	</c:forEach>
</table>

<c:if test= "${authInfo.id eq memid}"> 
<form:form modelAttribute="searchfoodObject">
	
	<p>
		<label><spring:message code="searchfoodPhrase"/>:<br>
		<form:input path="foodname"/>
		<form:errors path="foodname"/>
		</label>
	</p>
	<input type="hidden" path = "year" value=${yearNum }>
	<input type="hidden" path = "month" value=${monthNum }>
	<input type="hidden" path = "date" value=${dateNum }>
	<input type="submit" value="<spring:message code="search"/>">
	
	<input type="radio" name="chk_info" value="breakfast">아침
		<input type="radio" name="chk_info" value="lunch">점심
		<input type="radio" name="chk_info" value="dinner">저녁
		<input type="radio" name="chk_info" value="snack">간식
		<input type="radio" name="chk_info" value="midnight">야식
	<c:if test="${! empty foodlist}"> 
	<table>
	
		<c:forEach var="food" items="${foodlist }">
	<tr>
		<td> <a href="#" onclick = "next(${food.num }, ${year }, ${month }, ${date }, ${food.name }, ${food.kcal })" 
		> ${food.name }</a>
		</td>
	</c:forEach>
	
	</table>
 	</c:if> 
 	
	</form:form>

</c:if>

	
</body>
</html>