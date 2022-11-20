package tp3;


import org.graphstream.algorithm.generator.RandomGenerator;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.DefaultGraph;
import org.graphstream.graph.implementations.SingleGraph;
import org.omg.CORBA.SystemException;
import org.graphstream.algorithm.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;


public class App {


    /**
     *Générateur d'un graphe aléatoire
     * @param graph le graphe qui sera attribué au générateur
     * @param NbNoeuds nombre de noeuds
     * @param averageDegree le degré moyen du graphe
     * @param directed graphe oriente ou pas
     * @param poidsMax la distance max a généré avec random
     */
    private static void GeneratorGraph(Graph graph, int NbNoeuds , int averageDegree, Boolean directed,   int poidsMax ,Boolean display ) {
        //généré un graphe d'un degree donner en paramètre
        RandomGenerator generator =new RandomGenerator(averageDegree);
        //graphe orienté ou pas aléatoirement
        generator.setDirectedEdges(directed,true);
        //récupérer tous les événements de notre graphe
        generator.addSink(graph);
        generator.addEdgeAttribute("poids");
        //crrer un Node
        generator.begin();
        for(int i=0; i<NbNoeuds; i++)
            //il rajoute un autre node a chaque fois puis il fait une
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
        if (display==true){
            graph.display();}
        //System.out.println("le graphe "+graph.getId()+" est généré");

    }

    //dans cette parti on vas implémenter l'algorithme de Dijkstra vu en cours étape par étape
    //utiliser hashmap pour pouvoir stocker chaque node avec sa priorité
    private static  HashMap<Node, Integer> file = new HashMap<Node, Integer>();
    //
    public static void init(Graph grahe , Node source) {

        //s.dist ← 0 source.distance <- 0
        source.setAttribute("distance", 0);

        for (Node n : grahe) {
            if (!n.equals(source)) {
                //voisin.dist ← ∞
                // pour chaque noeud different de noeud source on instailise a l'infini
                n.setAttribute("distance", Integer.MAX_VALUE);
            }
        }
        // f.add(s, 0) On ajoute le paramètre de noeud de source a la fille
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
      //  System.out.println(file.toString());
     //   System.out.println("=============1================");
        file.remove(noeudminimale, distanceMinimale);
      //  System.out.println(file.toString());
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

       // System.out.println("Mis a jours ");
      //  System.out.println(file.toString());
    }
    //dans cette methode on va faire appelle à nos méthodes pour implémanter l'algorithme
    public static void DijkstraNaive(Graph graphe, Node source) {
        // initialisation
      //  System.out.println("========Dijkstra Naive =======");
       // double  temps_execution =0;
       // double temps_debut = System.nanoTime();
        init(graphe,source);

        // fille.empty() pour vérifier si la file est vide
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
        //double temps_fin = System.nanoTime();
        // temps_execution=(temps_fin-temps_debut)/1000000;
      //System.out.printf("temps1= "+temps_execution +"ms");

    }
    // affichage des distance entre la source les autres Noeuds
    public static void affichageDistance(Graph graph , Node source){
        System.out.print("Noeud de source " +source.getId()+"\n");
        System.out.println("========Dijkstra Naive =======");
        for (Node node : graph) {

            if ((int) node.getAttribute("distance") ==  Integer.MAX_VALUE) {
                System.out.print("la distance la plus courte entre" + source + " et" + node + " :  ====> " + "Infinity" + "\n");
            }else {
                System.out.print("la distance la plus courte entre" + source + " et " + node + " :  ====> " + node.getAttribute("distance") + "\n");
            }
        }
    }


    public static double Dijkstragraphstream(Graph graph, Node Source,Boolean afficher ){
        // System.out.println("========Dijkstra graphstream =======");
        double  temps_execution =0;
        //Dijkstra Dijkstragraphstream= new Dijkstra(Dijkstra.Element.EDGE,"null");
        Dijkstra Dijkstragraphstream = new Dijkstra(Dijkstra.Element.EDGE, "result", "poids");
        double temps_debut = System.nanoTime();
        Dijkstragraphstream.init(graph);
        Dijkstragraphstream.setSource(Source);
        //  float temps_debut = System.nanoTime();
        Dijkstragraphstream.compute();
        double temps_fin = System.nanoTime();
        temps_execution=(temps_fin-temps_debut)/1000000;
       // System.out.println("Le noeud source est : " + Source.getIndex());
       if(afficher==true){
        for (Node node : graph) {
          //  System.out.printf("%s->%s:%10.2f%n", Dijkstragraphstream.getSource(), node,
            //        Dijkstragraphstream.getPathLength(node));
            System.out.printf(" la distance la plus courte entre %s et %s : ====> %6.2f%n", Dijkstragraphstream.getSource(), node, Dijkstragraphstream.getPathLength(node));

        }
       }
      //  System.out.printf(temps_execution +"ms");
        return temps_execution;

    }

