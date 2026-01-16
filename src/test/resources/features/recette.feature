# language: fr
Fonctionnalité: Gestion des recettes
  En tant qu'utilisateur
  Je veux pouvoir gérer des recettes
  Afin d'organiser mes préparations culinaires

  Contexte:
    Étant donné que l'application est démarrée
    Et que la base de données est vide

  Scénario: Créer une nouvelle recette
    Quand je crée une recette avec le nom "Pizza Margherita"
    Alors la recette est créée avec succès
    Et la recette a un identifiant
    Et un événement de création est publié

  Scénario: Lister toutes les recettes
    Étant donné que les recettes suivantes existent:
      | nom                |
      | Pizza Margherita   |
      | Salade César       |
      | Tarte aux pommes   |
    Quand je récupère la liste des recettes
    Alors je reçois 3 recettes
    Et la liste contient "Pizza Margherita"
    Et la liste contient "Salade César"
    Et la liste contient "Tarte aux pommes"

  Scénario: Récupérer une recette par son identifiant
    Étant donné qu'une recette "Pizza Margherita" existe avec l'id 1
    Quand je récupère la recette avec l'id 1
    Alors je reçois la recette "Pizza Margherita"

  Scénario: Mettre à jour une recette existante
    Étant donné qu'une recette "Pizza Margherita" existe avec l'id 1
    Quand je modifie la recette 1 avec le nom "Pizza Napolitaine"
    Alors la recette est mise à jour avec succès
    Et le nom de la recette 1 est "Pizza Napolitaine"

  Scénario: Supprimer une recette
    Étant donné qu'une recette "Pizza Margherita" existe avec l'id 1
    Quand je supprime la recette avec l'id 1
    Alors la recette est supprimée avec succès
    Et la recette 1 n'existe plus

  Scénario: Créer une recette sans nom
    Quand je crée une recette avec un nom vide
    Alors je reçois une erreur de validation
    Et le message d'erreur indique "Le nom de la recette est obligatoire"

  Plan du scénario: Rechercher des recettes par nom
    Étant donné que les recettes suivantes existent:
      | nom                |
      | Pizza Margherita   |
      | Pizza Napolitaine  |
      | Salade César       |
    Quand je recherche des recettes avec le terme "<terme>"
    Alors je reçois <nombre> recettes
    Et toutes les recettes contiennent "<terme>" dans leur nom

    Exemples:
      | terme  | nombre |
      | Pizza  | 2      |
      | Salade | 1      |
      | Tarte  | 0      |
