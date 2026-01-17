package Architecture_log.TP.queries.dto;

/**
 * DTO pour la restitution d'une recette.
 * 
 * Transfer Object utilisé dans les réponses GET.
 * Contient les données à exposer au client pour une recette.
 * 
 * @see IngredientDTO pour les ingrédients
 */
public class RecetteDTO {

  /**
   * Identifiant unique de la recette.
   */
  private Long id;

  /**
   * Nom de la recette.
   */
  private String nom;

  /**
   * Constructeur par défaut.
   */
  public RecetteDTO() {}

  /**
   * Constructeur avec id et nom.
   * 
   * @param id ID de la recette
   * @param nom Nom de la recette
   */
  public RecetteDTO(Long id, String nom) {
    this.id = id;
    this.nom = nom;
  }

  /**
   * Récupère l'ID de la recette.
   * 
   * @return ID unique
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
