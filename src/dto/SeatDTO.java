package dto;

public class SeatDTO {
    private int seatId;       // 좌석 ID (PK, AUTO_INCREMENT)
    private int venueId;      // 공연장 ID (FK → venue)
    private String section;   // 구역 (VIP / A / B)
    private int rowNum;       // 행 번호
    private int colNum;       // 열 번호
    private int price;        // 해당 공연의 좌석 가격 (performance_seat에서 JOIN)
    private double avgRating; // 평균 평점 (review에서 AVG 집계, 리뷰 없으면 0.0)
    private boolean isBooked; // 예약 여부 (BOOKED/HOLD면 true)
    private String color;     // SeatPanel 색상 (GREEN/YELLOW/RED/GRAY)

    public SeatDTO() {}
    public int getSeatId() { return seatId; }
    public void setSeatId(int seatId) { this.seatId = seatId; }
    public int getVenueId() { return venueId; }
    public void setVenueId(int venueId) { this.venueId = venueId; }
    public String getSection() { return section; }
    public void setSection(String section) { this.section = section; }
    public int getRowNum() { return rowNum; }
    public void setRowNum(int rowNum) { this.rowNum = rowNum; }
    public int getColNum() { return colNum; }
    public void setColNum(int colNum) { this.colNum = colNum; }
    public int getPrice() { return price; }
    public void setPrice(int price) { this.price = price; }
    public double getAvgRating() { return avgRating; }
    public void setAvgRating(double avgRating) { this.avgRating = avgRating; }
    public boolean isBooked() { return isBooked; }
    public void setBooked(boolean isBooked) { this.isBooked = isBooked; }
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
}
