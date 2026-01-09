package Architecture_log.TP.common.repository;

import Architecture_log.TP.common.entity.Recette;
import org.jmolecules.ddd.annotation.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface RecetteRepository extends JpaRepository<Recette, Long> {}

