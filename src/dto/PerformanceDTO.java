package dto;

public class PerformanceDTO {
    private int performanceId;  // 공연 ID (PK, AUTO_INCREMENT)
    private int venueId;        // 공연장 ID (FK → venue)
    private String title;       // 공연 제목
    private String category;    // 카테고리 (콘서트/뮤지컬/스포츠)
    private String startTime;   // 공연 시작 일시
    private int runningTime;    // 러닝타임 (분)
    private String salesStatus; // 판매 상태 (OPEN/CLOSED/SOLD_OUT/COMING_SOON)
    private String bookingOpen; // 예매 오픈 일시

    public PerformanceDTO() {}
    public int getPerformanceId() { return performanceId; }
    public void setPerformanceId(int performanceId) { this.performanceId = performanceId; }
    public int getVenueId() { return venueId; }
    public void setVenueId(int venueId) { this.venueId = venueId; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getStartTime() { return startTime; }
    public void setStartTime(String startTime) { this.startTime = startTime; }
    public int getRunningTime() { return runningTime; }
    public void setRunningTime(int runningTime) { this.runningTime = runningTime; }
    public String getSalesStatus() { return salesStatus; }
    public void setSalesStatus(String salesStatus) { this.salesStatus = salesStatus; }
    public String getBookingOpen() { return bookingOpen; }
    public void setBookingOpen(String bookingOpen) { this.bookingOpen = bookingOpen; }
}