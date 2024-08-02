# Copy of Slither

## Description

Ce projet est une réplique du célèbre jeu **Slither.io**. Il vous permet de contrôler autant de serpent que de client sur une carte avec 9 autres serpents contrôlés par des bots. L'objectif est de survivre le plus longtemps possible en mangeant de la nourriture et en évitant les collisions avec d'autres serpents.

## Prérequis

- Java 21 (ou une version ultérieure)  
  (Le projet n'a pas été testé avec des versions antérieures.)

## Exécution

**Exécution directe via script :**
- **Serveur :**
    - Assurez-vous que le script `launchServer.sh` a les permissions d'exécution (vous pouvez le faire avec `chmod +x launchServer.sh` si nécessaire).
    - Lancez le serveur en utilisant :
      ```sh
      sh launchServer.sh
      ```
  - **Client :**
    - Assurez-vous que le script `launchClient.sh` a les permissions d'exécution (vous pouvez le faire avec `chmod +x launchClient.sh` si nécessaire).
    - Lancez le client en utilisant :
      ```sh
      sh launchClient.sh
      ```
      
## Utilisation

- **Contrôle du serpent :** Déplacez votre serpent en utilisant la souris.
- **Survie :** Évitez les collisions avec les autres serpents et essayez de ne pas toucher leur tête. Si vous touchez un serpent avec votre tête, vous mourrez. Les serpents touchés meurent également.
- **Croissance :** Mangez de la nourriture colorée pour faire grandir votre serpent.

## Fonctionnalités à Venir
- **Mode en ligne :** jouer en multijoueurs.

## Crédits

Développé par Thomas ARROUS.

## Contribuer

Les contributions sont les bienvenues ! Si vous souhaitez contribuer au projet, veuillez soumettre des pull requests ou des issues via GitHub.

## Licence

Ce projet est sous la **MIT License**. Voir le fichier [LICENSE](LICENSE) pour plus de détails.
