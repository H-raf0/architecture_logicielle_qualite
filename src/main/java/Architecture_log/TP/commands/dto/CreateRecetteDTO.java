package Architecture_log.TP.commands.dto;

public class CreateRecetteDTO {
  private String nom;

  public CreateRecetteDTO() {}

  public CreateRecetteDTO(String nom) {
    this.nom = nom;
  }

  public String getNom() {
    return nom;
  }

  public void setNom(String nom) {
    this.nom = nom;
  }
}

