package tp3;


import org.graphstream.algorithm.generator.RandomGenerator;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;
import org.omg.CORBA.SystemException;

import java.util.Random;


public class App {


    /**
     *Générateur d'un graphe aléatoire
      * @param graph le graphe qui sera attribué au générateur
     * @param NbNoueds nombre de noeuds
     * @param averageDegree le degré du graphe
     * @param directed graphe oriente ou pas
     * @param poidsMax la distance max a généré avec random
     */
    private static void GeneratorGraph(Graph graph, int NbNoueds , double averageDegree, Boolean directed,   int poidsMax ) {
        //généré un graphe d'un degree donner en paramètre
        RandomGenerator generator =new RandomGenerator(averageDegree);
        //graphe orienté ou pas aléatoirement
        generator.setDirectedEdges(directed,true);
        //récupérer tous les événements de notre graphe
        generator.addSink(graph);
        generator.addEdgeAttribute("poids");

        generator.begin();
        for(int i=0; i<NbNoueds; i++)
            generator.nextEvents();

        generator.end();

        graph.edges().forEach(e->{
            Random random =new Random();
            //Attribution des poids aléatoire pour chaque arête entre 0 et pointMax donner en paramètre
            e.setAttribute("poids", random.nextInt(poidsMax));
            // affichage de distance entre les noeuds
            e.setAttribute("ui.label", "" + e.getAttribute("poids"));
            //style CSS pour les arêtes
            e.setAttribute("ui.style", "text-size: 20px;");

        });
         //affichage des id des noeuds avec le style CSS
        graph.nodes().forEach(n -> {
            n.setAttribute("label", n.getId());
            n.setAttribute("ui.style","fill-color:Yellow; size:22; text-size:18px; ");

        });
        graph.display();
        System.out.println("le graphe "+graph.getId()+" est générer ");

    }
    public static void main( String[] args )
    {
        System.setProperty("org.graphstream.ui", "swing");
        Graph graph1 = new SingleGraph("Random");
        GeneratorGraph(graph1,10,3,true,15);
        //Graph graph2 = new SingleGraph("Random");
        //GeneratorGraph(graph2,150,2);
        //Graph graph3 = new SingleGraph("Random");
        //GeneratorGraph(graph3,400,3);
    }
}
