package Architecture_log.TP.common.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.jmolecules.ddd.annotation.AggregateRoot;

/**
 * Entité représentant une recette de cuisine.
 * 
 * Aggregate Root dans le context DDD (Domain-Driven Design).
 * Une recette peut contenir plusieurs ingrédients (relation 1-N).
 * 
 * Attributs:
 * - id: Identifiant unique généré en base de données
 * - nom: Nom de la recette
 * 
 * @see Ingredient pour les ingrédients associés
 */
@Entity
@AggregateRoot
public class Recette {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String nom;

  /**
   * Constructeur par défaut (requis par JPA).
   */
  public Recette() {}

  /**
   * Constructeur avec nom.
   * 
   * @param nom Nom de la recette
   */
  public Recette(String nom) {
    this.nom = nom;
  }

  /**
   * Récupère l'ID de la recette.
   * 
   * @return L'ID unique de la recette
   */
  public Long getId() {
    return id;
  }

  /**
   * Définit l'ID de la recette.
   * 
   * @param id Nouvel ID
   */
  public void setId(Long id) {
    this.id = id;
  }

  /**
   * Récupère le nom de la recette.
   * 
   * @return Nom de la recette
   */
  public String getNom() {
    return nom;
  }

  /**
   * Définit le nom de la recette.
   * 
   * @param nom Nouveau nom
   */
  public void setNom(String nom) {
    this.nom = nom;
  }
}
