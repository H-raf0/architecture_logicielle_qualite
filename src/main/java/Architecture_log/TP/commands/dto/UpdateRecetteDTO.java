package Architecture_log.TP.commands.dto;

public class UpdateRecetteDTO {

  private String nom;

  public UpdateRecetteDTO() {}

  public UpdateRecetteDTO(String nom) {
    this.nom = nom;
  }

  public String getNom() {
    return nom;
  }

  public void setNom(String nom) {
    this.nom = nom;
  }
}
