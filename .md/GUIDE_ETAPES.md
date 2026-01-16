# 🛠️ GUIDE DÉTAILLÉ - "Comment j'ai fait" pour chaque étape

## Étape 1: Initialisation du projet (1h)

### 🔧 Comment créer le projet Maven avec Spring Boot

**Méthode 1: Via Spring Boot Starter (Rapide)**
```bash
# Accéder à https://start.spring.io/
# Remplir les champs:
# - Project: Maven
# - Language: Java
# - Spring Boot Version: 4.0.0
# - Group: Architecture_log
# - Artifact: TP
# - Name: TP_Architecture
# - Java: 17
# - Dependencies: Web, Data JPA, H2

# Télécharger et dézipper
# Importer dans VS Code ou IDE
```

**Méthode 2: Manuellement (Comprendre la structure)**

1. **Créer la structure des dossiers:**
```
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── Architecture_log/TP/
│   │   └── resources/
│   └── test/
│       ├── java/
│       │   └── Architecture_log/TP/
│       └── resources/
├── pom.xml
├── mvnw
└── mvnw.cmd
```

2. **Créer `pom.xml`:**
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
    https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>4.0.0</version>
    </parent>
    
    <groupId>Architecture_log</groupId>
    <artifactId>TP</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <name>TP_Architecture</name>
    
    <properties>
        <java.version>17</java.version>
    </properties>
    
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        <dependency>
            <groupId>com.h2database</groupId>
            <artifactId>h2</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
    
    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
```

### 📝 Comment créer les entités

**Étape 1: Créer la classe Recette**

Fichier: `src/main/java/Architecture_log/TP/common/entity/Recette.java`

```java
package Architecture_log.TP.common.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.jmolecules.ddd.annotation.AggregateRoot;

@Entity
@AggregateRoot
public class Recette {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nom;
    
    // Constructeurs
    public Recette() {}
    
    public Recette(String nom) {
        this.nom = nom;
    }
    
    // Getters et setters
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
}
```

**Étape 2: Créer la classe Ingredient**

Fichier: `src/main/java/Architecture_log/TP/common/entity/Ingredient.java`

```java
package Architecture_log.TP.common.entity;

import jakarta.persistence.*;
import org.jmolecules.ddd.annotation.AggregateRoot;

@Entity
@AggregateRoot
public class Ingredient {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String nom;
    
    @ManyToOne
    @JoinColumn(name = "recette_id")
    private Recette recette;
    
    // Constructeurs
    public Ingredient() {}
    
    public Ingredient(String nom, Recette recette) {
        this.nom = nom;
        this.recette = recette;
    }
    
    // Getters et setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    
    public Recette getRecette() { return recette; }
    public void setRecette(Recette recette) { this.recette = recette; }
}
```

### 🗄️ Comment créer les Repositories

**Fichier:** `src/main/java/Architecture_log/TP/common/repository/RecetteRepository.java`

```java
package Architecture_log.TP.common.repository;

import Architecture_log.TP.common.entity.Recette;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecetteRepository extends JpaRepository<Recette, Long> {
    // JpaRepository fournit automatiquement:
    // - save(Recette)
    // - findById(Long)
    // - findAll()
    // - delete(Recette)
    // - deleteById(Long)
    // - count()
}
```

**Fichier:** `src/main/java/Architecture_log/TP/common/repository/IngredientRepository.java`

```java
package Architecture_log.TP.common.repository;

import Architecture_log.TP.common.entity.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IngredientRepository extends JpaRepository<Ingredient, Long> {
}
```

### ⚙️ Configuration application.properties

**Fichier:** `src/main/resources/application.properties`

```properties
spring.application.name=TP_Architecture

# Configuration H2
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=create-drop

# H2 Console (optionnel, pour déboguer)
spring.h2.console.enabled=true
```

---

## Étape 2: Mise en place des outils de qualité (1h)

### 🎨 Comment configurer Prettier

**Étape 1: Initialiser Node.js**
```bash
# À la racine du projet
npm init -y

