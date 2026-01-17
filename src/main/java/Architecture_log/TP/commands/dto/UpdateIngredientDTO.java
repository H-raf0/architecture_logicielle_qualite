package Architecture_log.TP.commands.dto;

/**
 * DTO pour la modification d'un ingrédient.
 * 
 * Transfer Object utilisé dans les requêtes PUT.
 * Contient les données nécessaires pour mettre à jour un ingrédient existant.
 * 
 * @see UpdateRecetteDTO pour la modification de recettes
 */
public class UpdateIngredientDTO {

  /**
   * Nouveau nom de l'ingrédient.
   */
  private String nom;

  /**
   * Constructeur par défaut.
   */
  public UpdateIngredientDTO() {}

  /**
   * Constructeur avec nom.
   * 
   * @param nom Nouveau nom de l'ingrédient
   */
  public UpdateIngredientDTO(String nom) {
    this.nom = nom;
  }

  /**
   * Récupère le nouveau nom de l'ingrédient.
   * 
   * @return Nom de l'ingrédient
   */
  public String getNom() {
    return nom;
  }

  /**
   * Définit le nouveau nom de l'ingrédient.
   * 
   * @param nom Nom à mettre à jour
   */
  public void setNom(String nom) {
    this.nom = nom;
  }
}
