package Calendar;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
import org.springframework.web.bind.annotation.ResponseBody;

import spring.AuthInfo;
import spring.MemberDao;

@Controller
public class FoodSearchController {
	
	private FoodDao foodDao; 
	public void setsearchfoodservice(FoodDao foodDao) {
		this.foodDao = foodDao; 
	}
	private MemberDao memberDao;
	public void setMemberdao(MemberDao memberDao) {
		this.memberDao = memberDao;
	}
	private ScheduleDao scheduleDao;
	public void setScheduleDao(ScheduleDao scheduleDao) {
		this.scheduleDao = scheduleDao;
	}
	 
	
	@RequestMapping(value = "calendar/searchfood", method = RequestMethod.GET)
	public String searchfood(@ModelAttribute("searchfoodObject") searchfoodObject searchfood,
			Model model) {

	  	return "calendar/searchfood"; 
	}
	
	@RequestMapping(value = "calendar/searchfood", method = RequestMethod.POST)
	public String searchfood(@ModelAttribute("searchfoodObject") searchfoodObject searchfood,
			Model model, Errors errors) {
		new searchfoodValidator().validate(searchfood, errors);
		if(errors.hasErrors()) {
			return "calendar/searchfood";
		}
		
		List<Food> foodlist = foodDao.finder(searchfood);
		
	  	model.addAttribute("foodlist", foodlist);
	  	return "calendar/searchfood"; 
	}
	
	@PostMapping("/calendar/RegistFood")
	public String execute(HttpServletRequest request,
	           HttpServletResponse response) throws Exception {

	    //long food_num = Integer.parseInt(request.getParameter("food_num"));
	    String food_num = request.getParameter("food_num");
	    Food food = foodDao.findbyNum(food_num);
	    
	    response.setContentType("text/html;charset=euc-kr");
	    PrintWriter out = response.getWriter();
	    
	    boolean result = foodDao.registfoodIn(food);
	    if(result) out.println("1");

	    out.close();
	    return null;
	}
	
	///////////////////////////////////////////////
	//????????? ??? ????????? ????????? ???, ??????
	@RequestMapping(value = "/calendar/calendar/"
			+ "{memId}/scheduleInsertForm", method = RequestMethod.GET)
	public String registerWindow(@PathVariable("memId") Long memId,
			HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute("searchfoodObject") searchfoodObject searchfood,
			HttpSession session, Model model) throws Exception {
		
		//??? ????????? ????????? ????????? ??????
		long yearNum = Integer.parseInt(request.getParameter("year"));
		long monthNum = Integer.parseInt(request.getParameter("month"));
		long dateNum = Integer.parseInt(request.getParameter("date"));
		
		model.addAttribute("year", yearNum);
		model.addAttribute("month", monthNum+1);
		model.addAttribute("date", dateNum);
		model.addAttribute("memid", memId);
		
		//?????? ????????? ????????? ??????, ????????? ?????? ??????
		long userId;
		try {
			AuthInfo authInfo=(AuthInfo) session.getAttribute("authInfo");
	        String usermail = authInfo.getEmail();
	        userId = memberDao.selectByEmail(usermail).getId();
		} catch(Exception e) {
			return "redirect:/main";
		}
		
		List<Schedule> scheduleList_1 = scheduleDao.findByMid(memId, 1);
		//if(scheduleList_1.size() > 1)
		//	System.out.println(scheduleList_1.get(0).getMonth());
		List<Schedule> scheduleList_2 = scheduleDao.findByMid(memId, 2);
		List<Schedule> scheduleList_3 = scheduleDao.findByMid(memId, 3);
		List<Schedule> scheduleList_4 = scheduleDao.findByMid(memId, 4);
		List<Schedule> scheduleList_5 = scheduleDao.findByMid(memId, 5);
		//System.out.println("??? ???????????? "+scheduleList_1.get(0).getMonth());
		model.addAttribute("scheduleList_1",scheduleList_1);
		model.addAttribute("scheduleList_2",scheduleList_2);
		model.addAttribute("scheduleList_3",scheduleList_3);
		model.addAttribute("scheduleList_4",scheduleList_4);
		model.addAttribute("scheduleList_5",scheduleList_5);
		return "/calendar/foodRegistForm";
	}
	
