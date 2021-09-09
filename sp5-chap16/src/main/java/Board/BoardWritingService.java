package Board;

import java.time.LocalDateTime;

import org.springframework.transaction.annotation.Transactional;

public class BoardWritingService {
	private BoardDao boardDao;

	public void setBoardDao(BoardDao boardDao) {
		this.boardDao = boardDao;
	}
	
	@Transactional //트랜잭션 범위에서 설정할 메서드에 붙임(여러 쿼리가 한 번에 쓰일 때, 그 범위에서 실행될 메서드에)
	public Long regist(BoardWritingCommand com) {
		Board newBoard = new Board(com.getWriter(),
				com.getTitle(), com.getContent(), LocalDateTime.now());
		long initNum=1;
		long zeroNum=0;
		
		newBoard.setWatcher(initNum);
		newBoard.setRecommend(zeroNum);
		newBoard.setReply(zeroNum);
		boardDao.insert(newBoard);
		return newBoard.getNum();
	}
	
	@Transactional //트랜잭션 범위에서 설정할 메서드에 붙임(여러 쿼리가 한 번에 쓰일 때, 그 범위에서 실행될 메서드에)
	public Long reupload(BoardWritingCommand com, long num) {
		
		Board board= boardDao.selectById(num);
		board.setContent(com.getContent());
		board.setTitle(com.getTitle());
		
		boardDao.reupload(board, num);
		return board.getNum();
	}
	
	@Transactional
	public void delete(Board board, long num) {
		boardDao.deleteBoard(board, num);
		return;
	}
}
