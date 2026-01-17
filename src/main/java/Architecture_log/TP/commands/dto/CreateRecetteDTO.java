package Architecture_log.TP.commands.dto;

/**
 * DTO pour la création d'une recette.
 *
 * Transfer Object utilisé dans les requêtes POST.
 * Contient uniquement les données nécessaires pour créer une recette.
 *
 * @see CreateIngredientDTO pour la création d'ingrédients
 */
public class CreateRecetteDTO {

  /**
   * Nom de la recette à créer.
   */
  private String nom;

  /**
   * Constructeur par défaut.
   */
  public CreateRecetteDTO() {}

  /**
   * Constructeur avec nom.
   *
   * @param nom Nom de la recette
   * @throws IllegalArgumentException Si le nom est null, vide ou trop court (< 3 caractères)
   */
  public CreateRecetteDTO(String nom) {
    if (nom == null) {
      throw new IllegalArgumentException(
        "Nom de la recette ne peut pas être null"
      );
    }
    if (nom.trim().isEmpty()) {
      throw new IllegalArgumentException(
        "Nom de la recette ne peut pas être vide"
      );
    }
    if (nom.trim().length() < 3) {
      throw new IllegalArgumentException(
        "Nom de la recette doit contenir au moins 3 caractères"
      );
    }
    this.nom = nom;
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
