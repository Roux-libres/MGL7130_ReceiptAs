[![Contributors][contributors-shield]][contributors-url]
[![Forks][forks-shield]][forks-url]
[![Stargazers][stars-shield]][stars-url]
[![Issues][issues-shield]][issues-url]



<br />
<p align="center">
  <a href="https://github.com/Roux-libres/MGL7130_ReceiptAs">
    <img src="images/receiptas_icon.png" alt="Logo" width="80" height="80">
  </a>

  <h3 align="center">Receipt'As</h3>

  <p align="center">
    Receipt'As est une application Android qui permet de scanner des reçus et connaître le montant que chaque personne participante doit payer.
    <br />
    <a href="https://github.com/Roux-libres/MGL7130_ReceiptAs"><strong>Explorer la documentation »</strong></a>
    <br />
    <br />
    <a href="https://github.com/Roux-libres/MGL7130_ReceiptAs/issues">Rapporter un bug ou demander une fonctionnalité</a>
  </p>
</p>



<details open="open">
  <summary><h2 style="display: inline-block">Table des matières</h2></summary>
  <ol>
    <li>
      <a href="#à-propos-du-projet">À propos du projet</a>
      <ul>
        <li><a href="#outils">Outils</a></li>
      </ul>
    </li>
    <li>
      <a href="#prise-en-main">Prise en main</a>
      <ul>
        <li><a href="#prérequis">Prérequis</a></li>
        <li><a href="#installation">Installation</a></li>
      </ul>
    </li>
    <li><a href="#utilisation">Utilisation</a></li>
    <li><a href="#feuille-de-route">Feuille de route</a></li>
    <li><a href="#contacts">Contacts</a></li>
  </ol>
</details>



## À propos du projet

L'application Receipt'As est une application Android développée en Java. Cette application permet de scanner des reçus et connaître le montant que chaque personne participante doit payer. L’application vise surtout à être utilisée entre amis pour payer un restaurant ou des courses par exemple.



### Outils

* [Android Studio](https://developer.android.com/studio)
* [Figma](https://www.figma.com)



## Prise en main

Pour obtenir une copie locale, suivez les étapes suivantes.

### Prérequis

Les éléments suivant doivent être installés pour faire fonctionner l'application.
* Android Studio
* Java 15


### Installation

1. Cloner le répertoire
   ```sh
   git clone https://github.com/Roux-libres/MGL7130_ReceiptAs.git
   ```

2. Lancer Android Studio et ouvrir le projet cloné.



## Utilisation

<strong>L'application est en phase de développement et toutes les fonctionnalités ne sont pas encore implémentées.</strong>

Receipt'As s'oriente automatiquement en fonction de la taille de l'écran de l'appareil, ainsi elle s'éxécute en mode portrait sur un téléphone et en mode paysage sur une tablette.

La navigation dans l’application se fait au travers d’un menu Navigation Drawer. Sur un téléphone, le menu est accessible depuis n’importe quel écran avec l’icône « Hamburger » en haut à gauche. Ce menu permet d’accéder à l’historique des reçus, à l’écran de création d’un nouveau reçu et aux paramètres. Il est possible de fermer le menu sans changer d’écran en appuyant à l’extérieur du Drawer.

L'écran d'historique comporte un reçu artificiel sur lequel il est possible de cliquer afin de voir un exemple de l'affichage prévu.
L'écran de paramètres est présent mais ne présente aucune information importante.

L’écran de création d’un nouveau reçu ("Scan receipt") est composé de deux zones de texte, une pour nommer le reçu et l’autre pour saisir le prix total du reçu, d’une liste déroulante permettant de sélectionner la devise utilisée ainsi qu’une icône permettant de sélectionner une ou plusieurs photos de la galerie ou bien de directement prendre une photo à l'aide de l’appareil photo. Il faut ensuite cliquer sur le bouton de validation (situé en haut à droite de la gallerie) pour traiter les photos.

Le traitement des photos se fait en deux étapes, tout d'abord il faut sélectionner la zone où les noms des produits apparaissent puis valider en ensuite faire la même chose pour les prix des produits. Le traitement est fini une fois que toutes les photos ont été traitées. L'écran de création du reçu est à nouveau afficher et il est possible de rajouter des images.

Avant de valider la création du reçu, il est possible de modifier les informations entrées et de supprimer les photos traitées. Si tout est bon, il faut cliquer sur le bouton de validation en bas à droite.

S'ensuit les écrans de corrections, le premier écran sert à fusionner des noms de produits ou bien à les supprimer. Le suivant sert à corriger les noms des produits et leurs prix. Une fois la correction finie, il faut valider les modifications.

Enfin, il faut ajouter les participants au reçu. Une fois tous les participants ajoutés, il faut valider.

Il n'y a pour l'instant pas de suite à cette dernière étape.



## Écart avec le plan de projet

Il n'y a pour l'instant pas de paramètre modifiable par l'utilisateur. 



## Feuille de route

Voir les [questions non résolues](https://github.com/Roux-libres/MGL7130_ReceiptAs/issues) pour une liste des fonctionnalités proposées (et d'erreur connues).



## Contacts

* DELLAC Aurélien - aurelien.dellac@viacesi.fr
* DEREN Nelson - nelson.deren@viacesi.fr
* PEYRET Romain - romain.peyret@viacesi.fr

Lien du projet: [https://github.com/Roux-libres/MGL7130_ReceiptAs](https://github.com/Roux-libres/MGL7130_ReceiptAs)



[contributors-shield]: https://img.shields.io/github/contributors/Roux-libres/MGL7130_ReceiptAs.svg?style=for-the-badge
[contributors-url]: https://github.com/Roux-libres/MGL7130_ReceiptAs/graphs/contributors
[forks-shield]: https://img.shields.io/github/forks/Roux-libres/MGL7130_ReceiptAs.svg?style=for-the-badge
[forks-url]: https://github.com/Roux-libres/MGL7130_ReceiptAs/network/members
[stars-shield]: https://img.shields.io/github/stars/Roux-libres/MGL7130_ReceiptAs.svg?style=for-the-badge
[stars-url]: https://github.com/Roux-libres/MGL7130_ReceiptAs/stargazers
[issues-shield]: https://img.shields.io/github/issues/Roux-libres/MGL7130_ReceiptAs.svg?style=for-the-badge
[issues-url]: https://github.com/Roux-libres/MGL7130_ReceiptAs/issues