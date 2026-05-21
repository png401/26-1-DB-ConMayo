package model;

public class Seat {

	private int seatId;
	private int venueId;
	private String section;
	private int rowNum;
	private int colNum;
	
	public Seat() {}

	public Seat(int seatId, int venueId, 
			String section, int rowNum, int colNum) {
		this.seatId = seatId;
		this.venueId = venueId;
		this.section = section;
		this.rowNum = rowNum;
		this.colNum = colNum;
	}

	public int getSeatId() {
		return seatId;
	}

	public void setSeatId(int seatId) {
		this.seatId = seatId;
	}

	public int getVenueId() {
		return venueId;
	}

	public void setVenueId(int venueId) {
		this.venueId = venueId;
	}

	public String getSection() {
		return section;
	}

	public void setSection(String section) {
		this.section = section;
	}

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
