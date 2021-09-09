package Board;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ReplyUpdateAction {
	private ReplyDao replyDao;
	public void setReplyDao(ReplyDao replyDao) {
		this.replyDao= replyDao;
	}
	
	@PostMapping("/bulletin/ReplyUpdateAction")
	public String execute(HttpServletRequest request,
            HttpServletResponse response) throws Exception {

		// 파라미터를 가져온다.
		request.setCharacterEncoding("utf-8");
		
        long comment_num = Integer.parseInt(request.getParameter("comment_num"));
        String comment_content = request.getParameter("comment_content");
//		System.out.print(comment_content);
        Reply updatedReply = replyDao.selectById(comment_num);
        
        boolean result= 
        		replyDao.updateComment(updatedReply, comment_content);
        
        response.setContentType("text/html;charset=euc-kr");
        PrintWriter out = response.getWriter();
        
     // 정상적으로 댓글을 수정했을경우 1을 전달한다.
        if(result) out.println("1");

        out.close();
        
        return null;
	}
}

/*
댓글 수정 누를 시, ReplyUpdateFormAction.co?num="+comment_num 라는 새 창을 띄움.
이 새 창은, ReplyUpdateFormAction 클래스의 ReplyUpdate 메소드에서 GET으로 관리
num 파라미터를 얻은 다음, 해당 num에 해당하는 reply를 찾아 model로 bulletin/CommentUpdateForm로 넘김.
CommentUpdateForm은, post로 ReplyUpdateAction을 열어, 여기에서 execute를 실행.
전달받은 param으로부터 comment_num과 name을 받은 다음, 이를 update함.
*/