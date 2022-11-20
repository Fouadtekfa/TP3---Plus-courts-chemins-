set terminal postscript eps enhanced color 15
set encoding utf8
set terminal png enhanced font 'Roboto,'
set output "comparaison_temps_execution_enFonction_degreé.png"
set title "Temps d'exécution des deux algorithmes en fonction du degrée"
set yrange [0:20]
set xrange [1:200]
set xtics font ",10"
set ytics font ",10"
set xlabel 'Nombre de noeuds des graphes' font ",10"
set ylabel 'Temps moyen en ms' font ",10"
set key font ",10" top left samplen 5 spacing 1
plot "NaiveDijkstradegree.dat" t "Version Naive de Dijkstra"   linecolor rgb'green', \
     "Dijkstradegree.dat" title "Dijkstra de  GraphStream" linecolor rgb 'blue'



