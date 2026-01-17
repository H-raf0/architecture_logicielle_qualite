# 🧪 Guide BDD - Scénarios d'Erreur

## 📋 Table des matières
- [Vue d'ensemble](#vue-densemble)
- [Scénarios](#scénarios)
- [Exécution](#exécution)
- [Structure des step definitions](#structure-des-step-definitions)
- [Bonnes pratiques](#bonnes-pratiques)

---

## 👁️ Vue d'ensemble

Les scénarios BDD d'erreur valident que le système gère correctement les cas d'erreur et les edge cases. Chaque scénario teste une erreur spécifique du système.

**Fichiers concernés:**
- Feature: `src/test/resources/features/recette.feature`
- Step definitions: `src/test/java/Architecture_log/TP/bdd/RecetteStepDefs.java`

---

## 🎯 Scénarios

### 1. Erreur - Création avec nom vide
**Description:** Vérifier qu'on ne peut pas créer une recette sans nom

```gherkin
Scénario: Erreur lors de création avec nom vide
  Quand je crée une recette avec un nom vide
  Alors une erreur de validation est levée
  Et le message d'erreur contient "obligatoire"
```

**Exécution:**
```bash
mvnw test -Dtest=CucumberTestRunner -Dtest.scenario="Erreur lors de création avec nom vide"
```

**Comportement attendu:**
- HTTP Status: 400 (Bad Request)
- Message: "Le nom de la recette est obligatoire"

---

### 2. Erreur - Création avec nom trop court
**Description:** Vérifier la validation de la longueur minimale

```gherkin
Scénario: Erreur lors de création avec nom trop court
  Quand je crée une recette avec le nom "AB"
  Alors une erreur de validation est levée
  Et le message d'erreur contient "3 caractères"
```

**Exécution:**
```bash
mvnw test -Dtest=CucumberTestRunner -Dtest.scenario="Erreur lors de création avec nom trop court"
```

**Comportement attendu:**
- HTTP Status: 400
- Message: "Le nom doit avoir au minimum 3 caractères"

---

### 3. Erreur - Création avec nom trop long
**Description:** Vérifier la validation de la longueur maximale

```gherkin
Scénario: Erreur lors de création avec nom trop long
  Quand je crée une recette avec un nom de plus de 255 caractères
  Alors une erreur de validation est levée
  Et le message d'erreur contient "255 caractères"
```

**Exécution:**
```bash
mvnw test -Dtest=CucumberTestRunner -Dtest.scenario="Erreur lors de création avec nom trop long"
```

**Comportement attendu:**
- HTTP Status: 400
- Message: "Le nom ne doit pas dépasser 255 caractères"

---

### 4. Erreur - Récupération d'une recette inexistante
**Description:** Vérifier que récupérer une recette inexistante retourne 404

```gherkin
Scénario: Erreur lors de récupération d'une recette inexistante
  Quand je récupère la recette avec l'id 999
  Alors une erreur HTTP 404 est levée
  Et le message d'erreur contient "introuvable"
```

**Exécution:**
```bash
mvnw test -Dtest=CucumberTestRunner -Dtest.scenario="Erreur lors de récupération d'une recette inexistante"
```

**Comportement attendu:**
- HTTP Status: 404 (Not Found)
- Message: "La recette avec l'ID 999 est introuvable"

---

### 5. Erreur - Modification d'une recette inexistante
**Description:** Vérifier que modifier une recette inexistante retourne 404

```gherkin
Scénario: Erreur lors de modification d'une recette inexistante
  Quand je modifie la recette 999 avec le nom "Pizza"
  Alors une erreur HTTP 404 est levée
  Et le message d'erreur contient "introuvable"
```

**Exécution:**
```bash
mvnw test -Dtest=CucumberTestRunner -Dtest.scenario="Erreur lors de modification d'une recette inexistante"
```

**Comportement attendu:**
- HTTP Status: 404
- Message: "La recette avec l'ID 999 est introuvable"

---

### 6. Erreur - Suppression d'une recette inexistante
**Description:** Vérifier que supprimer une recette inexistante retourne 404

```gherkin
Scénario: Erreur lors de suppression d'une recette inexistante
  Quand je supprime la recette avec l'id 999
  Alors une erreur HTTP 404 est levée
  Et le message d'erreur contient "introuvable"
```

**Exécution:**
```bash
mvnw test -Dtest=CucumberTestRunner -Dtest.scenario="Erreur lors de suppression d'une recette inexistante"
```

**Comportement attendu:**
- HTTP Status: 404
- Message: "La recette avec l'ID 999 est introuvable"

---

### 7. Erreur - Gestion de l'erreur Kafka
**Description:** Vérifier que la création d'une recette ne bloque pas si Kafka est indisponible

```gherkin
Scénario: Gestion des erreurs lors de la publication d'événements Kafka
  Étant donné qu'une recette "Pizza" existe avec l'id 1
  Quand je crée une recette avec le nom "Pâtes"
  Alors la recette est créée avec succès
  Et l'erreur de publication Kafka est loggée mais ne bloque pas la création
```

**Exécution:**
```bash
mvnw test -Dtest=CucumberTestRunner -Dtest.scenario="Gestion des erreurs lors de la publication d'événements Kafka"
```

**Comportement attendu:**
- HTTP Status: 201 (Created)
- Recette créée en base de données
- Erreur Kafka loggée (log level: WARN ou ERROR)
- La recette n'est pas supprimée

---

## 🏃 Exécution

### Exécuter tous les tests BDD
```bash
mvnw test -Dtest=CucumberTestRunner
```

### Exécuter tous les tests BDD d'erreur
```bash
mvnw test -Dtest=CucumberTestRunner -Dtest.scenario="*erreur*"
```

### Exécuter un scénario spécifique
```bash
mvnw test -Dtest=CucumberTestRunner -Dtest.scenario="Erreur lors de création avec nom vide"
```

### Avec rapports
```bash
mvnw test -Dtest=CucumberTestRunner
# Rapport dans: target/cucumber-reports/
```

---

## 🔧 Structure des Step Definitions

### Variables d'instance importantes
```java
private MvcResult lastResult;           // Résultat de la requête HTTP
private int lastStatusCode;              // Code HTTP (200, 400, 404, etc.)
private Exception lastException;         // Exception levée (si applicable)
private String lastErrorMessage;         // Message d'erreur
```

### Pattern - Step de "Quand" (Actions)
```java
@Quand("je crée une recette avec un nom vide")
public void creRecetteNomVide() throws Exception {
    String json = "{\"nom\": \"\"}";
    lastResult = mockMvc
      .perform(
        post("/api/recettes")
          .contentType("application/json")
          .content(json)
      )
      .andReturn();
    lastStatusCode = lastResult.getResponse().getStatus();
}
```

### Pattern - Step d'assertion d'erreur
```java
@Alors("une erreur de validation est levée")
public void erreurValidationLevee() {
    assertThat(lastStatusCode).isEqualTo(400);
}

@Alors("le message d'erreur contient {string}")
public void messageErreurContient(String text) throws Exception {
    String content = lastResult.getResponse().getContentAsString();
    assertThat(content).contains(text);
}
```

---

## ✅ Bonnes pratiques

### 1. Nommer clairement les scénarios
✅ BON:
```gherkin
Scénario: Erreur lors de création avec nom vide
```

❌ MAUVAIS:
```gherkin
Scénario: Erreur nom
```

### 2. Tester une seule chose par scénario
✅ BON:
```gherkin
Scénario: Erreur lors de création avec nom vide
  Quand je crée une recette avec un nom vide
  Alors une erreur de validation est levée
```

❌ MAUVAIS:
```gherkin
Scénario: Validations
  Quand je crée une recette avec un nom vide
  Et je crée une recette avec un nom trop court
  Et je crée une recette avec un nom trop long
  Alors trois erreurs sont levées
```

### 3. Utiliser des assertions explicites
✅ BON:
```java
@Alors("une erreur HTTP {int} est levée")
public void erreurHTTPLevee(int statusCode) {
    assertThat(lastStatusCode)
        .as("Le code HTTP doit être " + statusCode)
        .isEqualTo(statusCode);
}
```

❌ MAUVAIS:
```java
@Alors("une erreur est levée")
public void erreurLevee() {
    assertThat(lastStatusCode).isNotEqualTo(200);
}
```

### 4. Fournir des messages clairs
✅ BON:
```java
String content = lastResult.getResponse().getContentAsString();
assertThat(content)
    .as("Le message d'erreur doit contenir: " + text)
    .contains(text);
```

❌ MAUVAIS:
```java
assertThat(lastResult.getResponse().getContentAsString())
    .contains(text);
```

### 5. Gérer les exceptions correctement
✅ BON:
```java
@Quand("je crée une recette avec un nom vide")
public void creRecetteNomVide() throws Exception {
    try {
        // Action
    } catch (Exception e) {
        lastException = e;
    }
}
```

❌ MAUVAIS:
```java
@Quand("je crée une recette avec un nom vide")
public void creRecetteNomVide() {
    // Pas de gestion d'exception
}
```

---

## 🧩 Ajouter un nouveau scénario d'erreur

### Étape 1: Ajouter le scénario BDD
```gherkin
# Dans recette.feature

Scénario: Nouvelle erreur
  Quand je fais quelque chose
  Alors une erreur est levée
  Et le message d'erreur contient "..."
```

### Étape 2: Ajouter les step definitions
```java
// Dans RecetteStepDefs.java

@Quand("je fais quelque chose")
public void jeFaisQuelqueChose() throws Exception {
    // Implémentation
    lastStatusCode = lastResult.getResponse().getStatus();
}

@Alors("une erreur est levée")
public void uneErreurEstLevee() {
    assertThat(lastStatusCode).isNotEqualTo(200);
}
```

### Étape 3: Exécuter et vérifier
```bash
mvnw test -Dtest=CucumberTestRunner
```

---

## 📊 Coverage des scénarios d'erreur

| Scénario | Type d'erreur | Code HTTP | Couvert |
|----------|---------------|-----------|---------|
| Nom vide | Validation | 400 | ✅ |
| Nom trop court | Validation | 400 | ✅ |
| Nom trop long | Validation | 400 | ✅ |
| Recette inexistante (GET) | Not Found | 404 | ✅ |
| Recette inexistante (PUT) | Not Found | 404 | ✅ |
| Recette inexistante (DELETE) | Not Found | 404 | ✅ |
| Kafka indisponible | Non-bloquant | 201 | ✅ |

---

## 🚨 Dépannage des tests BDD

### Les tests BDD ne s'exécutent pas
```bash
# Vérifier que CucumberTestRunner existe
find . -name "CucumberTestRunner.java"

# Vérifier les dépendances Cucumber
mvnw dependency:tree | grep cucumber
```

### Étape definition manquante
```
UndefinedStepException: Step not implemented
```

**Solution:** Ajouter la step definition manquante dans RecetteStepDefs.java

### MockMvc non accessible
```
NullPointerException: Cannot invoke MockMvc
```

**Solution:** S'assurer que la classe a l'annotation `@SpringBootTest`

### Problème d'encoding French
```
Caractères accentués non reconnus
```

**Solution:** Ajouter `# language: fr` au début du fichier .feature

---

## 📚 Ressources

- [Cucumber BDD Guide](https://cucumber.io/docs/bdd/)
- [Spring Test Documentation](https://spring.io/guides/gs/testing-web/)
- [MockMvc API](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/test/web/servlet/MockMvc.html)
- [AssertJ Documentation](https://assertj.github.io/assertj-core-features-highlight.html)

---

**Dernière mise à jour:** 17 Janvier 2026  
**Document:** Guide BDD - Scénarios d'Erreur
