package Calendar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import Board.Board;
import Board.BoardNotFoundException;
import Board.Reply;
import Board.ReplyCommand;
import Board.ReplyDao;
import Board.ReplyService;
import Board.ReplyValidator;
import spring.AuthInfo;
import spring.MemberDao;


@Controller
public class CalendarController {
	private ScheduleDao scheduleDao;
	public void setScheduleDao(ScheduleDao scheduleDao) {
		this.scheduleDao = scheduleDao;
	}
	private MemberDao memberDao;
	public void setMemberdao(MemberDao memberDao) {
		this.memberDao = memberDao;
	}
	private ReplyDao replyDao;
	public void setReplydao(ReplyDao replyDao) {
		this.replyDao = replyDao;
	}
	private ReplyService replyService;
	public void setReplyService(ReplyService replyService) {
		this.replyService= replyService;
	}

	@RequestMapping(value = "calendar/calendar/{id}", method = RequestMethod.GET)
	public String calendar(@PathVariable("id") Long memId, Model model,
	//@RequestMapping(value = "calendar/calendar", method = RequestMethod.GET)
	//public String calendar(Model model, 
			HttpServletRequest request, DateData dateData,	HttpSession session,
			@ModelAttribute("replyCommnad") ReplyCommand repCommand){
		
		long userId;
		try {
			AuthInfo authInfo=(AuthInfo) session.getAttribute("authInfo");
	        String usermail = authInfo.getEmail();
	        userId = memberDao.selectByEmail(usermail).getId();
		} catch(Exception e) {
			return "redirect:/main";
		}
		
		Calendar cal = Calendar.getInstance();
		DateData calendarData;
		//검색 날짜
		if(dateData.getDate().equals("")&&dateData.getMonth().equals("")){
			dateData = new DateData(String.valueOf(cal.get(Calendar.YEAR)),
					String.valueOf(cal.get(Calendar.MONTH)),
					String.valueOf(cal.get(Calendar.DATE)),null);
		}
		//검색 날짜 end

		Map<String, Integer> today_info =  dateData.today_info(dateData);
		List<DateData> dateList = new ArrayList<DateData>();
		
		//실질적인 달력 데이터 리스트에 데이터 삽입 시작.
		//일단 시작 인덱스까지 아무것도 없는 데이터 삽입
		for(int i=1; i<today_info.get("start"); i++){
			calendarData= new DateData(null, null, null, null);
			dateList.add(calendarData);
		}
		
		//날짜 삽입
		for (int i = today_info.get("startDay"); i <= today_info.get("endDay"); i++) {
			if(i==today_info.get("today")){
				calendarData= new DateData(String.valueOf(dateData.getYear()), String.valueOf(dateData.getMonth()), String.valueOf(i), "today");
				//오늘 먹은 것에 대한 정보를 위한 삽입
				model.addAttribute("todayyear", calendarData.year);
				model.addAttribute("todaymonth", calendarData.month);
				model.addAttribute("todaydate", calendarData.date);
			}else{
				calendarData= new DateData(String.valueOf(dateData.getYear()), String.valueOf(dateData.getMonth()), String.valueOf(i), "normal_date");
			}
			dateList.add(calendarData);
		}

		//달력 빈곳 빈 데이터로 삽입
		int index = 7-dateList.size()%7;
		
		if(dateList.size()%7!=0){
			
			for (int i = 0; i < index; i++) {
				calendarData= new DateData(null, null, null, null);
				dateList.add(calendarData);
			}
		}
		//System.out.println(dateList);
		
		//해당 멤버의 총 먹은 음식 가져오기
		List<Schedule> scheduleList = scheduleDao.findByMid(memId, 0);
		
		//달력 1달마다 있는 댓글리스트
		List<Reply> replyList = replyDao.selectInCalendar(memId, 
				Long.parseLong(dateData.getYear()), Long.parseLong(dateData.getMonth()));
		model.addAttribute("replyList", replyList);
		//System.out.println("댓글 확인 "+dateData.getYear() +" "+ dateData.getMonth());
		//배열에 담음
		model.addAttribute("dateList", dateList);		//날짜 데이터 배열
		model.addAttribute("today_info", today_info);
		model.addAttribute("scheduleList", scheduleList);
		
		//랜덤난수 하나 보내기
		List<Long> memberNumList = memberDao.listingId();
		int rand = (int)(Math.random()*memberNumList.size());
		model.addAttribute("random", rand+1);
		
		//접속할 멤버 id의 캘린더로
		model.addAttribute("memId", memId);
		return "calendar/calendar";
	}

