package Calendar;

public class Eater {
	
	private String name; private long num; private String foodname;
	private long foodnum;
		
	public Eater(String name, long num, String foodname, long foodnum) {
		this.name = name; this.num = num; this.foodname = foodname; this.foodnum = foodnum;
	}
	
	public String getName() {return name;} void setName(String name) { this.name = name; }
	public String getFoodname() {return foodname;} void setFoodname(String foodname) { this.foodname = foodname; }
	public long getNum() {return num;} void setNum(long num) { this.num = num; }
	public long getFoodnum() {return foodnum;} void setFoodnum(long foodnum) { this.foodnum = foodnum; }
}
