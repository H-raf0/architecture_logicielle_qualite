package Architecture_log.TP.commands.service;

import Architecture_log.TP.commands.dto.CreateRecetteDTO;
import Architecture_log.TP.commands.dto.RecetteCreatedEvent;
import Architecture_log.TP.commands.dto.UpdateRecetteDTO;
import Architecture_log.TP.common.entity.Recette;
import Architecture_log.TP.common.repository.RecetteRepository;
import io.github.resilience4j.retry.annotation.Retry;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;

@Service
public class RecetteCommandService {

  private final RecetteRepository recetteRepository;
  private final RecetteEventPublisher recetteEventPublisher;

  public RecetteCommandService(
    RecetteRepository recetteRepository,
    RecetteEventPublisher recetteEventPublisher
  ) {
    this.recetteRepository = recetteRepository;
    this.recetteEventPublisher = recetteEventPublisher;
  }

  @Retry(name = "recetteRetry")
  public Recette createRecette(CreateRecetteDTO dto) {
    Recette recette = new Recette(dto.getNom());
    Recette savedRecette = recetteRepository.save(recette);

    RecetteCreatedEvent event = new RecetteCreatedEvent(
      savedRecette.getId(),
      savedRecette.getNom(),
      LocalDateTime.now()
    );
    recetteEventPublisher.publishRecetteCreated(event);

    return savedRecette;
  }

  public Recette updateRecette(Long id, UpdateRecetteDTO dto) {
    Recette recette = recetteRepository
      .findById(id)
      .orElseThrow(() -> new RuntimeException("Recette not found"));
    recette.setNom(dto.getNom());
    return recetteRepository.save(recette);
  }

  public void deleteRecette(Long id) {
    recetteRepository.deleteById(id);
  }
}
