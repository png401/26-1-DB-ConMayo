package dao;

import dto.WaitingQueueDTO;

public interface WaitingQueueDAO {
    int enter(WaitingQueueDTO dto);   // INSERT → 내 queue_id 반환
    int getMyRank(int queueId, int performanceId); // 내 앞에 몇 명
    void leave(int queueId);          // DELETE
}