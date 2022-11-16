package tp3;

import org.graphstream.algorithm.generator.Generator;
import org.graphstream.algorithm.generator.RandomGenerator;
import org.graphstream.graph.Graph;
import org.graphstream.graph.implementations.SingleGraph;



public class App {


    private static void RandomGenerator(Graph graph, int NbNoueds , double averageDegree) {

        RandomGenerator generator =new RandomGenerator(averageDegree);
        generator.addSink(graph);

        generator.addSink(graph);
        generator.begin();
        for(int i=0; i<NbNoueds; i++)
            generator.nextEvents();
        generator.end();
        graph.display();

    }
    public static void main( String[] args )
    {

        System.setProperty("org.graphstream.ui", "swing");
        Graph graph1 = new SingleGraph("Random");
        RandomGenerator(graph1,100,3);
        Graph graph2 = new SingleGraph("Random");
        RandomGenerator(graph2,150,2);
        Graph graph3 = new SingleGraph("Random");
        RandomGenerator(graph3,400,3);
    }
}
