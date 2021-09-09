package controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import spring.Member;
import spring.MemberDao;

@Controller
public class MemberListController {
	private MemberDao memberDao;
	public void setMemberDao(MemberDao memberDao) {
		this.memberDao= memberDao;
	}
	
	/* members url로 들어가면, cmd라는 이름의 listcommand를 model로 넣음
	 * 만약 from과 to 어떠한 것도 널이 아니라면, Member형을 넣는 리스트인 members에
	 * 해당 listcommand의 from과 to를 얻어와, 이 날짜에 부합하는 member를 찾아,
	 * 이를 "members"란 이름으로 커맨드객체로 만들어 memberList로 보내는 함수
	 * 
	 * 커맨드객체로 받은 listCommand의 from과 to프로퍼티를 이용해 해당기간에 가입한 Members목록을 구하고,
	 * 뷰에 "members"속성으로 전달. 뷰는 이에 맞게 ListCommand객체를 위한 폼을 제공하고, members 속성을
	 * 이용해 회원목록을 출력하도록 한다.
	 * 
	 * 이 컨트롤러 코드를 작성후엔, ControllerConfig설정클래스에 관련 빈설정을 추가해야함
	 * 
	 * errors를 인자에 추가하지않는 경우, 잘못된 입력예시를 보이면, 400에러가 발생. 
	 * 가령 20210124만 할 경우, 시간(hh)가 없어, 에러. 이를 막자.
	 */
	@RequestMapping("/members") 
	public String list(@ModelAttribute("cmd") ListCommand listCommand,
			Errors errors,
			Model model) {
		if(errors.hasErrors()) {
			return "member/memberList";
		}
		
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
