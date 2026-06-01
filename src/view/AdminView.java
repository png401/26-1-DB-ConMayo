package view;
import dto.MemberDTO;
import dto.PerformanceSeatDTO;
import dto.VenueDTO;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;
public class AdminView {
    private final Scanner sc = new Scanner(System.in);

 // 관리자 메뉴 출력 후 선택값 반환
    public int showAdminMenu() { 
    	
    	System.out.println("\n====== 관리자 메뉴 ======");
    	System.out.println("0. 로그아웃");
    	System.out.println("1. 공연 등록");
    	System.out.println("2. 공연 수정");
    	System.out.println("3. 공연 삭제");
    	System.out.println("4. 공연장 관리");
    	System.out.println("5. 좌석 가격 설정");
    	System.out.println("6. 블랙리스트 관리");
    	System.out.println();
    	System.out.print("선택 > ");
    	
    	int menu = sc.nextInt();
        sc.nextLine();

        return menu;
    	
    }
    
 // 블랙리스트 메뉴
    public int showMemberManageMenu() {

    	System.out.println("\n====== 회원 관리 ======");
    	System.out.println("1. 블랙리스트 조회");
    	System.out.println("2. 블랙리스트 등록");
    	System.out.println("3. 블랙리스트 해제");
    	System.out.println("0. 뒤로가기");
    	System.out.println();
    	System.out.print("선택 > ");

        int menu = sc.nextInt();
        sc.nextLine();

        return menu;
    }
    
 // 공연장 목록 출력
    public void printVenueList(List<VenueDTO> list) {
    	
    	if (list.isEmpty()) {
            System.out.println("등록된 공연장이 없습니다.");
            return;
        }
    	
    	System.out.println("\n====== 공연장 목록 ======");
    	System.out.println(list.size() + "개의 공연장이 존재합니다.\n");
    	
    	for (VenueDTO venue : list) {
    		System.out.println("ID > " + venue.getVenueId());
    		System.out.println("공연장 > " + venue.getVenueName());
    		System.out.println("주소 > " + venue.getAddress());
    		System.out.println("---------------------");
    	}
    	
    }
    
 // 공연장 이름 입력받기
    public String inputVenueName() { 
    	System.out.print("공연장 이름 > ");
    	return sc.nextLine();
    }

//공연장 아이디 입력받기 
    public int inputVenueId() {
        System.out.print("공연장 ID > ");
        int id = sc.nextInt();
        sc.nextLine();
        return id;
    }
    
 // 공연장 주소 입력받기
    public String inputVenueAddress() { 
    	System.out.print("공연장 주소 > ");
    	return sc.nextLine();
    } 
    
 // 블랙리스트 목록 출력
    public void printBlacklist(List<MemberDTO> list) {
    	
    	System.out.println("\n====== 블랙리스트 회원 목록 ======");
    	
    	if (list.isEmpty()) {
            System.out.println("블랙리스트 회원이 없습니다.");
            return;
        }
    	
    	System.out.println("회원ID\t\t제한 해제 시각\n");
    	
    	for (MemberDTO member : list) {
    		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    		System.out.println(member.getMemberId() + "\t\t" + member.getBlacklistUntil().format(formatter));
    	}
    	
		System.out.println("");

    } 
    
 // 블랙리스트 등록/해제할 회원 ID 입력받기
    public String inputMemberIdToBlacklist() { 
    	System.out.print("블랙리스트 회원 ID > ");
    	return sc.nextLine();
    }
    
 // 공연 ID 입력받기
    public int inputPerformanceId() { 
    	System.out.print("공연 ID > ");

    	int id = sc.nextInt();
        sc.nextLine();

        return id;
    }
    
 // 공연좌석-가격 목록 출력
    public void printPerfSeatList(List<PerformanceSeatDTO> list) {
    	System.out.println("\n====== 좌석 가격 목록 ======");

        if (list.isEmpty()) {
            System.out.println("등록된 좌석 정보가 없습니다.");
            return;
        }

        System.out.println("공연ID\t좌석ID\t가격\n");

        for (PerformanceSeatDTO seat : list) {
        	System.out.println(
                    seat.getPerformanceId() + "\t"
                    + seat.getSeatId() + "\t"
                    + seat.getPrice()
            );
        }
    }
    
 // 가격 입력받기
    public int inputPrice() { 
    	System.out.print("가격 > ");

    	int price = sc.nextInt();
        sc.nextLine();

        return price;
    } 
    
 // 에러 메시지 출력
    public void printError(String msg) {
    	System.out.println("\n⚠ " + msg + '\n');
    }
    
 // 성공 메시지 출력
    public void printSuccess(String msg) {
    	System.out.println("\n✓ " + msg + '\n');
    }

	public int showVenueManageMenu() {
		
		System.out.println("\n====== 공연장 관리 ======");
    	System.out.println("1. 공연장 추가");
    	System.out.println("2. 공연장 수정");
    	System.out.println("3. 공연장 삭제");
    	System.out.println("0. 뒤로가기");
    	System.out.println();
    	System.out.print("선택 > ");

        int menu = sc.nextInt();
        sc.nextLine();

        return menu;
	}

	public int inputPerformanceSeatId() {
		
		System.out.print("공연좌석 ID > ");
		
		int perfSeatId = sc.nextInt();
		return perfSeatId;
	}
   
}
