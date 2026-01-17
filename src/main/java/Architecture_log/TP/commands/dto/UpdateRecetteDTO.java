package Architecture_log.TP.commands.dto;

/**
 * DTO pour la modification d'une recette.
 * 
 * Transfer Object utilisé dans les requêtes PUT.
 * Contient les données nécessaires pour mettre à jour une recette existante.
 * 
 * @see UpdateIngredientDTO pour la modification d'ingrédients
 */
public class UpdateRecetteDTO {

  /**
   * Nouveau nom de la recette.
   */
  private String nom;

  /**
   * Constructeur par défaut.
   */
  public UpdateRecetteDTO() {}

  /**
   * Constructeur avec nom.
   * 
   * @param nom Nouveau nom de la recette
   */
  public UpdateRecetteDTO(String nom) {
    this.nom = nom;
  }

  /**
   * Récupère le nouveau nom de la recette.
   * 
   * @return Nom de la recette
   */
  public String getNom() {
    return nom;
  }

  /**
   * Définit le nouveau nom de la recette.
   * 
   * @param nom Nom à mettre à jour
   */
  public void setNom(String nom) {
    this.nom = nom;
  }
}
