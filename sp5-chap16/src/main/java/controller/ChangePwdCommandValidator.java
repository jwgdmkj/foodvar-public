package controller;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

//ChangePwdCommand객체를 검증하는 클래스
public class ChangePwdCommandValidator implements Validator{
	@Override
	public boolean supports(Class<?> clazz) {
		return ChangePwdCommand.class.isAssignableFrom(clazz);
	}
	
	@Override
	public void validate(Object target, Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(
				errors, "currentPassword", "required");
		
		ValidationUtils.rejectIfEmpty(errors, "newPassword", "required");
	}
}
