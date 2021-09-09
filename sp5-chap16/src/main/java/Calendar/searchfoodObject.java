package Calendar;

public class searchfoodObject {
	private String foodname;
	private Long month;
	private Long date;
	private Long year;
	
	public void setFoodname(String foodname) {
		this.foodname= foodname;
	}
	public String getFoodname() {
		return foodname;
	}
	public void setMonth(Long month) { this.month = month;	}
	public Long getMonth() { return month; }
	public void setYear(Long year) { this.year = year;	}
	public Long getYear() { return year; }
	public void setDate(Long date) { this.date = date;	}
	public Long getDate() { return date; }
}
