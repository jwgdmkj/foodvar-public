package spring;

import org.springframework.transaction.annotation.Transactional;

public class WithdrawService {
	private MemberDao memberDao;

	public void setMemberDao(MemberDao memberDao) {
		this.memberDao = memberDao;
	}
	
	@Transactional //트랜잭션 범위에서 설정할 메서드에 붙임(여러 쿼리가 한 번에 쓰일 때, 그 범위에서 실행될 메서드에)
	public void withdraw(String email, String realPwd) {
		Member member = memberDao.selectByEmail(email);
		if (member == null)
			throw new MemberNotFoundException();
		
		//암호화되어 전달된 pw가 member의 pw와 같은지 확인
		if(!member.matchPassword(realPwd))
			throw new WrongIdPasswordException();
		
		memberDao.deleteMember(member);
	}
}
