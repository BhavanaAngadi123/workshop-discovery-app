package app.discovery.repo;

import app.discovery.domain.Workshop;
import app.discovery.domain.WorkshopCategory;
import app.discovery.domain.WorkshopStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface WorkshopRepository extends JpaRepository<Workshop, Long> {
  Page<Workshop> findByStatus(WorkshopStatus status, Pageable pageable);
  Page<Workshop> findByHostId(Long hostId, Pageable pageable);

  @Query("select w from Workshop w where w.status = 'PUBLISHED'"
      + " and (:category is null or w.category = :category)"
      + " and (:city is null or lower(w.city) like lower(concat('%', :city, '%')))"
      + " and (:q is null or lower(w.title) like lower(concat('%', :q, '%'))"
      + "      or lower(w.description) like lower(concat('%', :q, '%')))"
      + " and (:maxPriceCents is null or w.priceCents <= :maxPriceCents)"
      + " and (:seasonSlug is null or w.seasonSlug = :seasonSlug)")
  Page<Workshop> search(@Param("category") WorkshopCategory category,
                        @Param("city") String city,
                        @Param("q") String q,
                        @Param("maxPriceCents") Long maxPriceCents,
                        @Param("seasonSlug") String seasonSlug,
                        Pageable pageable);
}