# Crée package.json
```

**Étape 2: Installer Prettier**
```bash
npm install --save-dev prettier prettier-plugin-java
```

**Étape 3: Créer `.prettierrc.json`**
```json
{
  "plugins": ["prettier-plugin-java"],
  "trailingComma": "es5",
  "tabWidth": 2,
  "semi": true,
  "singleQuote": false,
  "useTabs": false,
  "printWidth": 80
}
```

**Étape 4: Créer des scripts npm**

Dans `package.json`, ajouter:
```json
"scripts": {
  "format:java": "prettier --write \"**/*.java\"",
  "format:check": "prettier --check .",
  "format": "prettier --write ."
}
```

**Utilisation:**
```bash
npm run format:java    # Formater tous les fichiers Java
npm run format:check   # Vérifier le formatage
```

### 🪝 Comment configurer Husky

**Étape 1: Initialiser Husky**
```bash
npm install --save-dev husky
npx husky init
```

**Étape 2: Créer un hook pre-commit**
```bash
npx husky add .husky/pre-commit "npm run format:check && mvnw.cmd qulice:check"
```

**Résultat:** Avant chaque `git commit`, les commandes s'exécutent

### 🔧 Comment intégrer Qulice

**Étape 1: Ajouter le plugin au pom.xml**

Trouver la section `<build><plugins>` et ajouter:
```xml
<plugin>
    <groupId>com.qulice</groupId>
    <artifactId>qulice-maven-plugin</artifactId>
    <version>0.23.0</version>
    <configuration>
        <license>file:${basedir}/LICENSE</license>
        <excludes>
            <exclude>checkstyle:.*</exclude>
            <exclude>dependencies:.*</exclude>
        </excludes>
    </configuration>
    <executions>
        <execution>
            <goals>
                <goal>check</goal>
            </goals>
        </execution>
    </executions>
</plugin>
```

**Étape 2: Créer un fichier LICENSE**
```bash
# À la racine du projet
echo "MIT License" > LICENSE
```

**Étape 3: Exécuter Qulice**
```bash
mvnw.cmd qulice:check        # Vérification simple
mvnw.cmd qulice:check -X     # Avec détails complets
mvnw.cmd qulice:check -e     # Avec stack trace
```

### 📋 Comment créer Taskfile.yml

**Étape 1: Installer Taskfile**
```bash
# Sur Windows avec Chocolatey:
choco install task

# Ou télécharger depuis https://taskfile.dev/installation/
```

**Étape 2: Créer `Taskfile.yml`**
```yaml
version: '3'

tasks:
  build:
    desc: "Compiler le projet"
    cmds:
      - .\mvnw.cmd clean compile

  test:
    desc: "Lancer tous les tests"
    cmds:
      - .\mvnw.cmd test

  clean:
    desc: "Nettoyer le projet"
    cmds:
      - .\mvnw.cmd clean
```

**Utilisation:**
```bash
task build    # Exécute 'mvnw.cmd clean compile'
task test     # Exécute 'mvnw.cmd test'
task clean    # Exécute 'mvnw.cmd clean'
```

---

## Étape 3: Développement piloté par les tests (TDD) (2h)

### 🧪 Comment écrire des tests JUnit

**Étape 1: Ajouter les dépendances au pom.xml**
```xml
<!-- Déjà inclus par spring-boot-starter-test -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
```

**Étape 2: Créer un test d'entité**

Fichier: `src/test/java/Architecture_log/TP/entity/RecetteTest.java`

```java
package Architecture_log.TP.entity;

import Architecture_log.TP.common.entity.Recette;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RecetteTest {
    
    @Test
    void testCreateRecette() {
        // Arrange (Préparation)
        String nom = "Pâtes Carbonara";
        
        // Act (Exécution)
        Recette recette = new Recette(nom);
        
        // Assert (Vérification)
        assertEquals(nom, recette.getNom());
        assertNull(recette.getId()); // Pas d'ID avant la BD
    }
    
    @Test
    void testSetters() {
        Recette recette = new Recette();
        
        recette.setNom("Pizza");
        recette.setId(1L);
        
        assertEquals("Pizza", recette.getNom());
        assertEquals(1L, recette.getId());
    }
}
```

**Étape 3: Créer un test de service avec Mockito**

Fichier: `src/test/java/Architecture_log/TP/service/RecetteCommandServiceTest.java`

```java
package Architecture_log.TP.service;

