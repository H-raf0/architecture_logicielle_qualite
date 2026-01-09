package Architecture_log.TP.queries.service;

import Architecture_log.TP.common.entity.Recette;
import Architecture_log.TP.common.repository.RecetteRepository;
import Architecture_log.TP.queries.dto.RecetteDTO;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class RecetteQueryService {

  private final RecetteRepository recetteRepository;

  public RecetteQueryService(RecetteRepository recetteRepository) {
    this.recetteRepository = recetteRepository;
  }

  public List<RecetteDTO> getAllRecettes() {
    return recetteRepository
      .findAll()
      .stream()
      .map(this::toDTO)
      .collect(Collectors.toList());
  }

  public RecetteDTO getRecetteById(Long id) {
    Recette recette = recetteRepository
      .findById(id)
      .orElseThrow(() -> new RuntimeException("Recette not found"));
    return toDTO(recette);
  }

  private RecetteDTO toDTO(Recette recette) {
    return new RecetteDTO(recette.getId(), recette.getNom());
  }
}
