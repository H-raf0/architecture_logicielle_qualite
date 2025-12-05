package Architecture_log.TP.repository;

import Architecture_log.TP.entity.Recette;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecetteRepository extends JpaRepository<Recette, Long> {}
