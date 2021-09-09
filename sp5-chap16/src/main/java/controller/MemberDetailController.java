package controller;

import spring.Member;

import org.springframework.beans.TypeMismatchException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import spring.MemberDao;
import spring.MemberNotFoundException;

/* 가령 ID가 10인 멤버를 조회할 때 URL은 http://localhost:8080/sp5-chap14/members/10
 * 이 형식의 url을 이용하면, 각 회원마다 경로의 마지막부분이 달라짐. 이렇게 경로의 일부가
 * 고정되지 않고 달라질 때 사용하는 것이 @PathVariable로, 이를 통해 가변경로 처리가 가능.
 */
@Controller	//컨틀로러의 역할을 수행한다고 명시
public class MemberDetailController {
	private MemberDao memberDao;
	
	public void setMemberDao(MemberDao memberDao) {
		this.memberDao= memberDao;
	}
	/* 매핑경로의 {경로변수}에 해당하는 값은, PathVariable() 애노테이션을 가진
	 * 파라미터(여기선 Long memId)에 전달된다. 즉 요청경로가 "/members/10"이면, {id}에
	 * 해당하는 10이 memId파라미터에 전달됨. memId파라미터 타입은 Long이지만, 알아서
	 * String형 10은 알아서 Long으로 변환된다.
	 */
	@GetMapping("/members/{id}")
	public String detail(@PathVariable("id") Long memId, Model model) {
		Member member= memberDao.selectById(memId);
		if(member==null) {
			throw new MemberNotFoundException();
		}
		
		model.addAttribute("member", member);
		return "member/memberDetail";
	}
	
	/* 없는 id가 url상으로 들어오면(없는 회원데이터), MemberNotFoundException이 발생토록 한다.
	 * 이는 try-catch문으로.
	 * Long형타입으로 변환이 불가능한, char이나 string이 온다면, 이는 타입변환실패로인한 에러다.
	 * 여기엔 ExceptionHandler을 쓰면 된다.
	 */
	//경로변수값 타입이 올바르지 않을때, handleTypeMismatchException() 메서드를 실행
	@ExceptionHandler(TypeMismatchException.class)
	public String handleTypeMismatchException(TypeMismatchException ex) {
		return "member/invalidId";
	}
	//MemberNotFoundException 발생시, handleNotFoundException() 메서드를 실행
	@ExceptionHandler(MemberNotFoundException.class)
	public String handleNotFoundException(MemberNotFoundException ex) {
		return "member/noMember";
	}
}
