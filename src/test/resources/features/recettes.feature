Feature: Recipe Management
  As a user
  I want to be able to manage recipes
  So that I can organize my culinary preparations

  Background:
    Given que l'application est démarrée
    And que la base de données est vide

  Scenario: Create a new recipe
    When je crée une recette avec le nom "Pizza Margherita"
    Then la recette est créée avec succès
    And la recette a un identifiant
