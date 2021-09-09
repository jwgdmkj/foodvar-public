package controller;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import spring.DuplicateMemberException;
import spring.MemberRegisterService;
import spring.RegisterRequest;
import javax.validation.Valid;

/*특정 요청 URL을 처리할 코드를 Controller을 이용해 구현
 * 약관동의-회원정보입력-가입처리를 step 1,2,3로 나눈다음, 이를 하나의 컨트롤러 클래스에서 구현할 예정
 * 즉, 컨트롤러클래스 1개가 세 개의 메서드에서 각 step의 요청경로를 처리하도록 구현
 */
@Controller
public class RegisterController {
	
	private MemberRegisterService memberRegisterService;
	public void setMemberRegisterService
	(MemberRegisterService memberRegisterService) {
		this.memberRegisterService= memberRegisterService;
	}
	
	//단순 약관내용 보여쥬는 뷰 이름의 리턴 메서드 및 jsp
	@RequestMapping("/register/step1")
	public String handleStep1() {
		return "register/step1";
	}
	
	@PostMapping("/register/step2")
	/*public String handleStep2(HttpServletRequest request) {
		String agreeParam= request.getParameter("agree");
		if(agreeParam==null || !agreeParam.equals("true")) {
			return "register/step1";
		return "register/step2";	
		}*/
		
	//Model: 뷰가 무언가를 구성해야 할 때 필요한 데이터를 생성해서 전달하는것
	//가령 여기선, password나 confirmpassword등이 있다
	//혹은, addattribute를 없애고 Model model을 RegisterReqest reigsterRequest로 할수도있다.
	public String handlestep2(
	 	@RequestParam(value="agree", defaultValue="false") Boolean agree,
	 	Model model) {
	  		if(!agree) return "register/step1";
		  		
	  		//registerRequest이름의 registerRequest객체 생성, 전송시 step2.jsp는
	  		//<form:input>에서 해당 "..."내 요소를 input받아 바인딩(ex.name, password)
	  		model.addAttribute("registerRequest", new RegisterRequest());	//p.289
	  		return "register/step2"; 
		//agree요청 파라미터의 값을 읽어와 agreeVal 파라미터에 할당. 요청 파라미터 값이 없으면 false문자열이 값이됨
	}
	/* step1의 폼 코드(form code)는, 전송 방식을 post로 지정.
	 * POST 방식은 GET 방식과 달리, 데이터 전송을 기반으로 한 요청 메서드이다.
	 * GET방식은 URL에 데이터를 붙여서 보내는 반면, POST방식은 URL에 붙여서 보내지 않고
	 * BODY에다가 데이터를 넣어서 보낸다.  
	 * 따라서, 헤더필드중 BODY의 데이터를 설명하는 Content-Type이라는 헤더 필드가
	 * 들어가고 어떤 데이터 타입인지 명시한다.
	 * 예) application/x-www-form-urlencoded, text/plain, multipart/form-data
	 * 
	 * 단, Get방식으로, URL뒤에 붙이기가 가능
	 * 예)www.example.com?id=mommoo&pass=1234
	 * URL 뒤에 "?" 마크를 통해 URL의 끝을 알리면서, 데이터 표현의 시작점을 알린다.
	 * 데이터는 key 와 value 쌍으로 넣어야 한다 윗 예시에서의 key는 id 랑 pass고 
	 * value는 mommoo랑 1234가 되겠다.
	 * 중간에 &마크는 구분자 이다. 2개이상의 key - value 쌍 데이터를 보낼때는 &마크로 구분해준다.
	 * URL에 붙이므로, HTTP패킷의 해더에 포함되여 서버에 요청한다.
	 * 따라서, GET 방식에서 BODY에 특별한 내용을 넣을 것이 없으므로 BODY가 빈상태로 보내진다.
	 * 그러므로, 헤더의 내용중 BODY 데이터를 설명하는 Content-Type이라는 헤더필드는 들어가지 않는다.
	 * URL형태로 표현되므로, 특정 페이지를 다른사람 에게 접속하게 할 수 있다. 
	 * 또한 간단한 데이터를 넣도록 설계되어, 데이터를 보내는 양의 한계가 있다.
	 * 
	 * PostMapping은 Post방식의 /register/step2만 처리하고, get방식으론 처리 안함
	 * GetMapping을 하면, get방식으로만 처리. 
	 * RequestMapping은 아무거나, 즉 같은 경로에 대해 다른 메서드가 처리하도록도 가능
	 * 
	 * @GetMapping("/member/login")
	 * public String form() { }
	 * @PostMapping("/member/login")
	 * public String login() {}
	 * 
	 * step1.jsp에선, 약관에 동의할 경우, 값이 true인 agree요청 파라미터 값을 POST방식으로 전송. 즉
	 * 폼에서 지정한 agree요청 파라미터값을 이용해 약관동의여부를 확인 가능.
	 * 컨트롤러메서드에서 요청 파라미터를 사용하는 방법 1) HttpServletRequest의 이용 및 
	 * getParameter()을 이용해 파라미터 값 구하기
	 * 
	 * 이때, step2는 post만 처리하므로, get식으로 링크를 그대로 url창에 치면 안뜬다. 405에러가 발생.
	 * 만약 step2를 요청받으면, 이를 리다이렉트시키는 방법이 더 좋다. redirect:를 붙이면 됨
	 * 또는, 상대경로를 이용해 redirect:step1도 가능
	 * */
	@GetMapping("/register/step2")
	public String handleStep2Get() {
		return "redirect:/register/step1";
	}
	
	/* 일일이 String param=request.getParameter("param")... 하는 건 비효율적.
	 * 즉, 요청 파라미터의 값을 커맨드 객체에 담아주면 편함. 가령 이름이 name인 요청 파라미터 값을, 커맨드 객체의
	 * setName()을 이용해 커맨드 객체에 전달
	 * RegisterRequest클래스엔, setEmail, setName... 등이 있다. 스프링은 이를 이용,
	 * email, name 등의 요청 파라미터값을 커맨드 객체에 복사한 뒤 regReq파라미터로 잔달.
	 * regReq파라미터가 있으면, memberRegisterService의 커맨드들을 일일이 가져와 regReq에 넣는다.
	 * 따라서, 페이지 상에서 넣으면 알아서 set함수에 따라 객체에 값을 복사해줌. 이를 register에서 get.
	 */
	//커맨드 객체에 접근할 때 사용할 속성이름 변경하고 싶을 때, ModelAttribute("..")를 사용
	//RegisterRequest는 registerRequest지만, 아래를 통해 formData로 접근이 가능.
	@PostMapping("/register/step3")
	public String handleStep3(RegisterRequest regReq, Errors errors){
		new RegisterRequestValidator().validate(regReq, errors);
		if(errors.hasErrors()) {
			return "register/step2";
		}
		
		try {
			memberRegisterService.regist(regReq);
			return "register/step3";
		}
		catch (DuplicateMemberException ex) {
			errors.rejectValue("email", "duplicate");
			return "register/step2";	//실패시, step2로 귀환
		}
	}
}
