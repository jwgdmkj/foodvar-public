package Calendar;

import java.awt.desktop.SystemSleepEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;

import Board.Board;
import spring.AuthInfo;

public class FoodDao {
	private JdbcTemplate jdbcTemplate;
	
	private RowMapper<Food> foodRowMapper=
			new RowMapper<Food>() {
		@Override
		public Food mapRow(ResultSet rs, int rowNum) throws SQLException {
			Food food= new Food(rs.getLong("ID"),
					rs.getString("num"), rs.getString("code"),
					rs.getString("genre"), rs.getString("name"),
					rs.getString("size"), rs.getString("kcal"), rs.getString("carbs"),
					rs.getString("prot"), rs.getString("fat"), rs.getString("sugar"), 
					rs.getString("nat"), rs.getString("chole"), rs.getString("satur"), 
					rs.getString("trans"));

			return food;
		}
	};
	
	public FoodDao(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public List<Food> finder(searchfoodObject command) {
		List<Food> results = jdbcTemplate.query(
				"select * from nutri where name LIKE concat('%', ?, '%')", foodRowMapper, 
				command.getFoodname());
		
		return results;
	}
	
	public Food finderSpecific(String command) {
		List<Food> results = jdbcTemplate.query(
				"select * from nutri where name = ?", foodRowMapper, 
				command);
		
		return results.isEmpty() ? null : results.get(0);
	}
	
	public Food findbyNum(String foodnum) {
		String concater = "\'";
		String tmp = concater + foodnum + concater;
		
		//select * from nutri where num = '\'810\'';
		List<Food> results= jdbcTemplate.query(
				"select * from nutri where num = ?",
				foodRowMapper, tmp);
		
		return results.isEmpty()? null : results.get(0);
	}
	
	public boolean registfoodIn(Food food) {
//		System.out.println(food.getName());
		return true;
	}
	
	public int insertIntoMember(Long userId, Long mealNum, String foodNum, Long year,
			Long month, Long date, String mealname, double kcal) {

		//System.out.print("시작 " + userId + " "+ mealNum + " " + foodNum + " " + 
		//year + " " + month + " " + date);

		//foodnum : foodnum, meal : meal, year: yearNum, month: monthNum, date : dateNum
		
		if(jdbcTemplate.update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection con)
					throws SQLException {
				// 파라미터로 전달받은 Connection을 이용해서 PreparedStatement 생성
				PreparedStatement pstmt = con.prepareStatement(
					"insert into schedule (MID, FID, MEAL, YEAR, MONTH, DATE, MealName, Kcal)"
				+ "values (?, ?, ?, ?, ?, ?, ?, ?)");

				// 인덱스 파라미터 값 설정
				pstmt.setLong(1, userId);
				pstmt.setString(2, foodNum);
				pstmt.setLong(3, mealNum);
				pstmt.setLong(4, year);
				pstmt.setLong(5, month + 1);
				pstmt.setLong(6, date);
				pstmt.setString(7, mealname);
				pstmt.setDouble(8, kcal);
				// 생성한 PreparedStatement 객체 리턴
				return pstmt;
			}
		}) > 0)
			return 1;
		else
			return 0;
	}
}
