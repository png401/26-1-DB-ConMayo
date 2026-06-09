package dto;

public class WaitingQueueDTO {
    private int queueId;
    private String memberId;
    private int performanceId;

    public WaitingQueueDTO() {}

    public WaitingQueueDTO(String memberId, int performanceId) {
        this.memberId = memberId;
        this.performanceId = performanceId;
    }

    public int getQueueId() { return queueId; }
    public void setQueueId(int queueId) { this.queueId = queueId; }
    public String getMemberId() { return memberId; }
    public void setMemberId(String memberId) { this.memberId = memberId; }
    public int getPerformanceId() { return performanceId; }
    public void setPerformanceId(int performanceId) { this.performanceId = performanceId; }
}
