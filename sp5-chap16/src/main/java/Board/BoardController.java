package Board;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import spring.AuthInfo;

@Controller
public class BoardController {
	
	private BoardWritingService boardWritingService;
	public void setBoardWritingService
	(BoardWritingService boardWritingService) {
		this.boardWritingService= boardWritingService;
	}
	private BoardDao boardDao;
	public void setBoardDao(BoardDao boardDao) {
		this.boardDao=boardDao;
	}
	
	
	//게시판 메인
	@RequestMapping("/bulletin/main")
	public String handleMain(Model model) {
		List<Board> bulletins = boardDao.selectAll();
		model.addAttribute("bulletins", bulletins);
		return "/bulletin/main";
	}

	//글쓰기 포스트
	@GetMapping("/bulletin/write")
	public String handleWritePost(
			@ModelAttribute("command") BoardWritingCommand bodCommand,
			Model model) {
		
		//완전히 새로운 글을 쓰는 거면, editer은 false
		boolean editer=false;
		model.addAttribute(editer);
		return "/bulletin/write";
	}
	
	//게시판 글쓰기
	@PostMapping("/bulletin/write")
	public String handleWrite(@ModelAttribute("command") BoardWritingCommand bodCommand,
			Errors errors, HttpSession session, HttpServletResponse response
			,Model model) {
		new BoardValidator().validate(bodCommand, errors);
		if(errors.hasErrors()) {
			return "/bulletin/write";
		}
		//작자 등록
		AuthInfo authinfo=(AuthInfo)session.getAttribute("authInfo");
		bodCommand.setWriter(authinfo.getName());
		
		//업로드
		boardWritingService.regist(bodCommand);
		
		//다 쓰고 메인으로 돌아가면, 다시 모든 리스트를 보여줌
		List<Board> bulletins = boardDao.selectAll();
		model.addAttribute("bulletins", bulletins);
		return "/bulletin/main";
	}

	//게시판 글수정
	@GetMapping("/bulletin/edit")
	public String handleEditGet(
			@ModelAttribute("command") BoardWritingCommand bodCommand,
			Model model, @RequestParam("boardNum") long x) {
	
		Board reuploadBoard=boardDao.selectById(x);
		bodCommand.setContent(reuploadBoard.getContent());
		bodCommand.setTitle(reuploadBoard.getTitle());
		bodCommand.setWriter(reuploadBoard.getWriter());
		
//		System.out.println(x);
		
		//editer을 통해, 해당 글이 수정인지 아닌지 판단
		boolean editer=true;
		model.addAttribute(editer);
		
		return "/bulletin/write";
	}
	
	@PostMapping("/bulletin/edit")
	public String handleEdit(@ModelAttribute("command") BoardWritingCommand bodCommand,
			Errors errors, Model model, @RequestParam("boardNum") long x) {
		new BoardValidator().validate(bodCommand, errors);
		if(errors.hasErrors()) {
			return "/bulletin/write";
		}

		//보내야할 인자: 새로운 text, 숫자 x(board_id)
		boardWritingService.reupload(bodCommand, x);
			
		//보여줘야할 건 write와 같으나, 미리 글이 쓰여있음
		//다 쓰고 메인으로 돌아가면, 다시 모든 리스트를 보여줌
		List<Board> bulletins = boardDao.selectAll();
		model.addAttribute("bulletins", bulletins);
		return "/bulletin/main";
	}
	
	
	//게시판 글삭제
	@RequestMapping("/bulletin/delete")
	public String deleteBoard(@RequestParam("boardNum") long x, Model model) {
		Board deleteBoard=boardDao.selectById(x);
		boardWritingService.delete(deleteBoard, x);
		
		List<Board> bulletins = boardDao.selectAll();
		model.addAttribute("bulletins", bulletins);
		return "/bulletin/main";
	}
	/*
	@Controller
public class MemberListController {

	@RequestMapping("/members") 
	public String list(@ModelAttribute("cmd") ListCommand listCommand,
			Errors errors,
			Model model) {

		if(listCommand.getFrom() != null && listCommand.getTo() != null) {
			List<Member> members= memberDao.selectByRegdate(
					listCommand.getFrom(), listCommand.getTo());
			model.addAttribute("members", members);
		}
		
		return "member/memberList";
	}
	/*
	 프로퍼티 파일의 typeMismatch.java.time.LocalDateTime은 그 자체로 에러를 나타낸다.
해당 에러가 나타나면 여기에 대입된 문장을 출력한다. MemerListController클래스에서, 
요청매핑 애노테이션 적용 메서드(submit)가 Errors파라미터를 가지면, @DateTimeFormat에 지정한
형식에 맞지 않으면, Errors객쳉 typeMismatch에러코드를 추가한다. 
	 */
}
