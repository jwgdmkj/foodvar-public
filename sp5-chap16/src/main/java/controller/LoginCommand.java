package controller;

/* AuthService를 이용해 로그인요청을 처리하는 LoginController의 작성
 *  폼에 입력한 값을 전달받기 위한 LoginCommand와, 폼에 입력된 값이 올바른지 검사하기위한 LoginCommandValidator
 *  의 작성
 */
public class LoginCommand {
	private String email;
	private String password;
	private boolean rememberEmail;
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email= email;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password= password;
	}
	
	public boolean isRememberEmail() {
		return rememberEmail;
	}
	
	public void setRememberEmail(boolean rememberEmail) {
		this.rememberEmail= rememberEmail;
	}
}
