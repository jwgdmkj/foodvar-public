package interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;

/* 로그인하지 않은 상태에서 비밀번호 변경 폼을 요청 시, 로그인화면으로 이동시키고자한다. 
 * HttpSession에 authInfo객체가 존재하는지 검사해, 존재하지 않으면 로그인경로로 리다이렉트하도록
 * changePwdController클래스를 수정하고자 한다.
 * @GetMapping
 * public String form(@ModelAttribute("command") CHngePwdCommand pwdcmd, 
 * HttpSession session) {
 * 	AuthInfo authInfo=(AuthInfo)session.getAttribute("authInfo");
 *  if(authInfo==null) { return "redirect:/login"; }
 *  return "edit/changePwdForm"; 
 *  }
 * 그러나 실제 웹에서는, 비번변경기능 외 더 많은 기능에 로그인여부 확인이 필요하다. 
 * 각 기능을 구현한 컨트롤러코드마다 세션확인 코드를 삽입하면, 많은 중복이 발생한다.
 * 다수의 컨트롤러에 대해 동일한 기능을 적용해야할때, HandlerInterceptor이다.
 * 이 인터페이스를 통해, 컨트롤러 실행전&후(=뷰를 실행하기 전)&뷰 실행한 이후
 * 세 시점에서 공통기능의 구현이 가능하다.
 * 
 * preHandler()- false를 리턴시, 컨트롤러(혹은 다음 HandlerInterceptor)을 실행하지않음
 * postHandler()- 컨트롤러(핸들러) 실행후, 추가기능 구현시 사용
 * afterCompletion()- 뷰가 클라이언트에 응답 전송후 실행.익셉션이 발생하지 않으면, 4번째param은 널.
 * 즉, 컨트롤러실행후 예기치않은 익셉션을 로그로 남기기|실행시간 기록 등의 후처리에 적합
 * 
 * 순서: 1)DispatcherServlet에서 preHandler()로, 이후 HandlerIntercpetor가 return
 * 2)prehandle이 true면, @RequestMapping실행, 컨트롤러(HadnlerAdapter)는 return ModelAndView
 * 3)DispatcherServlet이 postHandler()로, HandlerInterceptor로 보내고, 뷰로 응답생성(4)
 * 5)DispatcherServlet이 afterCompletion()을 HandlerIntercpetro로.
 */

public class AuthCheckInterceptor implements HandlerInterceptor{
	
	/* authInfo속성이 HttpSession에 존재하면, true리턴하고 컨트롤러 실행, 
	 * 존재안하면 리다이렉트응답 생성 후(sendRedirect) false리턴, 리다이렉트
	 * request.getContextPath()는 현재 컨텍스트 경로를 리턴
	 * 즉, 아래 sendRedirect는 /sp5-chap13/login으로 리다이렉트
	 */
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		HttpSession session= request.getSession(false);
		if(session!=null) {
			Object authInfo=session.getAttribute("authInfo");
			if(authInfo!=null) {
				return true;
			}
		}
		
		response.sendRedirect(request.getContextPath()+"/login");
		return false;
	}
}
