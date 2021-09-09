package Board;

public class ReplyCommand {
    private Long parent_id;	//부모 id(대댓글용.)
    private Long depth;
    private String reply_content;
    private String writer;
    private Long reciever;
    private Long recieve_month;
    private Long recieve_year;
    
	public String getReply_content() { return reply_content; }
	public void setReply_content(String reply_content) 
	{	this.reply_content = reply_content; }
	
	public void setWriter(String writer) { this.writer= writer; }
	public String getWriter() { return writer; }
	  
	public Long getParent_id() { return parent_id; }
	public void setParent_id(Long parent_id) { this.parent_id = parent_id; }
	  
	public Long getDepth() { return depth; } 
	public void setDepth(Long depth) { this.depth = depth; } 
	
	public Long getReciever() { return reciever; }
    public void setReciever(Long reciever) {this.reciever = reciever; }
    public Long getRecieve_year() { return recieve_year; }
    public void setRecieve_year(Long recieve_year) {this.recieve_year = recieve_year; }
    public Long getRecieve_month() { return recieve_month; }
    public void setRecieve_month(Long recieve_month) {this.recieve_month = recieve_month; }
}
