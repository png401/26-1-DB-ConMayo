# 26-1-DB-ConMayo
26년 1학기 데이터베이스 수업 공연예매 관리 프로젝트 

```mermaid
classDiagram
    direction TOP_DOWN

    class DatabaseConnector {
        +getConnection() Connection
        +reset() void
    }

    class MainApplication {
        +main(String[] args) void
    }

    %% Controller Layer
    class MemberController {
        -MemberService memberService
        -MemberView memberView
        +register() void
        +login() MemberDTO
    }
    class BookingController {
        -BookingService bookingService
        -BookingView bookingView
        +book() void
        +handleCancel() boolean
        +showMyBookings() void
    }
    class PerformanceController {
        -PerformanceService performanceService
        -PerformanceView performanceView
        +showList() int
    }

    %% Service Layer
    class MemberService {
        -MemberDAO memberDAO
        +register() void
        +login() MemberDTO
        +isBlacklisted() boolean
    }
    class BookingService {
        -BookingDAO bookingDAO
        +book() void
        +cancel() boolean
    }
    class PerformanceService {
        -PerformanceDAO performanceDAO
        +getAllPerformances() List[PerformanceDTO]
        +getByCategory() List[PerformanceDTO]
    }

    %% DAO / Repository Layer
    class MemberDAOImpl {
        +insert() void
        +login() MemberDTO
    }
    class BookingDAOImpl {
        +insert() void
        +updateStatus() void
        +isMemberBlacklisted() boolean
    }
    class PerformanceDAOImpl {
        +findAll() List[PerformanceDTO]
        +findByCategory() List[PerformanceDTO]
    }

    %% DTO / Model Layer
    class MemberDTO {
        +String memberId
        +String passwd
        +String memberName
        +LocalDateTime blacklistUntil
    }
    class BookingDTO {
        +int bookingId
        +String memberId
        +int performanceSeatId
        +String bookingStatus
    }
    class PerformanceDTO {
        +int performanceId
        +String title
        +String salesStatus
        +int remainingSeats
    }

    %% Relationships
    MainApplication --> MemberController : 구동
    MainApplication --> BookingController : 구동

    MemberController --> MemberService : 요청 처리
    BookingController --> BookingService : 요청 처리
    PerformanceController --> PerformanceService : 요청 처리

    MemberService --> MemberDAOImpl : DB 접근
    BookingService --> BookingDAOImpl : DB 접근
    PerformanceService --> PerformanceDAOImpl : DB 접근

    MemberDAOImpl --> DatabaseConnector : Connection 획득
    BookingDAOImpl --> DatabaseConnector : Connection 획득
    PerformanceDAOImpl --> DatabaseConnector : Connection 획득

    MemberDAOImpl ..> MemberDTO : 데이터 전달
    BookingDAOImpl ..> BookingDTO : 데이터 전달
    PerformanceDAOImpl ..> PerformanceDTO : 데이터 전달
