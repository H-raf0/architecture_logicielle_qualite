package Architecture_log.TP.queries.dto;

/**
 * DTO pour la restitution d'un ingrédient.
 * 
 * Transfer Object utilisé dans les réponses GET.
 * Contient les données à exposer au client pour un ingrédient.
 * 
 * @see RecetteDTO pour les recettes
 */
public class IngredientDTO {

  /**
   * Identifiant unique de l'ingrédient.
   */
  private Long id;

  /**
   * Nom de l'ingrédient.
   */
  private String nom;

  /**
   * ID de la recette à laquelle cet ingrédient appartient.
   */
  private Long recetteId;

  /**
   * Constructeur par défaut.
   */
  public IngredientDTO() {}

  /**
   * Constructeur avec id, nom et recetteId.
   * 
   * @param id ID de l'ingrédient
   * @param nom Nom de l'ingrédient
   * @param recetteId ID de la recette propriétaire
   */
  public IngredientDTO(Long id, String nom, Long recetteId) {
    this.id = id;
    this.nom = nom;
    this.recetteId = recetteId;
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
   * Récupère l'ID de la recette propriétaire.
   * 
   * @return ID de la recette
   */
  public Long getRecetteId() {
    return recetteId;
  }

  /**
   * Définit l'ID de la recette propriétaire.
   * 
   * @param recetteId ID de la recette
   */
  public void setRecetteId(Long recetteId) {
    this.recetteId = recetteId;
  }
}
