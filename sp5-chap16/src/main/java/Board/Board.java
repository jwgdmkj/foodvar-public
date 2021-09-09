package Board;
import java.time.LocalDateTime;
public class Board {

	private Long num;
	private Long recommend;
	private Long watcher;
	private Long reply;
	
	private LocalDateTime registerDateTime;
	private String title;
	private String content;
	private String writer;
	
	public Board(String writer, 
			String title, String content, LocalDateTime regTime) {
		this.writer=writer;
		this.content=content;
		this.registerDateTime=regTime;
		this.title=title;
	}
	
	void setNum(Long num) {
		this.num=num;
	}
	void setWriter(String writer) {
		this.writer=writer;
	}
	void setWatcher(Long watcher) {
		this.watcher=watcher;
	}
	void setRecommend(Long recommend) {
		this.recommend=recommend;
	}
	void setReply(Long reply) {
		this.reply=reply;
	}
	void setContent(String content) {
		this.content=content;
	}
	void setTitle(String title) {
		this.title=title;
	}
	
	public Long getNum() {
		return num;
	}
	public String getWriter() {
		return writer;
	}
	public Long getRecommend() {
		return recommend;
	}
	public Long getReply() {
		return reply;
	}
	public Long getWatcher() {
		return watcher;
	}
	public String getTitle() {
		return title;
	}
	public String getContent() {
		return content;
	}
	public LocalDateTime getRegisterDateTime() {
		return registerDateTime;
	}
	
	/*
	 * public void changePassword(String oldPassword, String newPassword) { if
	 * (!password.equals(oldPassword)) throw new WrongIdPasswordException();
	 * this.password = newPassword; }
	 * 
	 * public boolean matchPassword(String password) { return
	 * this.password.equals(password); }
	 */
}
