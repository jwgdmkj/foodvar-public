package Config;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import Board.BoardDao;
import Board.BoardWritingService;
import Board.ReplyDao;
import Board.ReplyService;
import Calendar.FoodDao;
import Calendar.ScheduleDao;
import spring.AuthService;
import spring.ChangePasswordService;
import spring.MemberDao;
import spring.MemberRegisterService;
import spring.WithdrawService;

@Configuration
@EnableTransactionManagement //트랜잭션 처리를 통해, 두 개 이상의 쿼리를 하나의 작업으로 묶어줌
public class MemberConfig {

	@Bean(destroyMethod = "close")
	public DataSource dataSource() {
		DataSource ds = new DataSource();
		ds.setDriverClassName("com.mysql.jdbc.Driver");
		

		ds.setInitialSize(2);
		ds.setMaxActive(10);
		ds.setTestWhileIdle(true);
		ds.setMinEvictableIdleTimeMillis(60000 * 3);
		ds.setTimeBetweenEvictionRunsMillis(10 * 1000);
		return ds;
	}

	//EnalbeTransactionManagement애노테이션이, @Transactional이 붙은 메서드를 트랜잭션 범위에서 실행하는 기능 활성화
	//등록된 PlatformTransactionManager빈을 이용해 트랜잭션 적용
	//트랜잭션처리를 위한 설정 완료시, 트랜잭션범위에서 실행하고픈 스프링빈객체 메서드에 @Transaction을 붙이면 됨
	//ex)ChangePasswordService의 changePasswrod()를 트랜잭션범위에서 실행하고싶다?
	@Bean
	public PlatformTransactionManager transactionManager() {
		DataSourceTransactionManager tm= new DataSourceTransactionManager();
		tm.setDataSource(dataSource());
		//dataSource 프로퍼티를 이용해, 트랜잭션 연동에 쓸 DataSource를 지정
		
		return tm;
	}

	@Bean
	public MemberDao memberDao() {
		return new MemberDao(dataSource());
	}

	@Bean
	public MemberRegisterService memberRegSvc() {
		return new MemberRegisterService(memberDao());
	}

	@Bean
	public ChangePasswordService changePwdSvc() {
		ChangePasswordService pwdSvc = new ChangePasswordService();
		pwdSvc.setMemberDao(memberDao());
		
		return pwdSvc;
	}
	
	@Bean
	public WithdrawService withdrawSvc() {
		WithdrawService withdrawSvc= new WithdrawService();
		withdrawSvc.setMemberDao(memberDao());
		
		return withdrawSvc;
	}
	
	@Bean
	public BoardDao boardDao() {
		return new BoardDao(dataSource());
	}
	@Bean
	public BoardWritingService boardWritingSvc() {
		BoardWritingService bodwriteSvc= new BoardWritingService();
		bodwriteSvc.setBoardDao(boardDao());
		
		return bodwriteSvc;
	}
	
	@Bean
	public ReplyDao replyDao() {
		return new ReplyDao(dataSource());
	}
	@Bean
	public ReplyService replySvc() {
		ReplyService replySvc= new ReplyService();
		replySvc.setReplyDao(replyDao());
		
		return replySvc;
	}
	
	//캘린더 용 Bean 객체
	@Bean
	public FoodDao foodDao() {
		return new FoodDao(dataSource());
	}
	@Bean
	public ScheduleDao scheduleDao() {
		return new ScheduleDao(dataSource());
	}
	/*
	@Bean
	public MemberPrinter memberPrinter() {
		return new MemberPrinter();
	}

	@Bean
	public MemberListPrinter listPrinter() {
		return new MemberListPrinter(memberDao(), memberPrinter());
	}

	@Bean
	public MemberInfoPrinter infoPrinter() {
		MemberInfoPrinter infoPrinter = new MemberInfoPrinter();
		infoPrinter.setMemberDao(memberDao());
		infoPrinter.setPrinter(memberPrinter());
		return infoPrinter;
	}
	*/
	
	@Bean
	public AuthService authService() {
		AuthService authService= new AuthService();
		authService.setMemberDao(memberDao());
		return authService;
	}
}
