// PerformanceSeatDTO.java
package dto;

public class PerformanceSeatDTO {
    private int performanceSeatId;	// 공연좌석 ID (PK, AUTO_INCREMENT)
    private int performanceId;	// 공연 ID (FK → performance)
    private int seatId;			// 좌석 ID (FK → seat)
    private int price;			// 해당 공연에서의 좌석 가격
    private boolean isBooked;	// 예약 여부 (BOOKED/HOLD면 true)
    private String section;		// 좌석 등급 보관할 필드
    private int rowNum;			// 추가 - 행 정보
    private int colNum;			// 추가 - 열 정보

    public PerformanceSeatDTO() {}

	public PerformanceSeatDTO(int performanceSeatId, 
			int performanceId, int seatId, int price) {
		this.performanceSeatId = performanceSeatId;
		this.performanceId = performanceId;
		this.seatId = seatId;
		this.price = price;
	}

	public int getPerformanceSeatId() {
		return performanceSeatId;
	}

	public void setPerformanceSeatId(int performanceSeatId) {
		this.performanceSeatId = performanceSeatId;
	}

	public int getPerformanceId() {
		return performanceId;
	}

	public void setPerformanceId(int performanceId) {
		this.performanceId = performanceId;
	}

	public int getSeatId() {
		return seatId;
	}

	public void setSeatId(int seatId) {
		this.seatId = seatId;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public boolean isBooked() {
		return isBooked;
	}

	public void setBooked(boolean isBooked) {
		this.isBooked = isBooked;
	}

	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}
	
	// 추가
	public int getRowNum() {
	    return rowNum;
	}

	public void setRowNum(int rowNum) {
	    this.rowNum = rowNum;
	}

	public int getColNum() {
	    return colNum;
	}

	public void setColNum(int colNum) {
	    this.colNum = colNum;
	}
    
}