package app.discovery.repo;

import app.discovery.domain.Season;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface SeasonRepository extends JpaRepository<Season, Long> {
  Optional<Season> findBySlug(String slug);
  List<Season> findByActiveTrue();
}
