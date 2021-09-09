package Board;

public class BoardWritingCommand {
	private String title;
	private String content;
	private String writer;
	
	public void setWriter(String writer) {
		this.writer= writer;
	}
	
	public String getWriter() {
		return writer;
	}
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
