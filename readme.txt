Jeu d'aventures
================

Prérequis
---------
- JDK 17 ou plus récent. La commande javac doit être dans le PATH
  (un JRE seul ne suffit pas).
- Apache Maven 3.8 ou plus récent.

Vérification :
  javac -version
  mvn -version

Compilation et lancement
------------------------
Depuis le répertoire qui contient pom.xml :

  mvn -q compile exec:java

La fenêtre « Jeu d'aventures » s'ouvre. Aucun mot de passe ni fichier
de configuration n'est demandé.

Tests des règles (sans ouvrir la fenêtre) :

  mvn -q test

Données
-------
Les images des salles sont dans src/main/resources/images/.
Le jeu n'utilise pas de base de données.
L'application à évaluer est le projet Maven, sous src/main/java.
