Feature: Recipe Management
  As a user
  I want to be able to manage recipes
  So that I can organize my culinary preparations

  Background:
    Given the application is started
    And the database is empty

  Scenario: Create a new recipe
    When I create a recipe with name "Pizza Margherita"
    Then the recipe is created successfully
    And the recipe has an id

  Scenario: Retrieve a recipe by id
    Given a recipe "Pâtes Carbonara" exists
    When I retrieve the recipe by id
    Then the recipe is returned successfully
    And the recipe name is "Pâtes Carbonara"

  Scenario: Update a recipe
    Given a recipe "Salade Verte" exists
    When I update the recipe name to "Salade Niçoise"
    Then the recipe is updated successfully
    And the recipe name is "Salade Niçoise"

  Scenario: Delete a recipe
    Given a recipe "Soupe à l'oignon" exists
    When I delete the recipe
    Then the recipe is deleted successfully

  Scenario: List all recipes
    Given a recipe "Omelette" exists
    And a recipe "Crêpes" exists
    When I list all recipes
    Then I receive 2 recipes
