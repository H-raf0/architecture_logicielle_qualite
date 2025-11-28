package Architecture_log.TP.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import Architecture_log.TP.entity.Recette;

public interface RecetteRepository extends JpaRepository<Recette, Long> {
}
