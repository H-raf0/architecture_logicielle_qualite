package Architecture_log.TP.repository;

import Architecture_log.TP.entity.Recette;
import org.jmolecules.ddd.annotation.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface RecetteRepository extends JpaRepository<Recette, Long> {}
