1. Vérifiez son fonctionnement : observez la construction du
   graphe dans GS, Résolvez le problème de flot max et vérifiez à la main la solution obtenue.
   1. Le résultat obtenu semble correct, le flot max serait de 15 alors que les deux derniers liens ne peuvent supporter que 8 et 7 de flots. Donc 15 max dans le réseau.
2. Testez avec un second exemple (du cours ou construit par vous) :
   1. Le second exemple du cours (premier exemple cours flots), fonctionne comme prévu.
3. Donnez la solution du problème de flot max en utilisant l'algorithme mis à votre disposition. Vous afficherez le réseau, les valeurs du flot sur les arêtes, et vous mettrez en évidence les arêtes saturées. Prouvez que le flot obtenu est maximum.
   1. Le flot obtenu est maximum parce que, si le flot est complet, il est maximum. Pour qu'un flot soit complet, il faut qu'au moin un arc entre notre source et notre destination soit complet.
   Ici, tous les chemins pour aller vers I passent par G ou H. GI est saturé et tous les arcs allant vers H le sont aussi(HD/HF). Le flot est donc maximal.
