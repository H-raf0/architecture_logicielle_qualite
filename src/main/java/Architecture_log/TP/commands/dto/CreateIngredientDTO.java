package Architecture_log.TP.commands.dto;

public class CreateIngredientDTO {
  private String nom;

  public CreateIngredientDTO() {}

  public CreateIngredientDTO(String nom) {
    this.nom = nom;
  }

  public String getNom() {
    return nom;
  }

  public void setNom(String nom) {
    this.nom = nom;
  }
}

