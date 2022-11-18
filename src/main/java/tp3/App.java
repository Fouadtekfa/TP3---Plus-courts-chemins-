package tp3;


import org.graphstream.algorithm.generator.RandomGenerator;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.DefaultGraph;
import org.graphstream.graph.implementations.SingleGraph;
import org.omg.CORBA.SystemException;
import org.graphstream.algorithm.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
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

   /* public static void init( Graph graphe, Node source) {
        source.setAttribute("d", 0);
        for (Node n : graphe) {
            //v .dist ← ∞ mettre a la infinie
                n.setAttribute("distance",  Integer.MAX_VALUE);
        }
        //ajout de noode sde source a la fille
        file.put(source, (int) source.getAttribute("distance"));
        System.out.println(file.toString());

    }
*/
   //dans cette parti on vas implémenter l'algorithme de Dijkstra vu en cours étape par étape
    //utiliser hashmap pour pouvoir stocker chaque node avec ca priorité qui serra par la suite la plus petite distance
   private static  HashMap<Node, Integer> file = new HashMap<Node, Integer>();
    //
   public static void init(Graph grahe , Node source) {

       //s.dist ← 0 source.distance <- 0
       source.setAttribute("distance", 0);

       for (Node n : grahe) {
           if (!n.equals(source)) {
               //voisin.dist ← ∞
               // pour chaque noeud different de noeud de source on instailise a l'infini
               n.setAttribute("distance", Integer.MAX_VALUE);
           }
       }
       //    f.add(s, 0) On ajoute le paramètre de noeud de source a la fille
       file.put(source, (int) source.getAttribute("distance"));
   }

   // u ← f.extractMin()
   //récupère l’élément de priorité minimum
    public static   Node extractMin() {
       //le noeud minimale
        Node noeudminimale = file.entrySet().iterator().next().getKey();
        // la distance minimale
        int distanceMinimale = file.entrySet().iterator().next().getValue();

        for(Map.Entry<Node, Integer> it : file.entrySet()) {
            int currentDist = it.getValue();
            if(currentDist < distanceMinimale) {
                distanceMinimale = currentDist;
                noeudminimale = it.getKey();
            }
        }
        System.out.println(file.toString());
        System.out.println("=============1================");
        file.remove(noeudminimale, distanceMinimale);
        System.out.println(file.toString());
        return noeudminimale;
    }


    //mettre a jour les distances

    public static void UpdateDistance(Node u, Node voisin ){

        int uDistance = (int) u.getAttribute("distance");
        int voisinDistance = (int) voisin.getAttribute("distance");
        int w = (int) voisin.getEdgeBetween(u).getAttribute("poids");


        int sum = uDistance + w;

        if (sum < voisinDistance) {
            voisin.setAttribute("distance", sum);
            file.put(voisin, sum);
        }

        System.out.println("Mis a jours ");
        System.out.println(file.toString());
    }
    //dans cette methode on vas faire appelle a nous méthodes pour implémanter l'algorithme
    public static void DijkstraNaive(Graph graphe, Node source) {
        // initialisation
        init(graphe,source);
         // fille.empty() pour vérifie si la file est vide
        while(!file.isEmpty()){
            //récupère l’élément de priorité minimum
            Node u = extractMin();
            Iterator<Node> itNode = u.neighborNodes().iterator();
            while (itNode.hasNext()) {
                Node v=itNode.next();
                // mettre a jours les distances
                UpdateDistance(u, v);
            }

        }
        affichageDistance(graphe,source);
    }
// affichage des distance entre la source les autres noeud pour tester mon coode
    public static void affichageDistance(Graph graph , Node source){
        System.out.print("Noeud de source " +source.getId()+"\n");

        for (Node node : graph) {
            System.out.print("la distance entre  le Noeud  " + source  + " et " + node + " :  ====> "+  node.getAttribute("distance") + "\n" );
            }
        }


    public static void Dijkstragraphstream(Graph graph, Node Source ){
       System.out.println("========Dijkstra graphstream =======");

     //  Dijkstra Dijkstragraphstream= new Dijkstra(Dijkstra.Element.EDGE,"null");
        Dijkstra Dijkstragraphstream = new Dijkstra(Dijkstra.Element.EDGE, "result", "poids");
        Dijkstragraphstream.init(graph);
        Dijkstragraphstream.setSource(Source);
        Dijkstragraphstream.compute();
        System.out.println("Le noeud source est : " + Source.getIndex());
        for (Node node : graph) {
          //  System.out.printf("%s->%s:%10.2f%n", Dijkstragraphstream.getSource(), node,
            //        Dijkstragraphstream.getPathLength(node));
            System.out.printf(" Le plus court chemin entre %s et %s ==> %6.2f%n", Dijkstragraphstream.getSource(), node, Dijkstragraphstream.getPathLength(node));



        }


    }

    public static void main( String[] args )
    {
        System.setProperty("org.graphstream.ui", "swing");
        Graph graph1 = new DefaultGraph("Random");
        GeneratorGraph(graph1,4,2,false,15);
        Graph graph2 = new SingleGraph("Random");
        DijkstraNaive(graph1,graph1.getNode(0));
       // affichageDistance(graph1 ,graph1.getNode(0));
        Dijkstragraphstream(graph1,graph1.getNode(0));
        //GeneratorGraph(graph2,150,2);
        //Graph graph3 = new SingleGraph("Random");
        //GeneratorGraph(graph3,400,3);
    }
}
