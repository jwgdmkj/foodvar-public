<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="tf" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<!DOCTYPE html>
<html>
<head>
<title>회원 정보</title>
	<script type="text/javascript" src="http://code.jquery.com/jquery-1.11.3.js"></script>
	<script type="text/javascript">
/*  
	// 댓글 수정창
      function cmUpdateOpen(comment_num){
          window.name = "parentForm";
          window.open("ReplyUpdateFormAction.co?num="+comment_num,
                      "updateForm", "width=570, height=350, resizable = no, scrollbars = no");
      }
  	
      var httpRequest = null; 
      // httpRequest 객체 생성
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
      
	// 댓글 삭제창
      function cmDeleteOpen(comment_num){
          var msg = confirm("댓글을 삭제합니다.");    
          if(msg == true){ // 확인을 누를경우
              deleteCmt(comment_num);
          }
          else{
              return false; // 삭제취소
          }
      }

   // 댓글 삭제
   
   function deleteCmt(comment_num)
   {
      var param="comment_num="+comment_num;
      httpRequest = getXMLHttpRequest();
      httpRequest.onreadystatechange = checkFunc;
      httpRequest.open("POST", "ReplyDeleteAction.co", true);    
      httpRequest.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded;charset=EUC-KR'); 
      httpRequest.send(param);
   }
*/
/////////////documentready시작//////////////////////////
$(document).ready(function(){
    var status = false; //수정과 대댓글을 동시에 적용 못하도록
    $("button[name = 'bulletinlist']").click(function(){
       location.href = "./main";
    });
    
  //댓글 저장(reply_save 버튼을 클릭시 발동하는 func)
    $("#reply_save").click(function(){
        
        //널 검사(#textarea의 replycontent가 비어있으면)
        if($("#reply_content").val().trim() == ""){
            alert("내용을 입력하세요.");
            $("#reply_content").focus();
            return false;
        }
        
        var reply_content = $("#reply_content").val().replace("\n", "<br>"); //개행처리
        
        //값 셋팅
        var objParams = {
                board_id        : $("#board_id").val(),
                parent_id        : "0",    
                depth            : "0",
                reply_writer    : $("#reply_writer").val(),
                reply_content    : reply_content
        };
        
        var reply_id;
        
        //ajax 호출
        $.ajax({
            url            :    "/board/reply/save",
            dataType    :    "json",
            contentType :    "application/x-www-form-urlencoded; charset=UTF-8",
            type         :    "post",
            async        :     false, //동기: false, 비동기: ture
            data        :    objParams,
            success     :    function(retVal){

                if(retVal.code != "OK") {
                    alert(retVal.message);
                    return false;
                }else{
                    reply_id = retVal.reply_id;
                }
                
            },
            error        :    function(request, status, error){
                console.log("AJAX_ERROR");
            }
        });
        
        var reply_area = $("#reply_area");
        
        var reply = 
            '<tr reply_type="main">'+
            '    <td width="820px">'+
            reply_content+
            '    </td>'+
            '    <td width="100px">'+
            $("#reply_writer").val()+
            '    </td>'+
            '    <td width="100px">'+
            '        <input type="password" id="reply_password_'+reply_id+'" style="width:100px;" maxlength="10" placeholder="패스워드"/>'+
            '    </td>'+
            '    <td align="center">'+
            '       <button name="reply_reply" reply_id = "'+reply_id+'">댓글</button>'+
            '       <button name="reply_modify" r_type = "main" parent_id = "0" reply_id = "'+reply_id+'">수정</button>      '+
            '       <button name="reply_del" r_type = "main" reply_id = "'+reply_id+'">삭제</button>      '+
            '    </td>'+
            '</tr>';
            
         if($('#reply_area').contents().size()==0){
             $('#reply_area').append(reply);
         }else{
             $('#reply_area tr:last').after(reply);
         }

        //댓글 초기화
        $("#reply_writer").val("");
        $("#reply_content").val("");
        
    });
  
  /////////////////////////////////////////
  //댓글 삭제
  $(document).on("click","button[name='reply_del']", function(){
        var check = false;
        var reply_id = $(this).attr("reply_id");
        var r_type = $(this).attr("r_type");
        //패스워드와 아이디를 넘겨 삭제를 한다.
        //값 셋팅
        var objParams = {
                reply_id        : reply_id,
                r_type            : r_type
        };

        //ajax 호출
        $.ajax({
            url            : "ReplyDeleteAction.co",
            dataType    :    "json",
            contentType :    "application/x-www-form-urlencoded; charset=UTF-8",
            type         :    "post",
            async        :     false, //동기: false, 비동기: ture
            data        :    objParams,
            success     :    function(retVal){

                if(retVal.code != "OK") {
                    alert(retVal.message);
                }else{
                    check = true;                                
                }
                
            },
            error        :    function(request, status, error){
            	alert("asdf");
                console.log("AJAX_ERROR");
            }
        });
        
        if(check == true){
            if(r_type == "main"){//depth가 0이면 하위 댓글 다 지움
                //삭제하면서 하위 댓글도 삭제
                var prevTr = $(this).parent().parent().next(); //댓글의 다음
                while(prevTr.attr("reply_type")=="sub"){//댓글의 다음이 sub면 계속 넘어감
                    prevTr.remove();
                    prevTr = $(this).parent().parent().next();
                }                        
                $(this).parent().parent().remove();    
            }else{ //아니면 자기만 지움
                $(this).parent().parent().remove();    
            }
        }
        else {
        	alert("is it sub?");
        }
    });
  /////////////////////////////////////////
  
  ////////////////////////////////////////
  //댓글 수정 입력
  $(document).on("click","button[name='reply_modify']", function(){
      
      var check = false;
      var reply_id = $(this).attr("reply_id");
      var parent_id = $(this).attr("parent_id");
      var r_type = $(this).attr("r_type");
       
      //값 셋팅
      var objParams = {
              reply_id        : reply_id
      };
       
      //ajax 호출
      $.ajax({
          url         :   "ReplyUpdateAction.co",
          dataType    :   "json",
          contentType :   "application/x-www-form-urlencoded; charset=UTF-8",
          type        :   "post",
          async       :   false, //동기: false, 비동기: ture
          data        :   objParams,
          success     :   function(retVal){

              if(retVal.code != "OK") {
                  check = false;//패스워드가 맞으면 체크값을 true로 변경
                  alert(retVal.message);
              }else{
                  check = true;
              }
               
          },
          error       :   function(request, status, error){
              console.log("AJAX_ERROR");
          }
      });
      
      
      
      if(status){
          alert("수정과 대댓글은 동시에 불가합니다.");
          return false;
      }
      
      
      if(check){
          status = true;
          //자기 위에 댓글 수정창 입력하고 기존값을 채우고 자기 자신 삭제
          var txt_reply_content = $(this).parent().prev().prev().prev().html().trim(); //댓글내용 가져오기
          if(r_type=="sub"){
              txt_reply_content = txt_reply_content.replace("→ ","");//대댓글의 뎁스표시(화살표) 없애기
          }
          
          var txt_reply_writer = $(this).parent().prev().prev().html().trim(); //댓글작성자 가져오기
          
          //입력받는 창 등록
          var replyEditor = 
             '<tr id="reply_add" class="reply_modify">'+
             '   <td width="820px">'+
             '       <textarea name="reply_modify_content_'+reply_id+'" id="reply_modify_content_'+reply_id+'" rows="3" cols="50">'+txt_reply_content+'</textarea>'+ //기존 내용 넣기
             '   </td>'+
             '   <td width="100px">'+
             '       <input type="text" name="reply_modify_writer_'+reply_id+'" id="reply_modify_writer_'+reply_id+'" style="width:100%;" maxlength="10" placeholder="작성자" value="'+txt_reply_writer+'"/>'+ //기존 작성자 넣기
             '   </td>'+
             '   <td align="center">'+
             '       <button name="reply_modify_save" r_type = "'+r_type+'" parent_id="'+parent_id+'" reply_id="'+reply_id+'">등록</button>'+
             '       <button name="reply_modify_cancel" r_type = "'+r_type+'" r_content = "'+txt_reply_content+'" r_writer = "'+txt_reply_writer+'" parent_id="'+parent_id+'"  reply_id="'+reply_id+'">취소</button>'+
             '   </td>'+
             '</tr>';
          var prevTr = $(this).parent().parent();
             //자기 위에 붙이기
          prevTr.after(replyEditor);
          
          //자기 자신 삭제
          $(this).parent().parent().remove(); 
      }
    });
  /////////////////////////////////
});/*
          
          
          //댓글 수정 취소
          $(document).on("click","button[name='reply_modify_cancel']", function(){
              //원래 데이터를 가져온다.
              var r_type = $(this).attr("r_type");
              var r_content = $(this).attr("r_content");
              var r_writer = $(this).attr("r_writer");
              var reply_id = $(this).attr("reply_id");
              var parent_id = $(this).attr("parent_id");
              
              var reply;
              //자기 위에 기존 댓글 적고 
              if(r_type=="main"){
                  reply = 
                      '<tr reply_type="main">'+
                      '   <td width="820px">'+
                      r_content+
                      '   </td>'+
                      '   <td width="100px">'+
                      r_writer+
                      '   </td>'+
                      '   <td width="100px">'+
                      '       <input type="password" id="reply_password_'+reply_id+'" style="width:100px;" maxlength="10" placeholder="패스워드"/>'+
                      '   </td>'+
                      '   <td align="center">'+
                      '       <button name="reply_reply" reply_id = "'+reply_id+'">댓글</button>'+
                      '       <button name="reply_modify" r_type = "main" parent_id="0" reply_id = "'+reply_id+'">수정</button>      '+
                      '       <button name="reply_del" reply_id = "'+reply_id+'">삭제</button>      '+
                      '   </td>'+
                      '</tr>';
              }else{
                  reply = 
                      '<tr reply_type="sub">'+
                      '   <td width="820px"> → '+
                      r_content+
                      '   </td>'+
                      '   <td width="100px">'+
                      r_writer+
                      '   </td>'+
                      '   <td width="100px">'+
                      '       <input type="password" id="reply_password_'+reply_id+'" style="width:100px;" maxlength="10" placeholder="패스워드"/>'+
                      '   </td>'+
                      '   <td align="center">'+
                      '       <button name="reply_modify" r_type = "sub" parent_id="'+parent_id+'" reply_id = "'+reply_id+'">수정</button>'+
                      '       <button name="reply_del" reply_id = "'+reply_id+'">삭제</button>'+
                      '   </td>'+
                      '</tr>';
              }
              
              var prevTr = $(this).parent().parent();
                 //자기 위에 붙이기
              prevTr.after(reply);
                 
                //자기 자신 삭제
              $(this).parent().parent().remove(); 
                
              status = false;
              
          });
          
            //댓글 수정 저장
          $(document).on("click","button[name='reply_modify_save']", function(){
              
              var reply_id = $(this).attr("reply_id");
              
              //널 체크
              if($("#reply_modify_writer_"+reply_id).val().trim() == ""){
                  alert("이름을 입력하세요.");
                  $("#reply_modify_writer_"+reply_id).focus();
                  return false;
              }
               
              if($("#reply_modify_password_"+reply_id).val().trim() == ""){
                  alert("패스워드를 입력하세요.");
                  $("#reply_modify_password_"+reply_id).focus();
                  return false;
              }
               
              if($("#reply_modify_content_"+reply_id).val().trim() == ""){
                  alert("내용을 입력하세요.");
                  $("#reply_modify_content_"+reply_id).focus();
                  return false;
              }
              //DB에 업데이트 하고
              //ajax 호출 (여기에 댓글을 저장하는 로직을 개발)
              var reply_content = $("#reply_modify_content_"+reply_id).val().replace("\n", "<br>"); //개행처리
              
              var r_type = $(this).attr("r_type");
              
              var parent_id;
              var depth;
              if(r_type=="main"){
                  parent_id = "0";
                  depth = "0";
              }else{
                  parent_id = $(this).attr("parent_id");
                  depth = "1";
              }
              
              //값 셋팅
              var objParams = {
                      board_id        : $("#board_id").val(),
                      reply_id        : reply_id,
                      parent_id       : parent_id, 
                      depth           : depth,
                      reply_writer    : $("#reply_modify_writer_"+reply_id).val(),
                      reply_password  : $("#reply_modify_password_"+reply_id).val(),
                      reply_content   : 

              };

              $.ajax({
                  url         :   "/board/reply/update",
                  dataType    :   "json",
                  contentType :   "application/x-www-form-urlencoded; charset=UTF-8",
                  type        :   "post",
                  async       :   false, //동기: false, 비동기: ture
                  data        :   objParams,
                  success     :   function(retVal){

                      if(retVal.code != "OK") {
                          alert(retVal.message);
                          return false;
                      }else{
                          reply_id = retVal.reply_id;
                          parent_id = retVal.parent_id;
                      }
                       
                  },
                  error       :   function(request, status, error){
                      console.log("AJAX_ERROR");
                  }
              });
              
              //수정된댓글 내용을 적고
              if(r_type=="main"){
                  reply = 
                      '<tr reply_type="main">'+
                      '   <td width="820px">'+
                      $("#reply_modify_content_"+reply_id).val()+
                      '   </td>'+
                      '   <td width="100px">'+
                      $("#reply_modify_writer_"+reply_id).val()+
                      '   </td>'+
                      '   <td width="100px">'+
                      '       <input type="password" id="reply_password_'+reply_id+'" style="width:100px;" maxlength="10" placeholder="패스워드"/>'+
                      '   </td>'+
                      '   <td align="center">'+
                      '       <button name="reply_reply" reply_id = "'+reply_id+'">댓글</button>'+
                      '       <button name="reply_modify" r_type = "main" parent_id = "0" reply_id = "'+reply_id+'">수정</button>      '+
                      '       <button name="reply_del" r_type = "main" reply_id = "'+reply_id+'">삭제</button>      '+
                      '   </td>'+
                      '</tr>';
              }else{
                  reply = 
                      '<tr reply_type="sub">'+
                      '   <td width="820px"> → '+
                      $("#reply_modify_content_"+reply_id).val()+
                      '   </td>'+
                      '   <td width="100px">'+
                      $("#reply_modify_writer_"+reply_id).val()+
                      '   </td>'+
                      '   <td width="100px">'+
                      '       <input type="password" id="reply_password_'+reply_id+'" style="width:100px;" maxlength="10" placeholder="패스워드"/>'+
                      '   </td>'+
                      '   <td align="center">'+
                      '       <button name="reply_modify" r_type = "sub" parent_id = "'+parent_id+'" reply_id = "'+reply_id+'">수정</button>'+
                      '       <button name="reply_del" r_type = "sub" reply_id = "'+reply_id+'">삭제</button>'+
                      '   </td>'+
                      '</tr>';
              }
              
              var prevTr = $(this).parent().parent();
              //자기 위에 붙이기
              prevTr.after(reply);
                 
              //자기 자신 삭제
              $(this).parent().parent().remove(); 
                
              status = false;
              
          });
            
          //대댓글 입력창
          $(document).on("click","button[name='reply_reply']",function(){ //동적 이벤트
              
              if(status){
                  alert("수정과 대댓글은 동시에 불가합니다.");
                  return false;
              }
              
              status = true;
              
              $("#reply_add").remove();
              
              var reply_id = $(this).attr("reply_id");
              var last_check = false;//마지막 tr 체크
              
              //입력받는 창 등록
               var replyEditor = 
                  '<tr id="reply_add" class="reply_reply">'+
                  '    <td width="820px">'+
                  '        <textarea name="reply_reply_content" rows="3" cols="50"></textarea>'+
                  '    </td>'+
                  '    <td width="100px">'+
                  '        <input type="text" name="reply_reply_writer" style="width:100%;" maxlength="10" placeholder="작성자"/>'+
                  '    </td>'+
                  '    <td width="100px">'+
                  '        <input type="password" name="reply_reply_password" style="width:100%;" maxlength="10" placeholder="패스워드"/>'+
                  '    </td>'+
                  '    <td align="center">'+
                  '        <button name="reply_reply_save" parent_id="'+reply_id+'">등록</button>'+
                  '        <button name="reply_reply_cancel">취소</button>'+
                  '    </td>'+
                  '</tr>';
                  
              var prevTr = $(this).parent().parent().next();
              
              //부모의 부모 다음이 sub이면 마지막 sub 뒤에 붙인다.
              //마지막 리플 처리
              if(prevTr.attr("reply_type") == undefined){
                  prevTr = $(this).parent().parent();
              }else{
                  while(prevTr.attr("reply_type")=="sub"){//댓글의 다음이 sub면 계속 넘어감
                      prevTr = prevTr.next();
                  }
                  
                  if(prevTr.attr("reply_type") == undefined){//next뒤에 tr이 없다면 마지막이라는 표시를 해주자
                      last_check = true;
                  }else{
                      prevTr = prevTr.prev();
                  }
                  
              }
              
              if(last_check){//마지막이라면 제일 마지막 tr 뒤에 댓글 입력을 붙인다.
                  $('#reply_area tr:last').after(replyEditor);    
              }else{
                  prevTr.after(replyEditor);
              }
              
          });
          
          //대댓글 등록
          $(document).on("click","button[name='reply_reply_save']",function(){
                                  
              var reply_reply_writer = $("input[name='reply_reply_writer']");
              var reply_reply_password = $("input[name='reply_reply_password']");
              var reply_reply_content = $("textarea[name='reply_reply_content']");
              var reply_reply_content_val = reply_reply_content.val().replace("\n", "<br>"); //개행처리
              
              //널 검사
              if(reply_reply_writer.val().trim() == ""){
                  alert("이름을 입력하세요.");
                  reply_reply_writer.focus();
                  return false;
              }
              
              if(reply_reply_password.val().trim() == ""){
                  alert("패스워드를 입력하세요.");
                  reply_reply_password.focus();
                  return false;
              }
              
              if(reply_reply_content.val().trim() == ""){
                  alert("내용을 입력하세요.");
                  reply_reply_content.focus();
                  return false;
              }
              
              //값 셋팅
              var objParams = {
                      board_id        : $("#board_id").val(),
                      parent_id        : $(this).attr("parent_id"),    
                      depth            : "1",
                      reply_writer    : reply_reply_writer.val(),
                      reply_password    : reply_reply_password.val(),
                      reply_content    : reply_reply_content_val
              };
              
              var reply_id;
              var parent_id;
              
              //ajax 호출
              $.ajax({
                  url            :    "/board/reply/save",
                  dataType    :    "json",
                  contentType :    "application/x-www-form-urlencoded; charset=UTF-8",
                  type         :    "post",
                  async        :     false, //동기: false, 비동기: ture
                  data        :    objParams,
                  success     :    function(retVal){

                      if(retVal.code != "OK") {
                          alert(retVal.message);
                      }else{
                          reply_id = retVal.reply_id;
                          parent_id = retVal.parent_id;
                      }
                      
                  },
                  error        :    function(request, status, error){
                      console.log("AJAX_ERROR");
                  }
              });
              
              var reply = 
                  '<tr reply_type="sub">'+
                  '    <td width="820px"> → '+
                  reply_reply_content_val+
                  '    </td>'+
                  '    <td width="100px">'+
                  reply_reply_writer.val()+
                  '    </td>'+
                  '    <td width="100px">'+
                  '        <input type="password" id="reply_password_'+reply_id+'" style="width:100px;" maxlength="10" placeholder="패스워드"/>'+
                  '    </td>'+
                  '    <td align="center">'+
                  '       <button name="reply_modify" r_type = "sub" parent_id = "'+parent_id+'" reply_id = "'+reply_id+'">수정</button>'+
                  '       <button name="reply_del" r_type = "sub" reply_id = "'+reply_id+'">삭제</button>'+
                  '    </td>'+
                  '</tr>';
                  
              var prevTr = $(this).parent().parent().prev();
              
              prevTr.after(reply);
                                  
              $("#reply_add").remove();
              
              status = false;
              
          });
          
          //대댓글 입력창 취소
          $(document).on("click","button[name='reply_reply_cancel']",function(){
              $("#reply_add").remove();
              
              status = false;
          });
          
          //글수정
          $("#modify").click(function(){
              
              var password = $("input[name='password']");
              
              if(password.val().trim() == ""){
                  alert("패스워드를 입력하세요.");
                  password.focus();
                  return false;
              }
                                  
              //ajax로 패스워드 검수 후 수정 페이지로 포워딩
              //값 셋팅
              var objParams = {
                      id         : $("#board_id").val(),    
                      password : $("#password").val()
              };
                                  
              //ajax 호출
              $.ajax({
                  url            :    "/board/check",
                  dataType    :    "json",
                  contentType :    "application/x-www-form-urlencoded; charset=UTF-8",
                  type         :    "post",
                  async        :     false, //동기: false, 비동기: ture
                  data        :    objParams,
                  success     :    function(retVal){

                      if(retVal.code != "OK") {
                          alert(retVal.message);
                      }else{
                          location.href = "/board/edit?id="+$("#board_id").val();
                      }
                      
                  },
                  error        :    function(request, status, error){
                      console.log("AJAX_ERROR");
                  }
              });
              
          });
          
          //글 삭제
          $("#delete").click(function(){
              
              var password = $("input[name='password']");
              
              if(password.val().trim() == ""){
                  alert("패스워드를 입력하세요.");
                  password.focus();
                  return false;
              }
              
              //ajax로 패스워드 검수 후 수정 페이지로 포워딩
              //값 셋팅
              var objParams = {
                      id         : $("#board_id").val(),    
                      password : $("#password").val()
              };
                                  
              //ajax 호출
              $.ajax({
                  url            :    "/board/del",
                  dataType    :    "json",
                  contentType :    "application/x-www-form-urlencoded; charset=UTF-8",
                  type         :    "post",
                  async        :     false, //동기: false, 비동기: ture
                  data        :    objParams,
                  success     :    function(retVal){

                      if(retVal.code != "OK") {
                          alert(retVal.message);
                      }else{
                          alert("삭제 되었습니다.");
                          location.href = "/board/list";
                      }
                      
                  },
                  error        :    function(request, status, error){
                      console.log("AJAX_ERROR");
                  }
              });
              
          });
          
});
*/
</script>

