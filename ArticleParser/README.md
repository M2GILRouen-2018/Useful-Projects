#ArticleParser
Transformez votre .xml Pubmed de plusieurs Go en millions de fichiers ! :D

## Utilisation
Syntaxe de la commande :
```java -jar parser.jar (--flag=arg1,arg2,...)* FICHIER_1 FICHIER_2 ...```

## Options disponibles
- ```-f=xml,json,all``` : Utilise tous les formats spécifiés (xml, json). Le mot-clé all permet d'utiliser tous les formats en même temps.
- ```-d=directory``` : Place les fichiers produits dans le répertoire directory.
- ```-s=n``` : Permet de placer n articles par fichier produit (fixé par défaut à 1)

## Description
Ce programme lit les fichiers fournis en entrée et les convertit selon
les formats sélectionnés en option.
Les articles résultants sont alors stockés au sein de plusieurs fichiers dont
le nombre d'articles par fichier, ne peut excéder une certaine limite.

Sauf option contraire, les fichiers générés lors de l'utilisation de ce programme 
seront alors placés dans le répertoire courant.

