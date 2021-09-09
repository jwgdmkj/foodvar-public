package controller;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import add_on.EncryptionUtils;
import spring.AuthInfo;
import spring.ChangePasswordService;
import spring.WrongIdPasswordException;

//비밀번호 변경 요청을 처리하는 컨트롤러 클래스
@Controller
@RequestMapping("/edit/changePassword")
public class ChangePwdController {
	private ChangePasswordService changePasswordService;
	
	public void setChangePasswordService
	(ChangePasswordService changePasswordService) {
		this.changePasswordService= changePasswordService;
	}
	
	//changePwdCommand형 객체 pweCmd를, "command"란 이름으로 옮긴다.
	//그럼 jsp파일에서 <form:input path="email">은, path로 지정된 pwdCmd내의 email을 입력하면, 자연스레 바인딩된다.
	
	@GetMapping
	public String form(@ModelAttribute("command") ChangePwdCommand pweCmd) {
		return "edit/changePwdForm";
	}
	
	@PostMapping
	public String submit(@ModelAttribute("command") ChangePwdCommand pwdCmd,
			Errors errors, HttpSession session) {
		new ChangePwdCommandValidator().validate(pwdCmd, errors);
		EncryptionUtils E = new EncryptionUtils();
		
		if(errors.hasErrors()) {
			return "edit/changePwdForm";
		}
		
		AuthInfo authInfo=(AuthInfo) session.getAttribute("authInfo");
		try {
			changePasswordService.changePassword(authInfo.getEmail(),
					E.encryptSHA256(pwdCmd.getCurrentPassword()), 
					E.encryptSHA256(pwdCmd.getNewPassword()));
			
			return "edit/changePwd";
		} catch(WrongIdPasswordException e) {
			errors.rejectValue("currentPassword", "notMatching");
			return "edit/changePwdForm";
		}
	}
}