</head>
<body>

<!--<form method="post">
	<p>
		<label><spring:message code="repCommand.reply_content"/>:<br>
		<textarea name="reply_content" cols="50" rows="3">
		</textarea>

	
		</label>
	</p>
	</form> -->
	
	<p>게시글 번호:${board.num}</p>
	<p>게시글 작성자:${board.writer}</p>
	<p>게시글 조회수:${board.watcher}</p>
	<p>게시글 제목:${board.title }</p>
	<p>게시글 등록시간:<tf:formatDateTime value="${board.registerDateTime }"
	pattern="yyyy-MM-dd HH:mm"/></p>
	<p>게시글 내용:${board.content }</p>
	<p>게시글 추천수:${board.recommend }</p>
	<p>
	</p>
	
	<!-- 글쓴이==로그인한사람일 때 -->
	<p>
	<c:if test="${! flag==false }">
		<form action="delete">
		<input type="hidden" value=${board.num } name="boardNum">
		<input type="submit" value="글삭제">
		</form>
		
		<!-- 글 수정버튼 -->
		<form action="edit">
		<input type="hidden" value=${board.num } name="boardNum">
		<input type="submit" value="글수정">
		</form>
	</c:if>
	</p>

<form method="post">
 <p>
		<spring:message code="repCommand.reply_content"/>:<br>
		<textarea name="reply_content" cols="50" rows="3"></textarea>
		
		<p>
		<input type="submit" value="<spring:message code="go.bulletinmain"/>">
	</p>

