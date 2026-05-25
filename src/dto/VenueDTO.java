package dto;

public class VenueDTO {
    private int venueId;       // 공연장 ID (PK, AUTO_INCREMENT)
    private String venueName;  // 공연장 이름
    private String address;    // 주소

    public VenueDTO() {}
    public VenueDTO(int venueId, String venueName, String address) {
        this.venueId = venueId;
        this.venueName = venueName;
        this.address = address;
    }
    public int getVenueId() { return venueId; }
    public void setVenueId(int venueId) { this.venueId = venueId; }
    public String getVenueName() { return venueName; }
    public void setVenueName(String venueName) { this.venueName = venueName; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
}