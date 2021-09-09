package spring;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

public class MemberDao {

	private JdbcTemplate jdbcTemplate;

	private RowMapper<Member> memRowMapper=
			new RowMapper<Member>() {
		@Override
		public Member mapRow(ResultSet rs, int rowNum) throws SQLException {
			Member member= new Member(rs.getString("EMAIL"), rs.getString("PASSWORD"),
					rs.getString("NAME"), rs.getTimestamp("REGDATE").toLocalDateTime());
			member.setId(rs.getLong("ID"));
			return member;
		}
	};
	
	/*
	 * 드라이버 로드, 커넥션 생성 & DB 연결, SQL 실행, DB 연결 해제 부분은 매번 같은 동작을 반복한다
	 * JDBC Template을 이용해서 이러한 작업들을 간단하게 처리 할 수 있다
	 * 
	 * jdbtTemplate 객체를 위에서 생성했고, 이에 bean으로 등록한 DataSource를 주입.
	 * @Autowired에 의해 DataSource타입에 해당하는 bean을 찾아 ㅈㅜ입.
	 */
	public MemberDao(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public int count() {
		Integer count = jdbcTemplate.queryForObject(
				"select count(*) from MEMBER", Integer.class);
		return count;
	}
	
	public void insert(Member member) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection con)
					throws SQLException {
				// 파라미터로 전달받은 Connection을 이용해서 PreparedStatement 생성
				PreparedStatement pstmt = con.prepareStatement(
						"insert into MEMBER (EMAIL, PASSWORD, NAME, REGDATE) " +
						"values (?, ?, ?, ?)",
						new String[] { "ID" });
				// 인덱스 파라미터 값 설정
				pstmt.setString(1, member.getEmail());
				pstmt.setString(2, member.getPassword());
				pstmt.setString(3, member.getName());
				pstmt.setTimestamp(4,
						Timestamp.valueOf(member.getRegisterDateTime()));
				// 생성한 PreparedStatement 객체 리턴
				return pstmt;
			}
		}, keyHolder);
		Number keyValue = keyHolder.getKey();
		member.setId(keyValue.longValue());
	}

	public void update(Member member) {
		jdbcTemplate.update(
				"update MEMBER set NAME = ?, PASSWORD = ? where EMAIL = ?",
				member.getName(), member.getPassword(), member.getEmail());
	}

	public Member selectByEmail(String email) {
		List<Member> results = jdbcTemplate.query(
				"select * from MEMBER where EMAIL = ?",
				memRowMapper, email);

		return results.isEmpty() ? null : results.get(0);
	}

	

	public List<Member> selectAll() {
		List<Member> results = jdbcTemplate.query("select * from MEMBER",
				memRowMapper);
		return results;
	}

	public Member selectById(Long memId) {
		List<Member> results= jdbcTemplate.query("select * from MEMBER where ID=?",
				memRowMapper, memId);
		
		return results.isEmpty() ? null : results.get(0);
	}

	/* 회원가입일자를 기준으로 검색하는 기능 구현을 위한, 스프링 mvc
	 * REGDATE값이 두 파라미터로 전달받은 from과 to 사이에 있는 Member목록을 구함
	 * 
	 * RowMapper 인터페이스 구현을 통해, sql의 결과를 객체에 매핑해 결과를 리턴
	 * 이때, mapRow()라는 인터페이스 메소드를 통해 결과를 리턴함.
	 * Member형으로 래핑해야 하므로, RowMapper<Member>()
	 * maprow를 통해, Member형을 리턴. 이 때, 새 멤버는 sql에서 쿼리를 통해 얻어온 resultset에
	 * 이메일, 비번 등을 get해오고, 여기에 Id를 셋해서 리턴을 한다.
	 * 이것이 query의 첫번째 인자. 두번째와 세번째 인자는 위의 주석 참고.
	 */
	public List<Member> selectByRegdate(
			LocalDateTime from, LocalDateTime to) {
		List<Member> results= jdbcTemplate.query(
				"select*from MEMBER where REGDATE between ? and ?" +
		"order by REGDATE desc", memRowMapper, from, to);
		return results;
	}
	
	//삭제
	//DELETE TABLE[MEMBER] WHERE[email]=[...] (딜리트테이블 [테이블명] 웨얼[칼럼명]=[칼럼변수]
	public void deleteMember(Member member) {
		jdbcTemplate.update(
				"DELETE FROM MEMBER WHERE EMAIL=?", member.getEmail());
	}
	
	//select REPLY_ID from MEMBER
	public List<Long> listingId() {
		List<Long> results = new ArrayList<Long>();
		List<Member> member = selectAll();
		for(int i=0; i<member.size(); i++) {
			results.add(member.get(i).getId());
		}
		return results;
	}
}
