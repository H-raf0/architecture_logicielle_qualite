package Architecture_log.TP.commands.service;

import Architecture_log.TP.commands.dto.CreateRecetteDTO;
import Architecture_log.TP.commands.dto.UpdateRecetteDTO;
import Architecture_log.TP.common.entity.Recette;
import Architecture_log.TP.common.repository.RecetteRepository;
import org.springframework.stereotype.Service;

@Service
public class RecetteCommandService {

  private final RecetteRepository recetteRepository;

  public RecetteCommandService(RecetteRepository recetteRepository) {
    this.recetteRepository = recetteRepository;
  }

  public Recette createRecette(CreateRecetteDTO dto) {
    Recette recette = new Recette(dto.getNom());
    return recetteRepository.save(recette);
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

