package Board;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ReplyDeleteAction {
	private ReplyDao replyDao;
	public void setReplyDao(ReplyDao replyDao) {
		this.replyDao= replyDao;
	}
	
	@PostMapping("/bulletin/ReplyDeleteAction") 
	@ResponseBody
    public Object boardReplyDel(@RequestParam Map<String, Object> paramMap) {

		//System.out.println(paramMap.get("reply_id"));
		//System.out.println(paramMap.get("r_type"));
        //리턴값
        Map<String, Object> retVal = new HashMap<String, Object>();
        
        //정보입력
        int result = replyDao.delReply(paramMap);
        //System.out.println(result);
        if(result>0){
            retVal.put("code", "OK");
        }else{
            retVal.put("code", "FAIL");
            retVal.put("message", "삭제에 실패했습니다. 패스워드를 확인해주세요.");
        }
 
        return retVal;
 
    }
	@PostMapping("/bulletin/ReplyDeleteAction_sub")
	public String execute(HttpServletRequest request,
	           HttpServletResponse response) throws Exception {

	    long comment_num = Integer.parseInt(request.getParameter("comment_num"));
	    boolean result = replyDao.deleteComment(comment_num);
	        
	    response.setContentType("text/html;charset=euc-kr");
        PrintWriter out = response.getWriter();

        if(result) out.println("1");

        out.close();
        
        return null;
	}
}
