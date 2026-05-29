// Main.java
import db.DatabaseConnector;

import dao.MemberDAO;
import dao.VenueDAO;
import dao.PerformanceDAO;
import dao.SeatDAO;
import dao.PerformanceSeatDAO;
import dao.BookingDAO;
import dao.ReviewDAO;
import dao.CancellationDAO;

import daoImpl.MemberDAOImpl;
import daoImpl.VenueDAOImpl;
import daoImpl.PerformanceDAOImpl;
import daoImpl.SeatDAOImpl;
import daoImpl.PerformanceSeatDAOImpl;
import daoImpl.BookingDAOImpl;
import daoImpl.ReviewDAOImpl;
import daoImpl.CancellationDAOImpl;

import service.MemberService;
import service.VenueService;
import service.PerformanceService;
import service.SeatService;
import service.PerformanceSeatService;
import service.BookingService;
import service.ReviewService;

import controller.MemberController;
import controller.VenueController;
import controller.PerformanceController;
import controller.BookingController;
import controller.SeatController;
import controller.ReviewController;
import controller.PerformanceSeatController;

import view.MemberView;
import view.PerformanceView;
import view.BookingView;
import view.ReviewView;
import view.AdminView;

import java.sql.Connection;
import java.sql.SQLException;

public class Main {
	public static void main(String[] args) {
		try {
			// ① DB 연결 — 하나의 Connection을 모든 DAOImpl이 공유
			Connection conn = DatabaseConnector.getConnection();

			// ② DAOImpl 생성 — conn 주입
			// 인터페이스 타입으로 선언 -> Service는 구현체를 모름
			MemberDAO memberDAO                   = new MemberDAOImpl(conn);
			VenueDAO venueDAO                     = new VenueDAOImpl(conn);
			PerformanceDAO performanceDAO         = new PerformanceDAOImpl(conn);
			SeatDAO seatDAO                       = new SeatDAOImpl(conn);
			PerformanceSeatDAO perfSeatDAO        = new PerformanceSeatDAOImpl(conn);
			BookingDAO bookingDAO                 = new BookingDAOImpl(conn);
			ReviewDAO reviewDAO                   = new ReviewDAOImpl(conn);
			CancellationDAO cancellationDAO       = new CancellationDAOImpl(conn);

			// ③ Service 생성 — DAO 주입
			// BookingService는 취소 트랜잭션을 위해 cancellationDAO도 같이 주입
			// ReviewService는 BOOKED 상태 체크를 위해 bookingDAO도 같이 주입
			MemberService memberService             = new MemberService(memberDAO);
			VenueService venueService               = new VenueService(venueDAO);
			PerformanceService performanceService   = new PerformanceService(performanceDAO);
			SeatService seatService                 = new SeatService(seatDAO);
			PerformanceSeatService perfSeatService  = new PerformanceSeatService(perfSeatDAO);
			BookingService bookingService           = new BookingService(bookingDAO, cancellationDAO);
			ReviewService reviewService             = new ReviewService(reviewDAO, bookingDAO);

			// ④ View 생성 — 콘솔 입출력 담당
			// SeatPanel은 SeatController 안에서 직접 new 함 (Swing이라 별도)
			MemberView memberView           = new MemberView();
			PerformanceView performanceView = new PerformanceView();
			BookingView bookingView         = new BookingView();
			ReviewView reviewView           = new ReviewView();
			AdminView adminView             = new AdminView(); // 관리자 전용 뷰

			// ⑤ Controller 생성 — Service + View 주입
			// MemberController는 블랙리스트(관리자 기능)도 담당 -> adminView도 주입
			// PerformanceController는 공연 등록/수정/삭제(관리자) -> adminView도 주입
			MemberController memberController             = new MemberController(memberService, memberView, adminView);
			PerformanceController performanceController   = new PerformanceController(performanceService, performanceView, adminView);
			BookingController bookingController           = new BookingController(bookingService, bookingView);
			SeatController seatController                 = new SeatController(seatService, bookingController);
			ReviewController reviewController             = new ReviewController(reviewService, reviewView);
			VenueController venueController               = new VenueController(venueService, adminView);
			PerformanceSeatController perfSeatController  = new PerformanceSeatController(perfSeatService, adminView);

			// ⑥ 프로그램 시작 — 로그인 화면부터 시작
			// 로그인 후 USER면 일반 메뉴, ADMIN이면 관리자 메뉴로 분기
			memberController.start();

		} catch (SQLException e) {
			// DB 연결 실패 시 종료
			System.out.println("DB 연결 실패: " + e.getMessage());
		}
	}
}

