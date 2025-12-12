package Architecture_log.TP.service;

import Architecture_log.TP.entity.Recette;
import Architecture_log.TP.repository.RecetteRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class RecetteService {

  private final RecetteRepository recetteRepository;

  public RecetteService(RecetteRepository recetteRepository) {
    this.recetteRepository = recetteRepository;
  }

  public List<Recette> getAllRecettes() {
    return recetteRepository.findAll();
  }

  public Recette getRecetteById(Long id){
    return recetteRepository.getReferenceById(id);
  }

  public Recette createRecette(Recette recette) {
    return recetteRepository.save(recette);
  }

  public Recette updateRecette(Long id, Recette recette) {
    recette.setId(id);
    return recetteRepository.save(recette);
  }

  public void deleteRecette(Long id) {
    recetteRepository.deleteById(id);
  }
}