	//댓글 추가후
	@RequestMapping(value = "calendar/calendar/{id}", method = RequestMethod.POST)
	public String calendarPost(@PathVariable("id") Long memId, Model model,
			HttpServletRequest request, DateData dateData,	HttpSession session,
			@ModelAttribute("replyCommnad") ReplyCommand repCommand,
			Errors errors) {
		
		//비어있는지 체크
		new ReplyValidator().validate(repCommand, errors);
		if(errors.hasErrors()) {
			return "/bulletin/bulletinDetail";
		}
		
		long userId;
		String usermail;
		try {
			AuthInfo authInfo=(AuthInfo) session.getAttribute("authInfo");
	        usermail = authInfo.getEmail();
	        userId = memberDao.selectByEmail(usermail).getId();
		} catch(Exception e) {
			return "redirect:/main";
		}
		
		Calendar cal = Calendar.getInstance();
		DateData calendarData;
		//검색 날짜
		if(dateData.getDate().equals("")&&dateData.getMonth().equals("")){
			dateData = new DateData(String.valueOf(cal.get(Calendar.YEAR)),
					String.valueOf(cal.get(Calendar.MONTH)),
					String.valueOf(cal.get(Calendar.DATE)),null);
		}
		//검색 날짜 end

		Map<String, Integer> today_info =  dateData.today_info(dateData);
		List<DateData> dateList = new ArrayList<DateData>();
		
		//실질적인 달력 데이터 리스트에 데이터 삽입 시작.
		//일단 시작 인덱스까지 아무것도 없는 데이터 삽입
		for(int i=1; i<today_info.get("start"); i++){
			calendarData= new DateData(null, null, null, null);
			dateList.add(calendarData);
		}
		
		//날짜 삽입
		for (int i = today_info.get("startDay"); i <= today_info.get("endDay"); i++) {
			if(i==today_info.get("today")){
				calendarData= new DateData(String.valueOf(dateData.getYear()), String.valueOf(dateData.getMonth()), String.valueOf(i), "today");
				//오늘 먹은 것에 대한 정보를 위한 삽입
				model.addAttribute("todayyear", calendarData.year);
				model.addAttribute("todaymonth", calendarData.month);
				model.addAttribute("todaydate", calendarData.date);
			}else{
				calendarData= new DateData(String.valueOf(dateData.getYear()), String.valueOf(dateData.getMonth()), String.valueOf(i), "normal_date");
			}
			dateList.add(calendarData);
		}

		//달력 빈곳 빈 데이터로 삽입
		int index = 7-dateList.size()%7;
		
		if(dateList.size()%7!=0){
			
			for (int i = 0; i < index; i++) {
				calendarData= new DateData(null, null, null, null);
				dateList.add(calendarData);
			}
		}
		//System.out.println(dateList);
		
		//해당 멤버의 총 먹은 음식 가져오기
		List<Schedule> scheduleList = scheduleDao.findByMid(memId, 0);
		
		//배열에 담음
		model.addAttribute("dateList", dateList);		//날짜 데이터 배열
		model.addAttribute("today_info", today_info);
		model.addAttribute("scheduleList", scheduleList);
		
		//접속할 멤버 id의 캘린더로
		model.addAttribute("memId", memId);
		
		//댓글창 submit시, validator발동
		long findinYear = Long.parseLong(dateData.getYear());
		long findinMonth = Long.parseLong(dateData.getMonth());
		repCommand.setWriter(usermail);
		repCommand.setReciever(memId);
		repCommand.setRecieve_year(findinYear);
		repCommand.setRecieve_month(findinMonth);
		
		model.addAttribute("repCommand", repCommand);
	    replyService.regist(repCommand);

	    List<Reply> replyList = replyDao.selectInCalendar(memId, findinYear, findinMonth);
	    //댓글목록도 추가
	  	model.addAttribute("replyList", replyList);
	  	//System.out.println("댓글 확인 "+dateData.getYear() +" "+ dateData.getMonth());
		
	  //랜덤난수 하나 보내기
	  	List<Long> memberNumList = memberDao.listingId();
	  	int rand = (int)(Math.random()*memberNumList.size());
	  	model.addAttribute("random", rand+1);
	  		
		return "calendar/calendar";
	}
	
