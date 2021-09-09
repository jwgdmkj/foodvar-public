package controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import add_on.EncryptionUtils;
import spring.AuthInfo;
import spring.AuthService;
import spring.WrongIdPasswordException;

/* 로그인을 유지하는 데에는 HtpSession
 * 
 */
@Controller
@RequestMapping("/login")
public class LoginComtroller {
	private AuthService authService;
	
	//authService는 이메일, 비번이 일치하는지 확인하는 클래스
	//이 객체의 메소드를 이용해야하기에, 이 객체를 LoginController에 넣고, 
	//authservice를 통해 로그인요청을 처리하는 LoginCommand객체가 문제가 있는지 확인후(validate),
	//문제가 없다면 최종적으로 logincommand내에서 이메일과 비번을 얻어와 authInfo에 저장
	public void setAuthService(AuthService authService) {
		this.authService=authService;
	}
	
	/* form()메서드는, 이메일 정보를 기억하고 있는 쿠키가 존재하면 해당 쿠키의 값을 이용해
	 * LoginCommand객체의 email프로퍼티 값을 설정한다.
	 * @CookieValue 애노테이션은 요청매핑 애노테이션 적용메서드의 Cookie타입 파라미터에 적용
	 * 
	 * @CookieValue애노테이션의 value속성은 쿠키의 이름을 지정(=REMEMBER). 
	 * 지정한 이름을 가진 쿠키가 존재하지 않거나, 이메일 기억하기를 선택하지않을 수도 있으니, 
	 * required는 false로 set한다. true라면, 지정한 이름을 가진 쿠키가 존재하지 않으면 익셉션발생
	 * 
	 * REMEMBER쿠키가 존재하면, 쿠키의 값을 읽어와 커맨드객체의 EMAIL프로퍼티값을 설정.
	 * 커맨드객체를 사용해 폼을 출력하므로, REMEMBER쿠키가 존재하면 입력폼의 EMAL프로퍼티에 쿠키값이
	 * 채워져 출력됨.
	 * 실제로 REMEMBER쿠키를 처리하는 부분은 로그인을 처리하는 SUBMIT부분. 쿠키를 생성하려면
	 * HttpServletResponse객체가 필요하므로, submit()메서드의 파라미터로 HttpServletResponse
	 * 타입을 추가.
	 */
	@GetMapping
	public String form(LoginCommand loginCommand,
			@CookieValue(value="REMEMBER", required=false) Cookie rCookie) {
		if(rCookie!=null) {
			//rCookie가 널이 아니라면, 쿠키가 존재한다는 것.
			//즉, 보낼 커맨드객체에 쿠키의 value를 전달.
			loginCommand.setEmail(rCookie.getValue());
			loginCommand.setRememberEmail(true);
		}
		return "login/loginForm";
	}
	
	//로그인폼을 보여주고, 성공결과 보여주기 위해 loginSuccess뷰를 이용. 두 뷰를 위한 jsp코드 작성 필요
	@PostMapping
/*	public String submit(LoginCommand loginCommand, Errors errors) {
		new LoginCommandValidator().validate(loginCommand, errors);
		if(errors.hasErrors())
			return "login/loginForm";
		try {
			//이메일과 비번이 일치하는지 확인
			AuthInfo authInfo=authService.authenticate(
					loginCommand.getEmail(),
					loginCommand.getPassword());
			//TODO세션에 authInfo 저장 필수
			return "login/loginSuccess";
		}
		catch(WrongIdPasswordException ex) {
			errors.reject("idPasswordNotMatching");
			return "login/loginForm";
		}
	}*/
	public String submit(LoginCommand loginCommand, Errors errors, 
			HttpSession session, HttpServletResponse response) {
		new LoginCommandValidator().validate(loginCommand, errors);
		EncryptionUtils E = new EncryptionUtils();
		
		if(errors.hasErrors()) {
			return "login/loginForm";
		}
		try {
			//이메일과 비번이 일치하는지 확인 ( 암호화된 걸 확인 )
			AuthInfo authInfo=authService.authenticate(
					loginCommand.getEmail(),
					E.encryptSHA256(loginCommand.getPassword()));
			
			//로그인 성공시, HttpSession의 authinfo 속성에 인증정보객체(authInfo)를 저장
			//로그아웃은, HttpSession을 제거하면 됨. LogoutController을 추가한다
			session.setAttribute("authInfo", authInfo);
			
			Cookie rememberCookie =
					new Cookie("REMEMBER", loginCommand.getEmail());
			rememberCookie.setPath("/");
			if(loginCommand.isRememberEmail()) {
				rememberCookie.setMaxAge(60*60*24*30);
			} else {
				rememberCookie.setMaxAge(0);
			}
			response.addCookie(rememberCookie);
			
			//TODO세션에 authInfo 저장 필수
			return "login/loginSuccess";
		}
		catch(WrongIdPasswordException ex) {
			errors.reject("idPasswordNotMatching");
			return "login/loginForm";
		}
	}
	/* 로그인 상태의 유지 - httpSession이용 or 쿠키. 
	 * 우선 Http세션을 이용해본다. 요청 매핑 에노테이션 적용 메서드에 HttpSession이 존재하면,
	 * 스프링은 컨트롤러의 메서드 호출 시, HttpSession객체를 파라미터로 전달.
	 * HttpSession생성 전이면, 새로운 HttpSession을, 아니면 기존의 것을 전달
	 * 
	 * 또는 HttpServletRequest의 getSession()을 이용
	 * 이는 위의 Submit에서 구현.
	 */
}
