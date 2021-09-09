package spring;

import java.time.LocalDateTime;

import add_on.EncryptionUtils;

public class MemberRegisterService {
	private MemberDao memberDao;

	public MemberRegisterService(MemberDao memberDao) {
		this.memberDao = memberDao;
	}

	public Long regist(RegisterRequest req) {
		//Member의 비번 DB에 실제 저장되는건 암호화된 PW
		Member member = memberDao.selectByEmail(req.getEmail());
		EncryptionUtils E = new EncryptionUtils();
		String encrypted = E.encryptSHA256(req.getPassword());
		
		if (member != null) {
			throw new DuplicateMemberException("dup email " + req.getEmail());
		}
		Member newMember = new Member(
				req.getEmail(), encrypted, req.getName(), 
				LocalDateTime.now());
		memberDao.insert(newMember);
		
		return newMember.getId();
	}
}
