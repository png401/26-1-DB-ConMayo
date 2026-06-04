package main;

// Main.java
import db.DatabaseConnector;
import db.TransactionManager;
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
import view.SeatView;
import view.VenueView;
import view.AdminView;

public class Main {
    public static void main(String[] args) {

        // ① TransactionManager 생성 — conn 없이 생성 (begin()에서 getConnection() 호출)
        TransactionManager tm = new TransactionManager();

        // ② DAOImpl 생성 — conn 주입 없음 (매번 getConnection() 호출)
        // 인터페이스 타입으로 선언 -> Service는 구현체를 모름
        MemberDAO memberDAO                   = new MemberDAOImpl();
        VenueDAO venueDAO                     = new VenueDAOImpl();
        PerformanceDAO performanceDAO         = new PerformanceDAOImpl();
        SeatDAO seatDAO                       = new SeatDAOImpl();
        PerformanceSeatDAO perfSeatDAO        = new PerformanceSeatDAOImpl();
        BookingDAO bookingDAO                 = new BookingDAOImpl();
        ReviewDAO reviewDAO                   = new ReviewDAOImpl();
        CancellationDAO cancellationDAO       = new CancellationDAOImpl();

        // ③ Service 생성 — DAO 주입
        // BookingService는 취소 트랜잭션을 위해 cancellationDAO도 같이 주입
        // ReviewService는 BOOKED 상태 체크를 위해 bookingDAO도 같이 주입
        MemberService memberService             = new MemberService(memberDAO);
        VenueService venueService               = new VenueService(venueDAO);
        PerformanceService performanceService   = new PerformanceService(performanceDAO);
        SeatService seatService                 = new SeatService(seatDAO);
        PerformanceSeatService perfSeatService  = new PerformanceSeatService(perfSeatDAO);
        BookingService bookingService           = new BookingService(bookingDAO, cancellationDAO, tm);
        ReviewService reviewService             = new ReviewService(reviewDAO, bookingDAO);

        // ④ View 생성 — 콘솔 입출력 담당
        // SeatPanel은 SeatController 안에서 직접 new 함 (Swing이라 별도)
        MemberView memberView           = new MemberView();
        PerformanceView performanceView = new PerformanceView();
        BookingView bookingView         = new BookingView();
        ReviewView reviewView           = new ReviewView();
        AdminView adminView             = new AdminView(); // 관리자 전용 뷰
        VenueView venueView				= new VenueView();

        // ⑤ Controller 생성 — Service + View 주입
        // MemberController는 블랙리스트(관리자 기능)도 담당 -> adminView도 주입
        // PerformanceController는 공연 등록/수정/삭제(관리자) -> adminView도 주입
        PerformanceController performanceController   = new PerformanceController(performanceService, performanceView, adminView, perfSeatService);
        BookingController bookingController           = new BookingController(bookingService, bookingView);
        SeatController seatController                 = new SeatController(seatService, bookingController);
        ReviewController reviewController             = new ReviewController(reviewService, reviewView);
        SeatView seatView                             = new SeatView(seatController, bookingController, reviewController);
        VenueController venueController               = new VenueController(venueService, adminView, venueView);
        PerformanceSeatController perfSeatController  = new PerformanceSeatController(perfSeatService, performanceService, adminView);
        MemberController memberController             = new MemberController(memberService, memberView, adminView,
                                                            performanceController, bookingController, perfSeatController, venueController, seatController, seatView);
        memberController.setReviewController(reviewController); // 추가

        // ⑥ 프로그램 시작 — 로그인 화면부터 시작
        // 로그인 후 USER면 일반 메뉴, ADMIN이면 관리자 메뉴로 분기
        memberController.start();
    }
}