package Config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.validation.Validator;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import controller.RegisterRequestValidator;
import interceptor.AuthCheckInterceptor;

@Configuration
@EnableWebMvc
public class MvcConfig implements WebMvcConfigurer {

	@Override
	public void configureDefaultServletHandling
	(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}

	@Override
	public void configureViewResolvers(ViewResolverRegistry registry) {
		registry.jsp("/WEB-INF/view/", ".jsp");
	}

	/* step3.jsp의 회원가입 완료 후 첫 화면으로 이동하는 링크는, 뷰 이름만 리턴하도록 구현한다
	 * 이를 위한 컨트롤러클래스를 따로 구현하는 건 성가시다. 
	 * WebMvcConfigurere 인터페이스의 addViewControllers()를 통해, 이를 편하게 구현해본다.
	 * "/main"요청경로에 대해 뷰 이름으로 main을 사용토록 설정 후, 이에 해당하는 main.jsp를 만듬.
	 */
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/main").setViewName("main");
	}
	
	/* MessageSource인터페이스는
	 * String getMessage(String code, Object[] args, String defaultMessage, Local locale)
	 * 
	 * code파라미터는 메시지 구분 코드, local는 지역 구분. 같은 코드여도, 지역에 따라 다른 메시지 제공.
	 * messageSource의 구현체는, 자바의 프로퍼티파일로부터 메시지를 읽어오는 ResourceBundleMessageSource
	 * 이는, 메시지코드와 일치하는 이름을 가진 프로퍼티의 값을 메시지로 제공
	 * 
	 * ResourceBundleMessageSource는 자바의 리소스번들을 사용하기에, 해당 프로퍼티 파일이 클래스파일에 위치해야함
	 * 따라서, src/main/resources(=클래스패스)에 프로퍼티파일을 위치시킴
	 * <spring:message>는, 자동적으로 스프링설정에 등록된 <messageSource>빈을 이용, getMessage()를 실행해
	 * 필요메세지를 구하고, 이 때 code속성이 코드값으로 사용되며, 지정한 메시지가 code에 없으면 익셉션.
	 * 
	 * register.done=<strong>{0}님</strong>, 회원가입 완료
	 * {0}은 인덱스기반 변수중 0번째 인덱스. MessageSource의 getMessage()는 인덱스기반변수 전달을 위해
	 * Object배열 타입의 파라미터를 사용, 이를 이용해,
	 * Object[]args = new Object[1]; args[0]="자바"; messageSource.getMessage("register.done", args, Locale.KOREA);
	 * 처럼 인덱스기반 변수값 전달 가능.
	 * <spring:message>태그 사용시, argunmets속성 사용해 인덱스기반 변수값을 전달.
	 * step3에서, <spring:message code="register.done"
	arguments="${registerRequest.name }"/> 처럼, arguments를 이용해 regist.done메시지의 {0}위치에 삽입할 값을 결정함
	register.done=<strong>{0}님 ({1})</strong>, 회원가입 완료!
	처럼, {1}을 추가하기 위해, 두 개 이상의 값을 전달하려면 콤마로 구분|객체배열|<spring:argument>태그 사용이 필요
	가령, <spring:message code="register.done" arguments="${registerRequest.name}, ${registerRequest.email}"/>
	또는, <spring:message code>
	<spring:argument value="${registerRequest.name}"/> 
	<spring:argument value="${registerRequest.email"/>
	</spring:message>
	 */
	@Bean
	public MessageSource messageSource() {
		ResourceBundleMessageSource ms=
				new ResourceBundleMessageSource();
		//message패키지에 속한 label프로퍼티 파일로부터 메세지를 읽어온다고 설정
		//src/main/resource도 클래스패스에 포함되고, message폴더는 message패키지에 대응
		//즉, label.properties로부터 메시지를 얻어온다는 뜻
		ms.setBasenames("message.label");
		ms.setDefaultEncoding("UTF-8");
		return ms;
	}
	
	/* WebMvcConfigurer.addInterceptors() : 인터셉트 설정 메서드
	 * InterceptorRegistry.addInterceptor() : HandlerInterceptor객체 설정.
	 * 이때, AuthCheckInterceptor객체를 인터셉터로 설정
	 * InterceptorRegistry.addInterceptor() : InterceptorRegistration 객체를 리턴
	 * 이 객체의 addPathPatterns()은 인터셉터를 적용할 경로패턴을, Ant경로패턴을 사용
	 * 따라서, 밑에서는 /edit/changePassword경로의 AuthCheckInterceptor가 적용되며,
	 * 로그인되지 않은 상태에서 /edit/changePassword에 접근시, 로그인폼으로 리다이렉트됨
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(authCheckInterceptor()).
		addPathPatterns("/edit/**");
		//addPathPtterns()의 경로패턴중 일부를 제외하고싶다면 excludePathPatterns() 사용
		/* registry.addInterceptor(authCheckInterceptor()).
		addPathPatterns("/edit/**").excludePathPattenrs("/edit/help/**");
		 */
	}
	
	@Bean
	public AuthCheckInterceptor authCheckInterceptor() {
		return new AuthCheckInterceptor();
	}
}
