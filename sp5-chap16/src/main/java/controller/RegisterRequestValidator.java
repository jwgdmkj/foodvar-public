package controller;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import spring.RegisterRequest;

public class RegisterRequestValidator implements Validator{
	private static final String emailRegExp=				
			"^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" +
					"[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	private Pattern pattern;
	
	public RegisterRequestValidator() {
		pattern= Pattern.compile(emailRegExp);
	}
	
	/*
	 * 전달받은 clazz객체가 RegisterRequest로 타입변환이 가능한지 확인.
	 */
	@Override
	public boolean supports(Class<?> clazz) {
		return RegisterRequest.class.isAssignableFrom(clazz);
	}
	
	/* target - 검사대상 객체, errors- 검사결과 에러코드 설정을 위한 객체
	 * 우선 target을 RegisterRequest로, 실제 타입으로 변환한 다음 값을 검사.
	 * emial이 널이거나 빈문자열이라면, email프로퍼티의 에러코드는 'required'
	 * 또는, 정규표현식(matcher)을 이용해, 이메일이 올바르지않으면 에러코드는 'bad'
	 * 
	 * rejectValue()메서드는 첫 파라미터는 프로퍼티의 이름, 두번째는 에러코드.
	 * jsp는 이 에러코드를 이용해 에러메세지를 출력
	 */
	@Override
	public void validate(Object target, Errors errors) {
		RegisterRequest regReq= (RegisterRequest)target;
		if(regReq.getEmail()==null || regReq.getEmail().trim().isEmpty()) {
			errors.rejectValue("email", "required");
		} else {
			Matcher matcher = pattern.matcher(regReq.getEmail());
			if(!matcher.matches()) {
				errors.rejectValue("email", "bad");
			}
		}
			
		/* ValidationUtils는 객체의 값 검증코드를 간결히 작성토록 함
		 * 검사대상객체의 name프로퍼티가 null이거나 공백인 경우,name프로퍼티의 에러코드는 required
		 * 이때, 검사대상객체인 target을 전달받지 않아도, Validator을 쓰는 코드는 
		 * 요청 매핑 애노테이선 적용메서드에 Errors타입 파라미터를 전달받고(in RegisterRequest.java)
		 * 이 Errors객체를 Validator의 validator()의 두번째파라미터로 전달한다.
		 * 그럼 스프링mvc는 handleStep3()메서드 호출시, 커맨드객체(regReq)와 연결된 Errosr객체를 생성해
		 * 파라미터로 잔달. 이 객체는 커맨드객체의 특정 프로퍼티 값을 구할수있는 getFieldValue()를 제공.
		 * errors.getFieldValue("name")이 실행되어, name프로퍼티값을 자동으로 구함.
		 * 즉, ValidationUtils.rejectIfEmptyOrWhitespace()는 컴내드객체를 전달받지 않아도
		 * Errors객체를 통해 지정한 값을 구할 수 있음.
		 * 
		 * 각각의 name, password, confirmpassword는 jsp내의 form value.
		 * 마지막인자(reqired, nomatch등)은 label.property에서 그에맞춰 에러이유가 출력
		 */
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "required");
		ValidationUtils.rejectIfEmpty(errors, "password", "required");
		ValidationUtils.rejectIfEmpty(errors, "confirmPassword", "required");
			
		if (!regReq.getPassword().isEmpty()) {
			if (!regReq.isPasswordEqualToConfirmPassword()) {
				errors.rejectValue("confirmPassword", "nomatch");
			}
		}
		}
}
/* 커맨드객체 자체가 잘못된 경우엔, rejectValue()가 아닌, reject()를 사용
 * 예) try{} catch(WorngIdPasswordException ex) {
 * errors.reject("notMatchingIdPassword"); return "login/loginForm;"
 */