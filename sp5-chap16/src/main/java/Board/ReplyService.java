package Board;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;

public class ReplyService {
	private ReplyDao replyDao;

	public void setReplyDao(ReplyDao replyDao) {
		this.replyDao = replyDao;
	}
	
	@Transactional //트랜잭션 범위에서 설정할 메서드에 붙임(여러 쿼리가 한 번에 쓰일 때, 그 범위에서 실행될 메서드에)
	public Long regist(ReplyCommand com) {
		
		Reply newReply = new Reply(	com.getWriter(), com.getReply_content(),
				LocalDateTime.now(), com.getReciever(), com.getRecieve_year(),
				com.getRecieve_month());
		
		//댓글에 작성자, boardid, parentid 추가(대댓글은 나중에 수정)
		long zeronum=0;
		newReply.setParent_id(zeronum);
		newReply.setDepth(zeronum);
//		System.out.println(newReply.getBoard_id());
//		System.out.println(newReply.getWriter());
		
		replyDao.regReply(newReply);
		
		System.out.println(com.getReply_content());
		return newReply.getReply_id();
	}
	
//	public int regReply(Map<String, Object> paramMap) {
		
//		return replyDao.regReply(paramMap);
//	}
	
//	public List<Reply> getReplyList(Map<String, Object> paramMap) {
//		List<Reply> boardReplyList = replyDao.getReplyList(paramMap);
		 
        //msyql 에서 계층적 쿼리가 어려우니 여기서 그냥 해결하자
 
        //부모
 //       List<Reply> boardReplyListParent = new ArrayList<Reply>();
        //자식
  //      List<Reply> boardReplyListChild = new ArrayList<Reply>();
        //통합
  //      List<Reply> newBoardReplyList = new ArrayList<Reply>();
 
        //1.부모와 자식 분리
   //     for(Reply reply: boardReplyList){
    //        if(reply.getDepth().equals("0")){
     //           boardReplyListParent.add(reply);
      //      }else{
       //         boardReplyListChild.add(reply);
        //    }
       // }
 
        //2.부모를 돌린다.
      //  for(Reply boardReplyParent: boardReplyListParent){
            //2-1. 부모는 무조건 넣는다.
       //     newBoardReplyList.add(boardReplyParent);
            //3.자식을 돌린다.
        //    for(Reply boardReplyChild: boardReplyListChild){
                //3-1. 부모의 자식인 것들만 넣는다.
          //      if(boardReplyParent.getReply_id().equals(boardReplyChild.getParent_id())){
            //        newBoardReplyList.add(boardReplyChild);
             //   }
           // }
      //  }
        //정리한 list return
       // return newBoardReplyList;
	//}
}
