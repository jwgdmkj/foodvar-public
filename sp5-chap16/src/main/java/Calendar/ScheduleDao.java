package Calendar;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class ScheduleDao {
private JdbcTemplate jdbcTemplate;
	
	private RowMapper<Schedule> scheduleRowMapper=
			new RowMapper<Schedule>() {
		@Override
		public Schedule mapRow(ResultSet rs, int rowNum) throws SQLException {
			Schedule schedule= new Schedule(rs.getInt("MID"),
					rs.getString("FID"), rs.getInt("MEAL"),
					rs.getInt("YEAR"), rs.getInt("MONTH"), rs.getInt("DATE"),
					rs.getString("MealName"), rs.getLong("Kcal"));
			return schedule;
		}
	};
	
	public ScheduleDao(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	//@SuppressWarnings("null")
	public List<Schedule> findByMid(long userId, int mealtime) {
		
		List<Schedule> results;
		//아침 점심 저녁 간식 야식 순으로 results에 넣기
		switch(mealtime) {
		case 0:
			results = jdbcTemplate.query(
					"select * from schedule where MID = ?", scheduleRowMapper, 
					userId);
			return results;
		case 1:
			results = jdbcTemplate.query(
					"select * from schedule where MID = ? and MEAL = 1", scheduleRowMapper, 
					userId);
			return results;
		case 2:
			results = jdbcTemplate.query(
					"select * from schedule where MID = ? and MEAL = 2", scheduleRowMapper, 
					userId);
			return results;
		case 3:
			results = jdbcTemplate.query(
					"select * from schedule where MID = ? and MEAL = 3", scheduleRowMapper, 
					userId);
			return results;
		case 4:
			results = jdbcTemplate.query(
					"select * from schedule where MID = ? and MEAL = 4", scheduleRowMapper, 
					userId);
			return results;
		case 5:
			results = jdbcTemplate.query(
					"select * from schedule where MID = ? and MEAL = 5", scheduleRowMapper, 
					userId);
			return results;
		}
		return null;
	}
	
	public List<Schedule>findByMidDate(long userId, long year, long month, long date) {
		List<Schedule> results = jdbcTemplate.query(
				"select * from schedule where MID = ? and YEAR = ? and MONTH = ? and DATE = ?",
				scheduleRowMapper, userId, year, month, date);
		return results;
	};
	public List<Schedule> findByFidDate(long fid, long year, long month, long date) {
		List<Schedule> results = jdbcTemplate.query(
				"select * from schedule where FID = ? and YEAR = ? and MONTH = ? and DATE = ?",
				scheduleRowMapper, fid, year, month, date);
		return results;
	}
}
