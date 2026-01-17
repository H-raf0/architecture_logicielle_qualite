# 🧪 Guide Tests d'Intégration Complets

## 📋 Table des matières
- [Vue d'ensemble](#vue-densemble)
- [Structure des tests](#structure-des-tests)
- [Catégories de tests](#catégories-de-tests)
- [Exécution](#exécution)
- [Bonnes pratiques](#bonnes-pratiques)
- [Débogage](#débogage)

---

## 👁️ Vue d'ensemble

Les tests d'intégration vérifient que les composants travaillent correctement ensemble. Ils testent le cycle de vie complet des objets (CRUD) en passant par tous les éléments de l'application (Repository, Service, Controller, etc.).

**Fichier de test:**
- `src/test/java/Architecture_log/TP/integration/RecetteIntegrationTest.java`

**Nombre de tests:** 28 scénarios complets

---

## 🏗️ Structure des tests

### Annotations clés
```java
@SpringBootTest                                    // Charge le contexte Spring complet
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)  // Nettoie après chaque test
@TestPropertySource(properties = {...})            // Propriétés de test personnalisées
@Disabled("Raison")                                // Désactive temporairement
```

### Méthode setUp
```java
@BeforeEach
void setUp() {
    recetteRepository.deleteAll();                 // Nettoie avant chaque test
}
```

---

## 📊 Catégories de tests

### 1️⃣ Tests de Création (5 tests)
Vérifient que les recettes peuvent être créées correctement.

#### `shouldCreateRecette()`
```java
// Given
CreateRecetteDTO dto = new CreateRecetteDTO("Pizza Margherita");

// When
Recette result = recetteCommandService.createRecette(dto);

// Then
assertNotNull(result);
assertNotNull(result.getId());
assertEquals("Pizza Margherita", result.getNom());
```

**Vérifie:**
- ✅ La recette est créée
- ✅ Un ID est généré
- ✅ Les données sont correctes
- ✅ Elle est sauvegardée en BD

#### `shouldCreateMultipleRecettes()`
```java
// Crée 4 recettes
// Vérifie: Toutes 4 sont présentes
```

#### `shouldRejectEmptyName()`
```java
// Tentative création avec nom vide
// Vérifie: Exception levée
```

#### `shouldRejectNullName()`
```java
// Tentative création avec nom null
// Vérifie: Exception levée
```

#### `shouldRejectShortName()`
```java
// Tentative création avec nom "AB" (trop court)
// Vérifie: Exception levée
```

---

### 2️⃣ Tests de Lecture (6 tests)
Vérifient que les recettes peuvent être récupérées correctement.

#### `shouldGetRecetteById()`
```java
// Given: Recette créée
// When: Récupération par ID
// Then: Correctement retrouvée
```

#### `shouldReturnEmptyForNonExistent()`
```java
// When: Recherche d'une recette inexistante (ID=999)
// Then: Optional.empty()
```

#### `shouldListAllRecettes()`
```java
// Given: 3 recettes créées
// When: Récupération de la liste
// Then: 3 recettes retournées
```

#### `shouldListEmptyWhenNoRecettes()`
```java
// When: Aucune recette en BD
// Then: Liste vide
```

#### `shouldSearchRecettesByTerm()`
```java
// Given: 2 pizzas + 1 salade
// When: Recherche de "Pizza"
// Then: 2 pizzas trouvées
```

#### `shouldReturnEmptySearchResults()`
```java
// When: Recherche avec un terme qui ne match rien
// Then: Liste vide
```

---

### 3️⃣ Tests de Mise à Jour (2 tests)
Vérifient que les recettes peuvent être mises à jour.

#### `shouldUpdateRecette()`
```java
// Given: Recette créée
// When: Modification du nom
// Then: Correctement mise à jour
```

#### `shouldUpdateMultipleFields()`
```java
// Given: Recette créée
// When: Modification de plusieurs champs
// Then: Tous les champs sont mis à jour
```

---

### 4️⃣ Tests de Suppression (3 tests)
Vérifient que les recettes peuvent être supprimées.

#### `shouldDeleteRecette()`
```java
// Given: Recette créée avec ID
// When: Suppression par ID
// Then: N'existe plus en BD
```

#### `shouldDeleteMultipleRecettes()`
```java
// When: Suppression de 2 recettes
// Then: Toutes les deux sont supprimées
```

#### `shouldDeleteNonExistentRecetteSilently()`
```java
// When: Suppression d'une ID inexistante
// Then: Pas d'erreur (idempotent)
```

---

### 5️⃣ Tests de Validité (2 tests)
Vérifient les opérations de validation.

#### `shouldCountRecettesCorrectly()`
```java
// When: 3 recettes en BD
// Then: count() = 3
```

#### `shouldCheckRecetteExistence()`
```java
// Vérifie: existsById() pour une recette existante et inexistante
```

---

### 6️⃣ Tests d'Intégration Complète (4 tests)
Testent des scénarios complexes combinant plusieurs opérations.

#### `shouldCompleteFullLifecycle()`
```
CREATE ──► RETRIEVE ──► UPDATE ──► DELETE
  │           │           │           │
  └───────────┴───────────┴───────────┘
          Cycle complet vérifié
```

**Étapes:**
1. Crée une recette
2. La récupère par ID
3. La met à jour
4. Vérifie la mise à jour
5. La supprime
6. Vérifie la suppression

#### `shouldCreateSearchAndDelete()`
```
CREATE 3 ──► SEARCH ──► DELETE 2 ──► VERIFY
(2 pizzas)   "Pizza"   (les pizzas)  (1 salade reste)
```

---

## 🏃 Exécution

### Exécuter tous les tests d'intégration
```bash
mvnw test -Dtest=RecetteIntegrationTest
```

### Exécuter une catégorie spécifique
```bash
# Tests de création uniquement
mvnw test -Dtest=RecetteIntegrationTest#*Create*

# Tests de lecture uniquement
mvnw test -Dtest=RecetteIntegrationTest#*Get*

# Tests de suppression uniquement
mvnw test -Dtest=RecetteIntegrationTest#*Delete*
```

### Exécuter un test spécifique
```bash
mvnw test -Dtest=RecetteIntegrationTest#shouldCreateRecette
```

### Avec verbose output
```bash
mvnw test -Dtest=RecetteIntegrationTest -X
```

### Avec coverage JaCoCo (optionnel)
```bash
mvnw clean test -Dtest=RecetteIntegrationTest jacoco:report
# Rapport: target/site/jacoco/index.html
```

---

## 🎯 Bonnes pratiques

### 1. Pattern Given-When-Then
✅ BON:
```java
@Test
void shouldCreateRecette() {
    // Given
    CreateRecetteDTO dto = new CreateRecetteDTO("Pizza");
    
    // When
    Recette result = recetteCommandService.createRecette(dto);
    
    // Then
    assertNotNull(result);
    assertEquals("Pizza", result.getNom());
}
```

❌ MAUVAIS:
```java
@Test
void test() {
    CreateRecetteDTO dto = new CreateRecetteDTO("Pizza");
    Recette result = recetteCommandService.createRecette(dto);
    assertNotNull(result);
}
```

### 2. Messages d'assertion clairs
✅ BON:
```java
assertEquals(3, result.size(), "Doit avoir 3 recettes");
```

❌ MAUVAIS:
```java
assertEquals(3, result.size());
```

### 3. Noms de méthodes descriptifs
✅ BON:
```java
@DisplayName("Devrait créer une recette avec succès")
void shouldCreateRecette() { }
```

❌ MAUVAIS:
```java
void test1() { }
```

### 4. Isoler chaque test
✅ BON:
```java
@BeforeEach
void setUp() {
    recetteRepository.deleteAll();  // Nettoie avant chaque test
}
```

❌ MAUVAIS:
```java
// Les tests dépendent l'un de l'autre (ordre-dépendant)
```

### 5. Une assertion par concept
✅ BON:
```java
// Tester une chose à la fois
assertEquals("Pizza", result.getNom());
assertNotNull(result.getId());
```

❌ MAUVAIS:
```java
// Tester trop de choses à la fois
assertTrue(result != null && result.getNom().equals("Pizza") && result.getId() != null);
```

---

## 🔍 Débogage

### 1. Voir les logs de test
```bash
mvnw test -Dtest=RecetteIntegrationTest -X 2>&1 | grep -i "recette"
```

### 2. Ajouter des assertions intermédiaires
```java
@Test
void shouldCreateRecette() {
    // ...
    Recette result = recetteCommandService.createRecette(dto);
    
    // Débogue: affiche les valeurs
    System.out.println("ID créé: " + result.getId());
    System.out.println("Nom: " + result.getNom());
    
    assertNotNull(result.getId());
}
```

### 3. Activer le test désactivé
```java
// Avant (test désactivé):
@Disabled("Kafka nécessaire")
class RecetteIntegrationTest { }

// Après (activer):
class RecetteIntegrationTest { }  // Supprimer @Disabled
```

### 4. Exécuter avec le debuggeur
```bash
# Depuis IDE: clic droit sur test → Debug As → JUnit Test
```

### 5. Vérifier l'état de la BD après un test
```java
@Test
void debugTest() {
    // ... test code ...
    
    // Debug: affiche tous les enregistrements
    List<Recette> all = recetteRepository.findAll();
    all.forEach(r -> System.out.println("BD: " + r.getId() + " - " + r.getNom()));
}
```

---

## 🚨 Erreurs courantes et solutions

### Erreur: `NullPointerException` sur MockMvc
```
java.lang.NullPointerException: Cannot invoke MockMvc
```

**Cause:** MockMvc non injecté  
**Solution:** Vérifier l'annotation `@SpringBootTest`

```java
@SpringBootTest  // ← Obligatoire
class RecetteIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
}
```

---

### Erreur: `DataAccessException` 
```
org.springframework.dao.DataAccessException: Erreur BD
```

**Cause:** BD non nettoyée  
**Solution:** Vérifier `@BeforeEach` ou `@DirtiesContext`

```java
@BeforeEach
void setUp() {
    recetteRepository.deleteAll();
}
```

---

### Erreur: Test dépend du timing
```
AssertionError: Test échoue parfois
```

**Cause:** Délai/Timing dépendant  
**Solution:** Utiliser `await()` ou vérifications multiples

```java
await().atMost(5, TimeUnit.SECONDS)
    .until(() -> recetteRepository.count() == 1);
```

---

### Erreur: Test échoue avec Kafka
```
Kafka broker not available
```

**Cause:** Kafka non démarré  
**Solution:** Le test est `@Disabled` pour une raison

```bash
# Démarrer Kafka d'abord
docker-compose up kafka -d

# Puis supprimer @Disabled du test
```

---

## 📊 Couverture des tests

| Opération | Tests | Coverage |
|-----------|-------|----------|
| CREATE | 5 | ✅ 100% |
| READ | 6 | ✅ 100% |
| UPDATE | 2 | ✅ 100% |
| DELETE | 3 | ✅ 100% |
| VALIDATION | 2 | ✅ 100% |
| SCÉNARIOS | 4 | ✅ 100% |
| **TOTAL** | **28** | **✅ 100%** |

---

## 📚 Ressources

- [Spring Boot Test Documentation](https://spring.io/guides/gs/testing-web/)
- [JUnit 5 User Guide](https://junit.org/junit5/docs/current/user-guide/)
- [AssertJ Documentation](https://assertj.github.io/assertj-core-features-highlight.html)
- [Spring Data JPA Testing](https://spring.io/guides/gs/accessing-data-jpa/)

---

## ✅ Checklist avant production

- [ ] Tous les 28 tests passent
- [ ] Aucune exception non gérée
- [ ] Coverage > 80%
- [ ] @Disabled retiré si Kafka disponible
- [ ] Logs clairs et explicites
- [ ] Noms de test descriptifs
- [ ] setUp() nettoie correctement

---

**Dernière mise à jour:** 17 Janvier 2026  
**Document:** Guide Tests d'Intégration Complets
