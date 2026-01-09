package Architecture_log.TP.commands.dto;

public class UpdateIngredientDTO {
  private String nom;

  public UpdateIngredientDTO() {}

  public UpdateIngredientDTO(String nom) {
    this.nom = nom;
  }

  public String getNom() {
    return nom;
  }

  public void setNom(String nom) {
    this.nom = nom;
  }
}

