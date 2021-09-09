package Board;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.TypeMismatchException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import spring.AuthInfo;
import spring.MemberNotFoundException;

@Controller
public class BoardDetail {
	private BoardDao boardDao;
	private ReplyDao replyDao;
	private ReplyService replyService;
	
	public void setBoardDao(BoardDao boardDao) {
		this.boardDao=boardDao;
	}
	public void setReplyDao(ReplyDao replyDao) {
		this.replyDao= replyDao;
	}
	public void setReplyService
	(ReplyService replyService) {
		this.replyService= replyService;
	}
	
	//댓글추가 이전
	@GetMapping("/bulletin/{num}")
	public String detail(@PathVariable("num") Long boardNum, Model model,
			HttpSession session,
			@ModelAttribute("replyCommnad") ReplyCommand repCommand) {
		
		Board board= boardDao.selectByNum(boardNum);
		List<Reply> replyList = replyDao.selectByBoardId(boardNum);
		if(board==null) {
			//게시글 num이 존재하지 않는경우
			throw new BoardNotFoundException();
		}
		
		//board형을 커맨드객체로 추가, 위에 boardDao.selectByNum에서 받은 board를 전달
		model.addAttribute("board", board);
		//댓글목록도 추가
		model.addAttribute("replyList", replyList);
		
		//등록자==로그인자면 flag를 true로 해 게시글수정, 삭제버튼 활성화, 아니면 비활성화
		AuthInfo authInfo=(AuthInfo) session.getAttribute("authInfo");
		model.addAttribute("loginMan", authInfo.getName());
		
		if(board.getWriter().equals(authInfo.getName())) {
			model.addAttribute("flag", true);
		}
		else {
			model.addAttribute("flag", false);
		}
		
		//조회수증가
		Long watcher= board.getWatcher();
		board.setWatcher(++watcher);
		boardDao.updateLong(board);
		
		return "bulletin/bulletinDetail";
	}
			
	//댓글 추가후
	@PostMapping("/bulletin/{num}")
	public String detailWithReply(@PathVariable("num") Long boardNum, Model model,
			HttpSession session
			,@ModelAttribute("replyCommnad") ReplyCommand repCommand, Errors errors) {
		
		new ReplyValidator().validate(repCommand, errors);
		if(errors.hasErrors()) {
			return "/bulletin/bulletinDetail";
		}
		
		Board board= boardDao.selectByNum(boardNum);
		if(board==null) {
			//게시글 num이 존재하지 않는경우
			throw new BoardNotFoundException();
		}
		
		//board형을 커맨드객체로 추가, 위에 boardDao.selectByNum에서 받은 board를 전달
		model.addAttribute("board", board);
		
		//등록자==로그인자면 flag를 true로 해 게시글수정, 삭제버튼 활성화, 아니면 비활성화
		AuthInfo authInfo=(AuthInfo) session.getAttribute("authInfo");
		if(board.getWriter().equals(authInfo.getName())) {
			model.addAttribute("flag", true);
		}
		else {
			model.addAttribute("flag", false);
		}
		
		//댓글창 submit시, validator발동
	//	ReplyCommand repCommand= new ReplyCommand();
	//	repCommand.setBoard_id(boardNum);
		repCommand.setWriter(authInfo.getName());

		model.addAttribute("repCommand", repCommand);
	    replyService.regist(repCommand);

	    List<Reply> replyList = replyDao.selectByBoardId(boardNum);
	    //댓글목록도 추가
	  	model.addAttribute("replyList", replyList);
	  	
	  	Long reply= board.getReply();
		board.setWatcher(++reply);
		boardDao.updateLong(board);
	    
		//댓글목록의 수정/삭제 버튼 확인위해, 로그인한사람 정보 전송
		model.addAttribute("loginMan", authInfo.getName());
		
		return "bulletin/bulletinDetail";
	}
	
	//댓글 수정 전.후
	@GetMapping("/bulletin/editReply")
	public String handleEditGet(
			@ModelAttribute("command") ReplyCommand repCommand,
			Model model, @RequestParam("replyNum") long x) {
	
		Reply reuploadReply=replyDao.selectById(x);
		repCommand.setReply_content(reuploadReply.getReply_content());
	//	repCommand.setBoard_id(reuploadReply.getBoard_id());
		repCommand.setWriter(reuploadReply.getWriter());
		repCommand.setParent_id(reuploadReply.getParent_id());	
		
		System.out.println(x);
		//editer을 통해, 해당 글이 수정인지 아닌지 판단
		boolean editer=true;
		model.addAttribute(editer);
			
		return "/bulletin/bulletinDetail";
	}
		
//	@PostMapping("/bulletin/edit")
//	public String handleEdit(@ModelAttribute("command") BoardWritingCommand bodCommand,
//			Errors errors, Model model, @RequestParam("boardNum") long x) {
//		new BoardValidator().validate(bodCommand, errors);
//		if(errors.hasErrors()) {
//			return "/bulletin/write";
//		}

