package view;
import dto.PerformanceDTO;
import dto.PerformanceSeatDTO;
import dto.SalesStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;
public class PerformanceView {
    private final Scanner sc = new Scanner(System.in);
    
    // 날짜 포맷터 (상세 화면용: 연-월-일 시:분)
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    // 목록 화면용 포맷터 추가 (연-월-일만 깔끔하게 출력하기 위함)
    private final DateTimeFormatter listDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // 1. 공연 목록 출력
    public void printList(List<PerformanceDTO> list) {
    	System.out.println("\n=== 공연 목록 ==================");
        System.out.println("카테고리 (전체/콘서트/뮤지컬/스포츠)");
        System.out.println("선택 > 전체\n");
        
        if (list == null || list.isEmpty()) {
            System.out.println("등록된 공연이 없습니다.");
            return;
        }

        for (PerformanceDTO p : list) {
        	System.out.printf("[%d] %s\n", p.getPerformanceId(), p.getTitle());          
            
            String formattedDate = (p.getStartTime() != null) ? p.getStartTime().format(listDateFormatter) : "날짜미정";
            System.out.printf("%s | 공연장 ID: %d\n", formattedDate, p.getVenueId());
            System.out.printf("잔여석 %d | %s\n", p.getRemainingSeats(), p.getSalesStatus()); 
            System.out.println();
        }
    }

    // 2. 공연 상세 출력 
    public void printDetail(PerformanceDTO performance, List<PerformanceSeatDTO> seatList) {
        if (performance == null) {
            System.out.println("\n⚠ 해당 공연 정보가 존재하지 않습니다.\n");
            return;
        }
        String formattedStartTime = (performance.getStartTime() != null) ? performance.getStartTime().format(formatter) : "미정";
        
        System.out.println("\n=== 공연 상세 ==================");
        System.out.println("제목 : " + performance.getTitle());
        System.out.println("카테고리: " + performance.getCategory());
        System.out.println("일시 : " + formattedStartTime);
        System.out.println("장소 : 공연장 ID " + performance.getVenueId());
        System.out.println("러닝타임: " + performance.getRunningTime() + "분");
        System.out.printf("잔여석 : %d\n\n", performance.getRemainingSeats());
        
        if (seatList == null || seatList.isEmpty()) {
            System.out.println("등록된 좌석 가격 정보가 없습니다.\n");
        } else {
            for (PerformanceSeatDTO seat : seatList) {
                // 예: VIP 170,000원 형태로 출력 (등급 정보가 seatId나 별도 필드에 있다고 가정)
                System.out.printf("좌석 등급(ID: %d) : %,d원\n", seat.getSeatId(), seat.getPrice());
            }
            System.out.println();
        }
        
        System.out.println("1. 예매하기 0. 뒤로");
    }
    
    // 3. 카테고리 입력받기
    public String inputCategory() { 
        System.out.print("카테고리 (전체/콘서트/뮤지컬/스포츠) > ");
        return sc.nextLine();
    }
    
    // 4. 공연 ID 입력받기
    public int inputPerformanceId() { 
        System.out.print("선택 > ");
        int id = sc.nextInt();
        sc.nextLine(); // 엔터 버퍼 비우기
        return id;
    }
    
    // 5. 공연 정보 입력받기
    public PerformanceDTO inputPerformanceInfo() {
System.out.println("\n=== 공연 등록 ==================");
        
        System.out.print("공연 제목 > ");
        String title = sc.nextLine();
        
        System.out.print("카테고리 > ");
        String category = sc.nextLine();
        
        LocalDateTime startTime = null;
        while (startTime == null) {
            System.out.print("공연 일시 (형식: 2026-07-01 19:00) > ");
            String startStr = sc.nextLine();
            try {
                startTime = LocalDateTime.parse(startStr, formatter);
            } catch (DateTimeParseException e) {
                System.out.println("⚠ 날짜 형식이 올바르지 않습니다. 다시 입력해주세요.");
            }
        }
        
        System.out.print("러닝타임(분) > ");
        int runningTime = sc.nextInt();
        
        LocalDateTime bookingOpen = null;
        while (bookingOpen == null) {
            System.out.print("예매 오픈일 (형식: 2026-05-01 10:00) > ");
            String openStr = sc.nextLine();
            try {
                bookingOpen = LocalDateTime.parse(openStr, formatter);
            } catch (DateTimeParseException e) {
                System.out.println("⚠ 날짜 형식이 올바르지 않습니다. 다시 입력해주세요.");
            }
        }
        
        System.out.print("공연장 ID > ");
        int venueId = sc.nextInt();
        sc.nextLine(); // 엔터 버퍼 비우기
        
        SalesStatus salesStatus = null;
        while (salesStatus == null) {
            System.out.print("판매 상태 (OPEN/CLOSED/SOLD_OUT/COMING_SOON) > ");
            String statusStr = sc.nextLine().toUpperCase().trim();
            try {
                salesStatus = SalesStatus.valueOf(statusStr); // 문자열을 Enum 타입으로 변환
            } catch (IllegalArgumentException e) {
                System.out.println("⚠ 올바른 판매 상태를 입력해주세요.");
            }
        }
        
        PerformanceDTO performance = new PerformanceDTO();
        performance.setTitle(title);
        performance.setCategory(category);
        performance.setStartTime(startTime);      
        performance.setRunningTime(runningTime);
        performance.setBookingOpen(bookingOpen);  
        performance.setVenueId(venueId);
        performance.setSalesStatus(salesStatus);
        
        return performance; 
    }
}