package Architecture_log.TP.queries.dto;

public class IngredientDTO {
  private Long id;
  private String nom;
  private Long recetteId;

  public IngredientDTO() {}

  public IngredientDTO(Long id, String nom, Long recetteId) {
    this.id = id;
    this.nom = nom;
    this.recetteId = recetteId;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getNom() {
    return nom;
  }

  public void setNom(String nom) {
    this.nom = nom;
  }

  public Long getRecetteId() {
    return recetteId;
  }

  public void setRecetteId(Long recetteId) {
    this.recetteId = recetteId;
  }
}

