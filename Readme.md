|Nom|Prénom|
|--|--|
| *Deveaux* | *Julien*|
| *Dubois* | *Dimitri* |

### A. Vérifiez son fonctionnement : observez la construction du
   graphe dans GS, Résolvez le problème de flot max et vérifiez à la main la solution obtenue.
   - Le résultat obtenu semble correct, le flot max serait de 15 alors que les deux derniers liens ne peuvent supporter que 8 et 7 de flots. Donc 15 max dans le réseau.
   - Testez avec un second exemple (du cours ou construit par vous) :
       - Le second exemple du cours (premier exemple cours flots), fonctionne comme prévu.


### B. Donnez la solution du problème de flot max en utilisant l'algorithme mis à votre disposition.
Vous afficherez le réseau, les valeurs du flot sur les arêtes, et vous mettrez en évidence les arêtes saturées. Prouvez que le flot obtenu est maximum.
- Le flot obtenu est maximum parce que, si le flot est complet, il est maximum. Pour qu'un flot soit complet, il faut qu'au moin un arc entre notre source et notre destination soit complet.
Ici, tous les chemins pour aller vers I passent par G ou H. GI est saturé et tous les arcs allant vers H le sont aussi(HD/HF). Le flot est donc maximal.


### C. Donnez la solution du problème de flot max en utilisant l'algorithme mis à votre disposition.
Vous afficherez le réseau, les valeurs du flot sur les arêtes, et vous mettrez en évidence les arrêtes saturées.
Prouvez que le flot obtenu est maximum.
- Le flot maximal avec un tronçon en plus de 25000 de capacité entre AI augmenterais la capacité du réseau actuel, limité à 900 de capacité. L'ajout de ces tronçons devrait donc augmenter le flot maximal. 


### D. Que pouvez-vous déduire du flot max sur le réseau routier concernant les tronçons à construire ?
- Les coupes minimales associées sont A-B-C-E-F avec 5 arrêtes et D-H-G-I avec aussi 5 arrêtes. Le nombre de liens entre les deux parties de la coupe est 7. 
- Une coupe minimale avec peu de lien entre ses parties est signe d'un réseau pouvant facilement tomber en panne.
Nous en concluons donc qu'il peu probable d'obtenir des bouchons dans ce réseau en cas d'accident.