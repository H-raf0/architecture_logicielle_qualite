package Architecture_log.TP.common.repository;

import Architecture_log.TP.common.entity.Recette;
import org.jmolecules.ddd.annotation.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository pour accéder aux recettes en base de données.
 *
 * Fournit les méthodes CRUD standard (Create, Read, Update, Delete)
 * pour gérer les entités {@link Recette} en base de données.
 */
@Repository
public interface RecetteRepository extends JpaRepository<Recette, Long> {}
