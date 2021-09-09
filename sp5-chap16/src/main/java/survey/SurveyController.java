package survey;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller	//스프링mvc에서 컨트롤러로 사용됨
@RequestMapping("/survey") //"/survey"경로 요청에 대해 실행하는 메서드
public class SurveyController {
	@GetMapping	//url뒤에 surveyForm붙임으로써 진입가능
	public String form(Model model) {
		List<Question> questions= createQuestions();
		//해당 모델에 데이터의 이름과 그 값, 즉 "questins"와 questions를 전달
		//이는 그대로 뷰로 전달된다.(뷰에 모델이 전달됨)
		model.addAttribute("questions", questions);
		return "survey/surveyForm";
	}
	
	private List<Question> createQuestions() {
		Question q1= new Question("당신의 역할은?", Arrays.asList("서버", "프론트",
				"풀스택"));
		Question q2= new Question("주요 개발도구는?", Arrays.asList("eclipse",
				"인텔리j", "서브라임"));
		Question q3= new Question("하고픈 말은?");
		
		return Arrays.asList(q1, q2, q3);
		}
	
	//커맨드객체 ansData란 이름의 AnsweredData를 받아야 진입가능
	@PostMapping
	public String submit(@ModelAttribute("ansData") AnsweredData data) {
		return "survey/submitted";
	}
}
