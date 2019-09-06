Zadání: Nejkratší cesty mezi všemi vrcholy (shortest paths)
Další z velmi dobøe prozkoumaných grafových algoritmù, které mají využití jak ve fyzickém svìtì, tak ve svìtì poèítaèù. 
Na výstupu je seznam dvojic vrcholù s délkou minimální cesty mezi nimi (pokud existuje) a vypsanou cestou.

Na øešení jsem použil Dijkstrùv algoritmus, který je reprezentován tøídou dijkstra a má vrcholy vertex a hrany mezi nimi edge. 
Pomocí dijkstrova algoritmu vypoèítám z daného vrcholu cesty ke všem ostatním vrcholùm a zapíšu délku cesy a její body.
Ve vícevláknové èásti aplikace rozdìlím poèet vrholù na dvì poloviny a každou polovinu zpracovává samostatné vlákno.

Zdrojové soubory s grafy jsou soubory graf.txt (jednoduchý graf) a graph.txt (složitìjší).
Pøi výpoètu cesty u každého grafu se vlákno uspí na 100ms, aby výpoèet nebyl moc rychlý a daly se lépe porovnat èasy obou implementací.
Já na svém PC jsem namìøil èasy pro složitìjší graf v jednovláknové implementaci zhruba 2500ms a ve vícevláknové 1300ms, což odpovídá pøedpokladùm.
Celkový èas výpoètu je vypsán na výstup vždy po vypsání cest grafu.