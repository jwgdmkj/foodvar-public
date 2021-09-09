package Board;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ReplyUpdateFormAction {
	private ReplyDao replyDao;
	public void setReplyDao(ReplyDao replyDao) {
		this.replyDao= replyDao;
	}
	
	@GetMapping("/bulletin/ReplyUpdateFormAction")
	public String ReplyUpdate(HttpServletRequest request) throws Exception {
		
		long comment_num = Integer.parseInt(request.getParameter("num"));
//		System.out.println(comment_num);
//		return "bulletin/main";
		
		Reply updatedReply = replyDao.selectById(comment_num);
//		String comment = updatedReply.getReply_content();
		
	    // 댓글 정보를 request에 세팅한다.
	    request.setAttribute("comment", updatedReply);
	    return "bulletin/CommentUpdateForm";
	}
	
	
}
