Zad�n�: Nejkrat�� cesty mezi v�emi vrcholy (shortest paths)
Dal�� z velmi dob�e prozkouman�ch grafov�ch algoritm�, kter� maj� vyu�it� jak ve fyzick�m sv�t�, tak ve sv�t� po��ta��. 
Na v�stupu je seznam dvojic vrchol� s d�lkou minim�ln� cesty mezi nimi (pokud existuje) a vypsanou cestou.

Na �e�en� jsem pou�il Dijkstr�v algoritmus, kter� je reprezentov�n t��dou dijkstra a m� vrcholy vertex a hrany mezi nimi edge. 
Pomoc� dijkstrova algoritmu vypo��t�m z dan�ho vrcholu cesty ke v�em ostatn�m vrchol�m a zap�u d�lku cesy a jej� body.
Ve v�cevl�knov� ��sti aplikace rozd�l�m po�et vrhol� na dv� poloviny a ka�dou polovinu zpracov�v� samostatn� vl�kno.

Zdrojov� soubory s grafy jsou soubory graf.txt (jednoduch� graf) a graph.txt (slo�it�j��).
P�i v�po�tu cesty u ka�d�ho grafu se vl�kno usp� na 100ms, aby v�po�et nebyl moc rychl� a daly se l�pe porovnat �asy obou implementac�.
J� na sv�m PC jsem nam��il �asy pro slo�it�j�� graf v jednovl�knov� implementaci zhruba 2500ms a ve v�cevl�knov� 1300ms, co� odpov�d� p�edpoklad�m.
Celkov� �as v�po�tu je vyps�n na v�stup v�dy po vyps�n� cest grafu.