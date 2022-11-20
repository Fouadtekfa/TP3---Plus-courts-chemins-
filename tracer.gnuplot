set terminal postscript eps enhanced color 15
set encoding utf8
set terminal png enhanced font 'Roboto,'
set output "comparaison_temps_execution_enFonction_noeuds.png"
set title "Temps d'exécution des deux algorithmes en fonction de la taille du graph"
set yrange [0:400]
set xrange [1:10000]
set xtics font ",10"
set ytics font ",10"
set xlabel 'Nombre de noeuds des graphes' font ",10"
set ylabel 'Temps moyen en ms' font ",10"
set key font ",10" top left samplen 5 spacing 1
plot "NaiveDijkstra.dat" t "Version Naive de Dijkstra"   linecolor rgb'yellow', \
     "Dijkstra.dat" title "Dijkstra de  GraphStream" linecolor rgb 'magenta'