	@RequestMapping(value = "/calendar/calendar/{memId}/scheduleInsertForm", 
			method = RequestMethod.POST)
	public String registerWindowbyPost(@ModelAttribute("searchfoodObject") searchfoodObject searchfood,
			@PathVariable("memId") Long memId, HttpSession session, Model model, Errors errors) throws Exception {
		
		new searchfoodValidator().validate(searchfood, errors);
		if(errors.hasErrors()) {
			return "calendar/foodRegistForm";
		}
		//System.out.println(searchfood.getDate());
		//System.out.println(searchfood.getMonth());
		//System.out.println(searchfood.getYear());
		
		List<Food> foodlist = foodDao.finder(searchfood);
		
	  	model.addAttribute("foodlist", foodlist);
	  	model.addAttribute("date", searchfood.getDate());
	  	model.addAttribute("month", searchfood.getMonth());
	  	model.addAttribute("year", searchfood.getYear());
	  	model.addAttribute("memid", memId);
	  	
	  	//????????? ?????? ??????
	  	long userId;
		try {
			AuthInfo authInfo=(AuthInfo) session.getAttribute("authInfo");
	        String usermail = authInfo.getEmail();
	        userId = memberDao.selectByEmail(usermail).getId();
		} catch(Exception e) {
			return "redirect:/main";
		}
		
	  	List<Schedule> scheduleList_1 = scheduleDao.findByMid(memId, 1);
	//	if(scheduleList_1.size() > 0)
		//	System.out.println("?????? ??????: "+scheduleList_1.get(0).getFid());
		//else
			//System.out.println("?????????");

		List<Schedule> scheduleList_2 = scheduleDao.findByMid(memId, 2);
	//	if(scheduleList_2.size() > 0)
		//	System.out.println("?????? ?????? "+scheduleList_2.get(0).getFid());
	//	else
		//	System.out.println("2??? ?????????");
		
		List<Schedule> scheduleList_3 = scheduleDao.findByMid(memId, 3);
		List<Schedule> scheduleList_4 = scheduleDao.findByMid(memId, 4);
		List<Schedule> scheduleList_5 = scheduleDao.findByMid(memId, 5);
		
		model.addAttribute("scheduleList_1",scheduleList_1);
		model.addAttribute("scheduleList_2",scheduleList_2);
		model.addAttribute("scheduleList_3",scheduleList_3);
		model.addAttribute("scheduleList_4",scheduleList_4);
		model.addAttribute("scheduleList_5",scheduleList_5);
		
	  	return "calendar/foodRegistForm";
	}
	//????????? ????????? ????????? ???, ????????? ???????????? ???
	////////////////////////////////////////////////////
	//????????? ???????????? ?????? ???????????? action
	@PostMapping("/calendar/calendar/{memId}/foodRegistAction") 
	@ResponseBody
    public Object foodRegistAction(@RequestParam Map<String, Object> paramMap
    		, HttpSession session) {
	//	System.out.println(paramMap.get("meal"));
        //?????????
        Map<String, Object> retVal = new HashMap<String, Object>();
        AuthInfo authInfo=(AuthInfo) session.getAttribute("authInfo");
        String usermail = authInfo.getEmail();
        long userId = memberDao.selectByEmail(usermail).getId();
        
        long mealNum = 0;
		if(paramMap.get("meal").equals("breakfast")) {
			mealNum = 1;
		} else if(paramMap.get("meal").equals("lunch")) {
			mealNum = 2;
		} else if(paramMap.get("meal").equals("dinner")) {
			mealNum = 3;
		} else if(paramMap.get("meal").equals("snack")) {
			mealNum = 4;
		} else if(paramMap.get("meal").equals("midnight")) {
			mealNum = 5;
		}
		String foodNum = (String) paramMap.get("foodnum");
		String mealname = (String) paramMap.get("mealname");

		double kcal = Double.parseDouble((String) paramMap.get("kcal"));

		long year = Long.parseLong((String) paramMap.get("year"));
		long month = Long.parseLong((String) paramMap.get("month"));
		long date = Long.parseLong((String) paramMap.get("date"));

        //????????????
        int result = foodDao.insertIntoMember(userId, mealNum, foodNum, year, month, date,
        		mealname, kcal);

        if(result>0){
            retVal.put("code", "OK");
        }else{
            retVal.put("code", "FAIL");
            retVal.put("message", "????????? ??????????????????.");
        }
 
        return retVal;
    }
	
