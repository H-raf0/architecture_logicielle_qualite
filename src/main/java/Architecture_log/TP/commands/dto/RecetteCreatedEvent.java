package Architecture_log.TP.commands.dto;

import java.time.LocalDateTime;

public class RecetteCreatedEvent {

  private Long id;
  private String nom;
  private LocalDateTime createdAt;

  public RecetteCreatedEvent() {}

  public RecetteCreatedEvent(Long id, String nom, LocalDateTime createdAt) {
    this.id = id;
    this.nom = nom;
    this.createdAt = createdAt;
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

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }
}

