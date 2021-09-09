package Board;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import controller.WithdrawCommand;

public class BoardValidator implements Validator{
	@Override
	public boolean supports(Class<?> clazz) {
		return BoardWritingCommand.class.isAssignableFrom(clazz);
	}
	
	@Override
	public void validate(Object target, Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(
				errors, "title", "required");
		
		ValidationUtils.rejectIfEmpty(errors, "content", "required");
	}
}
