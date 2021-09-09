package spring;

public class RegisterRequest {

	private String email;
	private String password;
	private String confirmPassword;
	private String name;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isPasswordEqualToConfirmPassword() {
		return password.equals(confirmPassword);
	}
}

/* registerReques : 커맨드 객체에 접근할 때 사용한 속성 이름.
스프링 mvc는 커맨드 객체의 첫 글자를 소문자로 바꾼 클래스이름과 동일한 속성 이름을 사용해
커맨드 객체를 뷰에 전달. 즉 클래스명이 RegisterRequest이므로, registerRequest
라는 이름을 이용해 객체 접근이 가능
@PostMapping("/register/step3")
public String handleStep3(RegisterRequest regReq) { }
에서, RegisterRequest가 커맨드객체이므로, 뷰 코드는 이름을 사용해 registerRequest.name으로 접근
*/