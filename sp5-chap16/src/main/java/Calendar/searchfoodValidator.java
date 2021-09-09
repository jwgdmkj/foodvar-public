package Calendar;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;


public class searchfoodValidator implements Validator{
	@Override
	public boolean supports(Class<?> clazz) {
		return searchfoodObject.class.isAssignableFrom(clazz);
	}
	
	@Override
	public void validate(Object target, Errors errors) {
		ValidationUtils.rejectIfEmptyOrWhitespace(
				errors, "foodname", "required");
	}
}
