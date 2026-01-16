# language: fr
Fonctionnalité: Gestion des ingrédients d'une recette
  En tant qu'utilisateur
  Je veux pouvoir gérer les ingrédients d'une recette
  Afin de connaître la composition de mes plats

  Contexte:
    Étant donné que l'application est démarrée
    Et qu'une recette "Pizza Margherita" existe avec l'id 1

  Scénario: Ajouter un ingrédient à une recette
    Quand j'ajoute un ingrédient "Tomate" avec la quantité "500g" à la recette 1
    Alors l'ingrédient est ajouté avec succès
    Et l'ingrédient a un identifiant
    Et la recette 1 contient l'ingrédient "Tomate"

  Scénario: Lister les ingrédients d'une recette
    Étant donné que la recette 1 contient les ingrédients suivants:
      | nom        | quantité |
      | Tomate     | 500g     |
      | Mozzarella | 200g     |
      | Basilic    | 10g      |
    Quand je récupère les ingrédients de la recette 1
    Alors je reçois 3 ingrédients
    Et la liste contient "Tomate" avec "500g"
    Et la liste contient "Mozzarella" avec "200g"
    Et la liste contient "Basilic" avec "10g"

  Scénario: Récupérer un ingrédient spécifique
    Étant donné que la recette 1 contient un ingrédient "Tomate" avec l'id 1
    Quand je récupère l'ingrédient 1 de la recette 1
    Alors je reçois l'ingrédient "Tomate"

  Scénario: Modifier la quantité d'un ingrédient
    Étant donné que la recette 1 contient un ingrédient "Tomate" avec "500g" et l'id 1
    Quand je modifie l'ingrédient 1 avec la quantité "600g"
    Alors l'ingrédient est mis à jour avec succès
    Et la quantité de l'ingrédient 1 est "600g"

  Scénario: Supprimer un ingrédient d'une recette
    Étant donné que la recette 1 contient un ingrédient "Tomate" avec l'id 1
    Quand je supprime l'ingrédient 1 de la recette 1
    Alors l'ingrédient est supprimé avec succès
    Et la recette 1 ne contient plus l'ingrédient "Tomate"

  Scénario: Ajouter un ingrédient sans nom
    Quand j'ajoute un ingrédient avec un nom vide à la recette 1
    Alors je reçois une erreur de validation
    Et le message d'erreur indique "Le nom de l'ingrédient est obligatoire"

  Scénario: Ajouter un ingrédient à une recette inexistante
    Quand j'ajoute un ingrédient "Tomate" à la recette 999
    Alors je reçois une erreur 404
    Et le message d'erreur indique "Recette non trouvée"