    //calculer le temps d'execusion en ms de la version Naive Dijkstra
    private static double TimeNaiveDijkstra(Graph g, Node s,Boolean afficher) {
        //Instanciation
        App VersionNaiveDijkstra = new App();
        //Lancement du compteur
        double debut = System.nanoTime();
        //Appel de la méthode contenant l'algorithme de Dijkestra version naive
        VersionNaiveDijkstra.DijkstraNaive(g, s);
        //Stopper le compteur à la fin
        double fin = System.nanoTime();
        //Calcul du temps d'execusion en ms
        double temps = (fin - debut)/1000000;
        //Retourner le temps d'execusion de l'algorithme
        if(afficher==true) {
            affichageDistance(g, s);
        }
        return temps ;

    }
    public static void writeDataFile (double[] data, String fileName,int impt){
        try {
            String filePath = System.getProperty("user.dir") + File.separator + fileName + ".dat";
            FileWriter fileWriter = new FileWriter(filePath);
            StringBuilder txt = new StringBuilder();

            for (int i =0; i < data.length; i += impt) {
                txt.append(i).append(" ").append(data[i]).append("\n");

            }
            fileWriter.write(txt.toString());
            fileWriter.close();
            System.out.println("vous pouvez exécuter la commande gnuplot tracer.gnuplot  pour gennrer la courbe sur votre terminale  ");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main( String[] args )
    {
        System.setProperty("org.graphstream.ui", "swing");
        Graph graph1 = new DefaultGraph("Random");
        GeneratorGraph(graph1,10,2,false,15,true);
        Graph graph2 = graph1;
        Graph graph3 = graph1;
        DijkstraNaive(graph1,graph1.getNode(0));
        System.out.println("le plus court chemins entre le Node 0 et les autres ");
        System.out.println(" de temps d'exécution  =====>"+TimeNaiveDijkstra(graph1,graph1.getNode(0),true));
        System.out.println("========Dijkstra graphstream===============");
        System.out.println("de temps d'exécution "+ Dijkstragraphstream(graph1,graph1.getNode(0),true) +   "\n");
        Graph graph4 = new DefaultGraph("Random");
         System.out.println("======Test du temps d'exécution de deux version  en fonction de degré ======");
        int j =1;
        while (  j <= 200 ){
             graph4 = new DefaultGraph("Random");
             int degre=1+j;
            GeneratorGraph(graph4,50,j,false,15,false);
            System.out.println(  "\n"+"temps d'exécution  de   NaiveDijkstra en fonction de degree  " +degre+"=====>"+TimeNaiveDijkstra(graph4,graph4.getNode(0),false)+"ms") ;
            System.out.println(  "\n"+"temps   Dijkstra graphstream de en fonction de degree  " +degre+"=====>"+Dijkstragraphstream(graph4,graph4.getNode(0),false)+"ms") ;
            j=j+4;
        }

        System.out.println("======Test du temps d'exécution de deux version  en fonction de taille  ======");
        System.out.println("======!!!!un instant s'il vous plait!!!!!!!!!!!!!======");

        double[] NaiveDijkstra = new double[10001];
        double[]  Dijkstra = new double[10001];
        int i =0;
        while (  i <= 10000 ){

            Graph grapht = new SingleGraph("test");
            GeneratorGraph(grapht,i,10,false,15,false);
           //System.out.println(  "\n"+"NaiveDijkstra" +i+"=====>"+TimeNaiveDijkstra(grapht,grapht.getNode(0),false)) ;
            // System.out.println(  "\n"+"Dijkstra graphstream de " +i+"=====>"+Dijkstragraphstream(grapht,grapht.getNode(0),false)) ;
            NaiveDijkstra[i] =TimeNaiveDijkstra(grapht,grapht.getNode(0),false);
            Dijkstra[i] = Dijkstragraphstream(grapht,grapht.getNode(0),false);
            i=i+50;
        }
        writeDataFile(NaiveDijkstra,  "NaiveDijkstra",50);
        writeDataFile(Dijkstra, "Dijkstra",50);


    }
}