		//보내야할 인자: 새로운 text, 숫자 x(board_id)
//		boardWritingService.reupload(bodCommand, x);
				
		//보여줘야할 건 write와 같으나, 미리 글이 쓰여있음
		//다 쓰고 메인으로 돌아가면, 다시 모든 리스트를 보여줌
//		List<Board> bulletins = boardDao.selectAll();
//		model.addAttribute("bulletins", bulletins);
//		return "/bulletin/main";
//	}
		
		
	//댓글삭제
	//@RequestMapping("/bulletin/delete")
	//	public String deleteBoard(@RequestParam("boardNum") long x, Model model) {
	//		Board deleteBoard=boardDao.selectById(x);
	//		boardWritingService.delete(deleteBoard, x);
			
	//		List<Board> bulletins = boardDao.selectAll();
	//		model.addAttribute("bulletins", bulletins);
	//		return "/bulletin/main";
	//	}
	
	
	/*
	 * //게시글 상세 보기
	 * 
	 * @requestmapping(value = "/board/view") public string boardview(@requestparam
	 * map<string, object> parammap, model model) {
	 * 
	 * model.addattribute("replylist", boardservice.getreplylist(parammap));
	 * model.addattribute("boardview", boardservice.getcontentview(parammap));
	 * 
	 * return "boardview";
	 * 
	 * }
	 * 
	 * @Override
    public Board getContentView(Map<String, Object> paramMap) {
        return sqlSession.selectOne("selectContentView", paramMap);
    }
    
    @Override
    public List<BoardReply> getReplyList(Map<String, Object> paramMap) {
        return sqlSession.selectList("selectBoardReplyList", paramMap);
    }
    
    <select id="selectContentView" resultType="com.spring.myapp.domain.Board" parameterType="java.util.HashMap">
        select
            id,
            subject,
            content,
            writer,
            date_format(register_datetime, '%Y-%m-%d %H:%i:%s') register_datetime
        from
            board
        where
            id = #{id}
    </select>
	 */
	
	//경로변수값 타입이 올바르지 않을때, handleTypeMismatchException() 메서드를 실행
	@ExceptionHandler(TypeMismatchException.class)
	public String handleTypeMismatchException(TypeMismatchException ex) {
		return "bulletin/invalidId";
	}
	//MemberNotFoundException 발생시, handleNotFoundException() 메서드를 실행
	@ExceptionHandler(MemberNotFoundException.class)
	public String handleNotFoundException(MemberNotFoundException ex) {
		return "bulletin/noBoard";
	}
	
	@RequestMapping("/bulletin/recommend.do")
    public String recommend() throws Exception {
        
    //    BoardDao.recommend(member_bno);
		System.out.println("asdf");
        return "forward:/bulletin/list.do"; //페이지값을 그대로 넘겨받기위해서 포워딩을 사용해 컨트롤러로 리턴시킨다.
    }

	
	///////////////////////////////////////
//	private ReplyService replyService;
//	public void setReplyService
//	(BoardWritingService replyervice) {
//		this.replyService= replyService;
//	}
	
//	@RequestMapping(value="/bulletin/reply/save", method=RequestMethod.POST)
//	@ResponseBody //json형식 응답
//	public Object boardReplySave(@RequestParam Map<String, Object> paramMap) {
		 
        //리턴값
 //       Map<String, Object> retVal = new HashMap<String, Object>();
 
        //정보입력
  //      int result = replyService.regReply(paramMap);
 
   //     if(result>0){
    //        retVal.put("code", "OK");
     //       retVal.put("reply_id", paramMap.get("reply_id"));
      //      retVal.put("parent_id", paramMap.get("parent_id"));
       //     retVal.put("message", "등록에 성공 하였습니다.");
       // }else{
        //    retVal.put("code", "FAIL");
         //   retVal.put("message", "등록에 실패 하였습니다.");
        //}
 
        //return retVal;
 
    //}
}