	@GetMapping("/calendar/calendar/RandomMmbr")
	public String findRandomMbr() {
		return "calendar/calendar";
	}
	
	//댓글 수정 전.후
	/*@GetMapping("/bulletin/editReply")
	public String handleEditGet(
			@ModelAttribute("command") ReplyCommand repCommand,
			Model model, @RequestParam("replyNum") long x) {
	
		Reply reuploadReply=replyDao.selectById(x);
		repCommand.setReply_content(reuploadReply.getReply_content());
	//	repCommand.setBoard_id(reuploadReply.getBoard_id());
		repCommand.setWriter(reuploadReply.getWriter());
		repCommand.setParent_id(reuploadReply.getParent_id());	
		
		System.out.println(x);
		//editer을 통해, 해당 글이 수정인지 아닌지 판단
		boolean editer=true;
		model.addAttribute(editer);
			
		return "/bulletin/bulletinDetail";
	}*/
}


/*
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
public class CalendarController {

	
	@RequestMapping(value = "calendar/calendar", method = RequestMethod.GET)
	public String calendar(Model model, HttpServletRequest request, DateData dateData){
		
		Calendar cal = Calendar.getInstance();
		DateData calendarData;
		//검색 날짜
		if(dateData.getDate().equals("")&&dateData.getMonth().equals("")){
			dateData = new DateData(String.valueOf(cal.get(Calendar.YEAR)),String.valueOf(cal.get(Calendar.MONTH)),String.valueOf(cal.get(Calendar.DATE)),null);
		}
		//검색 날짜 end

		Map<String, Integer> today_info =  dateData.today_info(dateData);
		List<DateData> dateList = new ArrayList<DateData>();
		
		//실질적인 달력 데이터 리스트에 데이터 삽입 시작.
		//일단 시작 인덱스까지 아무것도 없는 데이터 삽입
		for(int i=1; i<today_info.get("start"); i++){
			calendarData= new DateData(null, null, null, null);
			dateList.add(calendarData);
		}
		
		//날짜 삽입
		for (int i = today_info.get("startDay"); i <= today_info.get("endDay"); i++) {
			if(i==today_info.get("today")){
				calendarData= new DateData(String.valueOf(dateData.getYear()), String.valueOf(dateData.getMonth()), String.valueOf(i), "today");
			}else{
				calendarData= new DateData(String.valueOf(dateData.getYear()), String.valueOf(dateData.getMonth()), String.valueOf(i), "normal_date");
			}
			dateList.add(calendarData);
		}

		//달력 빈곳 빈 데이터로 삽입
		int index = 7-dateList.size()%7;
		
		if(dateList.size()%7!=0){
			
			for (int i = 0; i < index; i++) {
				calendarData= new DateData(null, null, null, null);
				dateList.add(calendarData);
			}
		}
		System.out.println(dateList);
		
		//배열에 담음
		model.addAttribute("dateList", dateList);		//날짜 데이터 배열
		model.addAttribute("today_info", today_info);
		return "calendar/calendar";
	}
}
*/