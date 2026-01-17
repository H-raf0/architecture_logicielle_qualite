package Architecture_log.TP.common.entity;

import jakarta.persistence.*;
import org.jmolecules.ddd.annotation.AggregateRoot;

/**
 * Entité représentant un ingrédient d'une recette.
 *
 * Aggregate Root dans le context DDD (Domain-Driven Design).
 * Relation: Plusieurs ingrédients peuvent appartenir à une seule recette (N-1).
 *
 * Attributs:
 * - id: Identifiant unique généré en base de données
 * - nom: Nom de l'ingrédient
 * - recette: Référence à la recette propriétaire
 *
 * @see Recette pour la recette propriétaire
 */
@Entity
@AggregateRoot
public class Ingredient {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String nom;

  // Relation Many-to-One (from my hero academia :-)) vers Recette
  /**
   * Recette à laquelle appartient cet ingrédient.
   *
   * Relation Many-to-One : plusieurs ingrédients peuvent appartenir à une recette.
   */
  @ManyToOne
  @JoinColumn(name = "recette_id") // colonne de clé étrangère
  private Recette recette;

  /**
   * Constructeur par défaut (requis par JPA).
   */
  public Ingredient() {}

  /**
   * Constructeur avec nom et recette.
   *
   * @param nom Nom de l'ingrédient
   * @param recette Recette propriétaire
   */
  public Ingredient(String nom, Recette recette) {
    this.nom = nom;
    this.recette = recette;
  }

  /**
   * Récupère l'ID de l'ingrédient.
   *
   * @return ID unique
   */
  public Long getId() {
    return id;
  }

  /**
   * Définit l'ID de l'ingrédient.
   *
   * @param id Nouvel ID
   */
  public void setId(Long id) {
    this.id = id;
  }

  /**
   * Récupère le nom de l'ingrédient.
   *
   * @return Nom de l'ingrédient
   */
  public String getNom() {
    return nom;
  }

  /**
   * Définit le nom de l'ingrédient.
   *
   * @param nom Nouveau nom
   */
  public void setNom(String nom) {
    this.nom = nom;
  }

  /**
   * Récupère la recette propriétaire de cet ingrédient.
   *
   * @return Recette associée
   */
  public Recette getRecette() {
    return recette;
  }

  /**
   * Définit la recette propriétaire de cet ingrédient.
   *
   * @param recette Nouvelle recette
   */
  public void setRecette(Recette recette) {
    this.recette = recette;
  }
}
