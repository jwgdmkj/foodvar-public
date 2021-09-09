package Calendar;

import java.time.LocalDateTime;

public class Food {
	private Long ID;

	private String num; private String code; private String genre;
	private String name; private String size; private String kcal;
	private String carbs; private String prot; private String fat;
	private String sugar; private String nat; private String chole;
	private String satur; private String trans;
		
	public Food(Long ID, String num, String code, String genre, String name, String size,
			String kcal, String carbs, String prot, String fat, String sugar, String nat,
			String chole, String satur, String trans) {
		this.ID= ID;	this.num = num; 	this.code= code;
		this.genre = genre;		this.name= name;	this.size = size;
		this.kcal= kcal;	this.carbs = carbs;		this.prot= prot;
		this.fat = fat; 	this.sugar= sugar;		this.nat = nat;
		this.chole = chole;		this.satur = satur;		this.trans= trans;
	}
	
	public Long getID() {return ID;} void setID(Long ID) { this.ID = ID; }
	
	public String getNum() { return num; } public String getProt() { return prot; }
	public String getCode() {return code;} public String getFat() { return fat; }
	public String getGenre() {return genre;} public String getSugar() { return sugar; }
	public String getName() {return name;} public String getNat() { return nat; }
	public String getSize() {return size;} public String getChole() { return chole; }
	public String getKcal() {return kcal;} public String getSatur() { return satur; }
	public String getCarbs() {return carbs;} public String getTrans() { return trans; }
	
	void setNum(String num) {this.num=num;} void setProt(String prot) { this.prot=prot; }
	void setCode(String code) {this.code=code;} void setFat(String fat) { this.fat=fat; }
	void setGenre(String genre) {this.genre=genre;} void setSugar(String sugar) { this.sugar=sugar; }
	void setName(String name) {this.name=name;} void setNat(String nat) { this.nat=nat; }
	void setSize(String size) {this.size=size;} void setChole(String chole) { this.chole=chole; }
	void setKcal(String kcal) {this.kcal=kcal;} void setSatur(String satur) { this.satur=satur; }
	void setCarbs(String carbs) {this.carbs=carbs;} void setTrans(String trans) { this.trans=trans; }
}
