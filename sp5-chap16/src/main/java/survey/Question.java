package survey;

import java.util.Collections;
import java.util.List;

public class Question {
	private String title;
	private List<String> options;
	
	public Question(String title, List<String> options) {
		this.title= title;
		this.options=options;
	}
	
	//string만을 인자로 받으면, 위의 question문을 실행함
	public Question(String title) {
		this(title, Collections.<String>emptyList());
	}
	
	public String getTitle() { return title; }
	public List<String> getOptions() { return options; }
	public boolean isChoice() { return options != null && !options.isEmpty(); }
}
