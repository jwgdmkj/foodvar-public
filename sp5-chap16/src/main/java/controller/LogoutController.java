package controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LogoutController {
	@RequestMapping("/logout") 
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/main";
	}
}
//위의 새로운 컨트롤러를 추가시, 스프링 설정에 빈을 추가하는 것을 잊지않는다.