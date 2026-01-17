package Architecture_log.TP.commands.dto;

/**
 * DTO pour la création d'un ingrédient.
 * 
 * Transfer Object utilisé dans les requêtes POST.
 * Contient uniquement les données nécessaires pour créer un ingrédient.
 * La recette est spécifiée via le path parameter de la requête.
 * 
 * @see CreateRecetteDTO pour la création de recettes
 */
public class CreateIngredientDTO {

  /**
   * Nom de l'ingrédient à créer.
   */
  private String nom;

  /**
   * Constructeur par défaut.
   */
  public CreateIngredientDTO() {}

  /**
   * Constructeur avec nom.
   * 
   * @param nom Nom de l'ingrédient
   */
  public CreateIngredientDTO(String nom) {
    this.nom = nom;
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
}
