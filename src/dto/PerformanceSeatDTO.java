// PerformanceSeatDTO.java
package dto;

public class PerformanceSeatDTO {
    private int performanceSeatId; // 공연좌석 ID (PK, AUTO_INCREMENT)
    private int performanceId;     // 공연 ID (FK → performance)
    private int seatId;            // 좌석 ID (FK → seat)
    private int price;             // 해당 공연에서의 좌석 가격

    public PerformanceSeatDTO() {}
    public int getPerformanceSeatId() { return performanceSeatId; }
    public void setPerformanceSeatId(int performanceSeatId) { this.performanceSeatId = performanceSeatId; }
    public int getPerformanceId() { return performanceId; }
    public void setPerformanceId(int performanceId) { this.performanceId = performanceId; }
    public int getSeatId() { return seatId; }
    public void setSeatId(int seatId) { this.seatId = seatId; }
    public int getPrice() { return price; }
    public void setPrice(int price) { this.price = price; }
}