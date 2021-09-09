package Board;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import controller.WithdrawCommand;

public class ReplyValidator implements Validator{
	@Override
	public boolean supports(Class<?> clazz) {
		return ReplyCommand.class.isAssignableFrom(clazz);
	}
	
	@Override
	public void validate(Object target, Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "reply_content", "required");
	}
}