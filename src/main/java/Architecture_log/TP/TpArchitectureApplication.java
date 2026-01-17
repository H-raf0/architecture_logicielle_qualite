package Architecture_log.TP;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Point d'entrée de l'application Spring Boot pour le TP d'architecture.
 *
 * La classe démarre le contexte Spring et initialise l'application.
 */
@SpringBootApplication
public class TpArchitectureApplication {

  /**
   * Démarre l'application.
   *
   * @param args arguments de la ligne de commande transmis à Spring Boot
   */
  public static void main(String[] args) {
    SpringApplication.run(TpArchitectureApplication.class, args);
  }
}
