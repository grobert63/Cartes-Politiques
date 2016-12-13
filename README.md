# Cartes-Politiques
Cartes Politiques en forme de Grilles et Grilles Hexagonales


FRANCAIS :

Introduction :

Ce logiciel, réalisé par Julien Defiolles, Théophile Pumain, Guillaume Robert et Maxime Lemort, est licencié sous MPL 2.0. Celui-ci a été réalisé lors d'un projet, et des bugs peuvent ne pas avoir été détectés. 

I : Précaution d'usage et d'utilisation

Ce logiciel peut être utilisé avec un unique fichier .shp, cependant, la présence d'un fichier .dbf est fortement recommandée. 
De plus, veuillez noter que le fichier peut prendre plusieurs minutes pour être chargé sur le logiciel selon sa taille et sa précision générale. 
En aucun cas, ne veuillez quitter le logiciel lors du chargement ou de la sauvegarde d'un fichier, et ce, pour éviter un risque toutefois minime de perte de données. 

II : Utilisation

a/ Chargement d'un fichier 

Pour charger un fichier, veuillez appuyer sur le bouton 'file' situé en haut à gauche de la fenêtre principale. 
Après avoir appuyé sur open, un sous menu vous permettra de choisir si vous souhaitez ouvrir votre fichier avec ou sans un fichier .dbf, servant de base de donnée pour différentes informations. 
Après avoir effectué votre selection, le fichier .shp apparaîtra à gauche de la fenêtre, tandis qu'une grille d'hexagone correspondante sera présente à droite de celle-ci. 

b/ Manipulation des cartes et grilles. 

Une fois la carte chargée, plusieurs opérations sont possible. 

Tout d'abord, vous pouvez changer le nom affiché sur la carte à l'aide du menu 'Name', qui affichera les différentes options présentes dans le fichier .dbf, si il a été choisi. 

Le bouton 'best algo' permettra de charger automatiquement la représentation la plus fidèle possible de la carte. Si cette représentation ne vous convient pas, il est possible de changer plusieurs paramètres afin d'affiner la carte que vous souhaitez obtenir. 
Dans le menu 'Argument', se trouve 4 sous menus, chacun influant sur la carte finale. 
'Selected Region' permet de choisir, ou d'exclure, des régions spécifiques de la carte. 
'First country' permet de choisir la première région traitée par l'algorithme. 
'Direction' permet de choisir la direction de base de l'algorithme. Il est recommandé de choisir une direction sans trop de régions. 
Enfin, 'NbOccurence' permet de choisir le nombre de régions qui seront traitées, à partir de la région de départ. 

c/ Sauvegarde 

Pour sauvegarder les modifications effectuées, veuillez cliquer sur le bouton 'Export' dans le menu 'File'. Celui-ci permet de sauvegarder la carte d'hexagone en fichier image. 

III : Remerciements

L'équipe de production souhaite remercier M. Guilherme Dias Da Fonseca pour son regard externe sur le produit et ses recommandations.

ENGLISH :

Introduction :

This software is the result of the work of Julien Defiolles, Théophile Pumain, Guillaume Robert et Maxime Lemort, and is licenced under the MPL 2.0 licence. Some unexpected errors might be still present. 

I : Safety notice

This software can be used with a single .shp file, however the addition of a .dbf file is hugely recommended. 
Please note, the software can take a few minutes to correctly load the selected file and applying the algorithms. 
Please, do not exit the software while it is loading a file, to prevent a minimal risk of data corruption. 

II : Usage

a/ Loading a file 

To open a file, please click on the 'file' button at the top menu. 
After clicking on 'open', you will have the option to choose a .dbf file, that will be used as a database for the map, and will add informations on the display. 
Once the selection has been made, the original map will appear on the left, while the hexagon map will be displayed on the right. If a database was selected, informations will be displayed. 

b/ Map and grid operations 

Once the map is loaded, there are numerous possible actions. 

First of all, selecting 'name' will bring options to change the region's name on the map, if a .dbf file was selected and depending of its headers. 
It is possible to toggle on, or off, the display of the region names by clicking 'Nom des régions' right above the left display. 

You can drag and drop the map to move it, or zoom thanks to the mouse's wheel. Those operations can be completed with the sliders around the displays. 

The 'best algo' button allows to have the best display of the map. If this display isn't the one desired to represent some informations, there are options to change the arguments used to create the maps. 
The 'Argument' menu has 4 options, each of them changing the final grid. 
'Selected Region' allows the selection and display of specific regions, or their removal from the hexagon map. 
'First country' will decide the first region used by the algorithm. 
'Direction' makes the algorithm prioritize a specific direction while creating the hexagon map. It is recommended to choose a direction without many frontiers. 
Finally, 'NbOccurence' allows the user to choose the amount of regions close to the original one, that will be displayed.. 

c/ Saving a file 

To save the hexagon map, click 'export' on the 'file' menu, to allow the save as a picture file. 
Depending of the file's extension (.png, .gif), multiple extensions and picture formats are allowed.. 
Finally, there is the possibility to save the hexagon map as a .shp file, allowing future modifications. 

III : Special thanks

The whole production team would like to thank Mr. Guilherme Dias Da Fonseca for his help and suggestions concering the software's development.
