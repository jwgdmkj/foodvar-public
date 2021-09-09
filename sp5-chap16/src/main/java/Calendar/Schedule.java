package Calendar;

public class Schedule {
	private int mid; private String fid; private int meal;
	private int year; private int month; private int date;
	private String mealname; private double kcal;
	
	public Schedule(int MID, String FID, int MEAL, int YEAR, int MONTH, int DATE,
			String mealname, double kcal) {
		this.mid= MID;	this.fid = FID; 	this.meal= MEAL;
		this.year = YEAR; this.month = MONTH; this.date = DATE;
		this.mealname = mealname; this.kcal = kcal;
	}
	
	public int getMid() { return mid; } public String getFid() { return fid; }
	public int getMeal() {return meal;} public int getYear() { return year; }
	public int getMonth() {return month;} public int getDate() { return date; }
	public String getMealname() { return mealname; } 
	public double getKcal() { return kcal; }
	
	void setMid(int mid) {this.mid=mid;} void setFid(String fid) { this.fid=fid; }
	void setMeal(int meal) {this.meal=meal;} void setYear(int year) { this.year=year; }
	void setMonth(int month) {this.month=month;} void setDate(int date) { this.date=date; }
	void setMealname(String mealname) { this.mealname = mealname; }
	void setKcal(double kcal) { this.kcal = kcal; }
}
