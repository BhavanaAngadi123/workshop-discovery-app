package app.discovery.repo;

import app.discovery.domain.HostProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface HostProfileRepository extends JpaRepository<HostProfile, Long> {
  Optional<HostProfile> findByUserId(Long userId);
}
