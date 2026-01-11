package app.fisherman.repository;

import app.fisherman.model.Fisherman;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FishermanRepository extends JpaRepository<Fisherman, UUID> {

  Optional<Fisherman> findByUsername(String username);
}