</form>

	<p>
	<button type = "button" id = "btnRecommend" onClick='recommend()'>추천하기</button>
	</p>
	
	
 <!-- 댓글 쓰는 창 -->
<form method = "post">
<table border="1" width="1000px" bordercolor="#46AA46">
   <tr>
    <td width="500px">
      <p>
		<input type="submit" value="<spring:message code="go.bulletinmain"/>">
	</p>
    </td>
   </tr>
   <tr>
    <td>
    <textarea id="reply_content" name="reply_content" rows="4" cols="50"
     placeholder="댓글을 입력하세요."></textarea>
    </td>
   </tr>
</table>
</form>
	
 <!-- 댓글목록 -->
 <c:if test="${! empty replyList }">
 <table border="1" width="1000px" id="reply_area">
        <c:forEach var="replyList" items="${replyList }" varStatus = "status">
        	<tr reply_type="<c:if test="${replyList.depth == '0'}">main</c:if>
        		<c:if test="${replyList.depth == '1'}">sub</c:if>"> <!-- 댓글의 depth 표시 -->
        		
        		<td width="420px">
                 <c:if test="${replyList.depth == '1'}"> → </c:if>${replyList.reply_content}
                </td>
                 <td width="100px" align = "center"> ${replyList.writer}</td>
                 
                 <td align="center" width="200px"> 
                  <c:if test="${replyList.depth != '1'}">
                    <button name="reply_reply" 
                     parent_id = "${replyList.reply_id}" 
                     reply_id = "${replyList.reply_id}">댓글</button><!-- 첫댓글에만 댓글이 추가대댓글불가 -->
                  </c:if>
                  
                  <!-- 수정 및 삭제 -->
                  <c:if test="${replyList.writer eq authInfo.name}" > 
                  
                    <button name="reply_modify" parent_id = "${replyList.parent_id}" r_type = "<c:if test="${replyList.depth == '0'}">main</c:if><c:if test="${replyList.depth == '1'}">sub</c:if>" reply_id = "${replyList.reply_id}">수정</button>     
                    <button name="reply_del" r_type = "<c:if test="${replyList.depth == '0'}">main</c:if><c:if test="${replyList.depth == '1'}">sub</c:if>" reply_id = "${replyList.reply_id}">삭제</button>
                  </c:if>
                  
                    <td><tf:formatDateTime value="${replyList.registerDateTime}"
			            pattern="yyyy-MM-dd"/></td>
                 </td>
            </tr>     
    <!--     
	<tr>
		<td>댓글작성자: ${replyList.writer}</td>
		<td>댓글내용: ${replyList.reply_content }</td>
		<td><tf:formatDateTime value="${replyList.registerDateTime}"
			pattern="yyyy-MM-dd"/></td>
			
	<!-- 댓글 수정/삭제 버튼 -->
	<!-- 각각 클릭시 cmUpdateOpen(Delete)를 호출, 글 번호를 전달 -->
	<!--<c:if test="${replyList.writer eq loginMan}" > 
		<td> <a href="#" onclick="cmUpdateOpen(${replyList.reply_id })">[댓글수정]</a> </td> 
		<td> <a href="#" onclick="cmDeleteOpen(${replyList.reply_id })">[댓글삭제]</a> </td>
	</c:if>
		
	</tr>
	 -->  
	</c:forEach>
</table>
</c:if>

<!-- 게시판 홈으로 -->
<table width="800px">
 <tr>
  <td align="left">
  <button id="bulletinlist" name="bulletinlist">게시판</button>
  </td>
 </tr>
</table>
 <!-- 게시판 홈으로 끝 -->   
  
	<!-- 댓글 입력 -->
<!--  <table border="1" width="1200px" bordercolor="#46AA46">
                   <tr>
                       <td width="500px">
                        댓글: <input type="text" id="reply_writer" 
                        name="reply_writer" style="width:170px;" 
                        maxlength="10" placeholder="작성자"/>
                      
                      <!-- reply_save 버튼을 클릭시 위의 reply_save func이 발동 
                        <button id="reply_save" name="reply_save">댓글 등록</button>
                    </td>
                   </tr>
                   <tr>
                       <td>
                           <textarea id="reply_content" name="reply_content"
                            rows="4" cols="50" placeholder="댓글을 입력하세요.">
                            </textarea>
                       </td>
                   </tr>
               </table>-->
	
</body>
</html>