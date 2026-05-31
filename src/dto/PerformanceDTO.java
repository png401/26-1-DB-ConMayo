package dto;

import java.time.LocalDateTime;
import java.util.List;

public class PerformanceDTO {

	private int performanceId;	// 공연 ID (PK, AUTO_INCREMENT)
	private int venueId;		// 공연장 ID (FK → venue)
	private String title;       // 공연 제목
    private String category;    // 카테고리 (콘서트/뮤지컬/스포츠 등)
	private LocalDateTime startTime;	// 공연 시작 일시
    private int runningTime;    // 러닝타임 (분)
	private SalesStatus salesStatus;	// 판매 상태 (OPEN/CLOSED/SOLD_OUT/COMING_SOON)
	private LocalDateTime bookingOpen;	// 예매 오픈 일시
	private int remainingSeats; // DB에서 계산된 잔여석 담아둘 변수
	private List<PerformanceSeatDTO> seatList; // 좌석 리스트 변수
	
	public PerformanceDTO() {}
	
	public PerformanceDTO(int performanceId, String title, 
			String category, LocalDateTime startTime, 
			int runningTime, SalesStatus salesStatus, 
			LocalDateTime bookingOpen, int venueId) {
		this.performanceId = performanceId;
		this.title = title;
		this.category = category;
		this.startTime = startTime;
		this.runningTime = runningTime;
		this.salesStatus = salesStatus;
		this.bookingOpen = bookingOpen;
		this.venueId = venueId;
	}

	public int getPerformanceId() {
		return performanceId;
	}

	public void setPerformanceId(int performanceId) {
		this.performanceId = performanceId;
	}

	public int getVenueId() {
		return venueId;
	}

	public void setVenueId(int venueId) {
		this.venueId = venueId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public LocalDateTime getStartTime() {
		return startTime;
	}

	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}

	public int getRunningTime() {
		return runningTime;
	}

	public void setRunningTime(int runningTime) {
		this.runningTime = runningTime;
	}

	public SalesStatus getSalesStatus() {
		return salesStatus;
	}

	public void setSalesStatus(SalesStatus salesStatus) {
		this.salesStatus = salesStatus;
	}

	public LocalDateTime getBookingOpen() {
		return bookingOpen;
	}

	public void setBookingOpen(LocalDateTime bookingOpen) {
		this.bookingOpen = bookingOpen;
	}

	public int getRemainingSeats() {
		return remainingSeats;
	}
	

	public void setRemainingSeats(int remainingSeats) {
		this.remainingSeats = remainingSeats;
	}
	
	public List<PerformanceSeatDTO> getSeatList() {
        return seatList;
    }

    public void setSeatList(List<PerformanceSeatDTO> seatList) {
        this.seatList = seatList;
    }

}
