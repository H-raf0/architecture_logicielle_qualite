# language: fr
Fonctionnalité: Architecture CQRS pour les recettes
  En tant que système
  Je veux séparer les commandes des requêtes
  Afin d'optimiser les performances et la scalabilité

  Contexte:
    Étant donné que l'application CQRS est démarrée
    Et que Kafka est disponible

  Scénario: Création de recette via Command Service
    Quand j'envoie une commande de création de recette "Pizza Margherita"
    Alors la recette est persistée en base de données
    Et un événement "RecetteCreatedEvent" est publié sur Kafka
    Et l'événement contient l'identifiant de la recette
    Et l'événement contient le nom "Pizza Margherita"

  Scénario: Consultation de recette via Query Service
    Étant donné qu'une recette "Pizza Margherita" a été créée
    Et que l'événement a été traité
    Quand je consulte les recettes via le Query Service
    Alors je retrouve la recette "Pizza Margherita"
    Et les données sont cohérentes avec la commande

  Scénario: Séparation des modèles de lecture et d'écriture
    Étant donné qu'une recette complexe existe avec plusieurs ingrédients
    Quand je la consulte via le Query Service
    Alors je reçois un DTO optimisé pour la lecture
    Et le DTO contient toutes les informations nécessaires
    Et le DTO ne contient pas les détails d'implémentation

  Scénario: Gestion de la cohérence éventuelle
    Quand j'envoie une commande de création de recette "Pizza Margherita"
    Et que je consulte immédiatement le Query Service
    Alors la recette peut ne pas être encore disponible
    Mais après un délai raisonnable (< 5 secondes)
    La recette est disponible dans le Query Service

  Scénario: Retry automatique en cas d'erreur temporaire
    Étant donné que la base de données est temporairement indisponible
    Quand j'envoie une commande de création de recette
    Alors le système effectue des tentatives de retry
    Et la commande finit par réussir après le retry
    Et l'événement est publié correctement

  Scénario: Publication d'événements même en cas d'échec Kafka
    Étant donné que Kafka est temporairement indisponible
    Quand j'envoie une commande de création de recette "Pizza Margherita"
    Alors la recette est quand même créée en base
    Et une erreur est loggée concernant Kafka
    Mais la commande ne retourne pas d'erreur à l'utilisateur
