package Board;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

public class BoardDao {
	private JdbcTemplate jdbcTemplate;
	
	//SQL실행결과로 구한 ResultSet에서 한 행의 데이터를 읽어와 자바객체로 변환하는 매퍼
	private RowMapper<Board> bodRowMapper=
			new RowMapper<Board>() {
		@Override
		public Board mapRow(ResultSet rs, int rowNum) throws SQLException {
			Board board= new Board(rs.getString("WRITER"),
					rs.getString("TITLE"), 
					rs.getString("CONTENT"),
					rs.getTimestamp("REGDATE").toLocalDateTime());
			
			board.setNum(rs.getLong("NUM"));
	//		board.setWriter(rs.getString("WRITER"));
			board.setRecommend(rs.getLong("RECOMMEND"));
			board.setReply(rs.getLong("REPLY"));
			board.setWatcher(rs.getLong("WATCHER"));
			return board;
		}
	};
	
	/*
	 * 드라이버 로드, 커넥션 생성 & DB 연결, SQL 실행, DB 연결 해제 부분은 매번 같은 동작을 반복한다
	 * JDBC Template을 이용해서 이러한 작업들을 간단하게 처리 할 수 있다
	 * 
	 * jdbtTemplate 객체를 위에서 생성했고, 이에 bean으로 등록한 DataSource를 주입.
	 * @Autowired에 의해 DataSource타입에 해당하는 bean을 찾아 ㅈㅜ입.
	 */
	public BoardDao(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public int count() {
		Integer count = jdbcTemplate.queryForObject(
				"select count(*) from BULLETIN", Integer.class);
		return count;
	}
	
	//
	public void insert(Board board) {
		//자동생성된 키값 구해주는 구현클래스
		KeyHolder keyHolder = new GeneratedKeyHolder();

		jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection con)
					throws SQLException {
				// 파라미터로 전달받은 Connection을 이용해서 PreparedStatement 생성
				PreparedStatement pstmt = con.prepareStatement(
						"insert into BULLETIN (TITLE, CONTENT, REGDATE, "
						+ "WRITER, RECOMMEND, REPLY, WATCHER) "
				+ "values (?, ?, ?, ?, ?, ?, ?)",
						new String[] { "NUM" });
//				System.out.println("w");
				// 인덱스 파라미터 값 설정
				pstmt.setString(1, board.getTitle());
				pstmt.setString(2, board.getContent());
				pstmt.setTimestamp(3,
						Timestamp.valueOf(board.getRegisterDateTime()));
				pstmt.setString(4, board.getWriter());
				pstmt.setLong(5, board.getRecommend());
				pstmt.setLong(6, board.getReply());
				pstmt.setLong(7, board.getWatcher());
				
				// 생성한 PreparedStatement 객체 리턴
				return pstmt;
			}
		}, keyHolder);
		Number keyValue = keyHolder.getKey();
		board.setNum(keyValue.longValue());
	}

	
	//재업로드
	public void reupload(Board board, long num) {
		jdbcTemplate.update(
				"UPDATE BULLETIN SET CONTENT= ?, TITLE = ? WHERE NUM = ?", 
				board.getContent(), board.getTitle(), num);
	}

	//추천수나 댓글수, 조회수 업데이트
	public void updateLong(Board board) {
		jdbcTemplate.update(
				"update BULLETIN set RECOMMEND = ?, REPLY = ?, "
				+ "WATCHER=? where NUM = ?",
				board.getRecommend(), board.getReply(), board.getWatcher(),
				board.getNum());
	}
	
	public Board selectByNum(Long boardNum) {
		List<Board> results= jdbcTemplate.query(
				"select * from BULLETIN where NUM = ?",
				bodRowMapper, boardNum);
		
		return results.isEmpty()? null : results.get(0);
	}
	
	public Board selectByWriter(String Writer) {
		List<Board> results = jdbcTemplate.query(
				"select * from BULLETIN where WRITER = ?",
				bodRowMapper, Writer);

		return results.isEmpty() ? null : results.get(0);
	}

	public List<Board> selectAll() {
		List<Board> results = jdbcTemplate.query("select * from BULLETIN "
				+ "ORDER BY NUM DESC",
				bodRowMapper);
		return results;
	}

	public Board selectById(Long num) {
		List<Board> results= jdbcTemplate.query("select * from BULLETIN where NUM=?",
				bodRowMapper, num);
		
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
	public List<Board> selectByRegdate(
			LocalDateTime from, LocalDateTime to) {
		List<Board> results= jdbcTemplate.query(
				"select*from BULLETIN where REGDATE between ? and ?" +
		"order by REGDATE desc", bodRowMapper, from, to);
		return results;
	}
	
	//삭제
	//DELETE TABLE[MEMBER] WHERE[email]=[...] (딜리트테이블 [테이블명] 웨얼[칼럼명]=[칼럼변수]
	public void deleteBoard(Board board, long num) {
		jdbcTemplate.update(
				"DELETE FROM BULLETIN WHERE NUM=?", num);
	}
}