	//////////////////////////////////////////////////////////////////////
	//[?????? ?????? ?????? ?????? ?????? ??????]??? ??????, ?????? ?????? ?????? ?????? ?????? ???????????? ??????,(GET)
	@RequestMapping(value = "/calendar/calendar/"
			+ "{memId}/findsamefooduser", method = RequestMethod.GET)
	public String findSamefooduser_get(@PathVariable("memId") Long memId,
			@ModelAttribute("searchfoodObject") searchfoodObject searchfood,
			HttpServletRequest request, HttpServletResponse response,
			HttpSession session, Model model) throws Exception {

		//??? ????????? ????????? ????????? ??????
		long yearNum = Integer.parseInt(request.getParameter("year"));
		long monthNum = Integer.parseInt(request.getParameter("month"));
		long dateNum = Integer.parseInt(request.getParameter("date"));
		
		model.addAttribute("year", yearNum);
		model.addAttribute("month", monthNum+1);
		model.addAttribute("date", dateNum);
		model.addAttribute("memid", memId);
		
		//?????? ????????? ????????? ??????, ????????? ?????? ??????
		long userId;
		try {
			AuthInfo authInfo=(AuthInfo) session.getAttribute("authInfo");
	        String usermail = authInfo.getEmail();
	        userId = memberDao.selectByEmail(usermail).getId();
		} catch(Exception e) {
			return "redirect:/main";
		}
		
		List<Schedule> samefoodList = scheduleDao.findByMidDate(userId, yearNum,
				monthNum+1, dateNum);
		model.addAttribute("samefoodList",samefoodList);
		
		
		return "/calendar/finduserForm";
	}
	//??? ??? ?????? ????????? ????????? ??? ????????? ?????? ???????????? ????????? ?????? ???(POST)
	@PostMapping("/calendar/calendar/{memId}/findsamefooduser")
	public String findSamefooduser_post(
			@ModelAttribute("searchfoodObject") searchfoodObject searchfood, 
			@PathVariable("memId") Long memId,
			HttpSession session, Model model, Errors errors)
			throws Exception {

		new searchfoodValidator().validate(searchfood, errors);
		if(errors.hasErrors()) {
			return "calendar/foodRegistForm";
		}
		
		//?????? ????????? ????????? ??????, ????????? ?????? ??????
		long userId;
		try {
			AuthInfo authInfo=(AuthInfo) session.getAttribute("authInfo");
			String usermail = authInfo.getEmail();
			userId = memberDao.selectByEmail(usermail).getId();
		} catch(Exception e) {
			return "redirect:/main";
		}
		
		List<Schedule> samefoodList = scheduleDao.findByMidDate(userId, searchfood.getYear(),
				searchfood.getMonth()+1, searchfood.getDate());
		model.addAttribute("samefoodList",samefoodList);
		
		//?????? ??????
		model.addAttribute("memid", memId);
		String searchfood_name = "'" + searchfood.getFoodname() + "'";
		//????????????
		Food findedfood = foodDao.finderSpecific(searchfood_name);
		
		if(findedfood != null) {
			//System.out.println("????????? " + findedfood.getID());
			//System.out.println("??? " + searchfood.getYear());
			//System.out.println("??? " + searchfood.getMonth());
			//System.out.println("??? " + searchfood.getDate());
			List<Schedule> sameuserList = scheduleDao.findByFidDate(findedfood.getID(),
	        		searchfood.getYear(),searchfood.getMonth()+1, searchfood.getDate());

	        model.addAttribute("sameuserList",sameuserList);
	     //   System.out.println(searchfood_name);
	    //    System.out.println(searchfood_name);
	    //    System.out.println("???????????? " + sameuserList.get(0).getMid());
		}

        return "/calendar/finduserForm";
	}
}
