package view;
import dto.PerformanceDTO;
import dto.PerformanceSeatDTO;
import dto.SalesStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;
import java.util.Map; 
import java.util.LinkedHashMap; 
import controller.SeatController;


public class PerformanceView {
    private final Scanner sc = new Scanner(System.in);
    
    // 날짜 포맷터 (상세 화면용: 연-월-일 시:분)
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    // 목록 화면용 포맷터 추가 (연-월-일만 깔끔하게 출력하기 위함)
    private final DateTimeFormatter listDateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    
    // [헬퍼 메서드] 숫자를 문자가 섞여도 안전하게 받아오는 기능 추가
    private int inputSecureInteger(String message) {
        while (true) {
            System.out.print(message);
            String input = sc.nextLine().trim();
            try {
                // 숫자가 아닌 모든 문자 제거 (예: "30분" -> "30", "4번" -> "4")
                String numericOnly = input.replaceAll("[^0-9]", "");
                if (numericOnly.isEmpty()) {
                    System.out.println("⚠ 숫자만 입력할 수 있습니다. 다시 입력해주세요.");
                    continue;
                }
                return Integer.parseInt(numericOnly);
            } catch (NumberFormatException e) {
                System.out.println("⚠ 올바른 형식의 숫자로 입력해주세요.");
            }
        }
    }
    
    // 1. 공연 목록 출력
    public void printList(List<PerformanceDTO> list) {
    	System.out.println("\n====== 공연 목록 ======");
        
        if (list == null || list.isEmpty()) {
            System.out.println("등록된 공연이 없습니다.");
            return;
        }

        for (PerformanceDTO p : list) {
        	System.out.printf("[%d] %s\n", p.getPerformanceId(), p.getTitle());          
            
            String formattedDate = (p.getStartTime() != null) ? p.getStartTime().format(listDateFormatter) : "날짜미정";
            String venueName = (p.getVenueName() != null) ? p.getVenueName() : "장소미정";
            System.out.printf("%s | %s\n", formattedDate, venueName);
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
        
        int totalSeats = (seatList != null) ? seatList.size() : 0;
        
        System.out.println("\n=== 공연 상세 ==================");
        System.out.println("제목 : " + performance.getTitle());
        System.out.println("카테고리: " + performance.getCategory());
        System.out.println("일시 : " + formattedStartTime);
        System.out.println("장소 : " + performance.getVenueName());
        System.out.println("러닝타임: " + performance.getRunningTime() + "분");
        System.out.printf("잔여석 : %d / %d\n\n", performance.getRemainingSeats(), totalSeats);
        
        if (seatList == null || seatList.isEmpty()) {
            System.out.println("등록된 좌석 가격 정보가 없습니다.\n");
        } else {

        	System.out.println("\n=== 좌석 가격 ==="); 
 
        	Map<String, Integer> priceMap = new LinkedHashMap<>(); 
 
        	for (PerformanceSeatDTO seat : seatList) { 
 
        	    String section = seat.getSection(); 
 
        	    if (section == null) { 
        	        section = "UNKNOWN"; 
        	    } 
 
        	    priceMap.putIfAbsent(section, seat.getPrice()); 
        	} 
 
        	for (Map.Entry<String, Integer> entry : priceMap.entrySet()) { 
        	    System.out.printf("%-10s : %,d원\n", 
        	            entry.getKey(), 
        	            entry.getValue()); 
        	} 
        }
        
        System.out.println("1. 예매하기 0. 뒤로");
    }
    
    // 3. 카테고리 입력받기
    public int inputCategory() { 
    	System.out.println("\n====== 카테고리 선택 ======");
        System.out.println("1. 전체");
        System.out.println("2. 콘서트");
        System.out.println("3. 뮤지컬");
        System.out.println("4. 스포츠");
        System.out.println("0. 뒤로");
        System.out.println();
        System.out.print("선택 > ");

        return inputSecureInteger("선택 > ");
    }
    
    // 4. 공연 ID 입력받기
    public int inputPerformanceId() { 
    	return inputSecureInteger("선택 > ");
    }
    
    // 5. 공연 정보 입력받기
    public PerformanceDTO inputPerformanceInfo() {
System.out.println("\n=== 공연 등록 ==================");
        
        String title = "";
        while (title.isEmpty()) {
            System.out.print("공연 제목 > ");
            title = sc.nextLine().trim();
            if (title.isEmpty()) {
                System.out.println("⚠ 공연 제목은 필수 입력 항목입니다. 공백 없이 입력해주세요.");
            }
        }
        
        String category = "";
        while (category.isEmpty()) {
            System.out.print("카테고리(뮤지컬/스포츠/콘서트) > ");
            category = sc.nextLine().trim();
            if (category.isEmpty()) {
                System.out.println("⚠ 카테고리는 필수 입력 항목입니다. 공백 없이 입력해주세요.");
            }
        }
        
        LocalDateTime startTime = null;
        while (startTime == null) {
            System.out.print("공연 일시 (형식: 2026-07-01 19:00) > ");
            String startStr = sc.nextLine().trim();
            if (startStr.isEmpty()) {
                System.out.println("⚠ 공연 일시를 입력해주세요.");
                continue;
            }
            try {
                startTime = LocalDateTime.parse(startStr, formatter);
            } catch (DateTimeParseException e) {
                System.out.println("⚠ 날짜 형식이 올바르지 않습니다. 다시 입력해주세요.");
            }
        }
        
        int runningTime = inputSecureInteger("러닝타임(분) > ");
        
        //[수정] 예매 오픈일 입력 (공연 일시와 비교하는 로직 추가)
        LocalDateTime bookingOpen = null;
        while (bookingOpen == null) {
            System.out.print("예매 오픈일 (형식: 2026-05-01 10:00) > ");
            String openStr = sc.nextLine().trim();
            try {
                LocalDateTime tempOpen = LocalDateTime.parse(openStr, formatter);
                
                // 핵심 검증: 예매 오픈일은 반드시 공연 일시보다 앞서야 함!
                // 즉, 예매 오픈일이 공연 일시와 같거나 그 이후(isAfter)라면 에러를 뿜어야 합니다.
                if (tempOpen.isAfter(startTime) || tempOpen.isEqual(startTime)) {
                    System.out.println("⚠ 예매 오픈일은 공연 일시보다 무조건 빨라야 합니다.");
                    System.out.println("(입력하신 공연 일시: " + startTime.format(formatter) + ")");
                    continue; // 다시 예매 오픈일을 입력받도록 루프 처음으로 이동
                }
                
                // 검증을 통과하면 실제 변수에 할당하여 루프 탈출
                bookingOpen = tempOpen;
                
            } catch (DateTimeParseException e) {
                System.out.println("⚠ 날짜 형식이 올바르지 않습니다. 다시 입력해주세요.");
            }
        }
        
        int venueId = inputSecureInteger("공연장 ID > ");
        
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