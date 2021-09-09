package controller;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

//회원이 가입한 일시를 기준으로 회원을 검색하기 위한 시작시간 기준과 끝시간 기준을 파라미터로 전달받는다하자.
//검색기준시간을 표현하기 위해 아래의 커맨드클래스를 구현해 사용
public class ListCommand {
	
	@DateTimeFormat(pattern="yyyyMMddHH")
	private LocalDateTime from;
	@DateTimeFormat(pattern="yyyyMMddHH")
	private LocalDateTime to;
	
	public LocalDateTime getFrom() {
		return from;
	}
	public LocalDateTime getTo() {
		return to;
	}
	
	public void setFrom(LocalDateTime from) {
		this.from= from;
	}
	public void setTo(LocalDateTime to) {
		this.to= to;
	}
}
//검색을 위한 입력폼은, 이름이 from,to인 <input>태그 정의
//<input type="text" name="from"/> <input type="text" name="to"/>
//단, <input>에 입력한 문자열은 LocalDateTime으로의 형변환이 필요햐다. 따라서,
//@DateTimeFormat(pattern="yyyyMMddHH")를 통해, 2018030115등을 2018년3월1일15시 로의 객체값으로 변환한다