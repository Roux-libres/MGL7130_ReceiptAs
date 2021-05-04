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

Receipt'As s'oriente automatiquement en fonction de la taille de l'écran de l'appareil, ainsi elle s'exécute en mode portrait sur un téléphone mobile et en mode paysage sur une tablette.

La navigation dans l’application se fait au travers d’un menu Navigation Drawer. Sur un téléphone mobile, le menu est accessible depuis n’importe quel écran avec l’icône « Hamburger » en haut à gauche. Ce menu permet d’accéder à l’historique des reçus, à l’écran de création d’un nouveau reçu, à l'écran de réception de reçu et aux paramètres de l'application. Il est possible de fermer le menu sans changer d’écran en appuyant à l’extérieur du Drawer.

L'écran d'historique comporte un reçu artificiel sur lequel il est possible de cliquer afin de voir le détail de celui-ci. Les reçus créés se retrouvent sur cet écran.

L'écran de paramètres permet de modifier divers paramètres de l'application qui sont :
* La monnaie a utiliser par défaut (CAD, EUR, GBP, USD), si aucune préférence n'est attribuée alors la valeur par défaut est la monnaie locale en fonction de la langue du téléphone.
* Le thème de l'application (Thème système, Clair, Sombre), le thème système s'adapte au thème du téléphone et changera avec celui-ci.
* La langue utilisée dans l'application (Anglais, Français).

La version de l'application est affichée à la fin de cet écran de paramètres.


L’écran de création d’un nouveau reçu ("Scan receipt") est composé de deux zones de texte, une pour nommer le reçu et l’autre pour saisir le prix total du reçu, d’une liste déroulante permettant de sélectionner la devise utilisée ainsi qu’une icône permettant de sélectionner une ou plusieurs photos de la galerie ou bien de directement prendre une photo à l'aide de l’appareil photo. Il faut ensuite cliquer sur le bouton de validation (situé en haut à droite de la galerie) pour traiter les photos.

Le traitement des photos se fait en deux étapes, il faut tout d'abord sélectionner la zone où les noms des produits apparaissent puis valider en ensuite faire la même chose pour les prix des produits. Le traitement est fini une fois que toutes les photos ont été traitées. L'écran de création du reçu est à nouveau affiché et il est possible de rajouter des images.

Avant de valider la création du reçu, il est possible de modifier les informations entrées et de supprimer les photos traitées. Si tout est bon, il faut cliquer sur le bouton de validation en bas à droite.



<strong>À MODIFIER UNE FOIS L'ÉCRAN D'OREL FINI</strong>

S'ensuit l'écran de corrections, celui-ci sert à fusionner des noms de produits ou bien à les supprimer. Une fois la correction finie, il faut valider les modifications.

<strong>À MODIFIER UNE FOIS L'ÉCRAN D'OREL FINI</strong>



Ensuite c'est au tour de l'ajout des participants, il faut rentrer leurs noms dans la zone de texte et cliquer sur le bouton marqué d'un <strong>+</strong>. Une fois tous les participants ajoutés, il faut valider les modifications.

Il faut ensuite que chaque participant sélectionne les produits auxquels il a participé pour l'achat. Une fois que le dernier participant a fini il faut valider.

Enfin l'écran de finalisation apparait, les informations suivantes sont affichés :
* Le titre du reçu ;
* Le prix total du reçu ;
* Le prix total des articles qui n'ont pas de participant ;
* les participants et le montant qu'il doivent rembourser au payeur.

Il faut enfin sélectionner le payeur et valider le reçu. Le détail du reçu s'affiche et vous pouvez revenir à l'historique à l'aide de la flèche de retour en haut à gauche.

Sur le détail du reçu, tous les articles sont affichés avec les couleurs des participants et leurs prix. Le récapitulatif du reçu est aussi accessible.


Pour envoyer le reçu vers un autre appareil, il faut cliquer sur le bouton partage (représenté par une icône de partage) puis sélectionner le nom de l'appareil et cliquer sur "<strong>Envoyer un reçu</strong>". Pendant ce temps, l'autre appareil doit accéder à l'écran de réception en cliquant sur "<strong>Réception reçu</strong>" à l'aide du menu tiroir. Une fois le reçu recu, un message apparaît en indiquant le nom du reçu.



## Écart avec le plan de projet

* L'option du capteur NFC a été remplacée par la technologie Bluetooth pour l'envoi et la réception de reçu.



## Feuille de route

Voir les [questions non résolues](https://github.com/Roux-libres/MGL7130_ReceiptAs/issues) pour une liste des fonctionnalités proposées (et d'erreur connues).



## Contacts

* Aurélien Dellac - aurelien.dellac@viacesi.fr
* Nelson Deren - nelson.deren@viacesi.fr
* Romain Peyret - romain.peyret@viacesi.fr

Lien du projet: [https://github.com/Roux-libres/MGL7130_ReceiptAs](https://github.com/Roux-libres/MGL7130_ReceiptAs)



[contributors-shield]: https://img.shields.io/github/contributors/Roux-libres/MGL7130_ReceiptAs.svg?style=for-the-badge
[contributors-url]: https://github.com/Roux-libres/MGL7130_ReceiptAs/graphs/contributors
[forks-shield]: https://img.shields.io/github/forks/Roux-libres/MGL7130_ReceiptAs.svg?style=for-the-badge
[forks-url]: https://github.com/Roux-libres/MGL7130_ReceiptAs/network/members
[stars-shield]: https://img.shields.io/github/stars/Roux-libres/MGL7130_ReceiptAs.svg?style=for-the-badge
[stars-url]: https://github.com/Roux-libres/MGL7130_ReceiptAs/stargazers
[issues-shield]: https://img.shields.io/github/issues/Roux-libres/MGL7130_ReceiptAs.svg?style=for-the-badge
[issues-url]: https://github.com/Roux-libres/MGL7130_ReceiptAs/issues