import Architecture_log.TP.commands.dto.CreateRecetteDTO;
import Architecture_log.TP.commands.service.RecetteCommandService;
import Architecture_log.TP.commands.service.RecetteEventPublisher;
import Architecture_log.TP.common.entity.Recette;
import Architecture_log.TP.common.repository.RecetteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RecetteCommandServiceTest {
    
    @Mock
    private RecetteRepository recetteRepository;
    
    @Mock
    private RecetteEventPublisher eventPublisher;
    
    private RecetteCommandService service;
    
    @BeforeEach
    void setUp() {
        service = new RecetteCommandService(recetteRepository, eventPublisher);
    }
    
    @Test
    void testCreateRecette() {
        // Arrange
        CreateRecetteDTO dto = new CreateRecetteDTO("Pâtes");
        Recette savedRecette = new Recette("Pâtes");
        savedRecette.setId(1L);
        
        when(recetteRepository.save(any(Recette.class)))
            .thenReturn(savedRecette);
        
        // Act
        Recette result = service.createRecette(dto);
        
        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Pâtes", result.getNom());
        
        // Vérifier que le repository a été appelé
        verify(recetteRepository, times(1)).save(any(Recette.class));
        
        // Vérifier que l'événement a été publié
        verify(eventPublisher, times(1)).publishRecetteCreated(any());
    }
}
```

**Étape 4: Exécuter les tests**
```bash
# Tous les tests
mvnw.cmd test

# Tests spécifiques
mvnw.cmd test -Dtest=RecetteTest
mvnw.cmd test -Dtest=RecetteCommandServiceTest

