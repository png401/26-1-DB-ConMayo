package model;

import java.time.LocalDateTime;

public class Performance {

	private int performanceId;
	private String title;
	private String category;
	private LocalDateTime startTime;
	private int runningTime;
	private SalesStatus salesStatus;
	private LocalDateTime bookingOpen;
	private String venueId;
	
	public Performance() {}
	
	public Performance(int performanceId, String title, 
			String category, LocalDateTime startTime, 
			int runningTime, SalesStatus salesStatus, 
			LocalDateTime bookingOpen, String venueId) {
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

	public String getVenueId() {
		return venueId;
	}

	public void setVenueId(String venueId) {
		this.venueId = venueId;
	}
	
}
