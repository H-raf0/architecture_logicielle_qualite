package Architecture_log.TP.service;

import Architecture_log.TP.entity.RecetteTest;
import Architecture_log.TP.repository.RecetteRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class RecetteService {

  private final RecetteRepository recetteRepository;

  public RecetteService(RecetteRepository recetteRepository) {
    this.recetteRepository = recetteRepository;
  }

  public List<RecetteTest> getAllRecettes() {
    return recetteRepository.findAll();
  }

  public RecetteTest createRecette(RecetteTest recette) {
    return recetteRepository.save(recette);
  }

  public RecetteTest updateRecette(Long id, RecetteTest recette) {
    recette.setId(id);
    return recetteRepository.save(recette);
  }

  public void deleteRecette(Long id) {
    recetteRepository.deleteById(id);
  }
}
