package Architecture_log.TP;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * Point d'entrée de l'application Spring Boot pour le TP d'architecture.
 *
 * La classe démarre le contexte Spring et initialise l'application.
 * L'annotation @EnableAspectJAutoProxy active le support des annotations Resilience4J.
 */
@SpringBootApplication
@EnableAspectJAutoProxy
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