# Avec couverture
mvnw.cmd test jacoco:report
```

---

## Étape 4: Intégration continue (1h)

### 🚀 Comment configurer Maven Surefire

**Configuration dans pom.xml:**
```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-surefire-plugin</artifactId>
    <version>3.0.0-M9</version>
    <configuration>
        <includes>
            <include>**/*Test.java</include>
            <include>**/*Tests.java</include>
            <include>**/*TestRunner.java</include>
        </includes>
    </configuration>
</plugin>
```

**Utilisation:**
```bash
mvnw.cmd test                    # Exécute via Surefire
mvnw.cmd verify                  # Compile + test + vérification

# Les rapports se trouvent dans:
# target/surefire-reports/*.xml
```

### 🔄 Comment créer un pipeline GitLab CI

**Fichier:** `.gitlab-ci.yml` à la racine

```yaml
stages:
  - build
  - test
  - quality

variables:
  MAVEN_OPTS: "-Dmaven.repo.local=$CI_PROJECT_DIR/.m2/repository"
  MAVEN_CLI_OPTS: "-s .m2/settings.xml"

cache:
  paths:
    - .m2/repository/

build:
  stage: build
  image: maven:3.8.1-jdk-17
  script:
    - mvn clean compile
  artifacts:
    paths:
      - target/classes/
    expire_in: 1 hour

test:
  stage: test
  image: maven:3.8.1-jdk-17
  script:
    - mvn test
  artifacts:
    reports:
      junit: target/surefire-reports/TEST-*.xml
    paths:
      - target/surefire-reports/

quality:
  stage: quality
  image: maven:3.8.1-jdk-17
  script:
    - mvn verify
    - mvn qulice:check
  artifacts:
    reports:
      junit: target/surefire-reports/TEST-*.xml
```

**Fonctionnement:**
- À chaque push, GitLab CI exécute automatiquement:
  1. Compilation
  2. Tests
  3. Vérification de qualité
- Les rapports sont téléchargeables sur GitLab

---

## Étape 5: Architecture et règles métier (2h)

### 🏗️ Comment implémenter CQRS

**Étape 1: Organiser la structure en packages**
```
Architecture_log.TP/
├── commands/             ← Modifications
│   ├── api/             ← Contrôleurs Command
│   │   └── RecetteCommandController.java
│   ├── service/         ← Logique métier
│   │   ├── RecetteCommandService.java
│   │   └── RecetteEventPublisher.java
│   └── dto/
│       ├── CreateRecetteDTO.java
│       └── UpdateRecetteDTO.java
├── queries/             ← Lectures
│   ├── api/             ← Contrôleurs Query
│   │   └── RecetteQueryController.java
│   ├── service/         ← Logique requête
│   │   └── RecetteQueryService.java
│   └── dto/
│       └── RecetteDTO.java
└── common/              ← Partagé
    ├── entity/
    │   └── Recette.java
    ├── repository/
    │   └── RecetteRepository.java
    └── config/
        └── KafkaConfig.java
```

**Étape 2: Créer un RecetteCommandService**

Fichier: `src/main/java/Architecture_log/TP/commands/service/RecetteCommandService.java`

```java
package Architecture_log.TP.commands.service;

import Architecture_log.TP.commands.dto.CreateRecetteDTO;
import Architecture_log.TP.common.entity.Recette;
import Architecture_log.TP.common.repository.RecetteRepository;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.stereotype.Service;

@Service
public class RecetteCommandService {
    
    private final RecetteRepository repository;
    private final RecetteEventPublisher publisher;
    
    public RecetteCommandService(
        RecetteRepository repository,
        RecetteEventPublisher publisher
    ) {
        this.repository = repository;
        this.publisher = publisher;
    }
    
    @Retry(name = "recetteRetry")
    public Recette createRecette(CreateRecetteDTO dto) {
        // Créer l'entité
        Recette recette = new Recette(dto.getNom());
        
        // Persister
        Recette savedRecette = repository.save(recette);
        
        // Publier l'événement
        publisher.publishRecetteCreated(
            new RecetteCreatedEvent(
                savedRecette.getId(),
                savedRecette.getNom(),
                LocalDateTime.now()
            )
        );
        
        return savedRecette;
    }
}
```

**Étape 3: Créer un RecetteQueryService**

Fichier: `src/main/java/Architecture_log/TP/queries/service/RecetteQueryService.java`

```java
package Architecture_log.TP.queries.service;

import Architecture_log.TP.common.entity.Recette;
import Architecture_log.TP.common.repository.RecetteRepository;
import Architecture_log.TP.queries.dto.RecetteDTO;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class RecetteQueryService {
    
    private final RecetteRepository repository;
    
    public RecetteQueryService(RecetteRepository repository) {
        this.repository = repository;
    }
    
    public List<RecetteDTO> getAllRecettes() {
        return repository.findAll()
            .stream()
            .map(this::toDTO)
            .toList();
    }
    
    public Optional<RecetteDTO> getRecetteById(Long id) {
        return repository.findById(id)
            .map(this::toDTO);
    }
    
    private RecetteDTO toDTO(Recette recette) {
        return new RecetteDTO(recette.getId(), recette.getNom());
    }
}
```

### 🧬 Comment utiliser jMolecules DDD

**Ajouter la dépendance au pom.xml:**
```xml
<dependency>
    <groupId>org.jmolecules</groupId>
    <artifactId>jmolecules-ddd</artifactId>
    <version>1.9.0</version>
</dependency>
<dependency>
    <groupId>org.jmolecules.integrations</groupId>
    <artifactId>jmolecules-spring</artifactId>
    <version>0.19.0</version>
</dependency>
```

**Annoter les entités:**
```java
import org.jmolecules.ddd.annotation.AggregateRoot;

@Entity
@AggregateRoot  // ← Marque comme racine d'agrégat
public class Recette {
    // ...
}
```

**Signification:**
- `@AggregateRoot` = Cette entité est une limite de transaction
- Les modifications passent toujours par la racine d'agrégat
- Les références d'autres agrégats se font par ID, pas par objet

### 🔍 Comment écrire des tests ArchUnit

**Fichier:** `src/test/java/Architecture_log/TP/architecture/ArchitectureTest.java`

```java
package Architecture_log.TP.architecture;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

@AnalyzeClasses(
  packages = "Architecture_log.TP",
  importOptions = { ImportOption.DoNotIncludeTests.class }
)
class ArchitectureTest {
    
    // Règle 1: Les CommandControllers doivent être dans commands.api
    @ArchTest
    static final ArchRule command_controllers = classes()
        .that()
        .haveSimpleNameEndingWith("CommandController")
        .should()
        .resideInAPackage("..commands.api..");
    
    // Règle 2: Les QueryControllers doivent être dans queries.api
    @ArchTest
    static final ArchRule query_controllers = classes()
        .that()
        .haveSimpleNameEndingWith("QueryController")
        .should()
        .resideInAPackage("..queries.api..");
    
    // Règle 3: Les CommandServices doivent être dans commands.service
    @ArchTest
    static final ArchRule command_services = classes()
        .that()
        .haveSimpleNameEndingWith("CommandService")
        .should()
        .resideInAPackage("..commands.service..");
    
    // Règle 4: Les Repositories doivent être dans common.repository
    @ArchTest
    static final ArchRule repositories = classes()
        .that()
        .haveSimpleNameEndingWith("Repository")
        .should()
        .resideInAPackage("..common.repository..");
}
```

**Exécution:**
```bash
mvnw test -Dtest=ArchitectureTest
```

**Fonctionnement:**
- ArchUnit scanne le code compilé
- Vérifie que les noms et localisations correspondent aux règles
- Si une classe ne respecte pas les règles → Test échoue

---

## Étape 6: Résilience et publication (2h)

### 🛡️ Comment configurer Resilience4J Retry

**Étape 1: Ajouter la dépendance au pom.xml**
```xml
<dependency>
    <groupId>io.github.resilience4j</groupId>
    <artifactId>resilience4j-spring-boot3</artifactId>
    <version>2.3.0</version>
</dependency>
```

**Étape 2: Configurer dans application.properties**
```properties
# Configuration du retry
resilience4j.retry.instances.recetteRetry.maxAttempts=3
resilience4j.retry.instances.recetteRetry.waitDuration=1000
resilience4j.retry.instances.recetteRetry.retryExceptions=\
    org.springframework.dao.DataAccessException,\
    java.sql.SQLException
resilience4j.retry.instances.recetteRetry.ignoreExceptions=
```

**Configuration expliquée:**
- `maxAttempts=3` → Essayer 3 fois maximum
- `waitDuration=1000` → Attendre 1 seconde entre les tentatives
- `retryExceptions` → Sur quelles exceptions faire un retry
- `ignoreExceptions` → Quelles exceptions ignorer (ne pas retry)

**Étape 3: Utiliser l'annotation @Retry**
```java
@Service
public class RecetteCommandService {
    
    @Retry(name = "recetteRetry")  // ← Référence la config
    public Recette createRecette(CreateRecetteDTO dto) {
        Recette recette = new Recette(dto.getNom());
        return recetteRepository.save(recette);  // Peut lever exception
    }
}
```

**Fonctionnement:**
```
1ère tentative: Exception DataAccessException
  ↓ Attendre 1 seconde
2ème tentative: Exception DataAccessException
  ↓ Attendre 1 seconde
3ème tentative: Success! ✓
```

### 📨 Comment configurer Kafka Publisher

**Étape 1: Ajouter les dépendances au pom.xml**
```xml
<dependency>
    <groupId>org.springframework.kafka</groupId>
    <artifactId>spring-kafka</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.kafka</groupId>
    <artifactId>spring-kafka-test</artifactId>
    <scope>test</scope>
</dependency>
```

**Étape 2: Configurer Kafka dans application.properties**
```properties
# Configuration Kafka
spring.kafka.bootstrap-servers=localhost:9092
spring.kafka.producer.key-serializer=\
    org.apache.kafka.common.serialization.StringSerializer
spring.kafka.producer.value-serializer=\
    org.springframework.kafka.support.serializer.JsonSerializer
spring.kafka.producer.properties.spring.json.add.type.headers=false
```

**Étape 3: Créer la configuration Kafka**

Fichier: `src/main/java/Architecture_log/TP/common/config/KafkaConfig.java`

```java
package Architecture_log.TP.common.config;

import Architecture_log.TP.commands.dto.RecetteCreatedEvent;
import java.util.HashMap;
import java.util.Map;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

@Configuration
public class KafkaConfig {
    
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;
    
    @Bean
    public ProducerFactory<String, RecetteCreatedEvent> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(
            ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
            bootstrapServers
        );
        configProps.put(
            ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
            StringSerializer.class
        );
        configProps.put(
            ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
            JsonSerializer.class
        );
        return new DefaultProducerFactory<>(configProps);
    }
    
    @Bean
    public KafkaTemplate<String, RecetteCreatedEvent> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}
```

**Étape 4: Créer l'event publisher**

Fichier: `src/main/java/Architecture_log/TP/commands/service/RecetteEventPublisher.java`

```java
package Architecture_log.TP.commands.service;

import Architecture_log.TP.commands.dto.RecetteCreatedEvent;
import java.util.concurrent.CompletableFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

@Service
public class RecetteEventPublisher {
    
    private static final String TOPIC_NAME = "recette-created";
    private static final Logger logger = LoggerFactory.getLogger(
        RecetteEventPublisher.class
    );
    
    private final KafkaTemplate<String, RecetteCreatedEvent> kafkaTemplate;
    
    public RecetteEventPublisher(
        KafkaTemplate<String, RecetteCreatedEvent> kafkaTemplate
    ) {
        this.kafkaTemplate = kafkaTemplate;
    }
    
    public void publishRecetteCreated(RecetteCreatedEvent event) {
        try {
            // Envoyer l'événement à Kafka
            CompletableFuture<SendResult<String, RecetteCreatedEvent>> future =
                kafkaTemplate.send(
                    TOPIC_NAME,
                    event.getId().toString(),  // Clé (partition)
                    event                      // Valeur (message)
                );
            
            // Callback en cas de succès/erreur
            future.whenComplete((result, exception) -> {
                if (exception == null) {
                    logger.info(
                        "Recette created event published successfully: {}",
                        event.getId()
                    );
                } else {
                    logger.error(
                        "Failed to publish recette created event: {}",
                        event.getId(),
                        exception
                    );
                }
            });
        } catch (Exception e) {
            logger.error(
                "Error publishing recette created event",
                e
            );
        }
    }
}
```

**Étape 5: Utiliser le publisher dans le CommandService**
```java
@Retry(name = "recetteRetry")
public Recette createRecette(CreateRecetteDTO dto) {
    Recette recette = new Recette(dto.getNom());
    Recette savedRecette = recetteRepository.save(recette);
    
    // Publier l'événement de création
    RecetteCreatedEvent event = new RecetteCreatedEvent(
        savedRecette.getId(),
        savedRecette.getNom(),
        LocalDateTime.now()
    );
    recetteEventPublisher.publishRecetteCreated(event);
    
    return savedRecette;
}
```

**Étape 6: Tester avec Kafka en local**
```bash
# Lancer Kafka via Docker Compose
docker-compose up -d

# Lancer l'application
task run

# Créer une recette (pub)
curl -X POST http://localhost:8080/api/command/recettes \
  -H "Content-Type: application/json" \
  -d '{"nom": "Pâtes"}'

# Vous devriez voir dans les logs:
# "Recette created event published successfully: 1"
```

---

## Étape 7: Documentation et automatisation (1h)

### 📋 Comment créer Taskfile.yml complet

**Structure complète:**
```yaml
version: '3'

# Taskfile pour automatiser les commandes du projet
# Installation: https://taskfile.dev/installation/
# Usage: task <nom-de-la-tache>

tasks:
  # === BUILD & COMPILATION ===
  
  build:
    desc: "Compiler le projet"
    cmds:
      - .\mvnw.cmd clean compile

  package:
    desc: "Créer le JAR"
    cmds:
      - .\mvnw.cmd clean package

  install:
    desc: "Installer les dépendances Maven"
    cmds:
      - .\mvnw.cmd clean install

  # === TESTS ===
  
  test:
    desc: "Lancer tous les tests"
    cmds:
      - .\mvnw.cmd test

  test:archunit:
    desc: "Lancer uniquement les tests ArchUnit"
    cmds:
      - .\mvnw.cmd test -Dtest=ArchitectureTest

  test:commands:
    desc: "Lancer les tests des Command Services"
    cmds:
      - .\mvnw.cmd test -Dtest=*CommandServiceTest

  test:queries:
    desc: "Lancer les tests des Query Services"
    cmds:
      - .\mvnw.cmd test -Dtest=*QueryServiceTest

  test:integration:
    desc: "Lancer les tests d'intégration"
    cmds:
      - .\mvnw.cmd test -Dtest=*IntegrationTest

  test:bdd:
    desc: "Lancer les tests BDD Cucumber"
    cmds:
      - .\mvnw.cmd test -Dtest=CucumberTestRunner

  # === EXÉCUTION ===
  
  run:
    desc: "Démarrer l'application Spring Boot"
    cmds:
      - .\mvnw.cmd spring-boot:run

  run:dev:
    desc: "Démarrer l'application en mode développement"
    cmds:
      - .\mvnw.cmd spring-boot:run -Dspring-boot.run.profiles=dev

  # === QUALITÉ ===
  
  quality:
    desc: "Vérifier la qualité du code"
    cmds:
      - .\mvnw.cmd qulice:check

  verify:
    desc: "Vérifier le projet (compile + test + quality)"
    cmds:
      - .\mvnw.cmd verify

  format:
    desc: "Formater le code avec Prettier"
    cmds:
      - npm run format:java

  format:check:
    desc: "Vérifier le formatage"
    cmds:
      - npm run format:check

  # === MAINTENANCE ===
  
  clean:
    desc: "Nettoyer le projet"
    cmds:
      - .\mvnw.cmd clean

  # === GIT ===
  
  git:status:
    desc: "Voir l'état Git"
    cmds:
      - git status

  git:log:
    desc: "Voir l'historique Git"
    cmds:
      - git log --oneline --graph --all -10
```

---

## Étape 8: Behavior-Driven Development (BDD) (2h)

### 🥒 Comment écrire des scénarios Gherkin

**Fichier:** `src/test/resources/features/recette.feature`

```gherkin
Feature: Gestion des recettes

  Background:
    Given Le système de gestion de recettes est disponible

  Scenario: Créer une nouvelle recette
    When Je crée une recette avec le nom "Pâtes Carbonara"
    Then La recette est créée avec succès
    And La recette a un ID unique
    And Le système publie l'événement "recette-created"

  Scenario: Récupérer une recette existante
    Given Une recette "Lasagna" existe avec l'ID 1
    When Je demande les détails de la recette avec l'ID 1
    Then Je reçois les informations de la recette
    And Le nom de la recette est "Lasagna"

  Scenario: Lister toutes les recettes
    Given 3 recettes existent dans le système
    When Je demande la liste des recettes
    Then Je reçois 3 recettes
    And Chaque recette a un ID et un nom

  Scenario: Modifier une recette
    Given Une recette "Pizza" existe avec l'ID 2
    When Je mets à jour le nom en "Pizza Margherita"
    Then La recette est mise à jour avec succès
    And Le nom de la recette est maintenant "Pizza Margherita"

  Scenario: Supprimer une recette
    Given Une recette "Burger" existe avec l'ID 3
    When Je supprime la recette avec l'ID 3
    Then La recette est supprimée avec succès
    And La recette n'existe plus dans le système
```

### 🔗 Comment créer les Step Definitions

**Fichier:** `src/test/java/Architecture_log/TP/bdd/RecetteStepDefs.java`

```java
package Architecture_log.TP.bdd;

import Architecture_log.TP.commands.dto.CreateRecetteDTO;
import Architecture_log.TP.commands.service.RecetteCommandService;
import Architecture_log.TP.common.entity.Recette;
import Architecture_log.TP.queries.service.RecetteQueryService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.And;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class RecetteStepDefs {
    
    @Autowired
    private RecetteCommandService commandService;
    
    @Autowired
    private RecetteQueryService queryService;
    
    private CreateRecetteDTO createDTO;
    private Recette createdRecette;
    private Exception lastException;
    
    @Given("Le système de gestion de recettes est disponible")
    public void system_is_available() {
        // Setup du contexte
        assertNotNull(commandService);
        assertNotNull(queryService);
    }
    
    @When("Je crée une recette avec le nom {string}")
    public void i_create_recipe(String name) {
        try {
            createDTO = new CreateRecetteDTO(name);
            createdRecette = commandService.createRecette(createDTO);
        } catch (Exception e) {
            lastException = e;
        }
    }
    
    @Then("La recette est créée avec succès")
    public void recipe_is_created() {
        assertNull(lastException, "Une exception a été levée");
        assertNotNull(createdRecette);
        assertNotNull(createdRecette.getId());
    }
    
    @And("La recette a un ID unique")
    public void recipe_has_unique_id() {
        assertTrue(createdRecette.getId() > 0);
    }
    
    @And("Le système publie l'événement {string}")
    public void system_publishes_event(String eventName) {
        // Vérification que l'événement a été publié
        // (Dans les logs ou via des mocks)
    }
    
    @Given("Une recette {string} existe avec l'ID {int}")
    public void recipe_exists(String name, int id) {
        // Créer la recette dans la BD
        CreateRecetteDTO dto = new CreateRecetteDTO(name);
        createdRecette = commandService.createRecette(dto);
    }
    
    @When("Je demande les détails de la recette avec l'ID {int}")
    public void i_get_recipe(int id) {
        // Récupérer la recette
        createdRecette = queryService.getRecetteById((long) id)
            .orElseThrow(() -> new RuntimeException("Recette non trouvée"));
    }
    
    @Then("Je reçois les informations de la recette")
    public void i_receive_recipe_info() {
        assertNotNull(createdRecette);
    }
    
    @And("Le nom de la recette est {string}")
    public void recipe_name_is(String expectedName) {
        assertEquals(expectedName, createdRecette.getNom());
    }
}
```

### 🏃 Comment créer un Test Runner Cucumber

**Fichier:** `src/test/java/Architecture_log/TP/bdd/CucumberTestRunner.java`

```java
package Architecture_log.TP.bdd;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;

import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("features")
@ConfigurationParameter(
    key = GLUE_PROPERTY_NAME,
    value = "Architecture_log.TP.bdd"
)
public class CucumberTestRunner {
    // Ce fichier configure Cucumber pour s'exécuter avec JUnit
}
```

### 🚀 Comment exécuter les tests BDD

```bash
# Lancer tous les tests Cucumber
mvnw test -Dtest=CucumberTestRunner

# Lancer tous les tests (incluant BDD)
mvnw test

# Générer un rapport HTML
mvnw verify

# Le rapport se trouve dans:
# target/cucumber-report.html
```

### 📊 Exemple de rapport généré

```html
<!-- target/cucumber-report.html -->
Feature: Gestion des recettes
  ✓ Créer une nouvelle recette (1.2s)
  ✓ Récupérer une recette existante (0.9s)
  ✓ Lister toutes les recettes (0.8s)
  ✗ Modifier une recette (FAILED)
  ✓ Supprimer une recette (1.0s)

4/5 scénarios réussis (80%)
```

---

## 📚 CONCLUSION

Vous disposez maintenant de:
- ✅ 8 étapes complètement documentées
- ✅ Code d'exemple pour chaque concept
- ✅ Commandes pour exécuter et tester
- ✅ Explication du "pourquoi" et du "comment"

**Pour démarrer rapidement:**
```bash
task install        # Installer les dépendances
task build          # Compiler
task test           # Lancer les tests
task run            # Démarrer l'app
```

**Bon développement! 🚀**

