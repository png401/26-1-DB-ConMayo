package dto;

public class SeatDTO {

	private int seatId;             // 좌석 ID (PK, AUTO_INCREMENT)
	private int venueId;            // 공연장 ID (FK → venue)
	private int performanceSeatId;  // 공연좌석 ID (FK → performance_seat)
	private String section;         // 구역 (VIP / A / B)
	private int rowNum;             // 행 번호
	private int colNum;             // 열 번호
	private double avgRating;       // 평균 평점 (review에서 AVG 집계, 리뷰 없으면 0.0)
	private String color;           // SeatPanel 색상 (RED/ORANGE/YELLOW/LIME/GREEN/GRAY)

	public SeatDTO() {}

	public SeatDTO(int seatId, int venueId, int performanceSeatId,
	               String section, int rowNum, int colNum) {
		this.seatId = seatId;
		this.venueId = venueId;
		this.performanceSeatId = performanceSeatId;
		this.section = section;
		this.rowNum = rowNum;
		this.colNum = colNum;
	}

	public int getSeatId() { return seatId; }
	public void setSeatId(int seatId) { this.seatId = seatId; }

	public int getVenueId() { return venueId; }
	public void setVenueId(int venueId) { this.venueId = venueId; }

	public int getPerformanceSeatId() { return performanceSeatId; }
	public void setPerformanceSeatId(int performanceSeatId) { this.performanceSeatId = performanceSeatId; }

	public String getSection() { return section; }
	public void setSection(String section) { this.section = section; }

	public int getRowNum() { return rowNum; }
	public void setRowNum(int rowNum) { this.rowNum = rowNum; }

	public int getColNum() { return colNum; }
	public void setColNum(int colNum) { this.colNum = colNum; }

	public double getAvgRating() { return avgRating; }
	public void setAvgRating(double avgRating) { this.avgRating = avgRating; }

	public String getColor() { return color; }
	public void setColor(String color) { this.color = color; }

	@Override
	public String toString() {
		return String.format("[좌석%d] %s %d행 %d열 ★%.1f (%s)",
				seatId, section, rowNum, colNum, avgRating, color);
	}
}