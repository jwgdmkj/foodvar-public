package Board;

import java.time.LocalDateTime;

public class Reply {
	private Long reply_id;
    private Long parent_id;
    private Long depth;
    private String reply_content;
    private String writer;
    private LocalDateTime registerDateTime;
    private Long reciever;
    private Long recieve_year;
    private Long recieve_month;
    
    public Reply(String writer,
    		String reply_content, LocalDateTime regTime,
    		Long reciever, Long recieve_year, Long recieve_month) {
		this.writer=writer;
    	this.reply_content=reply_content;
		this.registerDateTime=regTime;
		this.recieve_month=recieve_month;
		this.recieve_year=recieve_year;
		this.reciever=reciever;
	}
    
    public Long getReply_id() {
        return reply_id;
    }
    public void setReply_id(Long reply_id) {
        this.reply_id = reply_id;
    }

    public Long getParent_id() {
        return parent_id;
    }
    public void setParent_id(Long parent_id) {
        this.parent_id = parent_id;
    }
    public Long getDepth() {
        return depth;
    }
    public void setDepth(Long depth) {
        this.depth = depth;
    }
    public String getReply_content() {
        return reply_content;
    }
    public void setReply_content(String reply_content) {
        this.reply_content = reply_content;
    }
    public String getWriter() {
        return writer;
    }
    public void setWriter(String reply_writer) {
        this.writer = reply_writer;
    }

    public LocalDateTime getRegisterDateTime() {
		return registerDateTime;
	}
    
    public void setRegisterDateTime(LocalDateTime register_datetime) {
        this.registerDateTime = register_datetime;
    }
    
    public Long getReciever() { return reciever; }
    public void setReciever(Long reciever) {this.reciever = reciever; }
    public Long getRecieve_year() { return recieve_year; }
    public void setRecieve_year(Long recieve_year) {this.recieve_year = recieve_year; }
    public Long getRecieve_month() { return recieve_month; }
    public void setRecieve_month(Long recieve_month) {this.recieve_month = recieve_month; }
}
