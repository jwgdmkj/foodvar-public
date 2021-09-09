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
import spring.WithdrawService;
import spring.WrongIdPasswordException;

@Controller
@RequestMapping("/edit/withdrawer")
public class WithdrawController {
	private WithdrawService withdrawService;
	
	public void setWithdrawService
	(WithdrawService withdrawService) {
		this.withdrawService= withdrawService;
	}
	
	//데이터를 url에
	@GetMapping
	public String form(@ModelAttribute("command") WithdrawCommand wthCmd) {
		//비밀번호를 입력해, 맞으면 탈퇴시키는 창
		return "edit/withdraw";
	}
	
	//데이터를 body에
	//submit의 첫번째 param은 커맨드객체를
	@PostMapping
	public String submit(@ModelAttribute("command") WithdrawCommand wthCmd,
			Errors errors, HttpSession session) {
		new WithdrawValidator().validate(wthCmd, errors);
		EncryptionUtils E = new EncryptionUtils();
		//E.encryptSHA256(loginCommand.getPassword())
		
		if(errors.hasErrors()) {
			return "edit/withdraw";
		}
		
		AuthInfo authInfo=(AuthInfo) session.getAttribute("authInfo");
		try {
			//획득한 계정의 pw의 암호화
			withdrawService.withdraw(authInfo.getEmail(),
					E.encryptSHA256(wthCmd.getRealPassword()));
			session.invalidate();
			//여기 수정
			return "main";
		} catch(WrongIdPasswordException e) {
			errors.rejectValue("realPassword", "notMatching");
			return "edit/withdraw";
		}
	}
}