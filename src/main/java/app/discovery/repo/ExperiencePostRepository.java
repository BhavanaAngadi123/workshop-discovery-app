package app.discovery.repo;

import app.discovery.domain.ExperiencePost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExperiencePostRepository extends JpaRepository<ExperiencePost, Long> {
  Page<ExperiencePost> findAllByOrderByCreatedAtDesc(Pageable pageable);
  Page<ExperiencePost> findByWorkshopIdOrderByCreatedAtDesc(Long workshopId, Pageable pageable);
}
