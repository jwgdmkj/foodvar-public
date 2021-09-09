<%@ page contentType="text/html; charset=euc-kr"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%> 
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=EUC-KR">
<title> 댓글 수정 </title>
    
    <style type="text/css">
        #wrap {
            width: 550px;
            margin: 0 auto 0 auto;
            text-align :center;
        }
    
        #commentUpdateForm{
            text-align :center;
        }
    </style>
    
    <script type="text/javascript">
    
        var httpRequest = null;
        
        // httpRequest 객체 생성
        // HTTP 클라이언트 인터페이스 구현, 폼 데이터 보내거나 서버로부터 데이터 받아옴
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
    
    	//등록시
        function checkValue()
        {
            var form = document.forms[0];
            // 전송할 값을 변수에 담는다.    
            // document: 해당 html파일 전체. 그 form중 첫번째
            // 첫번째 form의 변수 comment_content의 value
            var comment_num = "${comment.reply_id}";
            var comment_content = form.comment_content.value
            if(!comment_content)
            {
                alert("내용을 입력해주세요");
                return false;
            }
            else{
            	//param: commentnum과 commentcontent를 각각 key와 결합시킨 것
                var param="comment_num="+comment_num+"&comment_content="
                +encodeURIComponent(comment_content);
                
                httpRequest = getXMLHttpRequest();
                httpRequest.onreadystatechange = checkFunc;
                httpRequest.open("POST", "ReplyUpdateAction.co", true);    
                httpRequest.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded;charset=EUC-KR'); 
                httpRequest.send(param);
            }
        }
        
        function checkFunc(){
        	//complete, request가 끝나고 response가 준비된 상태(4)
            if(httpRequest.readyState == 4){
                // 결과값을 가져온다.
                var resultText = httpRequest.responseText;
                if(resultText == 1){
                    if (opener != null) {
                        // 부모창 새로고침
                        window.opener.document.location.reload(); 
                        opener.updateForm = null;
                        self.close();
                    }
                }
            }
        }
        
    </script>

</head>
<body>
<div id="wrap">
    <br>
    <b><font size="5" color="gray">댓글수정</font></b>
    <hr size="1" width="550">
    <br>
 
    <div id="commentUpdateForm">
        <form name="updateInfo" target="parentForm">        
            <textarea rows="7" cols="70" name="comment_content">${comment.reply_content }
            </textarea>
            <br><br>
		
            <input type="button" value="등록" onclick="checkValue()">
            <input type="button" value="창닫기" onclick="window.close()">
        </form>
    </div>
</div>    
</body>
</html>