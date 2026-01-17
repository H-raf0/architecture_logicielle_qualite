package Architecture_log.TP.commands.dto;

import java.time.LocalDateTime;

/**
 * Événement émis lorsqu'une recette est créée.
 *
 * Cet objet DTO contient les informations minimales nécessaires
 * pour signaler la création d'une recette aux systèmes consommateurs
 * (par exemple via Kafka).
 */
public class RecetteCreatedEvent {

  /** Identifiant de la recette créée. */
  private Long id;

  /** Nom de la recette créée. */
  private String nom;

  /** Horodatage de création de l'événement. */
  private LocalDateTime createdAt;

  /**
   * Constructeur par défaut requis pour la (dé)sérialisation.
   */
  public RecetteCreatedEvent() {}

  /**
   * Constructeur complet.
   *
   * @param id identifiant de la recette
   * @param nom nom de la recette
   * @param createdAt horodatage de création
   */
  public RecetteCreatedEvent(Long id, String nom, LocalDateTime createdAt) {
    this.id = id;
    this.nom = nom;
    this.createdAt = createdAt;
  }

  /** @return l'identifiant de la recette */
  public Long getId() {
    return id;
  }

  /** @param id définit l'identifiant de la recette */
  public void setId(Long id) {
    this.id = id;
  }

  /** @return le nom de la recette */
  public String getNom() {
    return nom;
  }

  /** @param nom définit le nom de la recette */
  public void setNom(String nom) {
    this.nom = nom;
  }

  /** @return la date/heure de création de l'événement */
  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  /** @param createdAt définit la date/heure de création de l'événement */
  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }
}
