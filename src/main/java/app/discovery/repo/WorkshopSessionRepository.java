package app.discovery.repo;

import app.discovery.domain.WorkshopSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.Instant;
import java.util.List;

public interface WorkshopSessionRepository extends JpaRepository<WorkshopSession, Long> {
  List<WorkshopSession> findByWorkshopIdOrderByStartsAtAsc(Long workshopId);

  @Query("select s from WorkshopSession s where s.workshop.id = :workshopId"
      + " and s.status = 'SCHEDULED' and s.startsAt >= :now order by s.startsAt asc")
  List<WorkshopSession> upcomingForWorkshop(Long workshopId, Instant now);
}
