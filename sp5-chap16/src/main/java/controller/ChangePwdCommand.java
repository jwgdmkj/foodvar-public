package controller;

/* 비밀번호 변경에 사용할 커맨드 객체&Validator 클래스 작성
 */
public class ChangePwdCommand {
	private String currentPassword;
	private String newPassword;
	
	public String getCurrentPassword() {
		return currentPassword;
	}
	public String getNewPassword() {
		return newPassword;
	}
	
	public void setCurrentPassword(String currentPassword) {
		this.currentPassword= currentPassword;
	}
	public void setNewPassword(String NewPassword) {
		this.newPassword= NewPassword;
	}
 }
