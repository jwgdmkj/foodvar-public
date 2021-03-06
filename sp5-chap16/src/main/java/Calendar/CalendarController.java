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
		//?????? ??????
		if(dateData.getDate().equals("")&&dateData.getMonth().equals("")){
			dateData = new DateData(String.valueOf(cal.get(Calendar.YEAR)),
					String.valueOf(cal.get(Calendar.MONTH)),
					String.valueOf(cal.get(Calendar.DATE)),null);
		}
		//?????? ?????? end

		Map<String, Integer> today_info =  dateData.today_info(dateData);
		List<DateData> dateList = new ArrayList<DateData>();
		
		//???????????? ?????? ????????? ???????????? ????????? ?????? ??????.
		//?????? ?????? ??????????????? ???????????? ?????? ????????? ??????
		for(int i=1; i<today_info.get("start"); i++){
			calendarData= new DateData(null, null, null, null);
			dateList.add(calendarData);
		}
		
		//?????? ??????
		for (int i = today_info.get("startDay"); i <= today_info.get("endDay"); i++) {
			if(i==today_info.get("today")){
				calendarData= new DateData(String.valueOf(dateData.getYear()), String.valueOf(dateData.getMonth()), String.valueOf(i), "today");
				//?????? ?????? ?????? ?????? ????????? ?????? ??????
				model.addAttribute("todayyear", calendarData.year);
				model.addAttribute("todaymonth", calendarData.month);
				model.addAttribute("todaydate", calendarData.date);
			}else{
				calendarData= new DateData(String.valueOf(dateData.getYear()), String.valueOf(dateData.getMonth()), String.valueOf(i), "normal_date");
			}
			dateList.add(calendarData);
		}

		//?????? ?????? ??? ???????????? ??????
		int index = 7-dateList.size()%7;
		
		if(dateList.size()%7!=0){
			
			for (int i = 0; i < index; i++) {
				calendarData= new DateData(null, null, null, null);
				dateList.add(calendarData);
			}
		}
		//System.out.println(dateList);
		
		//?????? ????????? ??? ?????? ?????? ????????????
		List<Schedule> scheduleList = scheduleDao.findByMid(memId, 0);
		
		//?????? 1????????? ?????? ???????????????
		List<Reply> replyList = replyDao.selectInCalendar(memId, 
				Long.parseLong(dateData.getYear()), Long.parseLong(dateData.getMonth()));
		model.addAttribute("replyList", replyList);
		//System.out.println("?????? ?????? "+dateData.getYear() +" "+ dateData.getMonth());
		//????????? ??????
		model.addAttribute("dateList", dateList);		//?????? ????????? ??????
		model.addAttribute("today_info", today_info);
		model.addAttribute("scheduleList", scheduleList);
		
		//???????????? ?????? ?????????
		List<Long> memberNumList = memberDao.listingId();
		int rand = (int)(Math.random()*memberNumList.size());
		model.addAttribute("random", rand+1);
		
		//????????? ?????? id??? ????????????
		model.addAttribute("memId", memId);
		return "calendar/calendar";
	}

	//?????? ?????????
	@RequestMapping(value = "calendar/calendar/{id}", method = RequestMethod.POST)
	public String calendarPost(@PathVariable("id") Long memId, Model model,
			HttpServletRequest request, DateData dateData,	HttpSession session,
			@ModelAttribute("replyCommnad") ReplyCommand repCommand,
			Errors errors) {
		
		//??????????????? ??????
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
		//?????? ??????
		if(dateData.getDate().equals("")&&dateData.getMonth().equals("")){
			dateData = new DateData(String.valueOf(cal.get(Calendar.YEAR)),
					String.valueOf(cal.get(Calendar.MONTH)),
					String.valueOf(cal.get(Calendar.DATE)),null);
		}
		//?????? ?????? end

		Map<String, Integer> today_info =  dateData.today_info(dateData);
		List<DateData> dateList = new ArrayList<DateData>();
		
		//???????????? ?????? ????????? ???????????? ????????? ?????? ??????.
		//?????? ?????? ??????????????? ???????????? ?????? ????????? ??????
		for(int i=1; i<today_info.get("start"); i++){
			calendarData= new DateData(null, null, null, null);
			dateList.add(calendarData);
		}
		
		//?????? ??????
		for (int i = today_info.get("startDay"); i <= today_info.get("endDay"); i++) {
			if(i==today_info.get("today")){
				calendarData= new DateData(String.valueOf(dateData.getYear()), String.valueOf(dateData.getMonth()), String.valueOf(i), "today");
				//?????? ?????? ?????? ?????? ????????? ?????? ??????
				model.addAttribute("todayyear", calendarData.year);
				model.addAttribute("todaymonth", calendarData.month);
				model.addAttribute("todaydate", calendarData.date);
			}else{
				calendarData= new DateData(String.valueOf(dateData.getYear()), String.valueOf(dateData.getMonth()), String.valueOf(i), "normal_date");
			}
			dateList.add(calendarData);
		}

		//?????? ?????? ??? ???????????? ??????
		int index = 7-dateList.size()%7;
		
		if(dateList.size()%7!=0){
			
			for (int i = 0; i < index; i++) {
				calendarData= new DateData(null, null, null, null);
				dateList.add(calendarData);
			}
		}
		//System.out.println(dateList);
		
		//?????? ????????? ??? ?????? ?????? ????????????
		List<Schedule> scheduleList = scheduleDao.findByMid(memId, 0);
		
		//????????? ??????
		model.addAttribute("dateList", dateList);		//?????? ????????? ??????
		model.addAttribute("today_info", today_info);
		model.addAttribute("scheduleList", scheduleList);
		
		//????????? ?????? id??? ????????????
		model.addAttribute("memId", memId);
		
		//????????? submit???, validator??????
		long findinYear = Long.parseLong(dateData.getYear());
		long findinMonth = Long.parseLong(dateData.getMonth());
		repCommand.setWriter(usermail);
		repCommand.setReciever(memId);
		repCommand.setRecieve_year(findinYear);
		repCommand.setRecieve_month(findinMonth);
		
		model.addAttribute("repCommand", repCommand);
	    replyService.regist(repCommand);

	    List<Reply> replyList = replyDao.selectInCalendar(memId, findinYear, findinMonth);
	    //??????????????? ??????
	  	model.addAttribute("replyList", replyList);
	  	//System.out.println("?????? ?????? "+dateData.getYear() +" "+ dateData.getMonth());
		
	  //???????????? ?????? ?????????
	  	List<Long> memberNumList = memberDao.listingId();
	  	int rand = (int)(Math.random()*memberNumList.size());
	  	model.addAttribute("random", rand+1);
	  		
		return "calendar/calendar";
	}
	
	@GetMapping("/calendar/calendar/RandomMmbr")
	public String findRandomMbr() {
		return "calendar/calendar";
	}
	
	//?????? ?????? ???.???
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
		//editer??? ??????, ?????? ?????? ???????????? ????????? ??????
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
		//?????? ??????
		if(dateData.getDate().equals("")&&dateData.getMonth().equals("")){
			dateData = new DateData(String.valueOf(cal.get(Calendar.YEAR)),String.valueOf(cal.get(Calendar.MONTH)),String.valueOf(cal.get(Calendar.DATE)),null);
		}
		//?????? ?????? end

		Map<String, Integer> today_info =  dateData.today_info(dateData);
		List<DateData> dateList = new ArrayList<DateData>();
		
		//???????????? ?????? ????????? ???????????? ????????? ?????? ??????.
		//?????? ?????? ??????????????? ???????????? ?????? ????????? ??????
		for(int i=1; i<today_info.get("start"); i++){
			calendarData= new DateData(null, null, null, null);
			dateList.add(calendarData);
		}
		
		//?????? ??????
		for (int i = today_info.get("startDay"); i <= today_info.get("endDay"); i++) {
			if(i==today_info.get("today")){
				calendarData= new DateData(String.valueOf(dateData.getYear()), String.valueOf(dateData.getMonth()), String.valueOf(i), "today");
			}else{
				calendarData= new DateData(String.valueOf(dateData.getYear()), String.valueOf(dateData.getMonth()), String.valueOf(i), "normal_date");
			}
			dateList.add(calendarData);
		}

		//?????? ?????? ??? ???????????? ??????
		int index = 7-dateList.size()%7;
		
		if(dateList.size()%7!=0){
			
			for (int i = 0; i < index; i++) {
				calendarData= new DateData(null, null, null, null);
				dateList.add(calendarData);
			}
		}
		System.out.println(dateList);
		
		//????????? ??????
		model.addAttribute("dateList", dateList);		//?????? ????????? ??????
		model.addAttribute("today_info", today_info);
		return "calendar/calendar";
	}
}
*/