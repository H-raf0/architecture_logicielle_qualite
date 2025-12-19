package Architecture_log.TP.entity;

import jakarta.persistence.*;

@Entity
public class Ingredient {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String nom;

  // Relation Many-to-One vers Recette
  @ManyToOne
  @JoinColumn(name = "recette_id") // colonne de clé étrangère
  private Recette recette;

  public Ingredient() {}

  public Ingredient(String nom, Recette recette) {
    this.nom = nom;
    this.recette = recette;
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

  public Recette getRecette() {
    return recette;
  }

  public void setRecette(Recette recette) {
    this.recette = recette;
  }
}
